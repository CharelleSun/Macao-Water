<template>
  <div class="app">
    <el-container>
      <el-header class="app-header">
        <div class="header-left">
          <h1>{{ $t('maintenance.systemTitle') }}</h1>
        </div>
        
        <div class="header-right">
          <el-select
            v-model="currentLocale"
            @change="changeLanguage"
            class="language-selector"
            popper-class="language-popper"
          >
            <el-option
              v-for="lang in languages"
              :key="lang.value"
              :value="lang.value"
              :label="lang.label"
            />
          </el-select>
        </div>
      </el-header>
      
      <el-main>
        <router-view />
      </el-main>
      
      <el-footer class="app-footer">
        {{ $t('maintenance.footerText') }}
      </el-footer>
    </el-container>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { ref, onMounted } from 'vue'

const { locale } = useI18n()

// 当前选择的语言
const currentLocale = ref(locale.value)

// 语言选项配置
const languages = ref([
  { value: 'zh-CN', label: '简体中文' },
  { value: 'en-US', label: 'English' },
  { value: 'pt-PT', label: 'Português' }
])

// 从本地存储加载语言设置
onMounted(() => {
  const savedLocale = localStorage.getItem('userLocale')
  if (savedLocale) {
    locale.value = savedLocale
    currentLocale.value = savedLocale
  }
})

// 切换语言
const changeLanguage = (lang) => {
  locale.value = lang
  currentLocale.value = lang
  localStorage.setItem('userLocale', lang)
}
</script>

<style scoped>
.app-header {
  background: #409EFF;
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
}

.header-left, .header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

/* 确保选择器在右上角 */
.header-right {
  margin-left: auto; /* 将选择器推到最右侧 */
}

.language-selector {
  width: 140px; /* 适当的宽度 */
}

.language-selector :deep(.el-input__inner) {
  background-color: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  height: 36px;
  border-radius: 18px;
  cursor: pointer;
  transition: all 0.3s;
  text-align: center; /* 文本居中 */
  padding: 0 10px; /* 简化内边距 */
}

.language-selector :deep(.el-input__inner):hover {
  background-color: rgba(255, 255, 255, 0.3);
}

.language-selector :deep(.el-input__suffix) {
  color: white;
}

.app-footer {
  text-align: center;
  padding: 20px;
  background: #f5f7fa;
  color: #606266;
  font-size: 14px;
}
</style>

<style>
/* 全局样式 - 语言选项 */
.language-popper .el-select-dropdown__item {
  padding: 10px 20px;
  text-align: center; /* 选项文本居中 */
}

/* 小屏幕适配 */
@media (max-width: 768px) {
  .app-header {
    padding: 0 10px;
  }
  
  .header-left h1 {
    font-size: 18px;
  }
  
  .language-selector {
    width: 120px;
  }
  
  .language-selector :deep(.el-input__inner) {
    font-size: 13px;
  }
}

/* 超小屏幕适配 */
@media (max-width: 480px) {
  .app-header {
    flex-wrap: wrap;
    height: auto;
    padding: 10px;
  }
  
  .header-left, .header-right {
    width: 100%;
    justify-content: center;
    margin-top: 10px;
  }
  
  .language-selector {
    width: 100%;
    max-width: 200px;
  }
}
</style>