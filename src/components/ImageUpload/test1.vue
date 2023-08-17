<template>
  <div class="component-upload-image">
    <el-upload
      :action="uploadImgUrl"
      list-type="picture-card"
      :on-success="handleUploadSuccess"
      :before-upload="handleBeforeUpload"
      :limit="limit"
      :on-change="handleChange"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      name="file"
      :on-remove="handleRemove"
      :show-file-list="true"
      :headers="headers"
      :file-list="fileList"
      :on-preview="handlePictureCardPreview"
      :class="{hide: this.fileList.length >= this.limit}"
    >
          <!-- :on-preview="handlePictureCardPreview" -->
      <i class="el-icon-plus"></i>
    </el-upload>
    
    <!-- 上传提示 -->
    <div class="el-upload__tip" slot="tip" v-if="showTip">
      请上传
      <template v-if="fileSize"> 大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b> </template>
      <template v-if="fileType"> 格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b> </template>
      的文件
      <template v-if="limit"> 文件数目不超过 <b style="color: #f56c6c">{{ limit }}个</b> </template>
    </div>

    <el-dialog
      :visible.sync="dialogVisible"
      title="预览"
      width="800"
      append-to-body
    >
      <img
        :src="dialogImageUrl"
        style="display: block; max-width: 100%; margin: 0 auto"
      />
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";
// import { handleCompressImg } from "@/utils/imgCompress"

export default {
  props: {
    value: [String, Object, Array],
    // 图片数量限制
    limit: {
      type: Number,
      default: 5,
    },
    // 大小限制(MB)
    fileSize: {
      type: Number,
      default: 5,
    },
    // 文件类型, 例如['png', 'jpg', 'jpeg']
    fileType: {
      type: Array,
      default: () => ["png", "jpg", "jpeg"],
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      dialogImageUrl: "",
      dialogVisible: false,
      hideUpload: false,
      baseUrl: process.env.VUE_APP_BASE_API,
      uploadImgUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传的图片服务器地址
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      fileList: []
    };
  },
  watch: {
    value: {
      handler(val) {
        if (val) {
          console.log("val:",val)
          console.log(typeof(val));
          // 首先将值转为数组
          const list = Array.isArray(val) ? val : this.value.split(',');
          console.log("list," ,list);
          // 然后将数组转为对象数组
          this.fileList = list.map(item => {
            console.log("item:",item);
            console.log(typeof item);
            console.log(item.indexOf(this.baseUrl));
            if (typeof item === "string") {
              if (item.indexOf(this.baseUrl) === -1) {
                  item = { name: this.baseUrl + item, url: this.baseUrl + item };
              } else {
                  item = { name: item, url: item };
              }
            }
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
  computed: {
    // 是否显示提示
    showTip() {
      return this.isShowTip && (this.fileType || this.fileSize);
    },
  },
  methods: {
    // 删除图片
    handleRemove(file, fileList) {
      const findex = this.fileList.map(f => f.name).indexOf(file.name);
      this.fileList.splice(findex, 1);
      this.$emit("input", this.listToString(this.fileList));
    },
    handleChange(file, fileList) {
      console.log("change",file);
      console.log("change",fileList);
      fileList.push(file);
      console.log("change",this.fileList);
    },
    // handlePreview(file) {
    //    console.log(file);
    //  },
    //  handleExceed(files, fileList) {
    //     this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    //   },

    // 上传成功回调
    handleUploadSuccess(res) {
      console.log("success res",res);
      this.fileList.push({ name: res.fileName, url: res.fileName });
      console.log("success",this.fileList);
      this.$emit("input", this.listToString(this.fileList));
      this.loading.close();
    },
    // 上传前loading加载
    handleBeforeUpload(file) {
      console.log("beforeupload",file);
      console.log("图片日期：",file.lastModifiedDate);
      console.log(this.fileList);
      let isImg = false;
      if (this.fileType.length) {
        let fileExtension = "";
        if (file.name.lastIndexOf(".") > -1) {
          fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
        }
        isImg = this.fileType.some(type => {
          if (file.type.indexOf(type) > -1) return true;
          if (fileExtension && fileExtension.indexOf(type) > -1) return true;
          return false;
        });
      } else {
        isImg = file.type.indexOf("image") > -1;
      }

      if (!isImg) {
        this.$message.error(
          `文件格式不正确, 请上传${this.fileType.join("/")}图片格式文件!`
        );
        return false;
      }
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize;
        if (!isLt) {
          this.$message.error(`上传头像图片大小不能超过 ${this.fileSize} MB!`);
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
        this.loading = this.$loading({
          lock: true,
          text: "上传中",
          background: "rgba(0, 0, 0, 0.7)",
        });
        image.onerror = () => {
              reject()
        }
      })  
     },


/*      beforeUpload(file) {
       console.log(file);
    let _this = this
    return new Promise((resolve, reject) => {
        let isLt2M = file.size / 1024 / 1024 < 10 // 判定图片大小是否小于10MB
        if(!isLt2M) {
            reject()
        }
        let image = new Image(), resultBlob = '';
        image.src = URL.createObjectURL(file);
        image.onload = () => {
            // 调用方法获取blob格式，方法写在下边
            resultBlob = _this.compressUpload(image, file);
            resolve(resultBlob)
        }
        this.loading = this.$loading({
            lock: true,
            text: "上传中",
            background: "rgba(0, 0, 0, 0.7)",
        });
        image.onerror = () => {
            reject()
        }
    })
}, */
 
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
     /*
    getCompressFile(file){
      console.log(file);
      file = await handleCompressImg(file);
      console.log(file);
    }, */
    // 文件个数超出
    handleExceed(files,fileList) {
      this.$message.error(`当前限制选择 ${this.limit} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件!`)
      // this.$message.error(`上传文件数量不能超过 ${this.limit} 个!`);
    },
    // 上传失败
    handleUploadError() {
      this.$message({
        type: "error",
        message: "上传失败",
      });
      this.loading.close();
    },
    // 预览
    handlePictureCardPreview(file) {
      console.log(file);
      console.log(this.baseUrl);
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    // 对象转成指定字符串分隔
    listToString(list, separator) {
      let strs = "";
      separator = separator || ",";
      for (let i in list) {
        strs += list[i].url.replace(this.baseUrl, "") + separator;
      }
      return strs != '' ? strs.substr(0, strs.length - 1) : '';
    }
  }
};
</script>
<style scoped lang="scss">
// .el-upload--picture-card 控制加号部分
::v-deep.hide .el-upload--picture-card {
    display: none;
}
// 去掉动画效果
::v-deep .el-list-enter-active,
::v-deep .el-list-leave-active {
    transition: all 0s;
}

::v-deep .el-list-enter, .el-list-leave-active {
    opacity: 0;
    transform: translateY(0);
}
</style>

