<template>
  <div class="work-management">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item><a href="/admin/dashboard">管理后台</a></el-breadcrumb-item>
      <el-breadcrumb-item>作品管理</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="management-header">
      <h2>作品管理</h2>
      <el-button type="primary" @click="openAddWorkDialog">添加作品</el-button>
    </div>

    <el-card class="management-card">
      <el-table :data="works" style="width: 100%">
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="name" label="作品名称" width="120"></el-table-column>
        <el-table-column label="作品图片" width="120" align="center">
          <template #default="scope">
            <div style="display: flex; align-items: center; justify-content: center;">
              <el-image
                v-if="scope.row.image"
                :src="scope.row.image"
                fit="cover"
                style="width: 60px; height: 60px;"
              ></el-image>
              <div v-else style="width: 60px; height: 60px; background-color: #f0f0f0; display: flex; align-items: center; justify-content: center; font-size: 12px;">
                无图片
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="作品标题" min-width="150"></el-table-column>
        <el-table-column prop="price" label="价格" width="80"></el-table-column>
        <el-table-column prop="stock" label="库存" width="80"></el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="scope">
            {{ formatStatus(scope.row.status) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" type="primary" @click="editWork(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteWork(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加作品对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="添加作品"
      width="500px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="作品名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="作品标题">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="作品描述">
          <el-input v-model="form.description" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="匠人">
          <el-select v-model="form.craftsmanId" placeholder="请选择匠人" style="width: 100%;">
            <el-option v-for="craftsman in craftsmen" :key="craftsman.id" :label="craftsman.name" :value="craftsman.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="作品图片">
          <!-- 上传本地图片 -->
          <el-upload
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :on-change="handleImageChange"
            :file-list="imageFileList"
            accept="image/*"
          >
            <el-button type="primary">选择图片</el-button>
            <template #tip>
              <div class="el-upload__tip">
                只能上传jpg/png/gif格式的图片，且不超过2MB
              </div>
            </template>
          </el-upload>
          
          <!-- 预览图片 -->
          <div v-if="form.image" style="margin-top: 10px; text-align: center;">
            <el-image
              :src="form.image"
              fit="cover"
              style="width: 100px; height: 100px;"
            ></el-image>
            <p style="margin-top: 5px; font-size: 12px; color: #999;">图片预览</p>
          </div>
        </el-form-item>

        <el-form-item label="价格">
          <el-input v-model="form.price" type="number"></el-input>
        </el-form-item>
        <el-form-item label="库存">
          <el-input v-model="form.stock" type="number"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="上架" value="active"></el-option>
            <el-option label="下架" value="inactive"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveWork">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import api from '../../api'

export default {
  name: 'WorkManagement',
  data() {
    return {
      works: [],
      craftsmen: [],
      dialogVisible: false,
      form: {
        name: '',
        title: '',
        description: '',
        image: '',
        price: 0,
        stock: 0,
        craftsmanId: '',
        status: 'active'
      },
      imageFileList: []
    }
  },
  mounted() {
    this.loadWorks()
    this.loadCraftsmen()
  },
  methods: {
    handleImageChange(file, fileList) {
      this.imageFileList = fileList;
      if (file.raw) {
        const reader = new FileReader();
        reader.onload = (e) => {
          // 这里可以处理图片预览
          console.log('图片预览:', e.target.result);
        };
        reader.readAsDataURL(file.raw);
        
        // 上传文件到服务器
        api.uploadWorkImage(file.raw).then(response => {
          this.form.image = response.data;
          this.$message.success('图片上传成功');
        }).catch(err => {
          console.error('图片上传失败:', err);
          this.$message.error('图片上传失败');
        });
      }
    },
    loadWorks() {
      api.getAllWorks().then(res => {
        this.works = res.data
      }).catch(err => {
        console.error('获取作品列表失败:', err)
      })
    },
    loadCraftsmen() {
      api.getAllCraftsmen().then(res => {
        this.craftsmen = res.data
      }).catch(err => {
        console.error('获取匠人列表失败:', err)
      })
    },
    openAddWorkDialog() {
      this.form = {
        name: '',
        title: '',
        description: '',
        image: '',
        price: 0,
        stock: 0,
        craftsmanId: '',
        status: 'active'
      }
      this.imageFileList = []
      this.dialogVisible = true
    },
    saveWork() {
      if (this.form.id) {
        // 更新作品
        api.updateWork(this.form).then(() => {
          this.$message.success('作品更新成功')
          this.dialogVisible = false
          this.loadWorks()
        }).catch(err => {
          console.error('更新作品失败:', err)
          this.$message.error('更新作品失败')
        })
      } else {
        // 添加作品
        api.createWork(this.form).then(() => {
          this.$message.success('作品添加成功')
          this.dialogVisible = false
          this.loadWorks()
        }).catch(err => {
          console.error('添加作品失败:', err)
          this.$message.error('添加作品失败')
        })
      }
    },
    editWork(work) {
      this.form = { ...work }
      this.imageFileList = []
      this.dialogVisible = true
    },
    deleteWork(id) {
      this.$confirm('确定要删除这个作品吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.deleteWork(id).then(() => {
          this.$message.success('作品删除成功')
          this.loadWorks()
        }).catch(err => {
          console.error('删除作品失败:', err)
          this.$message.error('删除作品失败')
        })
      })
    },
    formatStatus(status) {
      const statusMap = {
        'active': '上架',
        'inactive': '下架'
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style scoped>
.work-management {
  padding: 20px;
  min-height: 100vh;
  background-color: var(--background-color);
}

.management-header {
  margin-bottom: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

@media (max-width: 768px) {
  .work-management {
    padding: 10px;
  }
  
  .management-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .management-header h2 {
    font-size: 20px;
  }
}
</style>