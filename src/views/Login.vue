<template>
  <div class="login">
    <el-card class="login-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="back-btn" @click="goBack">
            <el-icon><ArrowLeft /></el-icon>
          </div>
          <h2>用户登录</h2>
        </div>
      </template>
      <div class="login-content">
        <div class="form-group">
          <div class="form-item">
            <label class="form-label">* 用户名</label>
            <el-input v-model="loginForm.username" placeholder="请输入用户名" class="form-input"></el-input>
          </div>
          <div class="form-item">
            <label class="form-label">* 密码</label>
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password class="form-input"></el-input>
          </div>
          <div class="form-item">
            <el-button type="primary" @click="login" class="form-button">登录</el-button>
          </div>
          <div class="form-item">
            <div class="register-link">
              <span>还没有账号？</span>
              <router-link to="/register" style="color: #409EFF;">立即注册</router-link>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import api from '../api'
import { ArrowLeft } from '@element-plus/icons-vue'

export default {
  name: 'Login',
  components: {
    ArrowLeft
  },
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符之间', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符之间', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    login() {
      // 简单的表单验证
      if (!this.loginForm.username || !this.loginForm.password) {
        this.$message.error('请输入用户名和密码')
        return
      }
      
      api.login(this.loginForm.username, this.loginForm.password).then(res => {
        if (res.data) {
          localStorage.setItem('user', JSON.stringify(res.data))
          localStorage.setItem('userId', res.data.id)
          // 管理员登录跳转到管理后台，普通用户跳转到首页
          if (res.data.role === 'admin') {
            this.$router.push('/admin')
            this.$message.success('管理员登录成功')
          } else {
            this.$router.push('/home')
            this.$message.success('用户登录成功')
          }
        } else {
          this.$message.error('用户名或密码错误')
        }
      }).catch(err => {
        console.error('登录失败:', err)
        this.$message.error('登录失败，请稍后重试')
      })
    },
    goBack() {
      this.$router.back()
    }
  }
}
</script>

<style scoped>
.login {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--background-color);
  padding: 0 var(--spacing-sm);
}

.login-card {
  max-width: 400px;
  width: 100%;
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-md);
  border: 1px solid var(--border-color);
  overflow: hidden;
}

.card-header {
  display: flex;
  align-items: center;
  padding: var(--spacing-md) 20px;
  background-color: var(--primary-color);
  position: relative;
}

.back-btn {
  position: absolute;
  left: 20px;
  font-size: 20px;
  cursor: pointer;
  color: white;
  padding: 5px;
  border-radius: 50%;
  transition: all 0.3s;
}

.back-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.card-header h2 {
  margin: 0 auto;
  font-size: var(--font-size-xlarge);
  color: var(--white);
  font-family: var(--font-family);
  font-weight: bold;
}

.login-content {
  padding: var(--spacing-lg);
  display: flex;
  justify-content: center;
}

.form-group {
  width: 100%;
  max-width: 300px;
}

.form-item {
  margin-bottom: 20px;
  text-align: center;
}

.form-label {
  display: block;
  text-align: left;
  margin-bottom: 5px;
  font-family: var(--font-family);
  color: var(--text-color);
  font-weight: 500;
}

.form-input {
  width: 100%;
}

.form-input .el-input__inner {
  font-family: var(--font-family) !important;
  border-radius: var(--border-radius-sm) !important;
  height: 40px;
  font-size: 14px;
}

.form-button {
  width: 100%;
  font-family: var(--font-family) !important;
  border-radius: var(--border-radius-sm) !important;
  height: 40px;
  font-size: 14px;
  font-weight: 500;
}

.register-link {
  text-align: center;
  font-family: var(--font-family);
}

@media (max-width: 768px) {
  .login-card {
    max-width: 90%;
  }
  
  .login-content {
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