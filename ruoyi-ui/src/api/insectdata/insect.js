import request from '@/utils/request'

// 查询昆虫列表
export function listInsect(query) {
  return request({
    url: '/insectdata/insect/list',
    method: 'get',
    params: query
  })
}

// 查询昆虫详细
export function getInsect(insectId) {
  return request({
    url: '/insectdata/insect/' + insectId,
    method: 'get'
  })
}

// 新增昆虫
export function addInsect(data) {
  return request({
    url: '/insectdata/insect',
    method: 'post',
    data: data
  })
}

// 修改昆虫
export function updateInsect(data) {
  return request({
    url: '/insectdata/insect',
    method: 'put',
    data: data
  })
}

// 删除昆虫
export function delInsect(insectId) {
  return request({
    url: '/insectdata/insect/' + insectId,
    method: 'delete'
  })
}

// 导出昆虫
export function exportInsect(query) {
  return request({
    url: '/insectdata/insect/export',
    method: 'get',
    params: query
  })
}