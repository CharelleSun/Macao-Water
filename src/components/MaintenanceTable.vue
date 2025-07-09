<template>
  <div>
    <el-table :data="data" v-loading="loading">
      <el-table-column :label="$t('maintenance.noticeTitle')" prop="title" width="200" />
  <el-table-column :label="$t('maintenance.maintenancePeriod')" width="300">
        <template #default="{ row }">
          {{ formatDateTime(row.startTime) }} →
          {{ formatDateTime(row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('maintenance.status')">
        <template #default="{ row }">
          <el-tag :type="getStatusTagType(row.status)" class="status-tag">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('maintenance.createdAt')" width="180">
        <template #default="{ row }">
          {{ formatDateTime(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('maintenance.operations')" width="180">
        <template #default="{ row }">
           <el-button size="small" @click="viewDetail(row)">{{ $t('maintenance.view') }}</el-button>
          <el-popconfirm
            :title="$t('maintenance.confirmDelete')"
            @confirm="deleteItem(row.id)"
          >
            <template #reference>
              <el-button
                size="small"
                type="danger"
                :disabled="row.status !== 'PENDING'"
              >
                {{ $t('maintenance.delete') }}
              </el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

     <div class="pagination">
      <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        @current-change="handlePageChange"
        layout="prev, pager, next, jumper"
        :page-sizes="[10, 20, 30]" 
        @size-change="handleSizeChange" 
      />
    </div>
  </div>
</template>

<script setup>

import { formatDateTime, getStatusTagType} from '../utils/dateFormatter'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

defineProps({
  data: Array,
  loading: Boolean,
  currentPage: Number,
  pageSize: Number,
  total: Number
})

const emit = defineEmits(['page-change', 'view-detail', 'delete-item'])

const getStatusText = (status) => {
  const map = {
    PENDING: t('maintenance.pending'),
    ONGOING: t('maintenance.ongoing'),
    COMPLETED: t('maintenance.completed')
  }
  return map[status] || status
}

const handlePageChange = (page) => {
  emit('page-change', page)
}

const viewDetail = (item) => {
  emit('view-detail', item)
}

const deleteItem = (id) => {
  emit('delete-item', id)
}

const handleSizeChange = (size) => {
  emit('size-change', size)
}
</script>

<style scoped>
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.status-tag {
  margin-left: 5px;
}
</style>