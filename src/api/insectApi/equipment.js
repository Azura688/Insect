import request from '@/utils/request'
// 查询设备列表
export function listAllEquipment(query){
  return request({
    url: 'insectdata/equipment/list',
    method: 'get',
    params: query
  })
}

// 根据设备编号查询设备详细信息
export function getEquipmentById(id){
  return request({
    url: 'insectdata/equipment/'+id,
    method: 'get',
  })
}

// 添加设备
export function addEquipment(data){
  return request({
    url: 'insectdata/equipment',
    method: 'post',
    data: data
  })
}

// 修改设备信息
export function updateEquipment(data){
  return request({
    url: 'insectdata/equipment/edit',
    method: 'post',
    data: data
  })
}
// 删除设备
export function delEquipment(ids){
  return request({
    url: 'insectdata/equipment/delete/'+ids,
    method: 'post',
  })
}

// 导出设备信息
export function exportEquipment(){
  return request({
    url: 'insectdata/equipment/export',
    method: 'get',
  })
}
// 上传设备拍摄图片
export function importCapture(data){
  return request({
    url: 'insectdata/data/batchData',
    method: 'post',
    params: data
  })
}