<!-- <template>
  <div class="app-container">
    <div class="header">
      <h1>维护公告管理</h1>
      <el-button type="primary" @click="goToCreate">新增公告</el-button>
    </div>

    <div class="search-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="公告标题">
          <el-input v-model="searchForm.title" placeholder="输入标题关键字" clearable />
        </el-form-item>
        <el-form-item label="维护时间">
          <el-date-picker
            v-model="searchForm.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部">
            <el-option label="全部" value="" />
            <el-option label="即将开始" value="PENDING" />
            <el-option label="进行中" value="ONGOING" />
            <el-option label="已完成" value="COMPLETED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div> -->

    <template>
  <div class="app-container">
    <div class="header">
      <h1>{{ $t('maintenance.listTitle') }}</h1>
      <el-button type="primary" @click="goToCreate">
        {{ $t('maintenance.createNotice') }}
      </el-button>
    </div>

    <div class="search-bar">
    <el-form :inline="true" :model="searchForm">
      <el-form-item :label="$t('maintenance.noticeTitle')">
        <el-input 
          v-model="searchForm.title" 
          :placeholder="$t('maintenance.noticeTitle')" 
          clearable 
        />
      </el-form-item>
      
      <el-form-item :label="$t('maintenance.status')">
        <el-select 
          v-model="searchForm.status" 
          :placeholder="$t('maintenance.allStatus')"
        >
          <el-option :label="$t('maintenance.allStatus')" value="" />
          <el-option :label="$t('maintenance.pending')" value="PENDING" />
          <el-option :label="$t('maintenance.ongoing')" value="ONGOING" />
          <el-option :label="$t('maintenance.completed')" value="COMPLETED" />
        </el-select>
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="search">
          {{ $t('maintenance.search') }}
        </el-button>
        <el-button @click="resetSearch">
          {{ $t('maintenance.reset') }}
        </el-button>
      </el-form-item>
    </el-form>
  </div>

    <div class="main-content">
      <MaintenanceTable
        :data="notices"
        :loading="loading"
        :current-page="pagination.page"
        :page-size="pagination.pageSize"
        :total="pagination.total"
        @page-change="handlePageChange"
        @view-detail="viewDetail"
        @delete-item="deleteNotice"
        @size-change="handleSizeChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n' // 导入国际化函数
import { useMaintenanceStore } from '../store/maintenance'
import MaintenanceTable from '../components/MaintenanceTable.vue'
import { ElMessage } from 'element-plus'

// 获取翻译函数
const { t } = useI18n()

const router = useRouter()
const store = useMaintenanceStore()
const phase2Enabled = store.phase2Enabled

const searchForm = ref({
  title: '',
  timeRange: [],
  status: ''
})

const handleSizeChange = (size) => {
  store.pagination.pageSize = size
  store.fetchMaintenanceNotices()
}

const notices = computed(() => store.notices)
const loading = computed(() => store.loading)
const pagination = computed(() => store.pagination)

onMounted(() => {
  store.fetchMaintenanceNotices()
})

const search = () => {
  const params = {
    title: searchForm.value.title,
    status: searchForm.value.status,
    page: store.pagination.page,
    pageSize: store.pagination.pageSize
  }

  store.fetchMaintenanceNotices(params)
}

// 确保重置方法正确重置查询条件
const resetSearch = () => {
  searchForm.value = {
    title: '',
    status: ''
  }
  store.fetchMaintenanceNotices()
}

const handlePageChange = (page) => {
  store.pagination.page = page
  store.fetchMaintenanceNotices()
}

const goToCreate = () => {
  router.push({ name: 'create' })
}

const viewDetail = (item) => {
  router.push({ name: 'detail', params: { id: item.id } })
}

const deleteNotice = async (id) => {
  const success = await store.deleteMaintenanceNotice(id)
  if (success) {
    ElMessage.success(t('maintenance.deleteSuccess'))
    store.fetchMaintenanceNotices()
  } else {
    ElMessage.error(t('maintenance.deleteFailed'))
  }
}
</script>

<style scoped>
.app-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.search-bar {
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.main-content {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>