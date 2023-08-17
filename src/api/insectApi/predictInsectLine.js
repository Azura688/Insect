import request from '@/utils/request'
// 根据某天
export function listByDate(query){
  return request({
    url: '/insectdata/predict/listByDate',
    method: 'get',
    params: query
  })
}
// 根据日期
export function listByDatePeriod(query){
  return request({
    url: '/insectdata/predict/listByDatePeriod',
    method: 'get',
    params: query
  })
}