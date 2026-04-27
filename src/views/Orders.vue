<template>
  <div class="orders">
    <TopNav />
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item><a href="/">首页</a></el-breadcrumb-item>
      <el-breadcrumb-item>我的订单</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="orders-header">
      <h2>我的订单</h2>
    </div>

    <div v-if="orders.length > 0" class="orders-content">
      <el-table :data="orders" style="width: 100%">
        <el-table-column prop="id" label="订单编号" width="180"></el-table-column>
        <el-table-column prop="workName" label="作品名称" min-width="200"></el-table-column>
        <el-table-column prop="quantity" label="数量" width="100"></el-table-column>
        <el-table-column prop="totalPrice" label="总价" width="120">
          <template #default="scope">
            ¥{{ scope.row.totalPrice.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="scope">
            {{ getStatusText(scope.row.status) }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="200">
          <template #default="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewOrderDetail(scope.row.id)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div v-else class="empty-orders">
      <el-empty description="暂无订单" />
      <el-button type="primary" @click="goToWorks">去购物</el-button>
    </div>
  </div>
</template>

<script>
import api from '../api'
import TopNav from '../components/TopNav.vue'

export default {
  name: 'Orders',
  components: {
    TopNav
  },
  data() {
    return {
      orders: []
    }
  },
  mounted() {
    this.loadOrders()
  },
  methods: {
    loadOrders() {
      const userId = localStorage.getItem('userId')
      if (userId) {
        api.getUserProductOrders(userId).then(res => {
          this.orders = res.data
        })
      }
    },
    viewOrderDetail(id) {
      this.$router.push(`/order/${id}`)
    },
    goToWorks() {
      this.$router.push('/works')
    },
    formatDateTime(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    getStatusText(status) {
      const statusMap = {
        'pending': '待处理',
        'processing': '处理中',
        'completed': '已完成',
        'cancelled': '已取消'
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style scoped>
.orders-container {
  padding: 20px;
}

.orders-header {
  margin-bottom: 30px;
}

.orders-header h2 {
  font-size: 24px;
  color: #C83C23;
  font-family: '思源宋体', 'Microsoft YaHei', sans-serif;
}

.orders-content {
  margin-bottom: 40px;
}

.empty-orders {
  text-align: center;
  padding: 60px 0;
}

.empty-orders .el-button {
  margin-top: 20px;
}
</style>