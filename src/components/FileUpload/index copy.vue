<template>
  <div>
    <el-upload
      :action="uploadFileUrl"
      :before-upload="handleBeforeUpload"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      :on-success="handleUploadSuccess"
      :http-request="handleUpload"
      :auto-upload="false"
      :on-preview="handlePreview"
      :before-remove="beforeRemove"
      :on-remove="handleRemove"
      multiple
      :limit="limit"
      :file-list="fileList"
      ref="upload"
      :headers="headers"
      class="upload-file-uploader"

      list-type="picture">
      <i class="el-icon-plus"></i>
      <!-- 上传按钮 -->
      <el-button size="mini" type="primary">选取文件</el-button>
      <!-- 上传提示 -->
      <div class="el-upload__tip" slot="tip" v-if="showTip">
        请上传
        <template v-if="fileSize"> 大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b> </template>
        <template v-if="fileType"> 格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b> </template>
        的文件
      </div>
    </el-upload>
    <el-button @click="subfileForm">提交上传</el-button>
    <!-- 文件列表 -->
    <!-- <transition-group class="upload-file-list el-upload-list el-upload-list--text" name="el-fade-in-linear" tag="ul">
      <li :key="file.uid" class="el-upload-list__item ele-upload-list__item-content" v-for="(file, index) in fileList">
        <el-link :href="`${baseUrl}${file.url}`" :underline="false" target="_blank">
          <span class="el-icon-document"> {{ getFileName(file.name) }} </span>
        </el-link>
        <div class="ele-upload-list__item-content-action">
          <el-link :underline="false" @click="handleDelete(index)" type="danger">删除</el-link>
        </div>
      </li>
    </transition-group> -->
  </div>
</template>
<script>
import axios from 'axios'
import { getToken } from "@/utils/auth";
var formData = new FormData();

  export default {
  name: "FileUpload",
  props: {
    // 值
    value: [String, Object, Array],
    // 数量限制
    limit: {
      type: Number,
      default: 5,
    },
    // 大小限制(MB)
    fileSize: {
      type: Number,
      default: 5,
    },
    // 文件类型, 例如['png', 'jpg', 'jpeg']  ["doc", "xls", "ppt", "txt", "pdf"]
    fileType: {
      type: Array,
      default: () => ['png', 'jpg', 'jpeg'],
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true
    }
  },
    data() {
      return {
        // formData: {},
        baseUrl: process.env.VUE_APP_BASE_API,
        uploadFileUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传的图片服务器地址
        headers: {
          Authorization: "Bearer " + getToken(),
          // Content-Type: 'application/x-www-form-urlencoded'
        },
      fileList: [],
        // fileList: [{name: 'food.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}, {name: 'food2.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}]
      };
    },
  // watch: {
  //   value: {
  //     handler(val) {
  //       if (val) {
  //         let temp = 1;
  //         // 首先将值转为数组
  //         const list = Array.isArray(val) ? val : this.value.split(',');
  //         // 然后将数组转为对象数组
  //         this.fileList = list.map(item => {
  //           if (typeof item === "string") {
  //             item = { name: item, url: item };
  //           }
  //           item.uid = item.uid || new Date().getTime() + temp++;
  //           return item;
  //         });
  //       } else {
  //         this.fileList = [];
  //         return [];
  //       }
  //     },
  //     deep: true,
  //     immediate: true
  //   }
  // },
  computed: {
    // 是否显示提示
    showTip() {
      return this.isShowTip && (this.fileType || this.fileSize);
    },
  },
  methods: {
    handleUpload(raw){
      // console.log(formData);
      console.log("raw",raw);
      formData.append('file', raw.file)
      // console.log(formData);
      this.fileList.push(raw.file);
      console.log(this.fileList);
    },
    // uploadFile(file){
    //   console.log("uploadFile",file);
    //   this.formData.append('file', file.file);
    // },
    async subfileForm(){
      this.$refs.upload.submit();// 这里是执行文件上传的函数，其实也就是获取我们要上传的文件
      this.fileList.forEach(function (file) {
        console.log(file);
        formData.append('file', file, file.name); // 因为要上传多个文件，所以需要遍历一下才行
        //不要直接使用我们的文件数组进行上传，你会发现传给后台的是两个Object
      })
      console.log("formData",formData);
      console.log(getToken());
      let config = {
                         headers: {
                             'Content-Type': 'application/x-www-form-urlencoded',
                            //  'token': getToken()
                             authorization: `Bearer ${getToken()}`
                         } 
                  }
      console.log(formData.get('file'))       
      console.log(config);
      axios.post( process.env.VUE_APP_BASE_API + "/common/upload", formData, config).then(res=>{
        console.log(res);
        // if(res.data.status==='OK'){
        //   console.log(res)
        // }
      })
    },

    // 上传前校检格式和大小
    handleBeforeUpload(file) {
      console.log("before",file);
      // 校检文件类型
      if (this.fileType) {
        let fileExtension = "";
        if (file.name.lastIndexOf(".") > -1) {
          fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
        }
        const isTypeOk = this.fileType.some((type) => {
          if (file.type.indexOf(type) > -1) return true;
          if (fileExtension && fileExtension.indexOf(type) > -1) return true;
          return false;
        });
        if (!isTypeOk) {
          this.$message.error(`文件格式不正确, 请上传${this.fileType.join("/")}格式文件!`);
          return false;
        }
      }
      // 校检文件大小
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize;
        if (!isLt) {
          this.$message.error(`上传文件大小不能超过 ${this.fileSize} MB!`);
          return false;
        }
      }
      let _this = this
      return new Promise((resolve, reject) => {
        let image = new Image(), resultBlob = '';
        image.src = URL.createObjectURL(file);
        image.onload = () => {
            // 调用方法获取blob格式，方法写在下边
            resultBlob = _this.compressUpload(image, file);
            resolve(resultBlob)
        }
        // this.loading = this.$loading({
        //   lock: true,
        //   text: "上传中",
        //   background: "rgba(0, 0, 0, 0.7)",
        // });
        image.onerror = () => {
              reject()
        }
      })  
      // return true;
    },  
    /* 图片压缩方法-canvas压缩 */
    compressUpload(img, file) {
/*         let canvas = document.createElement('canvas')
        let ctx = canvas.getContext('2d')
        let initSize = image.src.length
        let { width } = image, { height } = image
        canvas. width = width
        canvas.height = height
        ctx.fillRect(0, 0, canvas.width, canvas.height)
        ctx.drawImage(image, 0, 0, width, height) */
      // 压缩操作
      let initSize = file['size']
      let sizeByMB = parseFloat(parseInt(file['size'])/1024/1024).toFixed(2)
      console.log(initSize);
      console.log(sizeByMB);
      let canvas = document.createElement('canvas')
      let ctx = canvas.getContext('2d')
      // 瓦片canvas
      let tCanvas = document.createElement('canvas')
      let tctx = tCanvas.getContext('2d')
      // let initSize = img.src.length
      // console.log(initSize);
      console.log(img.src.length);
      let width = img.width
      let height = img.height
      // 如果图片大于二百万像素，计算压缩比并将大小压至200万以下
      let ratio
      if ((ratio = width * height / 2000000) > 1) {
        console.log('大于200万像素')
        ratio = Math.sqrt(ratio)
        width /= ratio
        height /= ratio
      } else {
        ratio = 1
      }
      canvas.width = width
      canvas.height = height
      //        铺底色
      ctx.fillStyle = '#fff'
      ctx.fillRect(0, 0, canvas.width, canvas.height)
      // 如果图片像素大于100万则使用瓦片绘制
      let count
      if ((count = width * height / 1000000) > 1) {
        console.log('超过100W像素')
        count = ~~(Math.sqrt(count) + 1) // 计算要分成多少块瓦片
        //            计算每块瓦片的宽和高
        let nw = ~~(width / count)
        let nh = ~~(height / count)
        tCanvas.width = nw
        tCanvas.height = nh
        for (let i = 0; i < count; i++) {
          for (let j = 0; j < count; j++) {
            tctx.drawImage(img, i * nw * ratio, j * nh * ratio, nw * ratio, nh * ratio, 0, 0, nw, nh)
            ctx.drawImage(tCanvas, i * nw, j * nh, nw, nh)
          }
        }
      } else {
        ctx.drawImage(img, 0, 0, width, height)
      }
      let compressData;
      if(sizeByMB<0.05){
          //如果图片小于50k那么不执行压缩操作
          compressData = canvas.toDataURL(file.type || 'image/jpeg', 1)
      }else if(sizeByMB>0.05&&sizeByMB<0.1){
          //如果图片大于50k并且小于100k那么压缩0.5
          compressData = canvas.toDataURL(file.type || 'image/jpeg', 0.5)
      }else{
          //如果图片超过100k那么压缩0.2
          compressData = canvas.toDataURL(file.type || 'image/jpeg', 0.2)
      }
      // 进行最小压缩0.1
      // let compressData = canvas.toDataURL(file.type || 'image/jpeg', 0.1)

      // 压缩后调用方法进行base64转Blob，方法写在下边
      let blobImg = this.dataURItoBlob(compressData)

      console.log('压缩前：' + initSize)
      console.log('压缩后：' + compressData.length)
      console.log('压缩率：' + ~~(100 * (initSize - compressData.length) / initSize) + '%')
      tCanvas.width = tCanvas.height = canvas.width = canvas.height = 0
      return blobImg
    },

    /* base64转Blob对象 */
    dataURItoBlob(data) {
        let byteString;
        if(data.split(',')[0].indexOf('base64') >= 0) {
            byteString = atob(data.split(',')[1])
        }else {
            byteString = unescape(data.split(',')[1])
        }
        let mimeString = data
            .split(',')[0]
            .split(':')[1]
            .split(';')[0];
        let ia = new Uint8Array(byteString.length)
        for( let i = 0; i < byteString.length; i += 1) {
            ia[i] = byteString.charCodeAt(i)
        }
        return new Blob([ia], {type: mimeString})
    },

  // 文件个数超出 
    handleExceed(files, fileList) {
      console.log(files);
      this.$message.warning(`当前限制选择  ${this.limit}  个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
  // 上传失败
    handleUploadError(err) {
      this.$message.error("上传失败, 请重试");
    },
    // 上传成功回调
    handleUploadSuccess(res, file, fileList) {
      console.log(res);
      console.log(file);
      console.log(fileList);
      this.$message.success("上传成功");
      this.fileList.push({ name: res.fileName, url: res.fileName });
      this.$emit("input", this.listToString(this.fileList));
    },
    // 删除文件
    handleDelete(index) {
      this.fileList.splice(index, 1);
      this.$emit("input", this.listToString(this.fileList));
    },
    // 获取文件名称
    getFileName(name) {
      if (name.lastIndexOf("/") > -1) {
        return name.slice(name.lastIndexOf("/") + 1).toLowerCase();
      } else {
        return "";
      }
    },
    // 对象转成指定字符串分隔
    listToString(list, separator) {
      let strs = "";
      separator = separator || ",";
      for (let i in list) {
        strs += list[i].url + separator;
      }
      return strs != '' ? strs.substr(0, strs.length - 1) : '';
    },  
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      handleExceed(files, fileList) {
        console.log(files);
        this.$message.warning(`当前限制选择  ${this.limit}  个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      }
    }
  }
</script>