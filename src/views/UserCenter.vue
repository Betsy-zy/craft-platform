<template>
  <div class="user-center">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item><router-link to="/home">首页</router-link></el-breadcrumb-item>
      <el-breadcrumb-item>个人中心</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="user-header">
      <h2>个人中心</h2>
    </div>

    <el-row :gutter="20">
      <el-col :span="6">
        <div class="user-sidebar">
          <div class="user-profile">
            <div class="avatar">
              <el-avatar :size="80" :src="user.avatar || 'https://img.yzcdn.cn/vant/cat.jpeg'">
                {{ user.username ? user.username.charAt(0).toUpperCase() : 'U' }}
              </el-avatar>
            </div>
            <div class="profile-info">
              <h3>{{ user.username }}</h3>
              <p>{{ user.role === 'admin' ? '管理员' : '普通用户' }}</p>
            </div>
          </div>
          
          <el-menu
            :default-active="activeMenu"
            class="user-menu"
            router
          >
            <el-menu-item index="/user-center" @click="setActiveMenu('profile')">
              <template #title>
                <el-icon><User /></el-icon>
                <span>个人信息</span>
              </template>
            </el-menu-item>
            <el-menu-item index="/user-center" @click="setActiveMenu('orders')">
              <template #title>
                <el-icon><Document /></el-icon>
                <span>我的订单</span>
              </template>
            </el-menu-item>
            <el-menu-item index="/user-center" @click="setActiveMenu('messages')">
              <template #title>
                <el-icon><Bell /></el-icon>
                <span>消息通知</span>
                <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="message-badge" />
              </template>
            </el-menu-item>
            <el-menu-item index="/user-center" @click="setActiveMenu('favorites')">
              <template #title>
                <el-icon><Star /></el-icon>
                <span>我的收藏</span>
              </template>
            </el-menu-item>
            <el-menu-item index="/user-center" @click="setActiveMenu('cart')">
              <template #title>
                <el-icon><ShoppingCart /></el-icon>
                <span>购物车</span>
              </template>
            </el-menu-item>
            <el-menu-item index="/user-center" @click="setActiveMenu('works')">
              <template #title>
                <el-icon><Picture /></el-icon>
                <span>浏览作品</span>
              </template>
            </el-menu-item>
            <el-menu-item @click="logout">
              <template #title>
                <el-icon><Switch /></el-icon>
                <span>退出登录</span>
              </template>
            </el-menu-item>
          </el-menu>
        </div>
      </el-col>

      <el-col :span="18">
        <!-- 个人信息 -->
        <el-card v-if="activeMenu === 'profile'" class="user-profile-card">
          <template #header>
            <div class="card-header">
              <span>个人信息</span>
            </div>
          </template>
          <div class="user-info">
            <div class="info-item">
              <span class="label">用户名：</span>
              <span class="value">{{ user.username }}</span>
            </div>
            <div class="info-item">
              <span class="label">邮箱：</span>
              <span class="value">{{ user.email }}</span>
            </div>
            <div class="info-item">
              <span class="label">手机号：</span>
              <span class="value">{{ user.phone }}</span>
            </div>
            <div class="info-item">
              <span class="label">角色：</span>
              <span class="value">{{ user.role === 'admin' ? '管理员' : '普通用户' }}</span>
            </div>
            <div class="info-item">
              <span class="label">注册时间：</span>
              <span class="value">{{ formatDateTime(user.createTime) }}</span>
            </div>
            <el-button type="primary" @click="editUserInfo" style="margin-top: 20px;">编辑信息</el-button>
          </div>
        </el-card>

        <!-- 我的订单 -->
        <el-card v-if="activeMenu === 'orders'" class="user-orders-card">
          <template #header>
            <div class="card-header">
              <span>我的订单</span>
            </div>
          </template>
          <div v-if="orders.length > 0" class="orders-content">
            <el-table :data="orders" style="width: 100%">
              <el-table-column prop="id" label="订单编号" width="180"></el-table-column>
              <el-table-column prop="workId" label="作品ID" width="100"></el-table-column>
              <el-table-column prop="quantity" label="数量" width="100"></el-table-column>
              <el-table-column prop="totalPrice" label="总价" width="120">
                <template #default="scope">
                  ¥{{ scope.row.totalPrice.toFixed(2) }}
                </template>
              </el-table-column>
              <el-table-column label="状态" width="120">
                <template #default="scope">
                  {{ formatStatus(scope.row.status) }}
                </template>
              </el-table-column>
              <el-table-column label="创建时间" width="200">
                <template #default="scope">
                  {{ formatDateTime(scope.row.createTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="250">
                <template #default="scope">
                  <el-button type="primary" size="small" @click="viewOrderDetail(scope.row.id)">
                    查看详情
                  </el-button>
                  <el-button type="danger" size="small" @click="cancelOrder(scope.row.id)" v-if="scope.row.status === 'pending' || scope.row.status === 'paid'">
                    取消订单
                  </el-button>
                  <el-button type="danger" size="small" @click="deleteOrder(scope.row.id)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div v-else class="empty-orders">
            <el-empty description="暂无订单" />
            <el-button type="primary" @click="goToWorks">去购物</el-button>
          </div>
        </el-card>

        <!-- 消息通知 -->
        <el-card v-if="activeMenu === 'messages'" class="user-messages-card">
          <template #header>
            <div class="card-header">
              <span>消息通知</span>
              <div class="message-actions">
                <el-button size="small" type="primary" @click="markAllAsRead" v-if="messages.some(msg => msg.status === 'unread')">
                  全部已读
                </el-button>
                <el-button size="small" type="danger" @click="deleteAllMessages" v-if="messages.length > 0">
                  全部删除
                </el-button>
              </div>
            </div>
          </template>
          <div v-if="messages.length > 0" class="messages-content">
            <el-table :data="messages" style="width: 100%">
              <el-table-column label="内容" min-width="400">
                <template #default="scope">
                  {{ scope.row.content }}
                </template>
              </el-table-column>
              <el-table-column label="类型" width="120">
                <template #default="scope">
                  {{ scope.row.type === 'order' ? '订单消息' : '系统消息' }}
                </template>
              </el-table-column>
              <el-table-column label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="scope.row.status === 'unread' ? 'warning' : 'success'">
                    {{ scope.row.status === 'unread' ? '未读' : '已读' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="创建时间" width="200">
                <template #default="scope">
                  {{ formatDateTime(scope.row.createTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150">
                <template #default="scope">
                  <div class="message-actions">
                    <el-button size="small" @click="markAsRead(scope.row.id)" v-if="scope.row.status === 'unread'">
                      标记已读
                    </el-button>
                    <el-button size="small" type="danger" @click="deleteMessage(scope.row.id)">
                      删除
                    </el-button>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div v-else class="empty-messages">
            <el-empty description="暂无消息" />
          </div>
        </el-card>

        <!-- 我的收藏 -->
        <el-card v-if="activeMenu === 'favorites'" class="user-favorites-card">
          <template #header>
            <div class="card-header">
              <span>我的收藏</span>
            </div>
          </template>
          <div v-if="favorites.length > 0" class="favorites-content">
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12" :md="8" v-for="favorite in favorites" :key="favorite.id">
                <el-card :body-style="{ padding: '0px' }" @click="goToWorkDetail(favorite.id)" style="cursor: pointer;">
                  <img :src="favorite.image" class="favorite-image" alt="收藏作品">
                  <div style="padding: 14px;">
                    <h4 style="margin-bottom: 10px;">{{ favorite.name }}</h4>
                    <p style="color: #666; margin-bottom: 15px;">{{ favorite.title }}</p>
                    <p style="color: #f56c6c; font-weight: bold;">¥{{ favorite.price }}</p>
                    <p v-if="favorite.stock !== undefined" class="stock-info">
                      {{ favorite.stock > 0 ? `库存: ${favorite.stock} 件` : '已售罄' }}
                    </p>
                    <div style="display: flex; justify-content: space-between; margin-top: 15px;">
                      <el-button size="small" type="primary" @click.stop="buyWork(favorite)" :disabled="favorite.stock !== undefined && favorite.stock <= 0">
                        {{ favorite.stock !== undefined && favorite.stock <= 0 ? '已售罄' : '立即购买' }}
                      </el-button>
                      <el-button size="small" type="danger" @click.stop="removeFavorite(favorite.id)">
                        取消收藏
                      </el-button>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
          <div v-else class="empty-favorites">
            <el-empty description="暂无收藏作品" />
            <el-button type="primary" @click="setActiveMenu('works')" style="margin-top: 20px;">去浏览作品</el-button>
          </div>
        </el-card>

        <!-- 购物车 -->
        <el-card v-if="activeMenu === 'cart'" class="user-cart-card">
          <template #header>
            <div class="card-header">
              <span>购物车</span>
            </div>
          </template>
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
                <span class="price">¥{{ cart.reduce((total, item) => total + item.price * item.quantity, 0).toFixed(2) }}</span>
              </div>
              <div class="cart-actions">
                <el-button @click="clearCart">清空购物车</el-button>
                <el-button type="primary" @click="checkout">结算</el-button>
              </div>
            </div>
          </div>
          <div v-else class="empty-cart">
            <el-empty description="购物车是空的" />
            <el-button type="primary" @click="setActiveMenu('works')" style="margin-top: 20px;">去购物</el-button>
          </div>
        </el-card>

        <!-- 浏览作品 -->
        <el-card v-if="activeMenu === 'works'" class="user-works-card">
          <template #header>
            <div class="card-header">
              <span>浏览作品</span>
            </div>
          </template>
          <div v-if="works.length > 0" class="works-content">
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12" :md="8" v-for="work in works" :key="work.id">
                <el-card :body-style="{ padding: '0px' }" @click="goToWorkDetail(work.id)" style="cursor: pointer;">
                  <img :src="work.image" class="favorite-image" alt="作品">
                  <div style="padding: 14px;">
                    <h4 style="margin-bottom: 10px;">{{ work.name }}</h4>
                    <p style="color: #666; margin-bottom: 15px;">{{ work.title }}</p>
                    <p style="color: #f56c6c; font-weight: bold;">¥{{ work.price }}</p>
                    <p v-if="work.stock !== undefined" class="stock-info">
                      {{ work.stock > 0 ? `库存: ${work.stock} 件` : '已售罄' }}
                    </p>
                    <div style="display: flex; justify-content: space-between; margin-top: 15px;">
                      <el-button size="small" type="primary" @click.stop="addToCart(work)" :disabled="work.stock !== undefined && work.stock <= 0">
                        {{ work.stock !== undefined && work.stock <= 0 ? '已售罄' : '加入购物车' }}
                      </el-button>
                      <el-button size="small" type="success" @click.stop="addFavorite(work)">
                        收藏
                      </el-button>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
          <div v-else class="empty-works">
            <el-empty description="暂无作品" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 编辑个人信息对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="'编辑个人信息'"
      width="500px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="头像">
          <div class="avatar-uploader">
            <el-avatar :size="100" :src="form.avatar || 'https://img.yzcdn.cn/vant/cat.jpeg'">
              {{ form.username ? form.username.charAt(0).toUpperCase() : 'U' }}
            </el-avatar>
            <el-button type="primary" plain @click="triggerAvatarUpload" style="margin-top: 10px;">更换头像</el-button>
            <input type="file" ref="avatarInput" style="display: none;" accept="image/*" @change="handleAvatarUpload">
          </div>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveUserInfo">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from '../api/index'
import { User, Document, Bell, Star, ShoppingCart, Picture, Switch } from '@element-plus/icons-vue'

export default {
  name: 'UserCenter',
  components: {
    User,
    Document,
    Bell,
    Star,
    ShoppingCart,
    Picture,
    Switch
  },
  data() {
    return {
      user: {
        id: '',
        username: '',
        email: '',
        phone: '',
        role: '',
        createTime: '',
        avatar: ''
      },
      orders: [],
      messages: [],
      favorites: [],
      dialogVisible: false,
      form: {
        id: '',
        username: '',
        email: '',
        phone: '',
        avatar: ''
      },
      activeMenu: 'profile',
      unreadCount: 0,
      cart: [],
      works: []
    }
  },
  mounted() {
    const userId = localStorage.getItem('userId')
    if (!userId) {
      this.$message.warning('请先登录以访问个人中心')
      this.$router.push('/login')
      return
    }
    const activeMenuParam = this.$route.query.activeMenu
    if (activeMenuParam) {
      this.activeMenu = activeMenuParam
    }
    this.loadUserInfo()
    this.loadOrders()
    this.loadMessages()
    this.loadFavorites()
    this.loadUnreadCount()
    this.loadCart()
    this.loadWorks()
  },
  methods: {
    loadUserInfo() {
      const userId = localStorage.getItem('userId')
      if (userId) {
        api.getUser(userId).then(res => {
          this.user = res.data
        })
      }
    },
    loadOrders() {
      const userId = localStorage.getItem('userId')
      if (userId) {
        api.getUserProductOrders(userId).then(res => {
          this.orders = res.data
        })
      }
    },
    loadMessages() {
      const userId = localStorage.getItem('userId')
      if (userId) {
        api.getUserMessages(userId).then(res => {
          this.messages = res.data
          this.loadUnreadCount()
        })
      }
    },
    loadUnreadCount() {
      const userId = localStorage.getItem('userId')
      if (userId) {
        api.getUnreadCount(userId).then(res => {
          this.unreadCount = res.data
        })
      }
    },
    setActiveMenu(menu) {
      this.activeMenu = menu
    },
    logout() {
      localStorage.removeItem('user')
      localStorage.removeItem('userId')
      this.$message.success('退出登录成功')
      window.location.href = '/login'
    },
    editUserInfo() {
      this.form = { ...this.user }
      this.dialogVisible = true
    },
    triggerAvatarUpload() {
      this.$refs.avatarInput.click()
    },
    handleAvatarUpload(event) {
      const file = event.target.files[0]
      if (file) {
        api.uploadUserAvatar(file).then(res => {
          this.form.avatar = res.data
          this.$message.success('头像上传成功')
        }).catch(err => {
          console.error('头像上传失败:', err)
          this.$message.error('头像上传失败')
        })
      }
    },
    saveUserInfo() {
      api.updateUser(this.form).then(() => {
        this.$message.success('个人信息更新成功')
        this.dialogVisible = false
        this.loadUserInfo()
      }).catch(err => {
        console.error('更新个人信息失败:', err)
        this.$message.error('更新个人信息失败')
      })
    },
    viewOrderDetail(id) {
      this.$router.push(`/order/${id}`)
    },
    cancelOrder(id) {
      this.$confirm('确定要取消该订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.updateProductOrderStatus(id, 'cancelled').then(() => {
          this.$message.success('订单已取消')
          this.loadOrders()
        }).catch(error => {
          console.error('取消订单失败:', error)
          this.$message.error('取消订单失败，请稍后重试')
        })
      })
    },
    deleteOrder(id) {
      this.$confirm('确定要删除该订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteOrder(id).then(() => {
          this.$message.success('订单已删除')
          this.loadOrders()
        }).catch(error => {
          console.error('删除订单失败:', error)
          this.$message.error('删除订单失败，请稍后重试')
        })
      })
    },
    goToWorks() {
      this.$router.push('/works')
    },
    markAsRead(id) {
      api.markMessageAsRead(id).then(() => {
        this.$message.success('消息已标记为已读')
        this.loadMessages()
        this.loadUnreadCount()
      })
    },
    deleteMessage(id) {
      this.$confirm('确定要删除这条消息吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteMessage(id).then(() => {
          this.$message.success('消息已删除')
          this.loadMessages()
          this.loadUnreadCount()
        }).catch(err => {
          console.error('删除消息失败:', err)
          this.$message.error('删除消息失败')
        })
      })
    },
    markAllAsRead() {
      this.$confirm('确定要将所有消息标记为已读吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const unreadMessages = this.messages.filter(msg => msg.status === 'unread')
        if (unreadMessages.length === 0) {
          this.$message.info('没有未读消息')
          return
        }
        Promise.all(unreadMessages.map(msg => api.markMessageAsRead(msg.id))).then(() => {
          this.$message.success('所有消息已标记为已读')
          this.loadMessages()
          this.loadUnreadCount()
        }).catch(err => {
          console.error('标记所有消息为已读失败:', err)
          this.$message.error('标记所有消息为已读失败')
        })
      })
    },
    deleteAllMessages() {
      this.$confirm('确定要删除所有消息吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (this.messages.length === 0) {
          this.$message.info('没有消息可删除')
          return
        }
        Promise.all(this.messages.map(msg => api.deleteMessage(msg.id))).then(() => {
          this.$message.success('所有消息已删除')
          this.loadMessages()
          this.loadUnreadCount()
        }).catch(err => {
          console.error('删除所有消息失败:', err)
          this.$message.error('删除所有消息失败')
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
    },
    loadFavorites() {
      const userId = localStorage.getItem('userId')
      if (userId) {
        api.getUserCollections(userId)
          .then(response => {
            const collections = response.data
            // 处理收藏数据，获取对应的作品信息
            const favoritePromises = collections.map(collection => {
              if (collection.targetType === 'work') {
                // 获取作品详情
                return api.getWork(collection.targetId)
                  .then(workResponse => {
                    const work = workResponse.data
                    return {
                      id: collection.id,
                      name: work.name,
                      title: work.title,
                      price: work.price,
                      image: work.image,
                      targetId: work.id,
                      targetType: collection.targetType
                    }
                  })
              }
            })
            
            // 等待所有请求完成
            Promise.all(favoritePromises)
              .then(favorites => {
                // 过滤掉无效的收藏
                this.favorites = favorites.filter(favorite => favorite)
              })
              .catch(error => {
                console.error('获取收藏详情失败:', error)
                this.$message.error('获取收藏详情失败')
              })
          })
          .catch(error => {
            console.error('获取收藏作品失败:', error)
            this.$message.error('获取收藏作品失败')
          })
      }
    },
    goToWorkDetail(favoriteId) {
      // 查找收藏项
      const favorite = this.favorites.find(item => item.id === favoriteId)
      if (favorite) {
        if (favorite.targetType === 'work') {
          // 跳转到作品详情页面
          this.$router.push(`/work/${favorite.targetId}`)
        }
      }
    },
    buyWork(favorite) {
      // 购买功能
      if (!favorite) return
      
      // 将收藏的作品添加到购物车
      const cartItem = {
        id: favorite.targetId || favorite.id,
        name: favorite.name,
        title: favorite.title,
        price: favorite.price,
        image: favorite.image,
        quantity: 1
      }
      
      // 从localStorage获取购物车数据
      let cart = localStorage.getItem('cart')
      if (cart) {
        try {
          cart = JSON.parse(cart)
        } catch (error) {
          console.error('解析购物车数据失败:', error)
          cart = []
        }
      } else {
        cart = []
      }
      
      // 检查作品是否已经在购物车中
      const existingItemIndex = cart.findIndex(item => item.id === cartItem.id)
      if (existingItemIndex >= 0) {
        // 如果已经在购物车中，增加数量
        cart[existingItemIndex].quantity += 1
      } else {
        // 如果不在购物车中，添加到购物车
        cart.push(cartItem)
      }
      
      // 将更新后的购物车数据保存到localStorage
      localStorage.setItem('cart', JSON.stringify(cart))
      
      this.$message.success('已加入购物车')
    },
    removeFavorite(favoriteId) {
      // 取消收藏
      api.deleteCollection(favoriteId)
        .then(response => {
          this.favorites = this.favorites.filter(item => item.id !== favoriteId)
          this.$message.success('已取消收藏')
        })
        .catch(error => {
          console.error('取消收藏失败:', error)
          this.$message.error('取消收藏失败')
        })
    },
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
        this.loadOrders()
        this.setActiveMenu('orders')
      }).catch(error => {
        console.error('创建订单失败:', error)
        this.$message.error('订单提交失败，请稍后重试')
      })
    },
    loadWorks() {
      api.getWorks().then(res => {
        this.works = res.data
      }).catch(err => {
        console.error('获取作品列表失败:', err)
      })
    },
    addToCart(work) {
      const cartItem = {
        id: work.id,
        name: work.name,
        title: work.title,
        price: work.price,
        image: work.image,
        quantity: 1
      }
      
      // 检查作品是否已经在购物车中
      const existingItemIndex = this.cart.findIndex(item => item.id === cartItem.id)
      if (existingItemIndex >= 0) {
        // 如果已经在购物车中，增加数量
        this.cart[existingItemIndex].quantity += 1
      } else {
        // 如果不在购物车中，添加到购物车
        this.cart.push(cartItem)
      }
      
      // 将更新后的购物车数据保存到localStorage
      localStorage.setItem('cart', JSON.stringify(this.cart))
      
      this.$message.success('已加入购物车')
    },
    addFavorite(work) {
      const userId = localStorage.getItem('userId')
      if (!userId) {
        this.$message.warning('请先登录')
        this.$router.push('/login')
        return
      }
      
      const collection = {
        userId: parseInt(userId),
        targetId: work.id,
        targetType: 'work'
      }
      
      api.createCollection(collection).then(() => {
        this.$message.success('收藏成功')
        this.loadFavorites()
      }).catch(error => {
        console.error('收藏失败:', error)
        this.$message.error('收藏失败，请稍后重试')
      })
    }
  }
}
</script>

<style scoped>
.user-center {
  padding: 20px;
  min-height: 100vh;
  background-color: var(--background-color);
}

.user-header {
  margin-bottom: 30px;
}

.user-header h2 {
  font-size: 24px;
  color: var(--primary-color);
  font-family: var(--font-family);
  margin: 0;
}

.user-sidebar {
  background-color: #fff;
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-md);
  padding: 20px;
  height: fit-content;
}

.user-profile {
  text-align: center;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 20px;
}

.avatar {
  margin-bottom: 15px;
}

.profile-info h3 {
  margin: 0 0 5px 0;
  font-size: 18px;
  font-weight: bold;
  color: var(--text-color);
}

.profile-info p {
  margin: 0;
  color: var(--text-color-secondary);
  font-size: 14px;
}

.user-menu {
  border: none !important;
}

.user-menu .el-menu-item {
  margin: 5px 0;
  border-radius: var(--border-radius-sm);
  height: 48px;
  line-height: 48px;
}

.user-menu .el-menu-item:hover {
  background-color: #f5f7fa !important;
}

.user-menu .el-menu-item.is-active {
  background-color: var(--primary-color) !important;
  color: #fff !important;
}

.message-badge {
  margin-left: 10px;
}

.user-profile-card,
.user-orders-card,
.user-messages-card,
.user-favorites-card,
.user-cart-card,
.user-works-card {
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-md);
}

.cart-content {
  margin-bottom: 20px;
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

.empty-cart,
.empty-works {
  text-align: center;
  padding: 60px 0;
}

.empty-cart .el-button,
.empty-works .el-button {
  margin-top: 20px;
}

.works-content {
  padding: 20px 0;
}

.stock-info {
  font-size: 12px;
  color: #666;
  margin-bottom: 10px;
}

.stock-info:contains('已售罄') {
  color: #F56C6C;
  font-weight: bold;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.message-actions {
  display: flex;
  gap: 10px;
}

.user-info {
  padding: 20px 0;
}

.info-item {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.label {
  width: 100px;
  font-weight: bold;
  color: var(--text-color);
}

.value {
  flex: 1;
  color: var(--text-color-secondary);
}

.orders-content,
.messages-content {
  padding: 20px 0;
}

.empty-orders, .empty-messages, .empty-favorites {
  text-align: center;
  padding: 60px 0;
}

.empty-orders .el-button,
.empty-messages .el-button,
.empty-favorites .el-button {
  margin-top: 20px;
}

.favorites-content {
  padding: 20px 0;
}

.favorite-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
}

.user-favorites-card {
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-md);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

@media (max-width: 768px) {
  .user-center {
    padding: 10px;
  }
  
  .user-header h2 {
    font-size: 20px;
  }
  
  .el-row {
    flex-direction: column;
  }
  
  .el-col {
    width: 100% !important;
    margin-bottom: 20px;
  }
}
</style>