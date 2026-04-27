<template>
  <div class="craftsman-management">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item><a href="/admin/dashboard">管理后台</a></el-breadcrumb-item>
      <el-breadcrumb-item>匠人管理</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="management-header">
      <h2>匠人管理</h2>
      <el-button type="primary" @click="openAddCraftsmanDialog">添加匠人</el-button>
    </div>

    <el-card class="management-card">
      <el-table :data="craftsmen" style="width: 100%">
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="name" label="姓名" width="120"></el-table-column>
        <el-table-column label="头像" width="120" align="center">
          <template #default="scope">
            <div style="display: flex; align-items: center; justify-content: center;">
              <el-image
                v-if="scope.row.avatar"
                :src="scope.row.avatar"
                fit="cover"
                style="width: 60px; height: 60px; border-radius: 50%;"
              ></el-image>
              <div v-else style="width: 60px; height: 60px; background-color: #f0f0f0; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 12px;">
                无头像
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="头衔" min-width="150"></el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="scope">
            {{ formatStatus(scope.row.status) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" type="primary" @click="editCraftsman(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteCraftsman(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加匠人对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="添加匠人"
      width="500px"
    >
      <el-form :model="form" label-width="100px" :rules="rules" ref="form">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="头衔" prop="title">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <el-upload
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :on-change="handleAvatarChange"
            :file-list="avatarFileList"
            accept="image/*"
          >
            <el-button type="primary">选择头像</el-button>
            <template #tip>
              <div class="el-upload__tip">
                只能上传jpg/png/gif格式的图片，且不超过2MB
              </div>
            </template>
          </el-upload>
          <el-input v-model="form.avatar" placeholder="头像路径" style="margin-top: 10px;" v-if="form.avatar"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="form.contact"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status">
            <el-option label="活跃" value="active"></el-option>
            <el-option label="非活跃" value="inactive"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveCraftsman">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from '../../api'

export default {
  name: 'CraftsmanManagement',
  data() {
    return {
      craftsmen: [],
      dialogVisible: false,
      form: {
        name: '',
        title: '',
        description: '',
        avatar: '',
        contact: '',
        status: 'active'
      },
      avatarFileList: [],
      rules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        title: [
          { required: true, message: '请输入头衔', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入描述', trigger: 'blur' }
        ],
        avatar: [
          { required: true, message: '请输入头像地址', trigger: 'blur' }
        ],
        contact: [
          { required: true, message: '请输入联系方式', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadCraftsmen()
  },
  methods: {
    handleAvatarChange(file, fileList) {
      this.avatarFileList = fileList;
      if (file.raw) {
        const reader = new FileReader();
        reader.onload = (e) => {
          // 这里可以处理图片预览
          console.log('头像预览:', e.target.result);
        };
        reader.readAsDataURL(file.raw);
        
        // 上传文件到服务器
        api.uploadCraftsmanAvatar(file.raw).then(response => {
          this.form.avatar = response.data;
          this.$message.success('头像上传成功');
        }).catch(err => {
          console.error('头像上传失败:', err);
          this.$message.error('头像上传失败');
        });
      }
    },
    loadCraftsmen() {
      api.getAllCraftsmen().then(res => {
        this.craftsmen = res.data
      }).catch(err => {
        console.error('获取匠人列表失败:', err)
      })
    },
    openAddCraftsmanDialog() {
      this.form = {
        name: '',
        title: '',
        description: '',
        avatar: '',
        contact: '',
        status: 'active'
      }
      this.avatarFileList = []
      this.dialogVisible = true
    },
    saveCraftsman() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.form.id) {
            // 更新匠人
            api.updateCraftsman(this.form).then(() => {
              this.$message.success('匠人更新成功')
              this.dialogVisible = false
              this.loadCraftsmen()
            }).catch(err => {
              console.error('更新匠人失败:', err)
              this.$message.error('更新匠人失败')
            })
          } else {
            // 添加匠人
            api.createCraftsman(this.form).then(() => {
              this.$message.success('匠人添加成功')
              this.dialogVisible = false
              this.loadCraftsmen()
            }).catch(err => {
              console.error('添加匠人失败:', err)
              this.$message.error('添加匠人失败')
            })
          }
        } else {
          this.$message.error('请填写所有必填字段')
          return false
        }
      })
    },
    editCraftsman(craftsman) {
      this.form = { ...craftsman }
      this.avatarFileList = []
      this.dialogVisible = true
    },
    deleteCraftsman(id) {
      this.$confirm('确定要删除这个匠人吗？删除匠人会同时删除其关联的体验记录。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 先获取并删除关联的体验记录
        api.getExperiencesByCraftsman(id).then(experiencesResponse => {
          const experiences = experiencesResponse.data || []
          const deletePromises = experiences.map(exp => api.deleteExperience(exp.id))
          
          Promise.all(deletePromises).then(() => {
            // 所有体验记录删除成功后，再删除匠人
            api.deleteCraftsman(id).then(() => {
              this.$message.success('匠人删除成功')
              this.loadCraftsmen()
            }).catch(err => {
              console.error('删除匠人失败:', err)
              this.$message.error('删除匠人失败')
            })
          }).catch(err => {
            console.error('删除关联体验记录失败:', err)
            this.$message.error('删除关联体验记录失败')
          })
        }).catch(err => {
          // 如果获取体验记录失败，直接尝试删除匠人
          console.error('获取关联体验记录失败:', err)
          api.deleteCraftsman(id).then(() => {
            this.$message.success('匠人删除成功')
            this.loadCraftsmen()
          }).catch(err => {
            console.error('删除匠人失败:', err)
            this.$message.error('删除匠人失败')
          })
        })
      })
    },
    formatStatus(status) {
      const statusMap = {
        'active': '活跃',
        'inactive': '非活跃'
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style scoped>
.craftsman-management {
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
  .craftsman-management {
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