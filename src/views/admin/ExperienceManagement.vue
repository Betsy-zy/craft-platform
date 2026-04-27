<template>
  <div class="experience-management">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item><a href="/admin/dashboard">管理后台</a></el-breadcrumb-item>
      <el-breadcrumb-item>体验管理</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="management-header">
      <h2>体验管理</h2>
      <el-button type="primary" @click="openAddExperienceDialog">添加体验</el-button>
    </div>

    <el-card class="management-card">
      <el-table :data="experiences" style="width: 100%">
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="name" label="体验名称" width="120"></el-table-column>
        <el-table-column prop="title" label="体验类型" min-width="150"></el-table-column>
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="price" label="价格" width="80"></el-table-column>
        <el-table-column prop="duration" label="时长" width="80"></el-table-column>
        <el-table-column label="匠人" width="120">
          <template #default="scope">
            {{ getCraftsmanName(scope.row.craftsmanId) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'active' ? 'success' : 'info'">
              {{ scope.row.status === 'active' ? '显示' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240">
          <template #default="scope">
            <el-button size="small" type="primary" @click="viewExperienceDetail(scope.row)">查看详情</el-button>
            <el-button size="small" type="primary" @click="editExperience(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteExperience(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑体验对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="form.id ? '编辑体验' : '添加体验'"
      width="600px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="体验名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="体验类型">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="form.price" type="number"></el-input>
        </el-form-item>
        <el-form-item label="时长">
          <el-input v-model="form.duration"></el-input>
        </el-form-item>
        <el-form-item label="匠人">
          <el-select v-model="form.craftsmanId" placeholder="请选择匠人" style="width: 100%;">
            <el-option v-for="craftsman in craftsmen" :key="craftsman.id" :label="craftsman.name" :value="craftsman.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%;">
            <el-option label="显示" value="active"></el-option>
            <el-option label="隐藏" value="inactive"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveExperience">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="体验详情预览"
      width="600px"
    >
      <div v-if="detailExperience" class="experience-detail-preview">
        <h2 class="detail-title">{{ detailExperience.name }}</h2>
        <div class="detail-meta">
          <span class="detail-type">类型：{{ detailExperience.title }}</span>
          <span class="detail-duration">时长：{{ detailExperience.duration }}</span>
        </div>
        <div class="detail-description">
          <h3>描述</h3>
          <p>{{ detailExperience.description }}</p>
        </div>
        <div class="detail-price">
          <span>价格：</span>
          <el-tag type="danger" size="large">{{ detailExperience.price }} 元</el-tag>
        </div>

        <div class="detail-craftsman">
          <span>匠人：</span>
          <el-tag type="primary">{{ getCraftsmanName(detailExperience.craftsmanId) }}</el-tag>
        </div>

        <div class="detail-status">
          <span>状态：</span>
          <el-tag :type="detailExperience.status === 'active' ? 'success' : 'info'">
            {{ detailExperience.status === 'active' ? '显示' : '隐藏' }}
          </el-tag>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from '../../api'

export default {
  name: 'ExperienceManagement',
  data() {
    return {
      experiences: [],
      craftsmen: [],
      dialogVisible: false,
      detailDialogVisible: false,
      detailExperience: null,
      form: {
        id: '',
        name: '',
        title: '',
        description: '',
        price: 0,
        duration: '',
        craftsmanId: '',
        status: 'active'
      }
    }
  },
  mounted() {
    this.loadExperiences()
    this.loadCraftsmen()
  },
  methods: {
    loadExperiences() {
      api.getExperiences().then(res => {
        // 检查返回数据结构，如果有value字段，则使用value，否则直接使用data
        this.experiences = res.data.value || res.data
      }).catch(err => {
        console.error('获取体验列表失败:', err)
        this.$message.error('获取体验列表失败')
      })
    },
    loadCraftsmen() {
      api.getAllCraftsmen().then(res => {
        this.craftsmen = res.data
      }).catch(err => {
        console.error('获取匠人列表失败:', err)
        this.$message.error('获取匠人列表失败')
      })
    },
    openAddExperienceDialog() {
      this.form = {
        id: '',
        name: '',
        title: '',
        description: '',
        price: 0,
        duration: '',
        craftsmanId: '',
        status: 'active'
      }
      this.dialogVisible = true
    },
    saveExperience() {
      if (!this.form.name) {
        this.$message.error('请输入体验名称')
        return
      }
      if (!this.form.title) {
        this.$message.error('请输入体验类型')
        return
      }
      if (!this.form.description) {
        this.$message.error('请输入描述')
        return
      }
      if (!this.form.price) {
        this.$message.error('请输入价格')
        return
      }
      if (!this.form.duration) {
        this.$message.error('请输入时长')
        return
      }
      if (!this.form.craftsmanId) {
        this.$message.error('请选择匠人')
        return
      }
      
      if (this.form.id) {
        api.updateExperience(this.form).then(() => {
          this.$message.success('体验更新成功')
          this.dialogVisible = false
          this.loadExperiences()
        }).catch(err => {
          console.error('更新体验失败:', err)
          this.$message.error('更新体验失败')
        })
      } else {
        api.createExperience(this.form).then(() => {
          this.$message.success('体验添加成功')
          this.dialogVisible = false
          this.loadExperiences()
        }).catch(err => {
          console.error('添加体验失败:', err)
          this.$message.error('添加体验失败')
        })
      }
    },
    editExperience(experience) {
      this.form = { ...experience }
      this.dialogVisible = true
    },
    viewExperienceDetail(experience) {
      this.detailExperience = experience
      this.detailDialogVisible = true
    },
    deleteExperience(id) {
      this.$confirm('确定要删除这个体验吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteExperience(id).then(() => {
          this.$message.success('体验已删除')
          this.loadExperiences()
        }).catch(err => {
          console.error('删除体验失败:', err)
          this.$message.error('删除体验失败')
        })
      })
    },

    getCraftsmanName(craftsmanId) {
      if (!craftsmanId) return ''
      const craftsman = this.craftsmen.find(c => c.id === craftsmanId)
      return craftsman ? craftsman.name : '未知匠人'
    },
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`
    }
  }
}
</script>

<style scoped>
.experience-management {
  padding: 20px;
  min-height: 100vh;
  background-color: var(--background-color);
}

.management-header {
  margin-bottom: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.management-header h2 {
  font-size: 24px;
  color: var(--primary-color);
  font-family: var(--font-family);
  margin: 0;
}

.management-card {
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-md);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.experience-detail-preview {
  padding: 10px;
}

.detail-title {
  margin-top: 0;
  margin-bottom: 15px;
  color: #333;
  font-size: 24px;
  text-align: center;
}

.detail-meta {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
  color: #666;
  font-size: 14px;
}

.detail-description {
  margin-bottom: 20px;
}

.detail-description h3 {
  margin-top: 0;
  margin-bottom: 10px;
  color: #333;
  font-size: 16px;
}

.detail-description p {
  line-height: 1.6;
  color: #666;
  margin: 0;
}

.detail-price {
  margin-bottom: 15px;
  font-size: 16px;
  color: #333;
}

.detail-craftsman {
  margin-bottom: 15px;
  font-size: 16px;
  color: #333;
}

.detail-status {
  font-size: 16px;
  color: #333;
}

@media (max-width: 768px) {
  .experience-management {
    padding: 10px;
  }
  
  .management-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .management-header h2 {
    font-size: 20px;
  }
}
</style>