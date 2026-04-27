<template>
  <div class="admin">
    <div class="admin-top-nav">
      <div class="nav-left">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>管理后台</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="nav-right">

        <el-button type="danger" plain @click="logout">退出登录</el-button>
      </div>
    </div>

    <div class="admin-header">
      <h2>管理后台</h2>
    </div>

    <!-- 数据统计 -->
    <div class="stats-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-item">
              <div class="stats-icon">
                <User />
              </div>
              <div class="stats-content">
                <div class="stats-value">{{ userCount }}</div>
                <div class="stats-label">用户总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-item">
              <div class="stats-icon">
                <Picture />
              </div>
              <div class="stats-content">
                <div class="stats-value">{{ workCount }}</div>
                <div class="stats-label">作品总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-item">
              <div class="stats-icon">
                <Document />
              </div>
              <div class="stats-content">
                <div class="stats-value">{{ orderCount }}</div>
                <div class="stats-label">订单总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-item">
              <div class="stats-icon">
                <Calendar />
              </div>
              <div class="stats-content">
                <div class="stats-value">{{ appointmentCount }}</div>
                <div class="stats-label">预约总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 数据图表 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>订单趋势</span>
              </div>
            </template>
            <div id="orderChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>用户分布</span>
              </div>
            </template>
            <div id="userChart" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="admin-content">
      <el-card class="admin-card">
        <template #header>
          <div class="card-header">
            <span>管理功能</span>
          </div>
        </template>
        <div class="admin-menu">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="menu-card" @click="goToManage('users')">
                <User class="menu-icon" />
                <h3>用户管理</h3>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="menu-card" @click="goToManage('works')">
                <Picture class="menu-icon" />
                <h3>作品管理</h3>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="menu-card" @click="goToManage('appointments')">
                <Calendar class="menu-icon" />
                <h3>预约管理</h3>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="menu-card" @click="goToManage('orders')">
                <Document class="menu-icon" />
                <h3>订单管理</h3>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="menu-card" @click="goToManage('craftsmen')">
                <Avatar class="menu-icon" />
                <h3>匠人管理</h3>
              </div>
            </el-col>

            <el-col :span="8">
              <div class="menu-card" @click="goToManage('information')">
                <Document class="menu-icon" />
                <h3>资讯管理</h3>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="menu-card" @click="goToManage('experiences')">
                <Calendar class="menu-icon" />
                <h3>体验管理</h3>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </div>

    <!-- 子路由视图 -->
    <router-view></router-view>
  </div>
</template>

<script>
import { User, Goods, Calendar, Document, Avatar, Picture } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import api from '../api'

export default {
  name: 'Admin',
  components: {
    User,
    Goods,
    Calendar,
    Document,
    Avatar,
    Picture
  },
  data() {
    return {
      userCount: 0,
      workCount: 0,
      orderCount: 0,
      appointmentCount: 0,
      users: [],
      orders: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      // 从后端API获取真实数据
      Promise.all([
        api.getUsers(),
        api.getWorks(),
        api.getProductOrders(),
        api.getAppointments()
      ]).then(([usersRes, worksRes, ordersRes, appointmentsRes]) => {
        this.users = usersRes.data
        this.orders = ordersRes.data
        
        // 计算统计数据
        this.userCount = usersRes.data.length
        this.workCount = worksRes.data.length
        this.orderCount = ordersRes.data.length
        this.appointmentCount = appointmentsRes.data.length
        
        // 初始化图表
        this.initCharts()
      }).catch(err => {
        console.error('获取数据失败:', err)
        this.$message.error('获取数据失败')
      })
    },
    initCharts() {
      // 订单趋势图表
      const orderChart = echarts.init(document.getElementById('orderChart'))
      
      // 处理订单数据，按月份统计
      const orderData = this.processOrderData()
      
      const orderOption = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['订单数量']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: orderData.months
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '订单数量',
            type: 'line',
            stack: 'Total',
            data: orderData.counts
          }
        ]
      }
      orderChart.setOption(orderOption)

      // 用户分布图表
      const userChart = echarts.init(document.getElementById('userChart'))
      
      // 处理用户数据，按角色统计
      const userData = this.processUserData()
      
      const userOption = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: '用户类型',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: userData
          }
        ]
      }
      userChart.setOption(userOption)

      // 响应式图表
      window.addEventListener('resize', function() {
        orderChart.resize()
        userChart.resize()
      })
    },
    processOrderData() {
      // 按月统计订单数量
      const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
      const counts = new Array(12).fill(0)
      
      this.orders.forEach(order => {
        const date = new Date(order.createTime)
        const month = date.getMonth() // 0-11
        counts[month]++
      })
      
      return { months, counts }
    },
    processUserData() {
      // 按角色统计用户数量
      const roleCount = {
        'user': 0,
        'admin': 0,
        'craftsman': 0
      }
      
      this.users.forEach(user => {
        if (roleCount.hasOwnProperty(user.role)) {
          roleCount[user.role]++
        } else {
          roleCount['user']++ // 默认用户角色
        }
      })
      
      return [
        { value: roleCount.user, name: '普通用户' },
        { value: roleCount.craftsman, name: '匠人' },
        { value: roleCount.admin, name: '管理员' }
      ]
    },
    goToManage(type) {
      switch (type) {
        case 'users':
          this.$router.push('/admin/users')
          break
        case 'appointments':
          this.$router.push('/admin/appointments')
          break
        case 'orders':
          this.$router.push('/admin/orders')
          break
        case 'craftsmen':
          this.$router.push('/admin/craftsmen')
          break
        case 'works':
          this.$router.push('/admin/works')
          break

        case 'information':
          this.$router.push('/admin/information')
          break
        case 'experiences':
          this.$router.push('/admin/experiences')
          break
      }
    },
    logout() {
      localStorage.removeItem('user')
      localStorage.removeItem('userId')
      this.$message.success('退出登录成功')
      window.location.href = '/login'
    },

  }
}
</script>

<style scoped>
.admin {
  padding: 20px;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.admin-top-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.nav-left {
  flex: 1;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.bell-icon {
  font-size: 20px;
}

.notification-badge {
  position: absolute;
  top: -5px;
  right: -5px;
}

.dropdown-item-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.dropdown-badge {
  margin-left: 10px;
}

.admin-header {
  margin-bottom: 30px;
}

.admin-header h2 {
  font-size: 24px;
  color: #303133;
  font-family: 'Microsoft YaHei', sans-serif;
  font-weight: 600;
  margin: 0;
}

/* 数据统计部分 */
.stats-section {
  margin-bottom: 30px;
}

.stats-card {
  border-radius: 4px;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.05);
}

.stats-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
}

.stats-icon {
  width: 50px;
  height: 50px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  background-color: #ecf5ff;
  color: #409eff;
}

.stats-content {
  flex: 1;
}

.stats-value {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 2px;
}

.stats-label {
  font-size: 12px;
  color: #909399;
}

/* 图表部分 */
.charts-section {
  margin-bottom: 30px;
}

.chart-card {
  border-radius: 4px;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.05);
}

.chart {
  height: 300px;
  width: 100%;
}

/* 功能模块部分 */
.admin-content {
  margin-top: 30px;
}

.admin-card {
  border-radius: 4px;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.05);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 15px;
  border-bottom: 1px solid #ebeef5;
}

.card-header span {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.admin-menu {
  padding: 15px;
}

.menu-card {
  transition: all 0.2s ease;
  cursor: pointer;
  text-align: center;
  padding: 20px 15px;
  border-radius: 4px;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
  background-color: white;
  border: 1px solid #ebeef5;
}

.menu-card:hover {
  box-shadow: 0 2px 6px 0 rgba(0, 0, 0, 0.15);
  border-color: #409eff;
}

.menu-icon {
  font-size: 36px;
  color: #409eff;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 70px;
  height: 70px;
  margin: 0 auto 15px;
  background-color: #ecf5ff;
  border-radius: 50%;
}

.menu-card h3 {
  font-size: 16px;
  color: #303133;
  font-family: 'Microsoft YaHei', sans-serif;
  font-weight: 500;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .admin {
    padding: 10px;
  }
  
  .admin-header h2 {
    font-size: 20px;
  }
  
  .stats-item {
    flex-direction: column;
    text-align: center;
    gap: 10px;
  }
  
  .chart {
    height: 250px;
  }
  
  .menu-card {
    padding: 20px 10px;
  }
  
  .menu-icon {
    font-size: 24px;
    width: 50px;
    height: 50px;
  }
  
  .el-col {
    margin-bottom: 15px;
  }
}

@media (max-width: 1024px) {
  .el-col {
    margin-bottom: 15px;
  }
}
</style>