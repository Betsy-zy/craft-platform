<template>
  <div class="info-detail">
    <el-header height="60px" class="header">
      <div class="header-content">
        <div class="back-btn" @click="goBack">
          <el-icon>
            <arrow-left />
          </el-icon>
        </div>
        <h1>资讯详情</h1>
      </div>
    </el-header>
    <el-main>
      <div v-if="info" class="info-content">
        <h2>{{ info.title }}</h2>
        <div class="info-meta">
          <span>{{ formatDate(info.createTime) }}</span>
        </div>
        <div class="info-image">
          <img :src="getImageForInfo(info)" alt="资讯图片">
        </div>
        <div class="info-text" v-html="info.content"></div>
      </div>
      <div v-else class="loading">
        <el-icon class="is-loading" style="font-size: 48px;">
          <loading />
        </el-icon>
      </div>
    </el-main>
  </div>
</template>

<script>
import { ArrowLeft, Loading } from '@element-plus/icons-vue'
import api from '../api/index'

export default {
  components: {
    ArrowLeft,
    Loading
  },
  data() {
    return {
      info: null
    }
  },
  mounted() {
    this.loadInfo()
  },
  methods: {
    loadInfo() {
      const id = this.$route.params.id
      api.getInformation(id)
        .then(response => {
          this.info = response.data
        })
        .catch(error => {
          console.error('获取资讯详情失败:', error)
        })
    },
    goBack() {
      this.$router.back()
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return d.toLocaleDateString()
    },
    getImageForInfo(info) {
      if (info.image) {
        return info.image
      }
      return '/images/info1-1.jpg'
    }
  }
}
</script>

<style scoped>
.info-detail {
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

.info-content {
  background-color: white;
  padding: 20px;
  margin: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.info-content h2 {
  margin-top: 0;
  color: #333;
  font-size: 24px;
}

.info-meta {
  color: #999;
  margin-bottom: 20px;
  font-size: 14px;
}

.info-image {
  margin-bottom: 20px;
}

.info-image img {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 8px;
}

.info-text {
  line-height: 1.8;
  color: #333;
  font-size: 16px;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}
</style>