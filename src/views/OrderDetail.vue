<template>
  <div class="order-detail-container">
    <TopNav />
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item><a href="/">首页</a></el-breadcrumb-item>
      <el-breadcrumb-item><a href="/orders">我的订单</a></el-breadcrumb-item>
      <el-breadcrumb-item>订单详情</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="order-detail-header">
      <h2>订单详情</h2>
    </div>

    <div v-if="order" class="order-detail-content">
      <el-card class="order-info-card">
        <template #header>
          <div class="card-header">
            <span>订单信息</span>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单编号">
            {{ order.id }}
          </el-descriptions-item>
          <el-descriptions-item label="订单状态">
            {{ getStatusText(order.status) }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(order.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="总价">
            ¥{{ order.totalPrice.toFixed(2) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="order-product-card" style="margin-top: 20px;">
        <template #header>
          <div class="card-header">
            <span>作品信息</span>
          </div>
        </template>
        <el-table :data="[order]" style="width: 100%">
          <el-table-column prop="workName" label="作品名称" min-width="200"></el-table-column>
          <el-table-column prop="quantity" label="数量" width="100"></el-table-column>
          <el-table-column prop="totalPrice" label="总价" width="120">
            <template #default="scope">
              ¥{{ scope.row.totalPrice.toFixed(2) }}
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <div style="text-align: center; margin-top: 30px;">
        <el-button type="primary" @click="goBack">返回订单列表</el-button>
      </div>
    </div>

    <div v-else class="loading">
      <el-icon class="is-loading" style="font-size: 48px;">
        <Loading />
      </el-icon>
    </div>
  </div>
</template>

<script>
import { Loading } from '@element-plus/icons-vue'
import api from '../api/index'
import TopNav from '../components/TopNav.vue'

export default {
  components: {
    Loading,
    TopNav
  },
  name: 'OrderDetail',
  data() {
    return {
      order: null,
      work: null
    }
  },
  mounted() {
    this.loadOrderDetail()
  },
  methods: {
    loadOrderDetail() {
      const orderId = this.$route.params.id
      api.getProductOrder(orderId).then(res => {
        this.order = res.data
        if (res.data.workId) {
          this.loadWorkDetail(res.data.workId)
        }
      }).catch(err => {
        console.error('获取订单详情失败:', err)
        this.$message.error('获取订单详情失败')
      })
    },
    loadWorkDetail(workId) {
      api.getWork(workId).then(res => {
        this.work = res.data
        if (this.order) {
          this.order.workName = res.data.name
        }
      }).catch(err => {
        console.error('获取作品详情失败:', err)
      })
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
    },
    goBack() {
      this.$router.push({
        path: '/user-center',
        query: { activeMenu: 'orders' }
      })
    }
  }
}
</script>

<style scoped>
.order-detail-container {
  padding: 20px;
}

.order-detail-header {
  margin-bottom: 30px;
}

.order-detail-header h2 {
  font-size: 24px;
  color: #C83C23;
  font-family: '思源宋体', 'Microsoft YaHei', sans-serif;
}

.order-detail-content {
  margin-bottom: 40px;
}

.card-header {
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: bold;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}
</style>