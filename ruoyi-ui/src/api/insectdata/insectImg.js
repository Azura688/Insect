import request from '@/utils/request'

// 查询昆虫图片列表
export function listInsectImg(query) {
  return request({
    url: '/insectdata/insectImg/list',
    method: 'get',
    params: query
  })
}

// 查询昆虫图片详细
export function getInsectImg(insectId) {
  return request({
    url: '/insectdata/insectImg/' + insectId,
    method: 'get'
  })
}

// 新增昆虫图片
export function addInsectImg(data) {
  return request({
    url: '/insectdata/insectImg',
    method: 'post',
    data: data
  })
}

// 修改昆虫图片
export function updateInsectImg(data) {
  return request({
    url: '/insectdata/insectImg',
    method: 'put',
    data: data
  })
}

// 删除昆虫图片
export function delInsectImg(insectId) {
  return request({
    url: '/insectdata/insectImg/' + insectId,
    method: 'delete'
  })
}

// 导出昆虫图片
export function exportInsectImg(query) {
  return request({
    url: '/insectdata/insectImg/export',
    method: 'get',
    params: query
  })
}