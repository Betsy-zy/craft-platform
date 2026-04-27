<template>
  <div class="user-management">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item><a href="/admin/dashboard">管理后台</a></el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="management-header">
      <h2>用户管理</h2>
    </div>

    <el-card class="management-card">
      <el-table :data="users" style="width: 100%">
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="username" label="用户名" width="120"></el-table-column>
        <el-table-column label="头像" width="120" align="center">
          <template #default="scope">
            <div style="display: flex; align-items: center; justify-content: center;">
              <el-image
                :src="scope.row.avatar || 'https://img.yzcdn.cn/vant/cat.jpeg'"
                fit="cover"
                style="width: 60px; height: 60px; border-radius: 50%;"
              ></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="180"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="120"></el-table-column>
        <el-table-column label="角色" width="80">
          <template #default="scope">
            {{ formatRole(scope.row.role) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="scope">
            {{ formatStatus(scope.row.status) }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间" min-width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" type="primary" @click="editUser(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteUser(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑用户对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="'编辑用户'"
      width="500px"
    >
      <el-form :model="form" label-width="100px" :rules="rules" ref="userForm">
        <el-form-item label="头像">
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
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role">
            <el-option label="用户" value="user"></el-option>
            <el-option label="管理员" value="admin"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="活跃" value="active"></el-option>
            <el-option label="禁用" value="inactive"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveUser">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from '../../api'

export default {
  name: 'UserManagement',
  data() {
    return {
      users: [],
      dialogVisible: false,
      form: {
        id: '',
        username: '',
        email: '',
        phone: '',
        role: 'user',
        status: 'active',
        avatar: ''
      },
      avatarFileList: [],
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符之间', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadUsers()
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
        api.uploadUserAvatar(file.raw).then(response => {
          this.form.avatar = response.data;
          this.$message.success('头像上传成功');
        }).catch(err => {
          console.error('头像上传失败:', err);
          this.$message.error('头像上传失败');
        });
      }
    },
    loadUsers() {
      api.getUsers().then(res => {
        this.users = res.data
      }).catch(err => {
        console.error('获取用户列表失败:', err)
      })
    },
    saveUser() {
      this.$refs.userForm.validate((valid) => {
        if (valid) {
          if (this.form.id) {
            // 更新用户
            api.updateUser(this.form).then(() => {
              this.$message.success('用户更新成功')
              this.dialogVisible = false
              this.loadUsers()
            }).catch(err => {
              console.error('更新用户失败:', err)
              this.$message.error('更新用户失败')
            })
          }
        } else {
          this.$message.error('请检查输入的格式是否正确')
          return false
        }
      })
    },
    editUser(user) {
      this.form = { ...user }
      this.avatarFileList = []
      this.dialogVisible = true
    },
    deleteUser(id) {
      this.$confirm('确定要删除这个用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        console.log('删除用户ID:', id, '类型:', typeof id)
        api.deleteUser(Number(id)).then(() => {
          this.$message.success('用户删除成功')
          this.loadUsers()
        }).catch(err => {
          console.error('删除用户失败:', err)
          this.$message.error('删除用户失败')
        })
      }).catch(() => {
        // 用户取消删除操作
      })
    },
    formatDateTime(dateTimeString) {
      if (!dateTimeString) return ''
      const date = new Date(dateTimeString)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`
    },
    formatStatus(status) {
      const statusMap = {
        'active': '活跃',
        'inactive': '禁用'
      }
      return statusMap[status] || status
    },
    formatRole(role) {
      const roleMap = {
        'user': '用户',
        'admin': '管理员'
      }
      return roleMap[role] || role
    }
  }
}
</script>

<style scoped>
.user-management {
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
  .user-management {
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