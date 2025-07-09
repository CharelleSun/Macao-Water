import api from './api'

export const fetchNotices = (params) => {
  // return api.get('/maintenance', { params })
  return api.post('/maintenance/getList', {"page": params.page, "pageSize":params.pageSize, "title":params.title})
}

export const createNotice = (data) => {
  return api.post('/maintenance', data)
}

export const deleteNotice = (id) => {
  // return api.delete('/maintenance/' + id)
  return api.post('/maintenance/delete', {"id":id})
}

export const sendVerificationCode = () => {
  return api.post('/maintenance/verification/send-code')
}