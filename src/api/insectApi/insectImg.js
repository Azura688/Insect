import request from '@/utils/request'

// 查询昆虫列表
export function listInsect(query) {
  return request({
    url: '/insectdata/insect/list',
    method: 'get',
    params: query
  })
}
export function listSpecies(query) {
  return request({
    url: '/insectdata/insect/listSpecies',
    method: 'get',
    params: query
  })
}


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
    params: data
  })
}

// 批量新增昆虫图像
export function addBatchInsectImg(data) {
  return request({
    url: 'insectdata/insectImg/upLoadAndInsertImg',
    method: 'post',
    params: data
  })
}

// 修改昆虫图像
export function updateInsectImg(data) {
  return request({
    url: 'insectdata/insectImg/edit',
    method: 'post',
    data: data
  })
}

// 删除昆虫图像
export function delInsectImg(data) {
  return request({
    url: 'insectdata/insectImg/delete',
    method: 'post',
    data: data
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