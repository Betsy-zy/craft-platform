<template>
  <div class="work-detail">
    <TopNav />
    <el-main>
      <div v-if="work" class="work-content">
        <el-row :gutter="30">
          <el-col :xs="24" :md="12">
            <div class="work-image">
              <img :src="work.image" alt="作品图片" @click="previewImage">
            </div>
          </el-col>
          <el-col :xs="24" :md="12">
            <div class="work-info">
              <h2>{{ work.name }}</h2>
              <p class="work-title">{{ work.title }}</p>
              <p class="work-price">¥{{ work.price }}</p>
              <p class="work-stock" v-if="work.stock !== undefined">
                库存: <span :class="work.stock > 0 ? 'stock-available' : 'stock-out'">
                  {{ work.stock > 0 ? `剩余 ${work.stock} 件` : '已售罄' }}
                </span>
              </p>
              
              <el-divider />
              
              <div class="work-details">
                <el-descriptions :column="1" border>
                  <el-descriptions-item label="作品描述">
                    {{ work.description }}
                  </el-descriptions-item>
                  <el-descriptions-item label="创作匠人">
                    <div class="craftsman-link" @click="goToCraftsmanDetail(work.craftsmanId)">
                      {{ craftsmanName }}
                    </div>
                  </el-descriptions-item>
                  <el-descriptions-item label="创作时间">
                    {{ work.createTime ? formatDate(work.createTime) : '未知' }}
                  </el-descriptions-item>
                </el-descriptions>
              </div>
              
              <el-divider />
              
              <div class="work-actions">
                <el-button type="primary" size="large" class="buy-btn" @click="buyWork">
                  立即购买
                </el-button>
                <el-button type="info" size="large" class="collect-btn" @click="toggleCollection">
                  <el-icon><Star /></el-icon> {{ isCollected ? '已收藏' : '收藏' }}
                </el-button>
              </div>
            </div>
          </el-col>
        </el-row>
        
        <el-divider content-position="center">作品介绍</el-divider>
        
        <div class="work-intro">
          <p>{{ work.introduction || work.description }}</p>
        </div>
        
        <el-divider content-position="center">相关推荐</el-divider>
        
        <div class="related-works">
          <el-row :gutter="20">
            <el-col :xs="24" :sm="12" :md="8" v-for="relatedWork in relatedWorks" :key="relatedWork.id">
              <el-card :body-style="{ padding: '0px' }" @click="goToWorkDetail(relatedWork.id)" style="cursor: pointer;">
                <img :src="relatedWork.image" class="related-work-image" alt="相关作品">
                <div style="padding: 14px;">
                  <h4 style="margin-bottom: 10px;">{{ relatedWork.name }}</h4>
                  <p style="color: #666; margin-bottom: 15px;">{{ relatedWork.title }}</p>
                  <p style="color: #f56c6c; font-weight: bold;">¥{{ relatedWork.price }}</p>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </div>
      <div v-else class="loading">
        <el-spinner size="large" />
      </div>
    </el-main>
  </div>
</template>

<script>
import { Right, Star, ZoomIn } from '@element-plus/icons-vue'
import api from '../api/index'
import TopNav from '../components/TopNav.vue'

export default {
  components: {
    Right,
    Star,
    ZoomIn,
    TopNav
  },
  data() {
    return {
      work: null,
      craftsmanName: '',
      relatedWorks: [],
      isCollected: false
    }
  },
  mounted() {
    this.loadWork()
  },
  watch: {
    '$route.params.id': {
      handler() {
        this.loadWork()
      },
      immediate: true
    }
  },
  methods: {
    loadWork() {
      const id = this.$route.params.id
      api.getWork(id)
        .then(response => {
          this.work = response.data
          this.loadCraftsmanName(response.data.craftsmanId)
          this.loadRelatedWorks(response.data.craftsmanId, id)
        })
        .catch(error => {
          console.error('获取作品详情失败:', error)
        })
    },
    loadCraftsmanName(craftsmanId) {
      api.getCraftsman(craftsmanId)
        .then(response => {
          this.craftsmanName = response.data.name
        })
        .catch(error => {
          console.error('获取匠人信息失败:', error)
        })
    },
    loadRelatedWorks(craftsmanId, currentWorkId) {
      api.getWorksByCraftsman(craftsmanId)
        .then(response => {
          // 过滤掉当前作品，只显示相关作品
          this.relatedWorks = response.data.filter(work => work.id !== parseInt(currentWorkId))
        })
        .catch(error => {
          console.error('获取相关作品失败:', error)
        })
    },
    goToCraftsmanDetail(craftsmanId) {
      this.$router.push(`/craftsman/${craftsmanId}`)
    },
    goToWorkDetail(workId) {
      this.$router.push(`/work/${workId}`)
    },
    previewImage() {
      // 图片预览功能
      const imageUrl = this.work.image
      window.open(imageUrl, '_blank')
    },
    formatDate(dateString) {
      // 日期格式化
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    },
    buyWork() {
      // 立即购买功能
      if (!this.work) return
      
      // 检查库存
      if (this.work.stock !== undefined && this.work.stock <= 0) {
        this.$message.error('库存不足，无法购买')
        return
      }
      
      // 将作品添加到购物车
      const cartItem = {
        id: this.work.id,
        name: this.work.name,
        title: this.work.title,
        price: this.work.price,
        image: this.work.image,
        quantity: 1,
        stock: this.work.stock
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
      
      // 检查作品是否已经在购物车中
      const existingItemIndex = cart.findIndex(item => item.id === cartItem.id)
      if (existingItemIndex >= 0) {
        // 检查库存是否足够
        if (this.work.stock !== undefined && cart[existingItemIndex].quantity + 1 > this.work.stock) {
          this.$message.error('库存不足，无法增加数量')
          return
        }
        // 如果作品已经在购物车中，增加数量
        cart[existingItemIndex].quantity += 1
      } else {
        // 如果作品不在购物车中，添加到购物车
        cart.push(cartItem)
      }
      
      // 将更新后的购物车数据保存到localStorage
      localStorage.setItem('cart', JSON.stringify(cart))
      
      this.$message.success('已加入购物车')
      console.log('购买作品:', this.work.name)
    },
    toggleCollection() {
      // 收藏功能
      if (!this.work) return
      
      // 这里假设用户ID为1，实际应从登录状态中获取
      const userId = 1
      
      if (this.isCollected) {
        // 取消收藏
        api.deleteCollectionByUserTarget(userId, this.work.id, 'work')
          .then(response => {
            this.isCollected = false
            this.$message.success('取消收藏')
            console.log('取消收藏作品:', this.work.name)
          })
          .catch(error => {
            console.error('取消收藏失败:', error)
            this.$message.error('取消收藏失败')
          })
      } else {
        // 添加收藏
        const collection = {
          userId: userId,
          targetId: this.work.id,
          targetType: 'work'
        }
        api.createCollection(collection)
          .then(response => {
            this.isCollected = true
            this.$message.success('收藏成功')
            console.log('收藏作品:', this.work.name)
          })
          .catch(error => {
            console.error('收藏失败:', error)
            this.$message.error('收藏失败')
          })
      }
    }
  }
}
</script>

<style scoped>
.work-detail {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.work-content {
  background-color: white;
  margin: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  padding: 30px;
}

.work-image {
  height: 400px;
  overflow: hidden;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
}

.work-image:hover {
  transform: scale(1.02);
}

.work-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.work-info {
  padding: 0;
}

.work-info h2 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 24px;
}

.work-title {
  margin: 0 0 15px 0;
  color: #666;
  font-size: 16px;
}

.work-price {
  margin: 0 0 10px 0;
  color: #8B4513;
  font-size: 28px;
  font-weight: bold;
}

.work-stock {
  margin: 0 0 20px 0;
  font-size: 14px;
  color: #666;
}

.stock-available {
  color: #67C23A;
  font-weight: bold;
}

.stock-out {
  color: #F56C6C;
  font-weight: bold;
}

.craftsman-link {
  color: #8B4513;
  text-decoration: none;
  font-weight: bold;
  cursor: pointer;
}

.craftsman-link:hover {
  text-decoration: underline;
}

.work-details {
  margin-bottom: 20px;
}



.work-actions {
  display: flex;
  gap: 15px;
  margin-top: 20px;
}

.buy-btn {
  flex: 1;
  background-color: #8B4513;
  border-color: #8B4513;
}

.buy-btn:hover {
  background-color: #6B3410;
  border-color: #6B3410;
}

.collect-btn {
  flex: 1;
}

.work-intro {
  margin: 30px 0;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  line-height: 1.8;
  color: #333;
}

.related-works {
  margin-top: 30px;
}

.related-work-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}

@media (max-width: 768px) {
  .work-content {
    padding: 20px;
  }
  
  .work-image {
    height: 300px;
  }
  
  .work-actions {
    flex-direction: column;
  }
  
  .buy-btn,
  .collect-btn {
    width: 100%;
  }
}
</style>