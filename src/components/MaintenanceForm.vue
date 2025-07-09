<template>
  <el-form
    :model="form"
    :rules="rules"
    ref="formRef"
    label-width="120px"
  >
    <el-form-item :label="$t('maintenance.noticeTitle')" prop="title">
      <el-input
        v-model="form.title"
        :placeholder="$t('maintenance.noticeTitle')"
        :maxlength="localeMaxLength"
        show-word-limit
      />
    </el-form-item>

    <el-form-item :label="$t('maintenance.noticeContent')" prop="content">
      <el-input
        v-model="form.content"
        type="textarea"
        :rows="5"
        :placeholder="$t('maintenance.noticeContent')"
      />
    </el-form-item>

    <el-form-item :label="$t('maintenance.maintenancePeriod')" prop="timeRange">
      <el-date-picker
        v-model="form.timeRange"
        type="datetimerange"
        :range-separator="$t('maintenance.to')"
        :start-placeholder="$t('maintenance.startTime')"
        :end-placeholder="$t('maintenance.endTime')"
        value-format="YYYY-MM-DD HH:mm:ss"
        style="width: 100%"
      />
    </el-form-item>



    <!-- 阶段二：验证信息 -->
<!-- 在 MaintenanceForm.vue 文件中 -->
<div v-if="phase2Enabled">
  <el-form-item 
    :label="$t('maintenance.securityVerification')" 
    prop="token">
    <div class="verification-group">
      <el-input
        v-model="form.token"
        :placeholder="$t('maintenance.enterVerificationCode')"
        style="flex: 1"
      />
      <el-button
        :disabled="countdown > 0"
        @click="handleSendVerification"
      >
        {{ 
          countdown > 0 
            ? $t('maintenance.resendAfter', { seconds: countdown }) 
            : $t('maintenance.getVerificationCode') 
        }}
      </el-button>
    </div>
  </el-form-item>
</div>

     <el-form-item>
      <el-button type="primary" @click="submitForm">{{ $t('maintenance.submit') }}</el-button>
      <el-button @click="cancel">{{ $t('maintenance.cancel') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
const { t, locale } = useI18n()
import { useMaintenanceStore } from '../store/maintenance'
import { ElMessage } from 'element-plus'


// 根据语言设置最大长度
const localeMaxLength = computed(() => {
  switch (locale.value) {
    case 'zh-CN': return 200
    case 'en-US': return 400
    case 'pt-PT': return 400
    default: return 200
  }
})


const props = defineProps({
  initialData: {
    type: Object,
    default: () => ({
      title: '',
      content: '',
      timeRange: [],
      token: ''
    })
  }
})

const emit = defineEmits(['submit', 'cancel'])

const store = useMaintenanceStore()
const form = ref({ ...props.initialData })
const formRef = ref(null)
const countdown = ref(0)

const phase2Enabled = computed(() => store.phase2Enabled)

const rules = {
  title: [
    { required: true, message: t('validation.titleRequired'), trigger: 'blur' },
    { 
      min: 5, 
      max: localeMaxLength.value, 
      message: t('validation.titleLength'), 
      trigger: 'blur' 
    }
  ],
  content: [
    { required: true, message: t('validation.contentRequired'), trigger: 'blur' }
  ],
  timeRange: [
    {
      required: true,
      message: t('validation.timeRangeRequired'),
      trigger: 'change',
      validator: (rule, value, callback) => {
        if (!value || value.length !== 2) {
          callback(new Error(t('validation.timeRangeRequired')))
        } else {
          callback()
        }
      }
    }
  ],

  token: [
    {
      required: phase2Enabled.value,
      message: '请输入验证码',
      trigger: 'blur'
    },
    {
      min: 6,
      max: 6,
      message: '验证码为6位数字',
      trigger: 'blur',
      validator: (rule, value, callback) => {
        if (!phase2Enabled.value) {
          callback()
          return
        }

        if (/^\d{6}$/.test(value)) {
          callback()
        } else {
          callback(new Error('验证码必须是6位数字'))
        }
      }
    }
  ]
}

const handleSendVerification = async () => {
  try {
    const success = await store.sendVerificationCode();
    if (success) {
      ElMessage.success(t('maintenance.codeSent'));
      // 开始倒计时...
    }
  } catch (error) {
    ElMessage.error(t('maintenance.codeSendFailed') + ': ' + error.message);
  }
}





const submitForm = async () => {
  try {
    const valid = await formRef.value.validate()
    if (!valid) return

    const [start, end] = form.value.timeRange
    if (new Date(start) >= new Date(end)) {
      ElMessage.error(t('validation.endAfterStart'))
      return
    }

    emit('submit', {
      title: form.value.title,
      content: form.value.content,
      startTime: start,
      endTime: end,
      //token: form.value.token
      token: "011026"
    })
  } catch (error) {
    console.error(t('validation.formValidationFailed'), error)
  }
}

const cancel = () => {
  emit('cancel')
}
</script>

<style scoped>
.verification-group {
  display: flex;
  gap: 10px;
}
</style>
