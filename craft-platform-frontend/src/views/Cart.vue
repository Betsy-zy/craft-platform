<template>
  <div class="cart">
    <TopNav />
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item><a href="/">首页</a></el-breadcrumb-item>
      <el-breadcrumb-item>购物车</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="cart-header">
      <h2>我的购物车</h2>
    </div>

    <div v-if="cart.length > 0" class="cart-content">
      <el-table :data="cart" style="width: 100%">
        <el-table-column prop="name" label="作品名称" min-width="200">
          <template #default="scope">
            <div class="product-info">
              <div v-if="scope.row.image" class="product-image-container">
                <img :src="scope.row.image" :alt="scope.row.name" class="product-image" />
              </div>
              <div class="product-details">
                <div class="product-name">{{ scope.row.name || '未知作品' }}</div>
                <div v-if="scope.row.title" class="product-title">{{ scope.row.title }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="单价" width="100">
          <template #default="scope">
            ¥{{ (scope.row.price || 0).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="180">
          <template #default="scope">
            <el-input-number
              v-model="scope.row.quantity"
              :min="1"
              :max="scope.row.stock || 999"
              @change="updateQuantity(scope.row)"
            ></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="小计" width="120">
          <template #default="scope">
            ¥{{ ((scope.row.price || 0) * (scope.row.quantity || 0)).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="danger" size="small" @click="removeFromCart(scope.row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="cart-footer">
        <div class="total-price">
          <span>合计：</span>
          <span class="price">¥{{ totalPrice.toFixed(2) }}</span>
        </div>
        <div class="cart-actions">
          <el-button @click="clearCart">清空购物车</el-button>
          <el-button type="primary" @click="checkout">结算</el-button>
        </div>
      </div>
    </div>

    <div v-else class="empty-cart">
      <el-empty description="购物车是空的" />
      <el-button type="primary" @click="goToWorks">去购物</el-button>
    </div>
  </div>
</template>

<script>
import api from '../api'
import TopNav from '../components/TopNav.vue'

export default {
  name: 'Cart',
  components: {
    TopNav
  },
  data() {
    return {
      cart: []
    }
  },
  computed: {
    totalPrice() {
      return this.cart.reduce((total, item) => total + item.price * item.quantity, 0)
    }
  },
  mounted() {
    this.loadCart()
  },
  methods: {
    loadCart() {
      const cart = localStorage.getItem('cart')
      if (cart) {
        try {
          this.cart = JSON.parse(cart)
        } catch (error) {
          console.error('解析购物车数据失败:', error)
          this.cart = []
          localStorage.setItem('cart', JSON.stringify(this.cart))
        }
      } else {
        this.cart = []
      }
    },
    updateQuantity(item) {
      localStorage.setItem('cart', JSON.stringify(this.cart))
    },
    removeFromCart(id) {
      this.cart = this.cart.filter(item => item.id !== id)
      localStorage.setItem('cart', JSON.stringify(this.cart))
      this.$message.success('已从购物车中删除')
    },
    clearCart() {
      this.$confirm('确定要清空购物车吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.cart = []
        localStorage.setItem('cart', JSON.stringify(this.cart))
        this.$message.success('购物车已清空')
      })
    },
    checkout() {
      const userId = localStorage.getItem('userId')
      if (!userId) {
        this.$message.warning('请先登录')
        this.$router.push('/login')
        return
      }

      // 实现结算逻辑
      const orderPromises = this.cart.map(item => {
        // 创建订单
        const order = {
          userId: parseInt(userId),
          workId: item.id,
          quantity: item.quantity,
          totalPrice: item.price * item.quantity,
          status: 'pending'
        }
        // 调用API创建订单
        return api.createProductOrder(order)
      })

      Promise.all(orderPromises).then(() => {
        // 清空购物车
        this.cart = []
        localStorage.setItem('cart', JSON.stringify(this.cart))
        this.$message.success('订单提交成功')
        this.$router.push('/orders')
      }).catch(error => {
        console.error('创建订单失败:', error)
        this.$message.error('订单提交失败，请稍后重试')
      })
    },
    goToWorks() {
      this.$router.push('/works')
    }
  }
}
</script>

<style scoped>
.cart-container {
  padding: 20px;
}

.cart-header {
  margin-bottom: 30px;
}

.cart-header h2 {
  font-size: 24px;
  color: #C83C23;
  font-family: '思源宋体', 'Microsoft YaHei', sans-serif;
}

.cart-content {
  margin-bottom: 40px;
}

.product-info {
  display: flex;
  align-items: flex-start;
  padding: 10px 0;
}

.product-image-container {
  margin-right: 15px;
}

.product-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.product-details {
  flex: 1;
  min-width: 0;
}

.product-name {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 5px;
  color: #333;
}

.product-title {
  font-size: 12px;
  color: #999;
  line-height: 1.4;
}

.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #E8E8E8;
}

.total-price {
  font-size: 18px;
}

.total-price .price {
  font-size: 24px;
  font-weight: bold;
  color: #C83C23;
  margin-left: 10px;
}

.cart-actions {
  display: flex;
  gap: 10px;
}

.empty-cart {
  text-align: center;
  padding: 60px 0;
}

.empty-cart .el-button {
  margin-top: 20px;
}
</style>