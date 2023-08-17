<template>
  <div class="app-container">
    <el-row :gutter="30">
      <!--昆虫种类选择-->
      <el-col :span="5" :xs="7">
        <!-- 昆虫图片上传（好像没用这个页面） -->
        <div class="head-container">
          <treeselect  :flat="true" :multiple="false" :always-open="true"  
          :default-expand-level="2" :options="insectOptions" :alwaysOpen="selectOpen" :disable-branch-nodes="true" :normalizer="normalizer"
          @select="handleNodeClick" 
          @input="inputChange"
          v-model="selectedInsectId"
          placeholder="选择种类" />
        </div>
      </el-col>
      <!--昆虫图像数据-->
      <el-col :span="19" :xs="17">
        <el-row>
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
            >新增</el-button>
        </el-row>
        <el-row>
          <!-- <el-col :span="6" @click="imageIndex = index" v-for="(item, index) in imgList" :key="index">
              <div class="wrapper">
                <img v-lazy="item.url" alt="" @click="showImg(index)">
              </div>
              <el-button icon="el-icon-delete" circle type="primary" @click="handleDelete(index)"></el-button>
          </el-col> -->
          <section class="img-list">
            <div  @click="imageIndex = index" v-for="(item, index) in imgList" :key="index" class="item">
              <div class="wrapper">
                <div class="imgBox">
                  <img v-lazy="item.url" alt="" @click="showImg(index)">
                </div>
              </div>
              <br>
              <div class="deleteBtn">
                <el-button icon="el-icon-delete" plain circle type="danger" @click="handleDelete(index)"></el-button>
              </div>
            </div>
          </section>
        </el-row>
      </el-col>
    </el-row>
    <!-- 上传昆虫图片的对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
            <!-- <el-form-item label="所属目科" prop="insectId">
              <treeselect v-model="form.insectId" :disable-branch-nodes="true" :options="insectOptions" :show-count="true" placeholder="请选择所属目科" />
            </el-form-item> -->
            <el-form-item label="所属种类" prop="insectId">
              <treeselect v-model="form.insectId" :options="insectOptions" :flat="true" :multiple="false" :default-expand-level="2"  :disable-branch-nodes="true" :normalizer="normalizer" placeholder="选择所属种类" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="昆虫图片" prop="imgs">
            <!-- <image-upload v-model="form.imgs" :limit="5" ></image-upload> -->
            <my-file-upload v-model="form.imgs" :limit="10" v-if="open"></my-file-upload>
          </el-form-item>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { listInsect, listSpecies, listInsectImg, getInsectImg, addInsectImg, addBatchInsectImg, updateInsectImg, delInsectImg, exportInsectImg } from "@/api/insectApi/insectImg.js"
import ImageUpload from "@/components/ImageUpload/index.vue"
import MyFileUpload from "@/components/MyFileUpload/index.vue"
import Treeselect from "@riophae/vue-treeselect";
import { getToken } from "@/utils/auth";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import "@/assets/myStyles/vue-treeselect.css";
import request from "@/utils/request"
export default {
  components: { Treeselect, ImageUpload, MyFileUpload},
  data() {
    return {
      // 下拉菜单常开
      selectOpen: true,
      selectedInsectId: undefined,
      //图片数组下标
      imageIndex: null,
      //当前设备屏幕宽度
      screenWidth: 0,
      insectList:[],
      insectOptions:[],
/*       defaultProps: {
        children: "children",
        label: "label",
      }, */
/*       imgList: [
        { id: 1, url: require('@/assets/myImg/fantasy/1.png') },
      ],
      images: [
        require('@/assets/myImg/fantasy/1.png'),
      ], */
      resList:[],
      imgList:[],
      images:[],
      // insectId: "",
      title: "",
      // 查询参数
      queryParams: {
        // pageNum: 1,
        // pageSize: 10,
        insectId: undefined,
      },
      form: {},
      open: false,
      rules: {
        insectId: [ {required: true, message: '请选择所属目科', trigger: 'change'}],
        imgs: [ {required: true, message: '请至少上传一张昆虫图片', trigger: 'change' }]
      },
    };
  },
  watch: {
    // 根据名称筛选昆虫种类树
    insectId(val) {
      this.$refs.tree.filter(val);
    }
  },
  created(){
    this.getInsect()
    this.getList()
    // this.selectedInsectId = "ALL"
    this.screenWidth = window.screen.width;
  },
  methods: {
    getInsect(){
      listInsect().then(response=>{
        //console.log(response);
        // this.insectOptions = response.data
        this.allInsect = response.data;
        this.allInsect.forEach( item => {
          if(item.type != '2'){
            item.isDisabled = true
          }
          else{
            item.isDisabled = false
          }
        })
        this.insectOptions = this.handleTree(response.data, "insectId");
        //console.log(this.insectOptions);
      })
    },
    /** 转换数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.insectId,
        label: node.insectName,
        children: node.children
      };
    },

    // 查询昆虫图像列表
    getList() {
      this.loading = true;
      this.imgList = [];
      this.images = [];
      if(this.queryParams.insectId===undefined){
        this.selectedInsectId = undefined;
      }
      listInsectImg(this.queryParams).then(response => {
          this.resList = response.rows;
          //console.log(this.resList);
          if(this.resList.length === 0){
            this.$message({
              type: 'warning',
              message: '当前种类昆虫暂无图片！'
            })
          }
          this.resList.forEach(ele=>{
            let newEle = {}
            newEle.id = ele.insectId
 /*            //console.log(ele.img.search(/http:\/\/81\.71\.138\.29:59999/));
            // if(ele.img.search(/http:\/\/81\.71\.138\.29:59999/) === 0){
            if(ele.img.search(/http:\/\/localhost:59999/) === 0){
              newEle.url = ele.img
            }
            else{
              // newEle.url = "http://81.71.138.29:59999"+ele.img
              newEle.url = "http://localhost:59999/"+ele.img
            }
            // newEle.url = "http://81.71.138.29:59999"+ele.img */
            this.imgList.push(newEle)
            this.images.push(newEle.url)
          })
          //console.log(this.imgList);
          //console.log(this.images);
          this.total = response.total;
          this.loading = false;
        }
      );
    },

    inputChange(data){
      //console.log(data);
      if(data === undefined){
        this.queryParams = {
          insectId: undefined
        }
      }    
      else{
        this.queryParams.insectId = data
        //console.log(this.queryParams);
      }
      this.getList();
    },
    // 节点单击事件
    handleNodeClick(data){
      // console.log(data);
      // this.queryParams.insectId = data.insectId
      // console.log(this.queryParams);
      // this.getList()
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
      this.form = {
        insectId: undefined,
        imgs: undefined,
      };
      this.resetForm("form");
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
      //console.log(this.form);
      //console.log(this.form.imgs);
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.insectId != undefined) {
            //console.log(this.form);

/*             addBatchInsectImg(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }); */

      
      const formData = new FormData()
      // 因为要传一个文件数组过去，所以要循环append
      //console.log(this.fileList);
      // formData.append('insectId', this.form.insectId) // 自定义参数
      //console.log(typeof(this.form.imgs));
      this.form.imgs.forEach(item => {
        //console.log(item);
        formData.append('file', item)
      })
      formData.append('insectId',this.form.insectId)
                     //自定义的接口也可以用ajax或者自己封装的接口
      //console.log(formData);
      for (var value of formData.values()) {
      //console.log(value);
      }
      for (var [a, b] of formData.entries()) {
        //console.log(typeof(a));
      //console.log(a, b);
      }
      //console.log(formData.get('file'));
      //console.log(typeof(formData.get('file')));
      request({
          method: 'POST',
          url: '/insectdata/insectImg/upLoadAndInsertImg',   //填写自己的接口
          data: formData        //填写包装好的formData对象
      }).then(res => {
        //console.log(res);
        let flag = true
        res.forEach(item => {
          if(item.code != 200){
            flag = false
          }
        })
        if(flag === false){
          this.$message.error('上传失败！')
        }
        this.$message.success('上传成功！')
        this.getList();
        this.open = false
          })
        }
      }
      });
    },
    cancel(){
      this.open = false
    },
    showImg(index) {
      //console.log(index);
      let options = {
        initialViewIndex: index //当前显示的图片下标
      };
      //console.log(options);
      // v-viewer控件Api
      this.$viewerApi({
        images: this.images,
        index: index, //当前显示的图片数组下标
        options: options,
      });
    },
    handleDelete(index){
      /* delInsect(row.insectId).then(response=>{
        console.log(response);
        if(response.code===200&&response.msg==="操作成功"){
          this.msgSuccess("删除成功");
          this.getList();
        }
      }) */
      let IMG = this.imgList[index];
      //console.log(IMG);
      //console.log(IMG.url);
      // console.log(IMG.url.match(/http:\/\/81\.71\.138\.29:59999/));
      // if(IMG.url.match(/http:\/\/81\.71\.138\.29:59999/)).
      // let imgUrl = IMG.url.replace(/http:\/\/81\.71\.138\.29:59999/, "")
      // console.log(imgUrl);
      let deleteForm = {insectId: IMG.id, img: IMG.url}
      let deleteArr = [deleteForm]
      this.$confirm('是否确认删除该图片?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delInsectImg(deleteArr);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    }
  },
};
</script>
<style scoped>
.vue-treeselect--open-below .vue-treeselect__menu{
  max-height: 80% ;
}
/* .head-container{
  min-width: 150px;
} */
.img-list{
  /* background-color: pink; */
  padding-left: 1%;
  /* padding-right: 1%; */
  display: flex;
  justify-content: left;
  flex-wrap: wrap;
  /* min-width: 800px; */
  width: 100%;
  position: relative;
  right: 0;
}
.img-list .item{
  width: 20.8%;
  /* background-color: skyblue; */
  min-width: 168px;
  min-height: 168px;
  border: none;
  outline: none;
  cursor: pointer;
  margin: 25px 1.3%;
}

.img-list .item .wrapper {
  position: relative;
  width: 100%;
  padding-bottom: 75%;
  overflow: hidden;
  -webkit-border-radius: 2px;
  -moz-border-radius: 2px;
  border-radius: 2px;
  /* -moz-box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.33);
  -webkit-box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.33);
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.33); */
}

.img-list .item .wrapper .imgBox{
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
}


img{
  width: 100%;
}
/* .img-wrapper{
  width: 100%;
  height: 80%;
} */

/* PC */
/* @media screen and (min-width: 1200px) and (max-width:1408px) {
  .img-list {


    grid-template-columns: repeat(4, 25%);

  }

} */
</style>