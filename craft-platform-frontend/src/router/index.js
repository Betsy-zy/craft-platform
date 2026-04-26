import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/craftsmen',
    name: 'Craftsmen',
    component: () => import('../views/Craftsmen.vue')
  },
  {
    path: '/works',
    name: 'Works',
    component: () => import('../views/Works.vue')
  },

  {
    path: '/cart',
    name: 'Cart',
    component: () => import('../views/Cart.vue')
  },
  {
    path: '/messages',
    name: 'Message',
    component: () => import('../views/Message.vue')
  },
  {
    path: '/experiences',
    name: 'Experiences',
    component: () => import('../views/Experiences.vue')
  },
  {
    path: '/appointment',
    name: 'Appointment',
    component: () => import('../views/Appointment.vue')
  },
  {
    path: '/information',
    name: 'Information',
    component: () => import('../views/Information.vue')
  },
  {
    path: '/information/:id',
    name: 'InfoDetail',
    component: () => import('../views/InfoDetail.vue')
  },
  {
    path: '/craftsman/:id',
    name: 'CraftsmanDetail',
    component: () => import('../views/CraftsmanDetail.vue')
  },
  {
    path: '/work/:id',
    name: 'WorkDetail',
    component: () => import('../views/WorkDetail.vue')
  },
  {
    path: '/admin',
    redirect: '/admin/dashboard'
  },
  {
    path: '/admin/dashboard',
    name: 'Admin',
    component: () => import('../views/Admin.vue')
  },
  {
    path: '/admin/users',
    name: 'UserManagement',
    component: () => import('../views/admin/UserManagement.vue')
  },

  {
    path: '/admin/appointments',
    name: 'AppointmentManagement',
    component: () => import('../views/admin/AppointmentManagement.vue')
  },
  {
    path: '/admin/orders',
    name: 'OrderManagement',
    component: () => import('../views/admin/OrderManagement.vue')
  },
  {
    path: '/admin/craftsmen',
    name: 'CraftsmanManagement',
    component: () => import('../views/admin/CraftsmanManagement.vue')
  },
  {
    path: '/admin/works',
    name: 'WorkManagement',
    component: () => import('../views/admin/WorkManagement.vue')
  },

  {
    path: '/admin/information',
    name: 'InformationManagement',
    component: () => import('../views/admin/InformationManagement.vue')
  },
  {
    path: '/admin/experiences',
    name: 'ExperienceManagement',
    component: () => import('../views/admin/ExperienceManagement.vue')
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import('../views/Orders.vue')
  },
  {
    path: '/order/:id',
    name: 'OrderDetail',
    component: () => import('../views/OrderDetail.vue')
  },

  {
    path: '/user-center',
    name: 'UserCenter',
    component: () => import('../views/UserCenter.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 暂时允许所有用户访问所有页面，以便测试功能
  next()
})

export default router