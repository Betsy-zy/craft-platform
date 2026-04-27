<template>
  <div class="craftsmen">
    <TopNav />
    <el-page-header :back-text="''" @back="goBack">
      <template #content>
        <span>非遗匠人</span>
      </template>
    </el-page-header>
    <el-row :gutter="20" style="margin-top: 30px;">
      <el-col :xs="24" :sm="12" :md="8" v-for="craftsman in craftsmen" :key="craftsman.id">
        <div style="display: flex; flex-direction: column; height: 100%;">
          <el-card shadow="hover" style="flex: 1; display: flex; flex-direction: column;">
            <template #header>
              <div class="card-header">
                <span style="font-weight: bold;">{{ craftsman.name }}</span>
              </div>
            </template>
            <div style="text-align: center; margin-bottom: 20px;">
              <el-avatar :size="150" :src="craftsman.avatar" style="margin-bottom: 15px;"></el-avatar>
              <el-tag type="info" size="medium" style="margin-bottom: 10px;">{{ craftsman.title }}</el-tag>
            </div>
            <el-descriptions :column="1" border style="flex: 1;">
              <el-descriptions-item label="简介">
                {{ craftsman.description }}
              </el-descriptions-item>
              <el-descriptions-item label="联系方式">
                {{ craftsman.contact }}
              </el-descriptions-item>
            </el-descriptions>
            <el-button type="primary" size="small" style="width: 100%; margin-top: 20px;" @click="goToCraftsmanDetail(craftsman.id)">查看作品</el-button>
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
  name: 'Craftsmen',
  components: {
    TopNav
  },
  data() {
    return {
      craftsmen: []
    }
  },
  mounted() {
    this.fetchCraftsmen()
  },
  methods: {
    fetchCraftsmen() {
      api.getCraftsmen().then(res => {
        this.craftsmen = res.data
      }).catch(err => {
        console.error('获取匠人列表失败:', err)
        this.$message.error('获取匠人列表失败')
      })
    },
    goToCraftsmanDetail(id) {
      this.$router.push(`/craftsman/${id}`)
    },
    goBack() {
      this.$router.back()
    }
  }
}
</script>

<style scoped>
.craftsmen {
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