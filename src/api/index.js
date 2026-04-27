import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

//// 用户相关
api.register = (user) => api.post('/users/register', user)
api.login = (username, password) => api.post('/users/login', { username, password })
api.getUsers = () => api.get('/users')
api.getUser = (id) => api.get(`/users/${id}`)
api.updateUser = (user) => api.put('/users', user)
api.deleteUser = (id) => api.delete(`/users/${id}`)
api.uploadUserAvatar = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return api.post('/users/upload', formData)
}

// 匠人相关
api.getCraftsmen = () => api.get('/craftsmen/status/active')
api.getAllCraftsmen = () => api.get('/craftsmen')
api.getCraftsman = (id) => api.get(`/craftsmen/${id}`)
api.createCraftsman = (craftsman) => api.post('/craftsmen', craftsman)
api.updateCraftsman = (craftsman) => api.put('/craftsmen', craftsman)
api.deleteCraftsman = (id) => api.delete(`/craftsmen/${id}`)
api.uploadCraftsmanAvatar = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return api.post('/craftsmen/upload', formData)
}

// 作品相关
api.getWorks = () => api.get('/works/active')
api.getAllWorks = () => api.get('/works')
api.getWork = (id) => api.get(`/works/${id}`)
api.getWorksByCraftsman = (craftsmanId) => api.get(`/works/craftsman/${craftsmanId}`)
api.createWork = (work) => api.post('/works', work)
api.updateWork = (work) => api.put('/works', work)
api.deleteWork = (id) => api.delete(`/works/${id}`)
api.uploadWorkImage = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return api.post('/works/upload', formData)
}

// 体验项目相关
api.getExperiences = () => api.get('/experiences')
api.getAllExperiences = () => api.get('/experiences')
api.getExperience = (id) => api.get(`/experiences/${id}`)
api.getExperiencesByCraftsman = (craftsmanId) => api.get(`/experiences/craftsman/${craftsmanId}`)
api.createExperience = (experience) => api.post('/experiences', experience)
api.updateExperience = (experience) => api.put('/experiences', experience)
api.deleteExperience = (id) => api.delete(`/experiences/${id}`)
api.updateExperienceStatus = (id, status) => api.put(`/experiences/status/${id}`, {}, { params: { status } })

// 预约相关
api.createAppointment = (appointment) => api.post('/appointments', appointment)
api.getAppointments = () => api.get('/appointments')
api.updateAppointment = (appointment) => api.put('/appointments', appointment)
api.deleteAppointment = (id) => api.delete(`/appointments/${id}`)
api.updateAppointmentStatus = (id, status) => api.put(`/appointments/status/${id}`, { status })



// 商品订单相关
api.getProductOrders = () => api.get('/product-orders')
api.getProductOrdersWithDetails = () => api.get('/product-orders/details')
api.getUserProductOrders = (userId) => api.get(`/product-orders/user/${userId}`)
api.getProductOrder = (id) => api.get(`/product-orders/${id}`)
api.createProductOrder = (order) => api.post('/product-orders', order)
api.updateProductOrderStatus = (id, status) => api.put(`/product-orders/status/${id}`, {}, { params: { status } })
api.deleteOrder = (id) => api.delete(`/product-orders/${id}`)

// 收藏相关
api.createCollection = (collection) => api.post('/collections', collection)
api.getUserCollections = (userId) => api.get(`/collections/user/${userId}`)
api.deleteCollection = (id) => api.delete(`/collections/${id}`)
api.deleteCollectionByUserTarget = (userId, targetId, targetType) => api.delete('/collections/user-target', { params: { userId, targetId, targetType } })

// 消息相关
api.getUserMessages = (userId) => api.get(`/messages/user/${userId}`)
api.markMessageAsRead = (id) => api.put(`/messages/read/${id}`)
api.markAllMessagesAsRead = (userId) => api.put(`/messages/read-all/${userId}`)
api.deleteMessage = (id) => api.delete(`/messages/${id}`)
api.deleteAllMessages = (userId) => api.delete(`/messages/all/${userId}`)
api.getUnreadCount = (userId) => api.get(`/messages/unread-count/${userId}`)

// 资讯相关
api.getAllInformation = () => api.get('/information')
api.getInformation = (id) => api.get(`/information/${id}`)
api.createInformation = (information) => api.post('/information', information)
api.updateInformation = (information) => api.put('/information', information)
api.deleteInformation = (id) => api.delete(`/information/${id}`)
api.updateInformationStatus = (id, status) => api.put(`/information/status/${id}`, { status })

export default api