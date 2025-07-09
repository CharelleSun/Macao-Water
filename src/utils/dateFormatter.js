import dayjs from 'dayjs'
import { useI18n } from 'vue-i18n'

// 格式化日期时间
export const formatDateTime = (dateStr, format = 'YYYY-MM-DD HH:mm') => {
  return dayjs(dateStr).format(format)
}

// 获取公告状态
export const getStatus = (startTime, endTime) => {
  const now = dayjs()
  const start = dayjs(startTime)
  const end = dayjs(endTime)

  if (now.isBefore(start)) return 'PENDING'
  if (now.isAfter(end)) return 'COMPLETED'
  return 'ONGOING'
}

// 获取状态标签类型
export const getStatusTagType = (status) => {
  switch (status) {
    case 'PENDING': return 'warning'
    case 'ONGOING': return 'danger'
    case 'COMPLETED': return 'success'
    default: return 'info'
  }
}

export const getStatusText = (status) => {
  const { t } = useI18n()
  
  const map = {
    PENDING: t('maintenance.pending'),
    ONGOING: t('maintenance.ongoing'),
    COMPLETED: t('maintenance.completed')
  }
  
  return map[status] || status
}
