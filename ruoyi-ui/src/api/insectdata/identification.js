import request from '@/utils/request'

// 查询识别结果列表
export function listIdentification(query) {
  return request({
    url: '/insectdata/identification/list',
    method: 'get',
    params: query
  })
}

// 查询识别结果详细
export function getIdentification(dataId) {
  return request({
    url: '/insectdata/identification/' + dataId,
    method: 'get'
  })
}

// 新增识别结果
export function addIdentification(data) {
  return request({
    url: '/insectdata/identification',
    method: 'post',
    data: data
  })
}

// 修改识别结果
export function updateIdentification(data) {
  return request({
    url: '/insectdata/identification',
    method: 'put',
    data: data
  })
}

// 删除识别结果
export function delIdentification(dataId) {
  return request({
    url: '/insectdata/identification/' + dataId,
    method: 'delete'
  })
}

// 导出识别结果
export function exportIdentification(query) {
  return request({
    url: '/insectdata/identification/export',
    method: 'get',
    params: query
  })
}