<template>
  <div class="works">
    <TopNav />
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item><a href="/">首页</a></el-breadcrumb-item>
      <el-breadcrumb-item>非遗作品</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="works-header">
      <h2>非遗手作作品</h2>
      <p>精选传统工艺制作的精美作品</p>
    </div>

    <div class="works-grid">
      <el-card
        v-for="work in works"
        :key="work.id"
        class="work-card"
        @click="goToWorkDetail(work.id)"
      >
        <img :src="work.image" :alt="work.name" class="work-image" />
        <div class="work-info">
          <h3>{{ work.name }}</h3>
          <p class="work-title">{{ work.title }}</p>
          <p class="work-price">¥{{ work.price.toFixed(2) }}</p>
          <div class="work-footer">
            <el-button type="primary" size="small" @click.stop="addToCart(work)">
              加入购物车
            </el-button>
            <el-button size="small" @click.stop="toggleCollection(work)">
              <i :class="isCollected(work.id) ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
              {{ isCollected(work.id) ? '已收藏' : '收藏' }}
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <el-pagination
      v-if="total > 12"
      :current-page="currentPage"
      :page-size="12"
      layout="prev, pager, next"
      :total="total"
      @current-change="handleCurrentChange"
      class="pagination"
    />
  </div>
</template>

<script>
import api from '../api'
import TopNav from '../components/TopNav.vue'

export default {
  name: 'Works',
  components: {
    TopNav
  },
  data() {
    return {
      works: [],
      currentPage: 1,
      total: 0,
      collections: []
    }
  },
  mounted() {
    this.fetchWorks()
    this.loadCollections()
  },
  methods: {
    fetchWorks() {
      api.getWorks().then(res => {
        this.works = res.data
        this.total = res.data.length
      }).catch(err => {
        console.error('获取作品列表失败:', err)
        this.$message.error('获取作品列表失败')
      })
    },
    loadCollections() {
      const userId = localStorage.getItem('userId')
      if (userId) {
        api.getUserCollections(userId).then(res => {
          this.collections = res.data
        })
      }
    },
    goToWorkDetail(id) {
      this.$router.push(`/work/${id}`)
    },
    addToCart(work) {
      const cart = JSON.parse(localStorage.getItem('cart') || '[]')
      const existingItem = cart.find(item => item.id === work.id)
      if (existingItem) {
        existingItem.quantity++
      } else {
        cart.push({ ...work, quantity: 1 })
      }
      localStorage.setItem('cart', JSON.stringify(cart))
      this.$message.success('已加入购物车')
    },
    toggleCollection(work) {
      const userId = localStorage.getItem('userId')
      if (!userId) {
        this.$message.warning('请先登录')
        return
      }
      
      const isCollected = this.isCollected(work.id)
      if (isCollected) {
        api.deleteCollectionByUserTarget(userId, work.id, 'work').then(() => {
          this.$message.success('已取消收藏')
          this.loadCollections()
        })
      } else {
        api.createCollection({ userId: parseInt(userId), targetId: work.id, targetType: 'work' }).then(() => {
          this.$message.success('已收藏')
          this.loadCollections()
        })
      }
    },
    isCollected(workId) {
      return this.collections.some(item => item.targetId === workId && item.targetType === 'work')
    },
    handleCurrentChange(val) {
      this.currentPage = val
      // 这里可以添加分页逻辑
    }
  }
}
</script>

<style scoped>
.works {
  padding: 20px;
}

.works-header {
  text-align: center;
  margin-bottom: 40px;
}

.works-header h2 {
  font-size: 32px;
  color: #C83C23;
  margin-bottom: 10px;
  font-family: '思源宋体', 'Microsoft YaHei', sans-serif;
}

.works-header p {
  font-size: 16px;
  color: #666;
}

.works-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.work-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #F8F5F0;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.work-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.work-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.work-info {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.work-info h3 {
  font-size: 18px;
  margin-bottom: 5px;
  color: #2D2D2D;
}

.work-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.work-price {
  font-size: 20px;
  font-weight: bold;
  color: #C83C23;
  margin-bottom: 15px;
}

.work-footer {
  display: flex;
  justify-content: space-between;
  margin-top: auto;
  padding-top: 15px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}
</style>