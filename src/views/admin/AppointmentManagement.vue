<template>
  <div class="appointment-management">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item><a href="/admin/dashboard">管理后台</a></el-breadcrumb-item>
      <el-breadcrumb-item>预约管理</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="management-header">
      <h2>预约管理</h2>
    </div>

    <el-card class="management-card">
      <el-table :data="appointments" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="userId" label="用户ID"></el-table-column>
        <el-table-column label="体验项目">
          <template #default="scope">
            {{ getExperienceName(scope.row.experienceId) }}
          </template>
        </el-table-column>
        <el-table-column label="预约时间">
          <template #default="scope">
            {{ formatDateTime(scope.row.appointmentTime) }}
          </template>
        </el-table-column>
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
            <el-button size="small" type="primary" @click="updateStatus(scope.row, 'accepted')" :disabled="scope.row.status !== 'pending'">接受</el-button>
            <el-button size="small" type="danger" @click="updateStatus(scope.row, 'rejected')" :disabled="scope.row.status !== 'pending'">拒绝</el-button>
            <el-button size="small" type="info" @click="deleteAppointment(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import api from '../../api'

export default {
  name: 'AppointmentManagement',
  data() {
    return {
      appointments: [],
      experiences: []
    }
  },
  mounted() {
    this.loadAppointments()
    this.loadExperiences()
  },
  methods: {
    loadAppointments() {
      api.getAppointments().then(res => {
        this.appointments = res.data.value || res.data
      }).catch(err => {
        console.error('获取预约列表失败:', err)
      })
    },
    loadExperiences() {
      api.getExperiences().then(res => {
        this.experiences = res.data.value || res.data
      }).catch(err => {
        console.error('获取体验项目列表失败:', err)
      })
    },
    getExperienceName(experienceId) {
      if (!experienceId) return ''
      const experience = this.experiences.find(e => e.id === experienceId)
      return experience ? experience.name : '未知体验'
    },
    updateStatus(appointment, status) {
      api.updateAppointmentStatus(appointment.id, status).then(() => {
        this.$message.success(`预约状态已更新为${status === 'accepted' ? '已接受' : '已拒绝'}`)
        this.loadAppointments()
      }).catch(err => {
        console.error('更新预约状态失败:', err)
        this.$message.error('更新预约状态失败')
      })
    },
    deleteAppointment(id) {
      this.$confirm('确定要删除这个预约吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteAppointment(id).then(() => {
          this.$message.success('预约删除成功')
          this.loadAppointments()
        }).catch(err => {
          console.error('删除预约失败:', err)
          this.$message.error('删除预约失败')
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
        'accepted': '已接受',
        'rejected': '已拒绝'
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style scoped>
.appointment-management {
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
  .appointment-management {
    padding: 10px;
  }
  
  .management-header h2 {
    font-size: 20px;
  }
}
</style>