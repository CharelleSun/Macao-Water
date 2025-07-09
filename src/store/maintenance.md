import { defineStore } from 'pinia'
import { ref, computed } from 'vue' 
import { getStatus } from '../utils/dateFormatter'
import { fetchNotices, createNotice, deleteNotice } from '../services/maintenance'

export const useMaintenanceStore = defineStore('maintenance', () => {
  const notices = ref([])
  const loading = ref(false)
  const pagination = ref({
    page: 1,
    pageSize: 10,
    total: 0
  })


    // 阶段二功能 - 使用 computed 定义
  const phase2Enabled = computed(() => import.meta.env.VITE_PHASE2_ENABLED === 'true')
  
  const verification = ref({
    sent: false,
    email: 'admin@macaowater.com'
  })
  

  // 获取公告列表
  const fetchMaintenanceNotices = async (params = {}) => {
  loading.value = true
  try {
    const response = await fetchNotices({
      page: pagination.value.page,
      pageSize: pagination.value.pageSize,
      ...params
    })

    
    notices.value = response.items.map(notice => ({
      ...notice,
      status: getStatus(notice.startTime, notice.endTime)
    }))
    pagination.value.total = response.total
  } catch (error) {
    console.error('获取公告列表失败:', error)
  } finally {
    loading.value = false
  }
}

  // 创建新公告
  const createMaintenanceNotice = async (noticeData) => {
    try {
      // const data = phase2Enabled.value
      //   ? { ...noticeData, token: verification.value.token }
      //   : noticeData

      await createNotice(data)
      return true
    } catch (error) {
      console.error('创建公告失败:', error)
      return false
    }
  }

  // 删除公告
  const deleteMaintenanceNotice = async (id) => {
    try {
      await deleteNotice(id)
      return true
    } catch (error) {
      console.error('删除公告失败:', error)
      return false
    }
  }

  // 阶段二：发送验证码
  // const sendVerificationCode = async () => {
  //   try {
  //     // 实际项目中调用API发送邮件
  //     console.log(`发送验证码到: ${verification.value.email}`)
  //     verification.value.sent = true
  //     return true
  //   } catch (error) {
  //     console.error('发送验证码失败:', error)
  //     return false
  //   }
  // }

  return {
    notices,
    loading,
    pagination,
    phase2Enabled,
    verification,
    fetchMaintenanceNotices,
    createMaintenanceNotice,
    deleteMaintenanceNotice,
    // sendVerificationCode
  }
})
