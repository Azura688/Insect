<template>
  <el-upload
                ref="upload"
                accept='image/jpeg,image/gif,image/png'
                :auto-upload="true"
                :headers="upload.headers"  //头部，含token
                :action="upload.url"
                :limit="upload.limit"  //上传图片张数限制
                :multiple="upload.multiple"  //是否允许上传多张
                list-type="picture-card"  //卡片式布局，还有其他类似列表式布局，参考官网
                :on-preview="handlePreview"  //预览事件
                :on-remove="handleOutlineRemove" //删除图片
                :on-success="handleOutlineSuccess" //上传成功
                :file-list="upload.outlineFileList"> //存储图片的变量，回显时使用
                <i class="el-icon-plus"></i>
              </el-upload>
 <!-- 图片预览对话框 -->
    <el-dialog title="图片预览" :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
</template>
<script>
import { getToken } from "@/utils/auth";
 
export default {
  name: "ProductInfo",
  data() {
    return {
      //图片预览对话框是否显示
      dialogVisible: false,
      //图片预览地址
      dialogImageUrl: "",
      // 图片上传
      upload: {
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/common/upload",
        //上传图片个数限制
        limit: 5,
        multiple: true,
        outlineFileList: [],//传输插件存储的附件路径内容，提交和回显时赋值使用
      },
    }
  },
  methods:{
    //上传图片-图片预览
    handleOutlinePreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleOutlineSuccess(response, file, fileList) {
      this.upload.outlineFileList=fileList;//将最新的图片数组（系统插件控制的）赋值给提交时绑定的变量，方便数据跟进和提交
    },
    //概要图片移除事件
    handleOutlineRemove(file, fileList) {
      this.upload.outlineFileList=fileList; //将最新的图片数组（系统插件控制的）赋值给提交时绑定的变量，方便数据跟进和提交
      this.$message({
        type: 'info',
        message: '已删除原有图片',
        duration: 1000
      });
    }
  }
}
</script>