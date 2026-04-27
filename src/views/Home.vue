<template>
  <div class="home">
    <TopNav />

    <el-main>
      <div class="simple-carousel">
        <div class="carousel-wrapper">
          <div class="carousel-slides" ref="carouselSlides">
            <div class="carousel-slide">
              <img src="../picture/1.jpg" alt="轮播图1" class="carousel-image" />
            </div>
            <div class="carousel-slide">
              <img src="../picture/2.jpg" alt="轮播图2" class="carousel-image" />
            </div>
            <div class="carousel-slide">
              <img src="../picture/3.jpg" alt="轮播图3" class="carousel-image" />
            </div>
          </div>
          <button class="carousel-arrow left" @click="prevSlide">&lt;</button>
          <button class="carousel-arrow right" @click="nextSlide">&gt;</button>
          <div class="carousel-indicators">
            <button v-for="(slide, index) in 3" :key="index" :class="{ active: currentSlide === index }" @click="goToSlide(index)"></button>
          </div>
        </div>
      </div>
      
      <div class="el-section" style="margin-top: 40px;">
        <h3 style="text-align: center; margin-bottom: 30px; font-size: 24px;">推荐匠人</h3>
        <el-row :gutter="30" type="flex" justify="center">
          <el-col :span="6" v-for="craftsman in craftsmen" :key="craftsman.id">
            <el-card :body-style="{ padding: '0px' }" class="craftsman-card">
              <img :src="craftsman.avatar" class="image" />
              <div style="padding: 14px;">
                <el-card :header="craftsman.name" style="margin-bottom: 10px;">
                  <template #header>
                    <div class="card-header">
                      <span>{{ craftsman.name }}</span>
                    </div>
                  </template>
                  <p>{{ craftsman.title }}</p>
                </el-card>
                <el-button type="info" size="small" style="width: 100%;" @click="goToCraftsmanDetail(craftsman.id)">查看详情</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      
      <div class="el-section" style="margin-top: 60px;">
        <h3 style="text-align: center; margin-bottom: 30px; font-size: 24px;">推荐作品</h3>
        <el-row :gutter="20" type="flex" justify="center">
          <el-col :span="4" v-for="work in works" :key="work.id">
            <el-card :body-style="{ padding: '0px' }" @click="goToWork(work.id)" class="work-card">
              <img :src="work.image" class="work-image" />
              <div style="padding: 14px;">
                <h4 style="margin-bottom: 10px;">{{ work.name }}</h4>
                <p style="color: #666; margin-bottom: 15px;">{{ work.title }}</p>
                <div style="display: flex; justify-content: space-between; align-items: center;">
                  <span style="color: #f56c6c; font-weight: bold;">{{ work.price.toFixed(2) }} 元</span>
                  <el-button type="primary" size="small">查看详情</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      

    </el-main>
    <el-footer style="background-color: #333; color: white; text-align: center; padding: 20px;">
      <p>© 2026 非遗手作平台</p>
    </el-footer>
  </div>
</template>

<script>
import api from '../api'
import TopNav from '../components/TopNav.vue'
import carousel1 from '../picture/1.jpg'
import carousel2 from '../picture/2.jpg'
import carousel3 from '../picture/3.jpg'

export default {
  name: 'Home',
  components: {
    TopNav
  },
  data() {
    return {
      craftsmen: [],
      works: [],
      unreadCount: 0,
      currentSlide: 0,
      slideInterval: null
    }
  },
  mounted() {
    this.fetchData()
    this.fetchUnreadCount()
    this.startAutoSlide()
  },
  beforeUnmount() {
    if (this.slideInterval) {
      clearInterval(this.slideInterval)
    }
  },
  methods: {
    fetchData() {
      api.getCraftsmen().then(res => {
        this.craftsmen = res.data.slice(0, 3)
      }).catch(err => {
        console.error('获取匠人列表失败:', err)
      })
      api.getWorks().then(res => {
        this.works = res.data.slice(0, 4)
      }).catch(err => {
        console.error('获取作品列表失败:', err)
      })
    },
    startAutoSlide() {
      this.slideInterval = setInterval(() => {
        this.nextSlide()
      }, 3000)
    },
    prevSlide() {
      this.currentSlide = (this.currentSlide - 1 + 3) % 3
      this.updateSlidePosition()
    },
    nextSlide() {
      this.currentSlide = (this.currentSlide + 1) % 3
      this.updateSlidePosition()
    },
    goToSlide(index) {
      this.currentSlide = index
      this.updateSlidePosition()
    },
    updateSlidePosition() {
      const slides = this.$refs.carouselSlides
      if (slides) {
        slides.style.transform = `translateX(-${this.currentSlide * 100}%)`
      }
    },
    goToWork(id) {
      this.$router.push(`/work/${id}`)
    },
    goToCraftsmanDetail(id) {
      this.$router.push(`/craftsman/${id}`)
    },
    fetchUnreadCount() {
      const userId = localStorage.getItem('userId')
      if (userId) {
        api.getUnreadCount(userId).then(res => {
          this.unreadCount = res.data
        }).catch(err => {
          console.error('获取未读消息数量失败:', err)
        })
      }
    }
  }
}
</script>

<style scoped>
.home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: var(--background-color);
}

.el-main {
  flex: 1;
  padding: 0;
}

.image {
  width: 100%;
  display: block;
  border-radius: var(--border-radius-md) var(--border-radius-md) 0 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}



.el-carousel {
  margin-bottom: var(--spacing-xl);
}

.simple-carousel {
  width: 100%;
  height: 500px;
  overflow: hidden;
  position: relative;
}

.carousel-wrapper {
  width: 100%;
  height: 100%;
  position: relative;
}

.carousel-slides {
  display: flex;
  width: 100%;
  height: 100%;
  transition: transform 0.5s ease;
}

.carousel-slide {
  flex: 0 0 100%;
  width: 100%;
  height: 100%;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  background-color: #f8f8f8;
}

.carousel-arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 40px;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  border-radius: 50%;
  font-size: 24px;
  cursor: pointer;
  z-index: 10;
}

.carousel-arrow.left {
  left: 20px;
}

.carousel-arrow.right {
  right: 20px;
}

.carousel-indicators {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
  z-index: 10;
}

.carousel-indicators button {
  width: 12px;
  height: 12px;
  border: none;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
}

.carousel-indicators button.active {
  background: white;
}

.craftsman-card {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  height: 100%;
}

.craftsman-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.work-card {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  height: 100%;
  cursor: pointer;
}

.work-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.image {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
}

.work-image {
  width: 100%;
  height: 140px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
}

.el-section {
  margin-top: 60px;
  padding: 0 var(--spacing-lg);
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
}

.el-section h3 {
  font-family: var(--font-family);
  font-size: var(--font-size-xlarge);
  color: var(--primary-color);
  text-align: center;
  margin-bottom: var(--spacing-lg);
  position: relative;
}

.el-section h3::after {
  content: '';
  display: block;
  width: 80px;
  height: 3px;
  background-color: var(--secondary-color);
  margin: var(--spacing-sm) auto 0;
  border-radius: 2px;
}

.el-card {
  border: 1px solid var(--border-color) !important;
  border-radius: var(--border-radius-md) !important;
  box-shadow: var(--shadow-sm) !important;
  transition: all 0.3s ease !important;
  overflow: hidden;
}

.el-card:hover {
  box-shadow: var(--shadow-md) !important;
  transform: translateY(-5px) !important;
}

.el-card .el-button {
  border-radius: var(--border-radius-sm) !important;
  font-family: var(--font-family) !important;
}

.el-footer {
  background-color: var(--primary-color) !important;
  color: var(--white) !important;
  text-align: center;
  padding: var(--spacing-md) !important;
  font-family: var(--font-family);
  margin-top: var(--spacing-xl);
}

@media (max-width: 768px) {
  .el-section {
    padding: 0 var(--spacing-sm);
  }
  
  .el-header h1 {
    font-size: var(--font-size-large);
  }
  
  .el-menu-item {
    font-size: 12px;
  }
}
</style>