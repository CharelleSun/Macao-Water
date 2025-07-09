<template>
  <div class="app-container">
    <div class="header">
      <h1>{{ $t('maintenance.detailTitle') }}</h1>
<el-button @click="goToList">
  {{ $t('maintenance.backToList') }}
</el-button>
    </div>

    <div class="main-content">
  <el-descriptions :title="$t('maintenance.detailTitle')" border>
    <el-descriptions-item :label="$t('detail.title')">{{ notice.title }}</el-descriptions-item>
    <el-descriptions-item :label="$t('maintenance.status')">
      <el-tag :type="getStatusTagType(notice.status)">
        {{ getStatusText(notice.status) }}
      </el-tag>
    </el-descriptions-item>
    <el-descriptions-item :label="$t('maintenance.createdAt')">{{ formatDateTime(notice.createdAt) }}</el-descriptions-item>
    <el-descriptions-item :label="$t('maintenance.startTime')">{{ formatDateTime(notice.startTime) }}</el-descriptions-item>
    <el-descriptions-item :label="$t('maintenance.endTime')">{{ formatDateTime(notice.endTime) }}</el-descriptions-item>
    <el-descriptions-item :label="$t('maintenance.noticeContent')" :span="3">
      <div class="content-box">
        {{ notice.content }}
      </div>
    </el-descriptions-item>
  </el-descriptions>
</div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useMaintenanceStore } from '../store/maintenance'
import {
  formatDateTime,
  getStatusTagType,
  getStatusText
} from '../utils/dateFormatter'

const props = defineProps({
  id: {
    type: String,
    required: true
  }
})

const router = useRouter()
const store = useMaintenanceStore()

const notice = computed(() => {
  return store.notices.find(item => item.id === props.id) || {}
})

const goToList = () => {
  router.push({ name: 'list' })
}
</script>

<style scoped>
.content-box {
  padding: 10px;
  background: #f9f9f9;
  border-radius: 4px;
  white-space: pre-line;
}
</style>
