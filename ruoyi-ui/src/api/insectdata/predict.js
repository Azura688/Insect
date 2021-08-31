import request from '@/utils/request'

// 查询预测结果列表
export function listPredict(query) {
  return request({
    url: '/insectdata/predict/list',
    method: 'get',
    params: query
  })
}

// 查询预测结果详细
export function getPredict(insectId) {
  return request({
    url: '/insectdata/predict/' + insectId,
    method: 'get'
  })
}

// 新增预测结果
export function addPredict(data) {
  return request({
    url: '/insectdata/predict',
    method: 'post',
    data: data
  })
}

// 修改预测结果
export function updatePredict(data) {
  return request({
    url: '/insectdata/predict',
    method: 'put',
    data: data
  })
}

// 删除预测结果
export function delPredict(insectId) {
  return request({
    url: '/insectdata/predict/' + insectId,
    method: 'delete'
  })
}

// 导出预测结果
export function exportPredict(query) {
  return request({
    url: '/insectdata/predict/export',
    method: 'get',
    params: query
  })
}