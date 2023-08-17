<template>
<!-- 上传图片 -->
  <div>
    <el-upload
      ref="upload"
      class="upload-demo"
      action="" 	
      :multiple="true"          
      :auto-upload="false"       
      :before-upload="beforeAvatarUploadImage" 
      :on-exceed="handleExceed"
      :on-remove="handleRemove"
      :file-list="emitList"      
      :limit="limit"                  
      :on-change="handleChange" 
      list-type="text"          
    >
      <el-button slot="trigger" size="small" type="primary">选取图片</el-button>
<!--       <el-button
        style="margin-left: 10px;"
        size="small"
        type="primary"
        @click="uploadForm" 
      >上传到服务器</el-button> -->
          <!-- 上传提示 -->
    <div class="el-upload__tip" slot="tip" v-if="showTip">
      请上传
      <template v-if="fileSize"> 大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB，</b> </template>
      <template v-if="fileType"> 格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b> </template>的文件，
      <template v-if="limit"> 文件数目不超过 <b style="color: #f56c6c">{{ limit }}个</b> </template>
    </div>
    </el-upload>
    <div>
      <div style="text-align: center; font-weight: bold">
        <template v-if="emitList.length>10">······</template>
      </div>
      
      <template>共上传<b style="color: #f56c6c">{{ emitList.length }}</b>张图片</template>
    </div>
  </div>
</template>
<script>
import request from "@/utils/request"
export default({
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
      default: 100,
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
  computed: {
    // 是否显示提示
    showTip() {
      return this.isShowTip && (this.fileType || this.fileSize);
    },
  },
  data(){
    return{
      fileList: [],
      resList: [],
      emitList: [],
      dialogVisible_up_image: true
    }
  },
  watch: {
    value: {
      handler(val) {
        if (val) {
          let temp = 1;
          // 首先将值转为数组
          const list = Array.isArray(val) ? val : this.value.split(',');
          //console.log('watchLIst ',list);
          // 然后将数组转为对象数组
          this.fileList = list.map(item => {
            if (typeof item === "string") {
              item = { name: item, url: item };
            }
            item.uid = item.uid || new Date().getTime() + temp++;
            return item;
          });
        } else {
          this.fileList = [];
          return [];
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
        // 判断图片格式
    beforeAvatarUploadImage(file) {
      // 校检文件类型
      if (this.fileType) {
        let fileExtension = "";
        if (file.name.lastIndexOf(".") > -1) {
          fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
        }
        const isTypeOk = this.fileType.some((type) => {
          if (file.raw.type.indexOf(type) > -1) return true;
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
        image.src = URL.createObjectURL(file.raw);
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
      //console.log(initSize);
      //console.log(sizeByMB);
      let canvas = document.createElement('canvas')
      let ctx = canvas.getContext('2d')
      // 瓦片canvas
      let tCanvas = document.createElement('canvas')
      let tctx = tCanvas.getContext('2d')
      // let initSize = img.src.length
      // //console.log(initSize);
      //console.log(img.src.length);
      let width = img.width
      let height = img.height
      // 如果图片大于二百万像素，计算压缩比并将大小压至200万以下
      let ratio
      if ((ratio = width * height / 2000000) > 1) {
        //console.log('大于200万像素')
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
        //console.log('超过100W像素')
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

      //console.log('压缩前：' + initSize)
      //console.log('压缩后：' + compressData.length)
      //console.log('压缩率：' + ~~(100 * (initSize - compressData.length) / initSize) + '%')
      tCanvas.width = tCanvas.height = canvas.width = canvas.height = 0
      return blobImg
    },

    /* base64转Blob对象 */
    dataURItoBlob(data) {
      //console.log(data);
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
    // 选择文件时，往fileList里添加
    async handleChange(file) {
      //console.log("change");
      //console.log(file);
      let result = await this.beforeAvatarUploadImage(file)
      let name = file.name
      //console.log(name);
      //console.log("result:",result);
      result.name = name
      if(result!=false){
        this.fileList.push(file)
        //console.log(this.fileList);
        // this.emitList.push(file.raw)
        this.emitList.push(result)
        this.$emit('input',this.emitList)
        this.fileList = []
      }
      // console.log(this.fileList);
      // this.fileList.forEach(file => {
      //   this.beforeAvatarUploadImage(file)
      // })
      // console.log(this.fileList);
    },
    // 批量上传
    uploadForm() {
      // this.$refs.upload.submit()
      if (this.fileList.length === 0) {
        this.$message.warning('请选取文件')
        return
      }

      let listForFiles = []
      this.fileList.forEach(file => {
        //console.log(file);
        //console.log(typeof(file));
        listForFiles.push(file.raw)
      })
      // //console.log(formData.files);
      // this.$emit("input", this.fileList)
      //console.log(typeof(listForFiles));
      //console.log(listForFiles);
      this.$emit("input", listForFiles)
      this.fileList = []
      // formData.delete('files')
 /*      request({
          method: 'POST',
          url: '/common/batchUploadFile',   //填写自己的接口
          data: formData        //填写包装好的formData对象
      }).then(res => {
        console.log(res);
        let flag = true
        res.forEach(item => {
          if(item.code != 200){
            flag = false
          }
        })
        if(flag === false){
          this.$message.error('上传失败！')
        }
        res.forEach(item => {
          this.resList.push({ name: item.fileName, url: 'http://81.71.138.29:59999'+item.fileName })
          // this.resList.push({ name: item.fileName, url: item.url });
        })
        console.log(this.resList);
        this.$emit("input", this.listToString(this.resList));
        // this.$emit("input", this.fileList);-------------------------------------
        this.$message.success('上传成功！')
 */

          // if (res.msg == 200) {
          //     this.$message.success('上传成功');
          //     setTimeout(() => {
          //       this.dialogVisible_up_image = false
          //     }, 500)
          // } else {
          //     this.$message.error('上传失败');
          //     setTimeout(() => {
          //       this.dialogVisible_up_image = false
          //     }, 500)
          // }
          //清空fileList
/*           this.fileList = []
      }) */
      // axios.post('http://localhost:59999/common/upload', formData).then(res => {
      //   this.$message.success('图片上传成功！')
      //   setTimeout(() => {
      //     this.dialogVisible_up_image = false
      //   }, 500)
      // }).catch(res => {
      //   this.$message.success('图片上传失败！')
      //   setTimeout(() => {
      //     this.dialogVisible_up_image = false
      //   }, 500)
      // })
      // // 清空图片列表（一定要清空，否则上传成功后还是会调用handleChange（）函数，上传成功后列表中还存在图片）
      // this.fileList = []
    },
  // 文件个数超出 
    handleExceed(files, fileList) {
      //console.log(files);
      this.$message.warning(`当前限制选择  ${this.limit}  个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },

    // 删除文件
    handleRemove(file) {
      //console.log("delete:",file);
      // console.log("fileList",this.fileList);
      // console.log(this.resList);
      let index = file.uid
      this.emitList.forEach( ele => {
        if(ele.uid === index){
          this.emitList.splice(this.emitList.indexOf(ele),1)
        }
      })
/*       this.List.forEach( ele => {
        if(ele.uid === index){
          this.emitList.splice(this.resList.indexOf(ele),1)
        }
      }) */
      //console.log(this.fileList);
      //console.log(this.emitList);

      this.$emit("input", this.emitList);
    },

    // 对象转成指定字符串分隔
    listToString(list, separator) {
      //console.log(list);
      let strs = "";
      separator = separator || ",";
      for (let i in list) {
        strs += list[i] + separator;
      }
      //console.log(strs);
      //console.log(strs != '' ? strs.substr(0, strs.length - 1) : '');
      return strs != '' ? strs.substr(0, strs.length - 1) : '';
    },  
  }
})
</script>
