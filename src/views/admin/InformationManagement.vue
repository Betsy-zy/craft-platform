<template>
  <div class="information-management">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item><a href="/admin/dashboard">管理后台</a></el-breadcrumb-item>
      <el-breadcrumb-item>资讯管理</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="management-header">
      <h2>资讯管理</h2>
      <el-button type="primary" @click="openAddInfoDialog">添加资讯</el-button>
    </div>

    <el-card class="management-card">
      <el-table :data="informations" style="width: 100%">
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="title" label="标题" min-width="150"></el-table-column>
        <el-table-column label="封面图片" width="120" align="center">
          <template #default="scope">
            <div style="display: flex; align-items: center; justify-content: center;">
              <el-image
                v-if="scope.row.image"
                :src="scope.row.image"
                fit="cover"
                style="width: 60px; height: 60px;"
              ></el-image>
              <div v-else style="width: 60px; height: 60px; background-color: #f0f0f0; display: flex; align-items: center; justify-content: center; font-size: 12px;">
                无图片
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="内容" min-width="200" show-overflow-tooltip></el-table-column>
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
            <el-button size="small" type="primary" @click="viewInfoDetail(scope.row)">查看详情</el-button>
            <el-button size="small" type="primary" @click="editInfo(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteInfo(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑资讯对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="form.id ? '编辑资讯' : '添加资讯'"
      width="600px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="封面图片">
          <el-upload
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :on-change="handleImageChange"
            :file-list="imageFileList"
            accept="image/*"
          >
            <el-button type="primary">选择图片</el-button>
            <template #tip>
              <div class="el-upload__tip">
                只能上传jpg/png/gif格式的图片，且不超过2MB
              </div>
            </template>
          </el-upload>
          <el-input v-model="form.image" placeholder="图片路径" style="margin-top: 10px;" v-if="form.image"></el-input>
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
          <el-button type="primary" @click="saveInfo">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="资讯详情预览"
      width="700px"
    >
      <div v-if="detailInfo" class="info-detail-preview">
        <h2 class="detail-title">{{ detailInfo.title }}</h2>
        <div class="detail-meta">
          <span>{{ formatDate(detailInfo.createTime) }}</span>
          <el-tag :type="detailInfo.status === 'active' ? 'success' : 'info'" size="small">
            {{ detailInfo.status === 'active' ? '显示' : '隐藏' }}
          </el-tag>
        </div>
        <div class="detail-image" v-if="detailInfo.image">
          <img :src="detailInfo.image" alt="资讯图片">
        </div>
        <div class="detail-content" v-html="detailInfo.content"></div>
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
  name: 'InformationManagement',
  data() {
    return {
      informations: [],
      dialogVisible: false,
      detailDialogVisible: false,
      detailInfo: null,
      form: {
        id: '',
        title: '',
        content: '',
        image: '',
        status: 'active'
      },
      imageFileList: []
    }
  },
  mounted() {
    this.loadInformations()
  },
  methods: {
    handleImageChange(file, fileList) {
      this.imageFileList = fileList
      if (file.raw) {
        const reader = new FileReader()
        reader.onload = (e) => {
          console.log('图片预览:', e.target.result)
        }
        reader.readAsDataURL(file.raw)
        
        api.uploadWorkImage(file.raw).then(response => {
          this.form.image = response.data
          this.$message.success('图片上传成功')
        }).catch(err => {
          console.error('图片上传失败:', err)
          this.$message.error('图片上传失败')
        })
      }
    },
    loadInformations() {
      api.getAllInformation().then(res => {
        this.informations = res.data
      }).catch(err => {
        console.error('获取资讯列表失败:', err)
        this.$message.error('获取资讯列表失败')
      })
    },
    openAddInfoDialog() {
      this.form = {
        id: '',
        title: '',
        content: '',
        image: '',
        status: 'active'
      }
      this.imageFileList = []
      this.dialogVisible = true
    },
    saveInfo() {
      if (!this.form.title) {
        this.$message.error('请输入标题')
        return
      }
      if (!this.form.content) {
        this.$message.error('请输入内容')
        return
      }
      
      if (this.form.id) {
        api.updateInformation(this.form).then(() => {
          this.$message.success('资讯更新成功')
          this.dialogVisible = false
          this.loadInformations()
        }).catch(err => {
          console.error('更新资讯失败:', err)
          this.$message.error('更新资讯失败')
        })
      } else {
        api.createInformation(this.form).then(() => {
          this.$message.success('资讯添加成功')
          this.dialogVisible = false
          this.loadInformations()
        }).catch(err => {
          console.error('添加资讯失败:', err)
          this.$message.error('添加资讯失败')
        })
      }
    },
    editInfo(info) {
      this.form = { ...info }
      this.imageFileList = []
      this.dialogVisible = true
    },
    viewInfoDetail(info) {
      this.detailInfo = info
      this.detailDialogVisible = true
    },
    deleteInfo(id) {
      this.$confirm('确定要删除这条资讯吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteInformation(id).then(() => {
          this.$message.success('资讯已删除')
          this.loadInformations()
        }).catch(err => {
          console.error('删除资讯失败:', err)
          this.$message.error('删除资讯失败')
        })
      })
    },
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString()
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
.information-management {
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

@media (max-width: 768px) {
  .information-management {
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

.info-detail-preview {
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
  gap: 15px;
  margin-bottom: 20px;
  color: #999;
  font-size: 14px;
}

.detail-image {
  margin-bottom: 20px;
}

.detail-image img {
  width: 100%;
  max-height: 300px;
  object-fit: cover;
  border-radius: 8px;
}

.detail-content {
  line-height: 1.8;
  color: #333;
  font-size: 16px;
}
</style>
