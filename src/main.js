import Vue from 'vue'

import Cookies from 'js-cookie'

import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';
import './assets/styles/myCss.css'
import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import directive from './directive' //directive

import './assets/icons' // icon
import './permission' // permission control
import { getDicts } from "@/api/system/dict/data";
import { getConfigKey } from "@/api/system/config";
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, download, handleTree } from "@/utils/ruoyi";
import Pagination from "@/components/Pagination";
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar"
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 字典标签组件
import DictTag from '@/components/DictTag'
// 头部标签组件
import VueMeta from 'vue-meta'

/* 引入VueLazyload图片懒加载库,npm安装：npm i vue-lazyload -S */
import VueLazyload from 'vue-lazyload'


//VueLazyload配置项
Vue.use(VueLazyload, {
  preLoad: 0.8,
  error: require('./assets/lazyLoadImg/img_err.png'),
  loading: require('./assets/lazyLoadImg/img_loading.gif'),
  attempt: 1,
  // listenEvents: ['scroll'],
  listenEvents:['scroll','wheel','mousewheel','resize','animationend','transitionend','touchmove']
})





// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}

// 全局组件挂载
Vue.component('DictTag', DictTag)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('FileUpload', FileUpload)
Vue.component('ImageUpload', ImageUpload)

Vue.use(directive)
Vue.use(VueMeta)

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */



/* 引入v-viewer图片预览库，npm 安装：npm install v-viewer */
import Viewer from 'v-viewer'
import 'viewerjs/dist/viewer.css'

//viewer设置
Vue.use(Viewer,{
  defaultOptions: {
    zIndex: 9999,
    // inline: true, //启用inline模式
    button: true, //显示右上角关闭按钮
    // navbar: true,//显示缩略图导航栏
    title: true, //显示当前图片标题
    toolbar: true, //显示工具栏
    tooltip: true, //显示缩放百分比
    movable: true, //图片是否可移动
    zoomable: true, //图片是否可缩放
    rotatable: true, //图标是否可旋转
    scalable: true, //图片是否可翻转
    transition: true, //使用css3过渡
    fullscreen: true, //播放是否全屏
    keyboard: true,  //是否支持键盘
    // url: ''//设置大图片的url
  }
});

//详细地址转经纬度
import { VueJsonp } from 'vue-jsonp'
Vue.use(VueJsonp)

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

// 控制标签宽高成比例的指令
import proportion from 'vue-proportion-directive';
Vue.use(proportion);


// import App from './App'
// import store from './store'
// import router from './router'
// import directive from './directive' //directive

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
