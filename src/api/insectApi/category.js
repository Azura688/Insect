import request from '@/utils/request'

// 查询昆虫种类列表
export function listCategory(query) {
  return request({
    url: '/system/category/list',
    method: 'get',
    params: query
  })
}

// 查询昆虫种类列表（排除节点）
export function listCategoryExcludeChild(categoryId) {
  return request({
    url: '/system/category/list/exclude/' + categoryId,
    method: 'get'
  })
}

// 查询昆虫种类详细
export function getCategory(categoryId) {
  return request({
    url: '/system/category/' + categoryId,
    method: 'get'
  })
}

// 查询昆虫种类下拉树结构
export function treeselect() {
  return request({
    url: '/system/category/treeselect',
    method: 'get'
  })
}


// 新增昆虫种类
export function addCategory(data) {
  return request({
    url: '/system/category',
    method: 'post',
    data: data
  })
}

// 修改昆虫种类
export function updateCategory(data) {
  return request({
    url: '/system/category',
    method: 'put',
    data: data
  })
}

// 删除昆虫种类
export function delCategory(categoryId) {
  return request({
    url: '/system/category/' + categoryId,
    method: 'delete'
  })
}