import request from '@/utils/request'

// 查询昆虫图像列表
export function listInsectImg(query) {
  return request({
    url: 'insectdata/insectImg/list',
    method: 'get',
    params: query
  })
}

// 查询昆虫图像详细
export function getInsectImg(imgId) {
  return request({
    url: 'insectdata/insectImg/' + imgId,
    method: 'get'
  })
}

// 新增昆虫图像
export function addInsectImg(data) {
  return request({
    url: 'insectdata/insectImg',
    method: 'post',
    data: data
  })
}

// 修改昆虫图像
export function updateInsectImg(data) {
  return request({
    url: 'insectdata/insectImg',
    method: 'put',
    data: data
  })
}

// 删除昆虫图像
export function delInsectImg(imgId) {
  return request({
    url: 'insectdata/insectImg' + imgId,
    method: 'delete'
  })
}

// 导出昆虫图像
export function exportInsectImg(query) {
  return request({
    url: 'insectdata/insectImg/export',
    method: 'get',
    params: query
  })
}