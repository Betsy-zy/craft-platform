<template>
  <div class="information">
    <TopNav />
    <el-page-header :back-text="''" @back="goBack">
      <template #content>
        <span>工艺科普资讯</span>
      </template>
    </el-page-header>
    <el-row :gutter="20" style="margin-top: 30px;">
      <el-col :xs="24" :sm="12" :md="8" v-for="info in information" :key="info.id">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span style="font-weight: bold;">{{ info.title }}</span>
            </div>
          </template>
          <div style="margin-bottom: 15px;">
            <el-image :src="getImageForInfo(info)" fit="cover" style="width: 100%; height: 150px; border-radius: 4px;"></el-image>
          </div>
          <p style="margin-bottom: 15px; color: #666;">{{ info.content.substring(0, 100) }}...</p>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <el-tag size="small" type="info">{{ formatDate(info.createTime) }}</el-tag>
            <el-button type="primary" size="small" @click="goToInfoDetail(info.id)">查看详情</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import api from '../api'
import TopNav from '../components/TopNav.vue'

export default {
  name: 'Information',
  components: {
    TopNav
  },
  data() {
    return {
      information: []
    }
  },
  mounted() {
    this.fetchInformation()
  },
  methods: {
    fetchInformation() {
      api.getAllInformation().then(res => {
        this.information = res.data.filter(info => info.status === 'active')
      }).catch(err => {
        console.error('获取资讯列表失败:', err)
        this.$message.error('获取资讯列表失败')
      })
    },
    formatDate(dateString) {
      const date = new Date(dateString)
      return date.toLocaleDateString()
    },
    goToInfoDetail(id) {
      this.$router.push(`/information/${id}`)
    },
    goBack() {
      this.$router.back()
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
.information {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 隐藏返回按钮文本 */
:deep(.el-page-header__left .el-page-header__back) span {
  display: none;
}
</style>