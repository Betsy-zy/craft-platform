<template>
  <div class="favorites">
    <el-header height="60px" class="header">
      <div class="header-content">
        <div class="back-btn" @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
        </div>
        <h1>我的收藏</h1>
      </div>
    </el-header>
    <el-main>
      <div v-if="favorites.length > 0" class="favorites-content">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" v-for="favorite in favorites" :key="favorite.id">
            <el-card :body-style="{ padding: '0px' }" @click="goToWorkDetail(favorite.id)" style="cursor: pointer;">
              <img :src="favorite.image" class="favorite-image" alt="收藏作品">
              <div style="padding: 14px;">
                <h4 style="margin-bottom: 10px;">{{ favorite.name }}</h4>
                <p style="color: #666; margin-bottom: 15px;">{{ favorite.title }}</p>
                <p style="color: #f56c6c; font-weight: bold;">¥{{ favorite.price }}</p>
                <div style="display: flex; justify-content: space-between; margin-top: 15px;">
                  <el-button size="small" type="primary" @click.stop="buyWork(favorite)">
                    立即购买
                  </el-button>
                  <el-button size="small" type="danger" @click.stop="removeFavorite(favorite.id)">
                    <el-icon><Delete /></el-icon> 取消收藏
                  </el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <div v-else class="empty-favorites">
        <el-empty description="暂无收藏作品" />
        <el-button type="primary" @click="goToWorks">去浏览作品</el-button>
      </div>
    </el-main>
  </div>
</template>

<script>
import { ArrowLeft, Delete } from '@element-plus/icons-vue'
import api from '../api/index'

export default {
  components: {
    ArrowLeft,
    Delete
  },
  data() {
    return {
      favorites: []
    }
  },
  mounted() {
    this.loadFavorites()
  },
  methods: {
    goBack() {
      this.$router.back()
    },
    goToWorks() {
      this.$router.push('/works')
    },
    goToWorkDetail(favoriteId) {
      // 查找收藏项
      const favorite = this.favorites.find(item => item.id === favoriteId)
      if (favorite) {
        if (favorite.targetType === 'work') {
          // 跳转到作品详情页面
          this.$router.push(`/work/${favorite.targetId}`)
        } else if (favorite.targetType === 'product') {
          // 跳转到商品详情页面
          this.$router.push(`/product/${favorite.targetId}`)
        }
      }
    },
    buyWork(favorite) {
      // 购买功能
      if (!favorite) return
      
      // 将收藏的商品或作品添加到购物车
      const cartItem = {
        id: favorite.targetId || favorite.id,
        name: favorite.name,
        title: favorite.title,
        price: favorite.price,
        image: favorite.image,
        quantity: 1
      }
      
      // 从localStorage获取购物车数据
      let cart = localStorage.getItem('cart')
      if (cart) {
        try {
          cart = JSON.parse(cart)
        } catch (error) {
          console.error('解析购物车数据失败:', error)
          cart = []
        }
      } else {
        cart = []
      }
      
      // 检查商品或作品是否已经在购物车中
      const existingItemIndex = cart.findIndex(item => item.id === cartItem.id)
      if (existingItemIndex >= 0) {
        // 如果已经在购物车中，增加数量
        cart[existingItemIndex].quantity += 1
      } else {
        // 如果不在购物车中，添加到购物车
        cart.push(cartItem)
      }
      
      // 将更新后的购物车数据保存到localStorage
      localStorage.setItem('cart', JSON.stringify(cart))
      
      this.$message.success('已加入购物车')
      console.log('购买:', favorite.name)
    },
    removeFavorite(favoriteId) {
      // 取消收藏
      api.deleteCollection(favoriteId)
        .then(response => {
          this.favorites = this.favorites.filter(item => item.id !== favoriteId)
          this.$message.success('已取消收藏')
          console.log('取消收藏作品:', favoriteId)
        })
        .catch(error => {
          console.error('取消收藏失败:', error)
          this.$message.error('取消收藏失败')
        })
    },
    loadFavorites() {
      // 从API获取收藏作品
      // 这里假设用户ID为1，实际应从登录状态中获取
      const userId = 1
      api.getUserCollections(userId)
        .then(response => {
          const collections = response.data
          // 处理收藏数据，获取对应的作品或商品信息
          const favoritePromises = collections.map(collection => {
            if (collection.targetType === 'work') {
              // 获取作品详情
              return api.getWork(collection.targetId)
                .then(workResponse => {
                  const work = workResponse.data
                  return {
                    id: collection.id,
                    name: work.name,
                    title: work.title,
                    price: work.price,
                    image: work.image,
                    targetId: work.id,
                    targetType: collection.targetType
                  }
                })
            } else if (collection.targetType === 'product') {
              // 获取商品详情
              return api.getProduct(collection.targetId)
                .then(productResponse => {
                  const product = productResponse.data
                  return {
                    id: collection.id,
                    name: product.name,
                    title: product.title,
                    price: product.price,
                    image: product.image,
                    targetId: product.id,
                    targetType: collection.targetType
                  }
                })
            }
          })
          
          // 等待所有请求完成
          Promise.all(favoritePromises)
            .then(favorites => {
              // 过滤掉无效的收藏
              this.favorites = favorites.filter(favorite => favorite)
            })
            .catch(error => {
              console.error('获取收藏详情失败:', error)
              this.$message.error('获取收藏详情失败')
              // 如果获取详情失败，使用默认数据
              this.favorites = [
                {
                  id: 1,
                  name: '木雕佛像',
                  title: '传统木雕',
                  price: 2000,
                  image: 'https://img.alicdn.com/imgextra/i4/O1CN01qWk7nN1qX5u7mUQkT_!!6000000005556-0-tps-800-800.jpg'
                },
                {
                  id: 2,
                  name: '陶瓷茶具',
                  title: '青花瓷',
                  price: 800,
                  image: 'https://img.alicdn.com/imgextra/i2/O1CN01Gkq6eW1Z7z8J7n7lK_!!6000000003198-0-tps-800-800.jpg'
                }
              ]
            })
        })
        .catch(error => {
          console.error('获取收藏作品失败:', error)
          this.$message.error('获取收藏作品失败')
          // 如果API调用失败，使用默认数据
          this.favorites = [
            {
              id: 1,
              name: '木雕佛像',
              title: '传统木雕',
              price: 2000,
              image: 'https://img.alicdn.com/imgextra/i4/O1CN01qWk7nN1qX5u7mUQkT_!!6000000005556-0-tps-800-800.jpg'
            },
            {
              id: 2,
              name: '陶瓷茶具',
              title: '青花瓷',
              price: 800,
              image: 'https://img.alicdn.com/imgextra/i2/O1CN01Gkq6eW1Z7z8J7n7lK_!!6000000003198-0-tps-800-800.jpg'
            }
          ]
        })
    }
  }
}
</script>

<style scoped>
.favorites {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.header {
  background-color: #8B4513;
  color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 20px;
}

.back-btn {
  font-size: 20px;
  cursor: pointer;
  padding: 10px;
  border-radius: 50%;
  transition: all 0.3s;
  z-index: 1000;
  position: relative;
  display: inline-block;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.header-content h1 {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
  flex: 1;
  text-align: center;
}

.favorites-content {
  padding: 20px;
}

.favorite-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
}

.empty-favorites {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;
  background-color: white;
  margin: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 40px;
}

.empty-favorites .el-button {
  margin-top: 30px;
}
</style>