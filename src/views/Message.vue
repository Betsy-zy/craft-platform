<template>
  <div class="message">
    <TopNav />
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item><a href="/">首页</a></el-breadcrumb-item>
      <el-breadcrumb-item>消息通知</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="message-header">
      <h2>我的消息</h2>
      <div class="message-actions">
        <el-button type="primary" size="small" @click="markAllAsRead">全部已读</el-button>
        <el-button type="danger" size="small" @click="deleteAllMessages">删除消息</el-button>
      </div>
    </div>

    <div v-if="filteredMessages.length > 0" class="message-list">
      <el-card
        v-for="message in filteredMessages"
        :key="message.id"
        :class="{ 'unread': message.status === 'unread' }"
        class="message-card"
      >
        <div class="message-content">
          <div class="message-header-info">
            <h3>{{ message.type === 'appointment' ? '预约通知' : '订单通知' }}</h3>
            <span class="message-time">{{ formatTime(message.createTime) }}</span>
          </div>
          <p>{{ message.content }}</p>
        </div>
        <div class="message-footer">
          <el-button
            v-if="message.status === 'unread'"
            type="primary"
            size="small"
            @click="markAsRead(message.id)"
          >
            标记为已读
          </el-button>
          <el-button
            type="danger"
            size="small"
            @click="deleteMessage(message.id)"
          >
            删除
          </el-button>
        </div>
      </el-card>
    </div>

    <div v-else class="empty-messages">
      <el-empty description="暂无消息" />
    </div>
  </div>
</template>

<script>
import api from '../api'
import TopNav from '../components/TopNav.vue'

export default {
  name: 'Message',
  components: {
    TopNav
  },
  data() {
    return {
      messages: []
    }
  },
  computed: {
    filteredMessages() {
      // 只显示预约和订单类型的消息
      return this.messages.filter(message => 
        message.type === 'appointment' || message.type === 'order'
      )
    }
  },
  mounted() {
    this.loadMessages()
  },
  methods: {
    loadMessages() {
      const userId = localStorage.getItem('userId')
      if (userId) {
        api.getUserMessages(userId).then(res => {
          this.messages = res.data
        }).catch(err => {
          console.error('获取消息列表失败:', err)
          this.$message.error('获取消息列表失败')
        })
      } else {
        this.$message.error('请先登录')
      }
    },
    markAsRead(id) {
      api.markMessageAsRead(id).then(() => {
        this.$message.success('消息已标记为已读')
        this.loadMessages()
      })
    },
    markAllAsRead() {
      const userId = localStorage.getItem('userId')
      if (userId) {
        api.markAllMessagesAsRead(userId).then(() => {
          this.$message.success('所有消息已标记为已读')
          this.loadMessages()
        })
      }
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
        })
      })
    },
    deleteAllMessages() {
      this.$confirm('确定要删除所有消息吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const userId = localStorage.getItem('userId')
        if (userId) {
          api.deleteAllMessages(userId).then(() => {
            this.$message.success('所有消息已删除')
            this.loadMessages()
          })
        }
      })
    },
    formatTime(time) {
      return new Date(time).toLocaleString()
    }
  }
}
</script>

<style scoped>
.message-container {
  padding: 20px;
  min-height: 100vh;
  background-color: var(--background-color);
}

.message-header {
  margin-bottom: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.message-header h2 {
  font-size: 24px;
  color: var(--primary-color);
  font-family: var(--font-family);
  margin: 0;
}

.message-actions {
  display: flex;
  gap: 10px;
}

.message-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 15px;
  border-top: 1px solid var(--border-color);
  gap: 10px;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.message-card {
  transition: all 0.3s ease;
  border-left: 4px solid var(--secondary-color);
}

.message-card.unread {
  border-left: 4px solid var(--primary-color);
  background-color: rgba(200, 60, 35, 0.05);
}

.message-card:hover {
  box-shadow: var(--shadow-md) !important;
  transform: translateY(-2px) !important;
}

.message-content {
  margin-bottom: 15px;
}

.message-header-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.message-header-info h3 {
  font-size: 18px;
  color: var(--text-color);
  font-family: var(--font-family);
}

.message-time {
  font-size: 12px;
  color: var(--light-text);
}

.message-content p {
  font-size: 14px;
  line-height: 1.6;
  color: var(--text-color);
}

.message-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 15px;
  border-top: 1px solid var(--border-color);
}

.empty-messages {
  text-align: center;
  padding: 60px 0;
}

@media (max-width: 768px) {
  .message-container {
    padding: 10px;
  }
  
  .message-header h2 {
    font-size: 20px;
  }
}
</style>