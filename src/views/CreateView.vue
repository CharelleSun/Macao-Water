<template>
  <div class="app-container">
    <div class="header">
      <h1>{{ $t('maintenance.createTitle') }}</h1>
      <el-button @click="goToList">
        {{ $t('maintenance.backToList') }}
      </el-button>
    </div>

    <div class="main-content form-container">
      <MaintenanceForm
        ref="formRef"
        @submit="handleSubmit"
        @cancel="goToList"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import { useMaintenanceStore } from '../store/maintenance'
import MaintenanceForm from '../components/MaintenanceForm.vue'


const { t } = useI18n()
const router = useRouter()
const store = useMaintenanceStore()
const formRef = ref(null)

const goToList = () => {
  router.push({ name: 'list' })
}


const handleSubmit = async (formData) => {
  // 按照后端要求的结构组织数据
  const requestData = {
    notice: {  
      title: formData.title,
      content: formData.content,
      startTime: formData.startTime,
      endTime: formData.endTime
    },
    token: formData.token 
  };
  
  const success = await store.createMaintenanceNotice(requestData);
  
  if (success) {
    ElMessage.success(t('maintenance.createSuccess'));
    goToList();
  } else {
    ElMessage.error(t('maintenance.createFailed'));
  }
}
// const handleSubmit = async (formData) => {
//   const success = await store.createMaintenanceNotice(formData)
//   if (success) {
//     ElMessage.success('公告创建成功')
//     goToList()
//   } else {
//     ElMessage.error('公告创建失败')
//   }
// }
</script>

<style scoped>
.form-container {
  max-width: 800px;
  margin: 0 auto;
}
</style>
