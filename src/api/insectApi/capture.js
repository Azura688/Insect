import request from '@/utils/request'
// 上传设备拍摄图片
export function importCapture(data){
  return request({
    url: 'insectdata/data/batchData',
    method: 'post',
    params: data
  })
}
export function delCapture(id){
  return request({
    url: 'insectdata/data/delete/'+id,
    method: 'post',
  })
}


//获取昆虫详细信息
export function DetailInsect(insectId) {
  return request({
      url: 'insectdata/insect/' + insectId,
      method: 'get',

  })
}
