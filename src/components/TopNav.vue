<template>
  <el-header height="60px" class="header">
    <div class="header-content">
      <h1>非遗手作平台</h1>
      <div class="desktop-menu">
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" background-color="#333" text-color="white" active-text-color="#ffd04b" :menu-trigger="'none'">
          <!-- 管理员只显示管理后台 -->
          <template v-if="isAdmin">
            <el-menu-item index="12"><router-link to="/admin">管理后台</router-link></el-menu-item>
            <el-menu-item index="10"><span @click="logout">退出登录</span></el-menu-item>
          </template>
          <!-- 普通用户显示完整菜单 -->
          <template v-else>
            <el-menu-item index="1"><router-link to="/home">首页</router-link></el-menu-item>
            <el-menu-item index="2"><router-link to="/craftsmen">匠人</router-link></el-menu-item>
            <el-menu-item index="3"><router-link to="/works">作品</router-link></el-menu-item>
            <el-menu-item index="4"><router-link to="/experiences">体验</router-link></el-menu-item>
            <el-menu-item index="5"><router-link to="/information">资讯</router-link></el-menu-item>
            <el-menu-item v-if="isLoggedIn" index="7"><router-link to="/user-center">个人中心</router-link></el-menu-item>
            <template v-else>
              <el-menu-item index="10"><router-link to="/login">登录</router-link></el-menu-item>
            </template>
          </template>
        </el-menu>
      </div>
      <div class="mobile-menu">
        <el-dropdown trigger="click">
          <el-button type="primary" plain>
            菜单 <el-icon><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <!-- 管理员只显示管理后台 -->
              <template v-if="isAdmin">
                <el-dropdown-item><router-link to="/admin">管理后台</router-link></el-dropdown-item>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </template>
              <!-- 普通用户显示完整菜单 -->
              <template v-else>
                <el-dropdown-item><router-link to="/home">首页</router-link></el-dropdown-item>
                <el-dropdown-item><router-link to="/craftsmen">匠人</router-link></el-dropdown-item>
                <el-dropdown-item><router-link to="/works">作品</router-link></el-dropdown-item>
                <el-dropdown-item><router-link to="/experiences">体验</router-link></el-dropdown-item>
                <el-dropdown-item><router-link to="/information">资讯</router-link></el-dropdown-item>
                <el-dropdown-item v-if="isLoggedIn"><router-link to="/user-center">个人中心</router-link></el-dropdown-item>
                <template v-else>
                  <el-dropdown-item><router-link to="/login">登录</router-link></el-dropdown-item>
                </template>
              </template>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </el-header>
</template>

<script>
import { ArrowDown } from '@element-plus/icons-vue'

export default {
  name: 'TopNav',
  components: {
    ArrowDown
  },
  data() {
    return {
      activeIndex: '1'
    }
  },
  computed: {
    currentUser() {
      const userStr = localStorage.getItem('user')
      return userStr ? JSON.parse(userStr) : null
    },
    isLoggedIn() {
      return !!this.currentUser
    },
    isAdmin() {
      return this.currentUser && this.currentUser.role === 'admin'
    }
  },
  mounted() {
    console.log('Current user:', this.currentUser)
    console.log('Is logged in:', this.isLoggedIn)
    console.log('Is admin:', this.isAdmin)
  },
  watch: {
    $route: {
      handler() {
        this.updateActiveIndex()
      },
      immediate: true
    },
    currentUser: {
      handler() {
        console.log('Current user changed:', this.currentUser)
        console.log('Is logged in:', this.isLoggedIn)
        console.log('Is admin:', this.isAdmin)
      },
      deep: true
    }
  },
  methods: {
    updateActiveIndex() {
      const path = this.$route.path
      if (path === '/home') {
        this.activeIndex = '1'
      } else if (path === '/craftsmen') {
        this.activeIndex = '2'
      } else if (path === '/works') {
        this.activeIndex = '3'
      } else if (path === '/experiences') {
        this.activeIndex = '4'
      } else if (path === '/information') {
        this.activeIndex = '5'
      } else if (path === '/user-center') {
        this.activeIndex = '7'
      }
    },
    logout() {
      localStorage.removeItem('user')
      localStorage.removeItem('userId')
      this.$message.success('退出登录成功')
      window.location.href = '/login'
    }
  }
}
</script>

<style scoped>
.header {
  background-color: var(--primary-color) !important;
  box-shadow: var(--shadow-md);
}

.header-content {
  display: flex;
  align-items: center;
  padding: 0 var(--spacing-lg);
  height: 100%;
  width: 100%;
  justify-content: space-between;
}

.header-content h1 {
  font-family: var(--font-family);
  font-size: var(--font-size-xlarge);
  color: var(--white);
  font-weight: bold;
  margin: 0;
  flex-shrink: 0;
}

.desktop-menu {
  display: flex;
  flex: 1;
  justify-content: center;
  margin: 0 20px;
}

.el-menu {
  width: 100%;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
}

.el-menu--horizontal {
  overflow-x: auto;
  white-space: nowrap;
  flex-wrap: nowrap;
  width: 100%;
  display: flex;
  justify-content: space-around;
}

.mobile-menu {
  display: none;
}

.el-menu {
  background-color: var(--primary-color) !important;
}

.el-menu-item {
  color: var(--white) !important;
  font-family: var(--font-family);
  font-size: 14px !important;
  padding: 0 10px !important;
}

.el-menu-item.is-active {
  color: #ffd04b !important;
}

@media (max-width: 768px) {
  .desktop-menu {
    display: none;
  }
  
  .mobile-menu {
    display: block;
  }
  
  .header-content {
    padding: 0 var(--spacing-sm);
  }
  
  .header-content h1 {
    font-size: var(--font-size-large);
  }
  
  .el-dropdown-menu {
    min-width: 160px;
  }
  
  .el-dropdown-item {
    font-family: var(--font-family);
  }
}
</style>