import request from '@/utils/request'
// 根据某天
export function listByDate(query){
  return request({
    url: '/insectdata/identification/listByDate',
    method: 'get',
    params: query
  })
}
// 根据日期
export function listByDatePeriod(query){
  return request({
    url: '/insectdata/identification/listByDatePeriod',
    method: 'get',
    params: query
  })
}


// 下面的没用
export function addInsectNumber(data){
  return request({
    url: '/insectdata/identification',
    method: 'post',
    params: data
  })
}

export function updateInsectNumber(data){
  return request({
    url: '/insectdata/identification/edit',
    method: 'post',
    params: data
  })
}

export function listAllInsectNumber(query){
  return request({
    url: '/insectdata/identification/list',
    method: 'get',
    params: query
  })
}