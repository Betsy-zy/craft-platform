<template>
  <div class="order-management">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item><a href="/admin/dashboard">管理后台</a></el-breadcrumb-item>
      <el-breadcrumb-item>订单管理</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="management-header">
      <h2>订单管理</h2>
    </div>

    <el-card class="management-card">
      <div class="card-header" style="margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center;">
        <div></div>
        <el-button type="danger" @click="batchDeleteOrders" :disabled="selectedOrders.length === 0">
          批量删除 ({{ selectedOrders.length }})
        </el-button>
      </div>
      <el-table :data="orders" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="订单ID" width="100"></el-table-column>
        <el-table-column prop="username" label="用户名称"></el-table-column>
        <el-table-column prop="workName" label="商品名称"></el-table-column>
        <el-table-column prop="quantity" label="数量"></el-table-column>
        <el-table-column prop="totalPrice" label="总价"></el-table-column>
        <el-table-column label="状态">
          <template #default="scope">
            {{ formatStatus(scope.row.status) }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间">
          <template #default="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-select v-model="scope.row.status" @change="updateOrderStatus(scope.row)">
              <el-option label="待处理" value="pending"></el-option>
              <el-option label="已发货" value="shipped"></el-option>
            </el-select>
            <el-button size="small" type="danger" @click="deleteOrder(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import api from '../../api'

export default {
  name: 'OrderManagement',
  data() {
    return {
      orders: [],
      selectedOrders: []
    }
  },
  mounted() {
    this.loadOrders()
  },
  methods: {
    loadOrders() {
      api.getProductOrdersWithDetails().then(res => {
        this.orders = res.data
      }).catch(err => {
        console.error('获取订单列表失败:', err)
      })
    },
    updateOrderStatus(order) {
      api.updateProductOrderStatus(order.id, order.status).then(() => {
        this.$message.success('订单状态更新成功')
        this.loadOrders()
      }).catch(err => {
        console.error('更新订单状态失败:', err)
        this.$message.error('更新订单状态失败')
        this.loadOrders() // 恢复原状态
      })
    },
    deleteOrder(id) {
      this.$confirm('确定要删除这个订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteOrder(id).then(() => {
          this.$message.success('订单删除成功')
          this.loadOrders()
        }).catch(err => {
          console.error('删除订单失败:', err)
          this.$message.error('删除订单失败')
        })
      })
    },
    handleSelectionChange(selection) {
      this.selectedOrders = selection
    },
    batchDeleteOrders() {
      if (this.selectedOrders.length === 0) {
        this.$message.warning('请选择要删除的订单')
        return
      }
      
      this.$confirm(`确定要删除这 ${this.selectedOrders.length} 个订单吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const deletePromises = this.selectedOrders.map(order => api.deleteOrder(order.id))
        Promise.all(deletePromises).then(() => {
          this.$message.success('批量删除成功')
          this.loadOrders()
          this.selectedOrders = []
        }).catch(err => {
          console.error('批量删除失败:', err)
          this.$message.error('批量删除失败')
        })
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
        'pending': '待处理',
        'paid': '已支付',
        'shipped': '已发货',
        'completed': '已完成',
        'cancelled': '已取消'
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style scoped>
.order-management {
  padding: 20px;
  min-height: 100vh;
  background-color: var(--background-color);
}

.management-header {
  margin-bottom: 30px;
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

@media (max-width: 768px) {
  .order-management {
    padding: 10px;
  }
  
  .management-header h2 {
    font-size: 20px;
  }
}
</style>