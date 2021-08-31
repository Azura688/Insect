import request from '@/utils/request'

// 查询识别数据列表
export function listData(query) {
  return request({
    url: '/insectdata/data/list',
    method: 'get',
    params: query
  })
}

// 查询识别数据详细
export function getData(dataId) {
  return request({
    url: '/insectdata/data/' + dataId,
    method: 'get'
  })
}

// 新增识别数据
export function addData(data) {
  return request({
    url: '/insectdata/data',
    method: 'post',
    data: data
  })
}

// 修改识别数据
export function updateData(data) {
  return request({
    url: '/insectdata/data',
    method: 'put',
    data: data
  })
}

// 删除识别数据
export function delData(dataId) {
  return request({
    url: '/insectdata/data/' + dataId,
    method: 'delete'
  })
}

// 导出识别数据
export function exportData(query) {
  return request({
    url: '/insectdata/data/export',
    method: 'get',
    params: query
  })
}