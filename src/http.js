import axios from 'axios'
import Vue from 'vue'
// import {Message} from "element-ui";
// import 'element-ui/lib/theme-chalk/index.css'
// const baseURL = 'http://81.71.138.29:59999'
const baseURL = 'http://localhost:59999'
const http = axios.create({
    baseURL: baseURL
        // timeout: 5000
})

http.interceptors.request.use(config => {
    console.log(config)
    return config
})

http.interceptors.response.use(res => {
    console.log(res)
    return res
}, err => {
    console.log(err.response);
    if (err.response.data.message) {
        Vue.prototype.$message({
            type: 'error',
            message: err.response.data.message
        })
    }
    return Promise.reject(err)
})

// Vue.prototype.$http.interceptors.response.use( success =>{
//     // 业务逻辑错误
//     if(success.status&&success.status === 200){
//       if(success.data.code === 500||success.data.code===401||success.data.code===403){
//         Message.error({message:success.data.message})
//         return
//       }
//       if(success.data.message){
//         Message.success({message:success.data.message})
//       }
//     }
//     return success.data
//   },error => {
//     Message.error(error.message)
//   })
export { http, baseURL }