import request from '@/utils/request'

// 查询昆虫列表
export function listInsect(query) {
  return request({
    url: '/insectdata/insect/list',
    method: 'get',
    params: query
  })
}

// 根据昆虫种类(目0、科1、种2)查询
export function listSpecies(query) {
  return request({
    url: '/insectdata/insect/listSpecies',
    method: 'get',
    params: query
  })
}

// 查询昆虫列表（排除节点）
export function listInsectExcludeChild(insectId) {
  return request({
    url: '/insectdata/insect/list/exclude/' + insectId,
    method: 'get'
  })
}

// 查询昆虫详细
export function getInsect(insectId) {
  return request({
    url: '/insectdata/insect/' + insectId,
    method: 'get'
  })
}

// 查询昆虫下拉树结构
export function treeselect() {
  return request({
    url: '/insectdata/insect/treeselect',
    method: 'get'
  })
}

// 根据角色ID查询昆虫树结构
export function roleInsectTreeselect(roleId) {
  return request({
    url: '/insectdata/insect/roleInsectTreeselect/' + roleId,
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
    url: '/insectdata/insect/edit',
    method: 'post',
    data: data
  })
}

// 删除昆虫
export function delInsect(insectId) {
  return request({
    url: '/insectdata/insect/delete/' + insectId,
    method: 'post'
  })
}
//导出昆虫数据
export function exportInsect() {
  return request({
    url: '/insectdata/insect/export',
    method: 'get'
  })
}