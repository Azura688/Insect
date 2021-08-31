import request from '@/utils/request'

// 查询设备列表
export function listEquipment(query) {
  return request({
    url: '/insectdata/equipment/list',
    method: 'get',
    params: query
  })
}

// 查询设备详细
export function getEquipment(id) {
  return request({
    url: '/insectdata/equipment/' + id,
    method: 'get'
  })
}

// 新增设备
export function addEquipment(data) {
  return request({
    url: '/insectdata/equipment',
    method: 'post',
    data: data
  })
}

// 修改设备
export function updateEquipment(data) {
  return request({
    url: '/insectdata/equipment',
    method: 'put',
    data: data
  })
}

// 删除设备
export function delEquipment(id) {
  return request({
    url: '/insectdata/equipment/' + id,
    method: 'delete'
  })
}

// 导出设备
export function exportEquipment(query) {
  return request({
    url: '/insectdata/equipment/export',
    method: 'get',
    params: query
  })
}