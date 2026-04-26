<template>
  <div class="register">
    <el-card class="register-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="back-btn" @click="goBack">
            <el-icon><ArrowLeft /></el-icon>
          </div>
          <h2>用户注册</h2>
        </div>
      </template>
      <div class="register-content">
        <div class="form-group">
          <div class="form-item avatar-item">
            <label class="form-label">头像</label>
            <el-upload
              class="small-avatar-uploader"
              :show-file-list="false"
              :on-change="handleAvatarChange"
              :auto-upload="true"
              :action="uploadUrl"
            >
              <img v-if="registerForm.avatar" :src="registerForm.avatar" class="small-avatar">
              <el-icon v-else class="small-avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </div>
          <div class="form-item">
            <label class="form-label">* 用户名</label>
            <el-input v-model="registerForm.username" placeholder="请输入用户名" class="form-input"></el-input>
          </div>
          <div class="form-item">
            <label class="form-label">* 密码</label>
            <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" show-password class="form-input"></el-input>
          </div>
          <div class="form-item">
            <label class="form-label">* 确认密码</label>
            <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请确认密码" show-password class="form-input"></el-input>
          </div>
          <div class="form-item">
            <label class="form-label">* 邮箱</label>
            <el-input v-model="registerForm.email" placeholder="请输入邮箱" class="form-input"></el-input>
          </div>
          <div class="form-item">
            <label class="form-label">* 手机号</label>
            <el-input v-model="registerForm.phone" placeholder="请输入手机号" class="form-input"></el-input>
          </div>
          <div class="form-item">
            <el-button type="primary" @click="register" class="form-button">注册</el-button>
          </div>
          <div class="form-item">
            <div class="login-link">
              <span>已有账号？</span>
              <router-link to="/login" style="color: #409EFF;">立即登录</router-link>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import api from '../api'
import { ArrowLeft, Plus } from '@element-plus/icons-vue'

export default {
  name: 'Register',
  components: {
    ArrowLeft,
    Plus
  },
  data() {
    return {
      uploadUrl: 'http://localhost:8080/users/upload',
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
        email: '',
        phone: '',
        avatar: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符之间', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符之间', trigger: 'blur' },
          { pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]+$/, message: '密码必须包含字母和数字', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.registerForm.password) {
                callback(new Error('两次输入的密码不一致'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
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
  methods: {
    handleAvatarChange(file, fileList) {
      if (file.raw) {
        const reader = new FileReader()
        reader.onload = (e) => {
          console.log('头像预览:', e.target.result)
        }
        reader.readAsDataURL(file.raw)
        
        // 上传文件到服务器
        api.uploadUserAvatar(file.raw).then(response => {
          this.registerForm.avatar = response.data
          this.$message.success('头像上传成功')
        }).catch(err => {
          console.error('头像上传失败:', err)
          this.$message.error('头像上传失败')
        })
      }
    },
    register() {
      // 简单的表单验证
      if (!this.registerForm.username || !this.registerForm.password || !this.registerForm.confirmPassword || !this.registerForm.email || !this.registerForm.phone) {
        this.$message.error('请填写所有必填字段')
        return
      }
      
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        this.$message.error('两次输入的密码不一致')
        return
      }
      
      api.register(this.registerForm).then(res => {
        if (res.data) {
          this.$message.success('注册成功，请登录')
          this.$router.push('/login')
        } else {
          this.$message.error('注册失败')
        }
      }).catch(err => {
        console.error('注册失败:', err)
        this.$message.error('注册失败，请稍后重试')
      })
    },
    goBack() {
      this.$router.back()
    }
  }
}
</script>

<style scoped>
.register {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--background-color);
  padding: 0 var(--spacing-sm);
}

.register-card {
  max-width: 500px;
  width: 100%;
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-md);
  border: 1px solid var(--border-color);
  overflow: hidden;
}

.card-header {
  display: flex;
  align-items: center;
  padding: var(--spacing-lg) 30px;
  background-color: var(--primary-color);
  position: relative;
}

.back-btn {
  position: absolute;
  left: 30px;
  font-size: 24px;
  cursor: pointer;
  color: white;
  padding: 8px;
  border-radius: 50%;
  transition: all 0.3s;
}

.back-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.card-header h2 {
  margin: 0 auto;
  font-size: var(--font-size-xxlarge);
  color: var(--white);
  font-family: var(--font-family);
  font-weight: bold;
}

.register-content {
  padding: var(--spacing-xl);
  display: flex;
  justify-content: center;
}

.form-group {
  width: 100%;
  max-width: 400px;
}

.form-item {
  margin-bottom: 24px;
  text-align: center;
}

.form-label {
  display: block;
  text-align: left;
  margin-bottom: 8px;
  font-family: var(--font-family);
  color: var(--text-color);
  font-weight: 500;
  font-size: 16px;
}

.form-input {
  width: 100%;
}

.form-input .el-input__inner {
  font-family: var(--font-family) !important;
  border-radius: var(--border-radius-sm) !important;
  height: 48px;
  font-size: 16px;
}

.form-button {
  width: 100%;
  font-family: var(--font-family) !important;
  border-radius: var(--border-radius-sm) !important;
  height: 48px;
  font-size: 16px;
  font-weight: 500;
}

.login-link {
  text-align: center;
  font-family: var(--font-family);
}

.avatar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.small-avatar-uploader .el-upload {
  border: 1px dashed var(--border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--transition);
  width: 50px;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.small-avatar-uploader .el-upload:hover {
  border-color: var(--primary-color);
}

.small-avatar-uploader-icon {
  font-size: 14px;
  color: #909399;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.small-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

@media (max-width: 768px) {
  .register-card {
    max-width: 90%;
  }
  
  .register-content {
    padding: var(--spacing-md);
  }
  
  .card-header h2 {
    font-size: var(--font-size-large);
  }
  
  .form-group {
    max-width: 100%;
  }
}
</style>