import { createI18n } from 'vue-i18n'
import zhCN from './locales/zh-CN'
import enUS from './locales/en-US'
import ptPT from './locales/pt-PT'

// 从本地存储获取用户语言偏好
const savedLocale = localStorage.getItem('userLocale') || 'zh-CN'

const i18n = createI18n({
  legacy: false,
  locale: savedLocale,
  fallbackLocale: 'zh-CN',
  messages: {
    'zh-CN': zhCN,
    'en-US': enUS,
    'pt-PT': ptPT
  }
})

export default i18n