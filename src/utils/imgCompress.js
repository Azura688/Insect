// 压缩图片
export function handleCompressImg (file) {
  console.log(file);
  let Orientation
  if (/^image/.test(file.type)) {
    return new Promise((resolve, reject) => {
      // 创建一个reader
      let reader = new FileReader()
      // 将图片转成 base64 格式
      reader.readAsDataURL(file)
      // 读取成功后的回调
      reader.onloadend = function (e) {
        let img = new Image()
        let result = e.target.result
        img.src = result
        // 判断图片是否大于100K,是就压缩图片，反之直接上传
        if (result.length <= (100 * 1024)) {
          console.log('图片小于100k') // 不对图片进行压缩操作
          return resolve(file)
        } else {
          img.onload = function () {
            return resolve(dataURLtoFile(compress(img, Orientation), file.name))
            // return resolve(dataURItoBlob(compress(img, Orientation)))
          }
        }
      }
    })
  }
}

function compress (img, Orientation) {
  // 压缩操作
  let canvas = document.createElement('canvas')
  let ctx = canvas.getContext('2d')
  // 瓦片canvas
  let tCanvas = document.createElement('canvas')
  let tctx = tCanvas.getContext('2d')
  let initSize = img.src.length
  let width = img.width
  let height = img.height
  // 如果图片大于四百万像素，计算压缩比并将大小压至400万以下
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
  // 修复ios上传图片的时候 被旋转的问题
  if (Orientation !== '' && Orientation !== 1) {
    switch (Orientation) {
      case 6:// 需要顺时针（向左）90度旋转
        this.rotateImg(img, 'left', canvas)
        break
      case 8:// 需要逆时针（向右）90度旋转
        this.rotateImg(img, 'right', canvas)
        break
      case 3:// 需要180度旋转
        this.rotateImg(img, 'right', canvas)// 转两次
        this.rotateImg(img, 'right', canvas)
        break
    }
  }
  // 进行最小压缩
  let ndata = canvas.toDataURL('image/jpeg', 0.2)
  console.log('压缩前：' + initSize)
  console.log('压缩后：' + ndata.length)
  console.log('压缩率：' + ~~(100 * (initSize - ndata.length) / initSize) + '%')
  tCanvas.width = tCanvas.height = canvas.width = canvas.height = 0
  console.log(ndata);
  return ndata
}

// base64转化成文件对象格式
function dataURLtoFile (dataurl, filename) {
  var arr = dataurl.split(',')
  var mime = arr[0].match(/:(.*?);/)[1]
  var bstr = atob(arr[1])
  var n = bstr.length
  var u8arr = new Uint8Array(n)
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n)
  }
  // 转换成file对象
  return new File([u8arr], filename, {type: mime})
}


    /* 图片格式转换——base64转Blob对象 */
function  dataURItoBlob(data) {
      let byteString;
      if (data.split(",")[0].indexOf("base64") >= 0) {
        byteString = data.split(",")[1];
      } else {
        byteString = unescape(data.split(",")[1]);
      }
      let mimeString = data
        .split(",")[0]
        .split(":")[1]
        .split(";")[0];
      let ia = new Uint8Array(byteString.length);
      for (let i = 0; i < byteString.length; i += 1) {
        ia[i] = byteString.charCodeAt(i);
      }
      console.log(new Blob([ia], { type: mimeString }));
      return new Blob([ia], { type: mimeString });
}


/*
//base64转码（压缩完成后的图片为base64编码，这个方法可以将base64编码转回file文件）
function dataURLtoFile(dataurl) {
  var arr = dataurl.split(','),
      mime = arr[0].match(/:(.*?);/)[1],
      bstr = atob(arr[1]),
      n = bstr.length,
      u8arr = new Uint8Array(n);
  while (n--) {
      u8arr[n] = bstr.charCodeAt(n);
  }
  return new File([u8arr], { type: mime });

}
//压缩图片
function compressImg(file){
  var src;
  var files;
  var fileSize = parseFloat(parseInt(file['size'])/1024/1024).toFixed(2);
  var read = new FileReader();
  read.readAsDataURL(file);
  return new Promise(function(resolve, reject){
    read.onload = function (e) {
        var img = new Image();
        img.src = e.target.result;
        img.onload = function(){
            //默认按比例压缩
            var w = this.width,
                h = this.height;
            //生成canvas
            var canvas = document.createElement('canvas');
            var ctx = canvas.getContext('2d');
            var base64;
            // 创建属性节点
            canvas.setAttribute("width", w);
            canvas.setAttribute("height", h);
            ctx.drawImage(this, 0, 0, w, h);
            if(fileSize<0.2){
                //如果图片小于一兆 那么不执行压缩操作
                base64 = canvas.toDataURL(file['type'], 1);
            }else if(fileSize>0.2&&fileSize<0.5){
                //如果图片大于1M并且小于2M 那么压缩0.5
                base64 = canvas.toDataURL(file['type'], 0.5);
            }else{
                //如果图片超过2m 那么压缩0.2
                base64 = canvas.toDataURL(file['type'], 0.2);
            }
            // 回调函数返回file的值（将base64编码转成file）
           files = dataURLtoFile(base64); //如果后台接收类型为base64的话这一步可以省略
           
           resolve(files)
        };
    };
  })
};

//结尾处将该方法暴露出来供外部调用
export default {	
compressImg,
} */
