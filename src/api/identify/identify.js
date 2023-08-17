import request from '@/utils/request'

//查询图片栏列表
export function Image(query) {
    return request({
        url: '/insectdata/data/list',
        method: 'get',
        params: query
    })
}
//查询昆虫列表
export function InsectList(dataId) {
    return request({
        url: '/insectdata/identification/detail/' + dataId,
        method: 'get',
        // params: params
    })
}
//增加昆虫
export function AddInsect(data) {
    return request({
        url: 'insectdata/identification',
        method: 'post',
        data: data
    })
}
//修改昆虫列表
export function updateInsect(data) {
    return request({
        url: 'insectdata/identification/edit',
        method: 'post',
        data: data
    })
}
//删除data指定昆虫ID
export function deleteInsect(params) {
    return request({
        url: 'insectdata/identification/delete/{dataId}&{insectId}',
        method: 'post',
        params: params,
    })
}


//修改数据内容
export function updateData(data) {
    return request({
        url: 'insectdata/data/edit',
        method: 'post',
        data: data
    })
}
// 查询全部昆虫列表
export function listInsect(query) {
    return request({
        url: '/insectdata/insect/list',
        method: 'get',
        params: query
    })
}

//获取昆虫详细信息
export function DetailInsect(insectId) {
    return request({
        url: 'insectdata/insect/' + insectId,
        method: 'get',

    })
}

export function DetailInsect_new(insectId){
    return request({
        url: 'pythonUse/pythonModel/'+insectId,
        method:'post',
        
    })
}

//根据地址查询图片
export function searchByArea(params) {
    return request({
        url: "/insectdata/data/listByAddress",
        mathod: 'get',
        params: params
    })
}
//根据时间和地址查询图片
export function searchByTimeAndAddress(params) {
    return request({
        url: "/insectdata/data/listByAddressAndTime",
        mathod: 'get',
        params: params
    })
}
//根据时间查询图片
export function searchByTime(params) {
    return request({
        url: "/insectdata/data/list",
        mathod: 'get',
        params: params
    })
}