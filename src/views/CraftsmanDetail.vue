<template>
  <div class="craftsman-detail">
    <TopNav />
    <el-main>
      <div v-if="craftsman" class="craftsman-content">
        <div class="craftsman-info">
          <div class="craftsman-avatar">
            <img :src="craftsman.avatar" alt="匠人头像" v-if="craftsman.avatar">
            <div class="avatar-placeholder" v-else>{{ craftsman.name.charAt(0) }}</div>
          </div>
          <div class="craftsman-basic">
            <h2>{{ craftsman.name }}</h2>
            <p class="craftsman-title">{{ craftsman.title }}</p>
            <p class="craftsman-description">{{ craftsman.description }}</p>
            <div class="craftsman-contact">
              <el-icon><Phone /></el-icon>
              <span>{{ craftsman.contact }}</span>
            </div>
          </div>
        </div>
        
        <div class="craftsman-works">
          <h3>匠人作品</h3>
          <div class="works-grid">
            <div v-for="work in works" :key="work.id" class="work-item" @click="goToWorkDetail(work.id)">
              <div class="work-image">
                <img :src="work.image" alt="作品图片">
              </div>
              <div class="work-info">
                <h4>{{ work.name }}</h4>
                <p class="work-price">¥{{ work.price }}</p>
              </div>
            </div>
            <div v-if="works.length === 0" class="no-works">
              <p>该匠人暂无作品</p>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="loading">
        <el-spinner size="large" />
      </div>
    </el-main>
  </div>
</template>

<script>
import { Phone } from '@element-plus/icons-vue'
import api from '../api/index'
import TopNav from '../components/TopNav.vue'

export default {
  components: {
    Phone,
    TopNav
  },
  data() {
    return {
      craftsman: null,
      works: []
    }
  },
  mounted() {
    this.loadCraftsman()
  },
  methods: {
    loadCraftsman() {
      const id = this.$route.params.id
      api.getCraftsman(id)
        .then(response => {
          if (response.data) {
            this.craftsman = response.data
            this.loadCraftsmanWorks(id)
          } else {
            console.error('获取匠人详情失败: 数据为空')
            this.$message.error('获取匠人详情失败')
          }
        })
        .catch(error => {
          console.error('获取匠人详情失败:', error)
          this.$message.error('获取匠人详情失败')
        })
    },
    loadCraftsmanWorks(craftsmanId) {
      api.getWorksByCraftsman(craftsmanId)
        .then(response => {
          this.works = response.data || []
        })
        .catch(error => {
          console.error('获取匠人作品失败:', error)
          this.works = []
        })
    },
    goToWorkDetail(workId) {
      this.$router.push(`/work/${workId}`)
    }
  }
}
</script>

<style scoped>
.craftsman-detail {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.craftsman-content {
  background-color: white;
  margin: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.craftsman-info {
  display: flex;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.craftsman-avatar {
  width: 100px;
  height: 100px;
  margin-right: 20px;
}

.craftsman-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: #8B4513;
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 40px;
  font-weight: bold;
}

.craftsman-basic {
  flex: 1;
}

.craftsman-basic h2 {
  margin: 0 0 10px 0;
  color: #333;
}

.craftsman-title {
  margin: 0 0 10px 0;
  color: #666;
  font-size: 14px;
}

.craftsman-description {
  margin: 0 0 15px 0;
  color: #666;
  line-height: 1.5;
}

.craftsman-contact {
  display: flex;
  align-items: center;
  color: #8B4513;
}

.craftsman-contact span {
  margin-left: 5px;
}

.craftsman-works {
  padding: 20px;
}

.craftsman-works h3 {
  margin: 0 0 20px 0;
  color: #333;
}

.works-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.work-item {
  cursor: pointer;
  transition: all 0.3s;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.work-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.work-image {
  height: 150px;
  overflow: hidden;
}

.work-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.work-info {
  padding: 10px;
}

.work-info h4 {
  margin: 0 0 5px 0;
  color: #333;
  font-size: 14px;
}

.work-price {
  margin: 0;
  color: #8B4513;
  font-weight: bold;
}

.no-works {
  grid-column: 1 / -1;
  text-align: center;
  padding: 40px 0;
  color: #999;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}
</style>