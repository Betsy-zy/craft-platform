<template>
  <div class="experiences">
    <TopNav />
    <el-page-header :back-text="''" @back="goBack">
      <template #content>
        <span>手作体验</span>
      </template>
    </el-page-header>
    <el-row :gutter="20" style="margin-top: 30px;">
      <el-col :xs="24" :sm="12" :md="8" v-for="experience in experiences" :key="experience.id">
        <div style="display: flex; flex-direction: column; height: 100%;">
          <el-card shadow="hover" style="flex: 1; display: flex; flex-direction: column;">
            <template #header>
              <div class="card-header">
                <span style="font-weight: bold;">{{ experience.name }}</span>
              </div>
            </template>
            <el-descriptions :column="1" border style="flex: 1;">
              <el-descriptions-item label="类型">
                {{ experience.title }}
              </el-descriptions-item>
              <el-descriptions-item label="描述">
                {{ experience.description }}
              </el-descriptions-item>
              <el-descriptions-item label="价格">
                <span style="color: #f56c6c; font-weight: bold;">{{ experience.price }} 元</span>
              </el-descriptions-item>
              <el-descriptions-item label="时长">
                {{ experience.duration }}
              </el-descriptions-item>
            </el-descriptions>
            <el-button type="primary" size="small" style="width: 100%; margin-top: 20px;" @click="bookExperience(experience.id)">立即预约</el-button>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import api from '../api'
import TopNav from '../components/TopNav.vue'

export default {
  name: 'Experiences',
  components: {
    TopNav
  },
  data() {
    return {
      experiences: []
    }
  },
  mounted() {
    this.fetchExperiences()
  },
  methods: {
    fetchExperiences() {
      api.getExperiences().then(res => {
        // 检查返回数据结构，如果有value字段，则使用value，否则直接使用data
        this.experiences = res.data.value || res.data
      }).catch(err => {
        console.error('获取体验项目失败:', err)
        this.$message.error('获取体验项目失败')
      })
    },
    bookExperience(experienceId) {
      const userId = localStorage.getItem('userId')
      if (!userId) {
        this.$message.warning('请先登录后再预约体验')
        this.$router.push('/login')
        return
      }
      this.$router.push(`/appointment?experienceId=${experienceId}`)
    },
    goBack() {
      this.$router.back()
    }
  }
}
</script>

<style scoped>
.experiences {
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