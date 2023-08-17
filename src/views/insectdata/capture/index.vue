<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--上传的昆虫图像-->
      <el-col :span="24" :xs="18">
        <el-row>
        </el-row>
        <el-row>
          <section class="img-list">
            <article  @click="imageIndex = index" v-for="(item, index) in imgList" :key="index" class="item">
              <div class="wrapper">
                <img v-lazy="item.originalPicture" alt="" @click="showImg(index)">
              </div>
              <p>{{item.dataId + "," + item.equipmentId + ","  + item.photoArea + ","  + item.photoTime}}</p>
              <el-button icon="el-icon-delete" circle type="primary" @click="handleDelete(item.dataId)"></el-button>
              <el-button type="primary" @click="getDetail(item.dataId)">查看</el-button>
            </article>
          </section>
        </el-row>
      </el-col>
    </el-row>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>
<script>
import {importCapture} from "@/api/insectApi/equipment"
import { listInsect, listSpecies, listInsectImg, getInsectImg, addInsectImg, addBatchInsectImg, updateInsectImg, delInsectImg, exportInsectImg } from "@/api/insectApi/insectImg.js"
import {
  Image,
  searchByArea,
  searchByTime,
} from "@/api/identify/identify";
import { delCapture } from "@/api/insectApi/capture"
import ImageUpload from "@/components/ImageUpload"
import MyFileUpload from '@/components/MyFileUpload'
import Treeselect from "@riophae/vue-treeselect";
import { getToken } from "@/utils/auth";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import dateUtil from "@/libs/dateUtil"
export default {
  components: { Treeselect, ImageUpload, MyFileUpload},
  data() {
    return {
      // fileForm: {insectId: 5, imgs: ''},
      fileForm: {originalPictures:"",equipmentId: 1,photoTime: dateUtil.format(new Date(), 'yyyy-MM-dd hh:mm:00'), photoArea: '湖北省-武汉市-洪山区'},
      search: false,
      area: "",
      timeToSearch: "",
      // 下拉菜单常开
      selectOpen: true,
      //图片数组下标
      imageIndex: null,
      //当前设备屏幕宽度
      screenWidth: 0,
      insectList:[],
      insectOptions:[],
      resList:[],
      imgList:[],
      images:[],
      insectId: "",
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 12,
        insectId: undefined,
      },
      form: {},
      total: 0
    };
  },

  created(){
    this.getList()
    this.screenWidth = window.screen.width;
  },
  methods: {
    // 查询昆虫图像列表
    getList() {
      this.loading = true;
      this.imgList = [];
      this.images = [];
      // console.log(this.queryParams);
      Image(this.queryParams).then(response => {
          this.imgList = response.rows;
          // console.log(this.imgList);
          this.imgList.forEach(ele=>{
            // console.log(ele.originalPicture.search(/http:\/\/81\.71\.138\.29:59999/));
            /* if(ele.originalPicture.search(/https:\/\/7039vz8591\.imdo\.co/) === 0){
              ele.originalPicture = ele.originalPicture
            }
            else{
              ele.originalPicture = ele.originalPicture
            } */
            this.images.push(ele.originalPicture)
          })
          // console.log("hhh");
          // console.log(this.imgList);
          // console.log(this.images);
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    getImgList() {
      Image().then((res) => {
        // console.log(res);
        this.total = res.total;
        this.imgList = res.rows;
        for (let i = 0; i < this.total; i++) {
          this.imgList[i].originalPicture =
            baseURL + this.imgList[i].originalPicture;
          this.imgList[i].markList = JSON.parse(this.imgList[i].markerBox);
        }
        this.activePath = this.imgList[0].originalPicture;
        // console.log(this.imgList)
        this.currentId = this.imgList[0].dataId;
        this.getInsectList();
        this.initCanvas();
      });
    },
    // 节点单击事件
    handleNodeClick(data){
      // console.log(data);
      this.queryParams.insectId = data.insectId
      // console.log(this.queryParams);
      this.getList()
    },



        /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
        // 表单重置
    reset() {
      this.fileForm = {originalPictures: undefined,
      equipmentId: 1,
      photoTime: dateUtil.format(new Date(), 'yyyy-MM-dd hh:mm:00'), 
      photoArea: '湖北省-武汉市-洪山区'}
      // this.form = {
      //   insectId: undefined,
      //   imgs: undefined,
      // };
      this.resetForm("fileForm");
    },
       /** 新增按钮操作 */
    handleAdd(){
      this.reset();
      listInsect().then(response=>{
        this.allInsect = response.data;
        this.allInsect.forEach( item => {
          if(item.type != '2'){
            item.isDisabled = true
          }
          else{
            item.isDisabled = false
          }
        })
        // 转换成树形结构
        this.insectOptions = this.handleTree(response.data, "insectId");
      })
      this.open = true
      this.title = "新增昆虫图像"
    },
    /** 修改按钮操作 */
/*     handleUpdate(row) {
      this.reset();
      const insectId = row.insectId || this.ids
      getInsectImg(insectId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改昆虫图片";
      });
    }, */
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.insectId != undefined) {
            // console.log(this.form);
            addBatchInsectImg(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    cancel(){
      this.open = false
    },
    showImg(index) {
      // console.log(index);
      let options = {
        initialViewIndex: index //当前显示的图片下标
      };
      // console.log(options);
      // v-viewer控件Api
      this.$viewerApi({
        images: this.images,
        index: index, //当前显示的图片数组下标
        options: options,
      });
    },
    handleDelete(id){

/*       deleteImg(id).then(response => {
        console.log(response);
        if(response.code===200&&response.msg==="操作成功"){
          this.msgSuccess("删除成功");
          this.getList();
        }
      }) */
      // console.log(id);
      // console.log(typeof(id.toString()));
      this.$confirm('是否确认删除该图片?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCapture(id.toString());
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },


    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "设备拍摄图片导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importTemplate().then(response => {
        this.download(response.msg);
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      // console.log(this.response);
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      // this.$refs.upload.submit();
      // console.log(this.fileForm);
      importCapture(this.fileForm).then(response => {
          this.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
    },
    fullScreen() {
      this.main = 0;
    },
    back() {
      this.main = 1;
    },
    searchByArea() {
      this.area = this.$store.state.user.globaladdr;
      this.search = 1;
      // console.log(this.area);
      searchByArea({ address: this.area }).then((res) => {
        // console.log(res);
        this.total = res.total;
        this.imgList = res.rows;
        for (let i = 0; i < this.total; i++) {
          this.imgList[i].originalPicture = this.imgList[i].originalPicture;
          // console.log(this.imgList[i].originalPicture);
          // console.log(this.imgList[i].markerBox);
          this.imgList[i].markList = JSON.parse(this.imgList[i].markerBox);
        }
        this.activePath = this.imgList[0].originalPicture;
        // console.log(this.imgList)
        this.currentId = this.imgList[0].dataId;
        this.getInsectList();
        this.initCanvas();
      });
    },
    All() {
      this.search = 0;
      this.getList();
    },
    searchByTime(){
      this.area=this.$store.state.user.globaladdr;
      // console.log(this.area);
      // console.log(this.timeToSearch);
      searchByTime({ address: this.area,photoTime:this.timeToSearch }).then((res)=>{
        // console.log(res);
        this.total = res.total;
        this.imgList = res.rows;
        if(!this.imgList){
          this.imgList=[]
        }
        for (let i = 0; i < this.total; i++) {
          this.imgList[i].originalPicture = this.imgList[i].originalPicture;
          // console.log(this.imgList[i].originalPicture);
          // console.log(this.imgList[i].markerBox);
          this.imgList[i].markList = JSON.parse(this.imgList[i].markerBox);
        }
        this.activePath = this.imgList[0].originalPicture;
        // console.log(this.imgList)
        this.currentId = this.imgList[0].dataId;
        this.getInsectList();
        this.initCanvas();
      })
    },
    getDetail(id){
      // console.log(id);
      // console.log(typeof(id));
    }
  },
};
</script>
<style scoped>
.img-list{
  display: flex;
  justify-content: left;
  flex-wrap: wrap;
  min-width: 900px;
}
.img-list .item{
  width: 23%;
  min-width: 200px;
  max-height: 200px;
  border: none;
  outline: none;
  cursor: pointer;
  margin: 25px 1%;
}
.img-list .item .wrapper {
  width: 100%;
  height: 70%;
  -webkit-border-radius: 2px;
  -moz-border-radius: 2px;
  border-radius: 2px;
  -moz-box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.33);
  -webkit-box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.33);
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.33);
}

img{
  width: 100%;
  height: 100%;
}
/* .img-list{
  display: grid;
  grid-template-columns: repeat(4, 25%); 
  justify-items: center;
  min-width: 900px;
}
.img-list .item{
  width: 200px;
  height: 200px;
  border: none;
  outline: none;
  cursor: pointer;
  margin: 25px 25px;
}
.img-list .item .wrapper {
  width: 100%;
  height: 70%;
  -webkit-border-radius: 2px;
  -moz-border-radius: 2px;
  border-radius: 2px;
  -moz-box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.33);
  -webkit-box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.33);
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.33);
}

img{
  width: 100%;
  height: 100%;
}
.img-wrapper{
  width: 100%;
  height: 70%;
} */

/* PC */
/* @media screen and (min-width: 1200px) and (max-width:1408px) {
  .img-list {

    grid-template-columns: repeat(4, 25%);

  }

} */
</style>