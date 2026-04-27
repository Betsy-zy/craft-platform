<template>
  <div class="appointment">
    <el-page-header :back-text="''" @back="goBack">
      <template #content>
        <span>预约体验</span>
      </template>
    </el-page-header>
    <el-card class="appointment-card" shadow="hover" style="max-width: 500px; margin: 30px auto;">
      <el-form :model="appointment" :rules="rules" ref="appointmentForm" label-width="120px">
        <el-form-item label="体验项目" prop="experienceId">
          <!-- 如果是从体验项目详情页跳转过来的，显示只读文本 -->
          <div v-if="isFromExperienceDetail" class="experience-info">
            {{ selectedExperienceName }}
          </div>
          <!-- 否则显示下拉框 -->
          <el-select v-else v-model="appointment.experienceId" placeholder="请选择体验项目" style="width: 100%;">
            <el-option v-for="experience in experiences" :key="experience.id" :label="`${experience.name} - ${experience.price} 元`" :value="experience.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预约时间" prop="appointmentTime">
          <el-date-picker v-model="appointment.appointmentTime" type="datetime" placeholder="请选择预约时间" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitAppointment" style="width: 100%;">提交预约</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import api from '../api'

export default {
  name: 'Appointment',
  data() {
    return {
      experiences: [],
      isFromExperienceDetail: false,
      selectedExperienceName: '',
      appointment: {
        userId: JSON.parse(localStorage.getItem('user'))?.id || 1,
        experienceId: '',
        appointmentTime: ''
      },
      rules: {
        experienceId: [
          { required: true, message: '请选择体验项目', trigger: 'change' }
        ],
        appointmentTime: [
          { required: true, message: '请选择预约时间', trigger: 'change' }
        ]
      }
    }
  },
  mounted() {
    this.fetchExperiences()
    // 从URL参数中获取体验项目ID
    const experienceId = this.$route.query.experienceId
    if (experienceId) {
      this.appointment.experienceId = experienceId
      this.isFromExperienceDetail = true
      // 查找对应的体验项目名称
      this.findExperienceName(experienceId)
    }
  },
  methods: {
    fetchExperiences() {
      api.getExperiences().then(res => {
        // 检查返回数据结构，如果有value字段，则使用value，否则直接使用data
        this.experiences = res.data.value || res.data
      }).catch(err => {
        console.error('获取体验项目失败:', err)
        this.$message.error('获取体验项目失败')
      })
    },
    findExperienceName(experienceId) {
      api.getExperience(experienceId).then(res => {
        if (res.data) {
          this.selectedExperienceName = `${res.data.name} - ${res.data.price} 元`
        }
      }).catch(err => {
        console.error('获取体验项目详情失败:', err)
        this.$message.error('获取体验项目详情失败')
      })
    },
    submitAppointment() {
      this.$refs.appointmentForm.validate((valid) => {
        if (valid) {
          api.createAppointment(this.appointment).then(res => {
            if (res.data) {
              this.$message.success('预约成功！')
              // 预约成功后跳转到用户中心，而不是登录页
              this.$router.push('/user-center')
            }
          }).catch(err => {
            console.error('预约失败:', err)
            this.$message.error('预约失败，请稍后重试')
          })
        }
      })
    },
    goBack() {
      this.$router.back()
    }
  }
}
</script>

<style scoped>
.appointment {
  padding: 20px;
}

.appointment-card {
  border-radius: 10px;
}

.experience-info {
  padding: 10px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #f5f7fa;
  font-size: 14px;
  color: #303133;
}

/* 隐藏返回按钮文本 */
:deep(.el-page-header__left .el-page-header__back) span {
  display: none !important;
}

/* 确保返回按钮只显示图标 */
:deep(.el-page-header__back) {
  font-size: 18px;
}
</style>