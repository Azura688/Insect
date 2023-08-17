<template>
  <div class="container">
    <div class="notice">
      请用"火狐"或"谷歌"浏览器， 若使用QQ、360、搜狗等浏览器，请使用"极速模式"
    </div>
    <!-- 昆虫图片识别 -->
    <div class="main-top">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-full-screen"
            size="mini"
            @click="fullScreen"
            v-if="main"
            >全屏</el-button
          >
          <el-button
            type="primary"
            plain
            icon="el-icon-refresh-left"
            size="mini"
            @click="back()"
            v-if="!main"
            >返回</el-button
          >
        </el-col>
        <el-col :span="1.5">
          <el-date-picker
            v-model="timeToSearch"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:00"
            format="yyyy-MM-dd HH:mm:00"
            placeholder="根据时间搜索"
          >
          </el-date-picker>
          <el-button
            type="primary"
            icon="el-icon-search"
            plain
            @click="searchByTime()"
          ></el-button>
        </el-col>
      </el-row>
    </div>
    <div class="main" v-if="main">
      <div class="vueGallery">
        <div class="father">
          <div class="thumbnails">
            <div
              v-for="(item, index) in imgList"
              :key="index"
              class="item"
              @click="getActiveImg(index)"
            >
              <img v-lazy="item.originalPicture" alt="" class="imgItem" />
              <div>{{ item.photoTime }}</div>
            </div>
          </div>
        </div>

        <div class="activeBg" id="myImg">
          <img
            style="width: 100%; height: 100%"
            :src="activePath"
            :preview-src-list="activePathList"
          />
          <canvas ref="markCanvasbkg" tabindex="1" id="about"></canvas>
          <canvas ref="markCanvas" tabindex="1" id="about"></canvas>
        </div>
      </div>
      <div class="details" v-if="!isEdit">
        <div class="button">
          <br />
          <br />
          <el-button type="primary" plain id="button" @click="edit()"
            >识别</el-button
          >
        </div>
        <el-table
          :data="insectList"
          highlight-current-row
          max-height="350"
          border
          class="insect_list"
        >
          <el-table-column prop="name" label="名称" width="120px" />
          <el-table-column prop="num" label="数目" width="" />
        </el-table>
      </div>

      <!-- 编辑 -->
      <div class="details" v-if="isEdit">
        <div class="button">
          <el-button type="primary" plain id="button">识别</el-button>
          <br />
          <el-button type="primary" plain id="button" @click="save()"
            >保存</el-button
          >
        </div>
        <div class="list_content">
          <el-table
            :data="insectList"
            :key="timeStamp"
            max-height="250"
            highlight-current-row
          >
            <el-table-column prop="name" label="名称" width="100px" />
            <el-table-column prop="num" label="数目" width="50px" />
          </el-table>
        </div>
      </div>
    </div>
    <div class="fullscreen" v-if="!main">
      <div class="img_container">
        <div
          v-for="(item, index) in imgList"
          :key="index"
          class="item"
          @click="getActiveImg(index)"
        >
          <!-- <img :src="item.originalPicture" alt="" class="imgItem" /> -->
          <img v-lazy="item.originalPicture" alt="" class="imgItem" />
          <!-- <div>{{item.photoTime.slice(0,10)}}</div>
          <div>{{item.photoTime.slice(11)}}</div> -->
          <div>{{ item.photoTime }}</div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import { draw } from "@/utils/draw"; // 矩形绘制方法
import {
  Image,
  InsectList,
  AddInsect,
  updateInsect,
  updateData,
  deleteInsect,
  searchByArea,
  searchByTime,
  searchByTimeAndAddress,
  DetailInsect,
  DetailInsect_new,
} from "@/api/identify/identify";
import { listInsect } from "@/api/insectApi/insectImg.js";
export default {
  data() {
    return {
      total: 0,
      insectotal: 0,
      imgList: [],
      imgwidth: 0,
      imgheight: 0,
      activePath: "",
      activePathList: [],
      markList: [],
      currentId: 0,
      currentIndex: 0,
      insectId: 0,
      insectList: [],
      addInsect: [],
      //list:[],
      saveList: [],
      isEdit: 0,
      markerEdit: 0,
      main: 1,
      open: 0,
      search: 0,
      form: {},
      listform: {},
      insectOptions: [],
      optionProps: {
        value: "insectId",
        label: "insectName",
        children: "children",
        emitPath: false,
      },
      area: "",
      timeToSearch: "",
      count: 0,
      currenteditname: "",
      timeStamp: "",
    };
  },
  created() {
    if (this.$store.state.user.admin) {
      this.getImgList();
    } else if (!this.$store.state.user.admin) {
      this.searchByArea();
    }
  },
  mounted() {
    window.onresize = () => {
      let img = document.getElementById("myImg");
      // ////console.log(img);
      this.imgwidth = img.clientWidth;
      this.imgheight = img.clientHeight;
      ////console.log("imgWidth", this.imgwidth);
      ////console.log("imgHeight", this.imgheight);
    };
  },
  methods: {
    getImgList() {
      Image().then((res) => {
        // //console.log("admin:::" + this.$store.state.user.admin);
        //console.log(res);
        this.total = res.total;
        this.imgList = res.rows;
        for (let i = 0; i < this.total; i++) {
          this.imgList[i].originalPicture = this.imgList[i].originalPicture;
        }
        if (this.imgList[0].tagPicture) {
          // console.log("被标记了");
          this.activePath = this.imgList[0].tagPicture;
        } else {
          // console.log("lalal");
          this.activePath = this.imgList[0].originalPicture;
        }
        //console.log(this.imgList)
        // this.activePath = this.imgList[0].originalPicture;
        // //console.log(this.imgList)
        this.currentId = this.imgList[0].dataId;
        // ---------------------------
        let img = document.getElementById("myImg");
        // //console.log(img);
        this.imgwidth = img.clientWidth;
        this.imgheight = img.clientHeight;
        //console.log("imgWidth", this.imgwidth);
        //console.log("imgHeight", this.imgheight);
        // ------------------------------
        this.getInsectList();
      });
    },
    searchByArea() {
      this.area = this.$store.state.user.globaladdr;
      this.search = 1;
      //console.log(this.area);
      searchByArea({ address: this.area }).then((res) => {
        //console.log(res);
        this.total = res.total;
        this.imgList = res.rows;
        for (let i = 0; i < this.total; i++) {
          // this.imgList[i].originalPicture =
          //   baseURL + this.imgList[i].originalPicture;
          this.imgList[i].originalPicture = this.imgList[i].originalPicture;
        }
        this.activePath = this.imgList[0].originalPicture;
        // //console.log(this.imgList)
        this.currentId = this.imgList[0].dataId;
        let img = document.getElementById("myImg");
        // //console.log(img);
        this.imgwidth = img.clientWidth;
        this.imgheight = img.clientHeight;
        //console.log("imgWidth", this.imgwidth);
        //console.log("imgHeight", this.imgheight);
        // ------------------------------
        this.getInsectList();
      });
    },
    getInsectList() {
      //console.log("this.getInsectList");
      const id = this.currentId;
      InsectList(id).then((res) => {
        //console.log("打印res");
        // console.log(res);
        this.insectotal = res.total;
        this.insectList = res.rows;
        // //console.log(res.rows[0].insect.insectName);
        // //console.log(res.rows[0].number);

        for (let i = 0; i < this.insectotal; i++) {
          this.insectList[i].name = res.rows[i].insect.insectName;
          this.insectList[i].id = res.rows[i].insect.insectId;
          this.insectList[i].tagPicture = res.rows[i].data.tagPicture;
          this.insectList[i].num = res.rows[i].number;
          if (this.insectList[i].name == this.currenteditname) {
            this.currentedit = 1;
          } else {
            this.insectList[i].currentedit = 0;
          }

          // let Box = JSON.parse(res.rows[i].markerBox);
          // this.insectList[i].num = Box.length;
          // if (this.insectList[i].num == 0 || !this.insectList) {
          //   //console.log(this.insectList[i].name);
          //   deleteInsect({
          //     dataId: this.currentId,
          //     insectId: this.insectList[i].id,
          //   }).then((res) => {
          //     //console.log("删除成功");
          //     this.getInsectList();
          //   });
          // }
        }
      });
      listInsect().then((response) => {
        this.allInsect = response.data;
        this.allInsect.forEach((item) => {
          if (item.type != "2") {
            item.isDisabled = true;
          } else {
            item.isDisabled = false;
          }
        });
        this.insectOptions = this.handleTree(response.data, "insectId");
        // //console.log(this.insectOptions);
      });
    },
    getActiveImg(index) {
      // //console.log(index);
      //console.log("getActiveImg");
      if (this.markerEdit == 1) {
        this.$message.warning("请完成修改!!!");
        return 0;
      }
      this.main = 1;
      this.isEdit = 0;
      this.open = false;
      // if(!this.insectList[index].tagPicture){
      //   console.log("图片未标记")
      // console.log(this.imgList[index]);
      // console.log(this.imgList[index].originalPicture);
      if (this.imgList[index].tagPicture) {
        this.activePath = this.imgList[index].tagPicture;
      } else {
        this.activePath = this.imgList[index].originalPicture;
      }

      // }else{
      //   console.log("图片被标记")
      //
      // }

      this.activePathList.push(this.activePath);
      // //console.log(this.imgList[index].dataId);
      this.currentId = this.imgList[index].dataId;
      this.currentIndex = index;
      this.getInsectList();
    },
    fullScreen() {
      this.main = 0;
    },
    back() {
      this.main = 1;
    },

    edit() {
      // console.log("识别");

      DetailInsect_new(this.currentId).then((res) => {
        // console.log(res);
        this.getActiveImg(this.currentIndex)
      });
      // setTimeout(function () {
      //   this.getActiveImg(this.currentIndex);
      // }, 20000);
    },
    searchByTime() {
      this.area = this.$store.state.user.globaladdr;
      //console.log(this.area);
      //console.log(this.timeToSearch);
      if (this.$store.state.user.admin) {
        searchByTime({ address: this.area, photoTime: this.timeToSearch }).then(
          (res) => {
            //console.log(res);
            this.total = res.total;
            this.imgList = res.rows;
            if (!this.imgList) {
              this.imgList = [];
            }
            for (let i = 0; i < this.total; i++) {
              // this.imgList[i].originalPicture =
              //   baseURL + this.imgList[i].originalPicture;
              this.imgList[i].originalPicture = this.imgList[i].originalPicture;
            }
            this.activePath = this.imgList[0].originalPicture;
            // //console.log(this.imgList)
            this.currentId = this.imgList[0].dataId;
            this.getInsectList();
          }
        );
      } else if (!this.$store.state.user.admin) {
        if (!this.timeToSearch) {
          this.searchByArea();
        } else {
          searchByTimeAndAddress({
            address: this.area,
            photoTime: this.timeToSearch,
          }).then((res) => {
            //console.log(res);
            this.total = res.total;
            this.imgList = res.rows;
            if (!this.imgList) {
              this.imgList = [];
            }
            for (let i = 0; i < this.total; i++) {
              // this.imgList[i].originalPicture =
              //   baseURL + this.imgList[i].originalPicture;
              this.imgList[i].originalPicture = this.imgList[i].originalPicture;
            }
            this.activePath = this.imgList[0].originalPicture;
            // //console.log(this.imgList)
            this.currentId = this.imgList[0].dataId;
            this.getInsectList();
          });
        }
      }
    },
  },
};
</script>

<style scoped>
.notice {
  position: absolute;
  top: 0px;
  /* left:0px; */
  color: red;
  /* background-color: yellow; */
}
.container {
  padding: 20px;
}
/* .main-top{
  margin-bottom: 10px;
} */
canvas {
  position: absolute;
  top: 64px;
  /* left: 24%; */
  left: 13.7%;
  /* width: 630px;
  height: 450px; */
  /* background:rgba(255,255,255,0);关键点 */
}
.vueGallery {
  /* width: 800px; */
  width: 75%;
  height: 35vw;
  /* width: 80%; */
  /* height: 450px; */
  display: flex;
  justify-content: space-between;
  /* background: gainsboro; */
}

.activeBg {
  /* height: 100%;
  width: 70%;  */
  width: 100%;
  /* height: 450px; */
  /* width: 600px; */
  /* background: skyblue; */
}
.father {
  width: 20%;
  /* height:35vw; */
  display: flex;
  flex-grow: 1;
  flex-direction: column;
  overflow-x: hidden;
  overflow-y: scroll;
}
.thumbnails {
  /* width: 20%;
  height:35vw;
  display: flex;
  flex-grow: 1;
  flex-direction: column;
  overflow-x: hidden;
  overflow-y: scroll; */
  float: left;

  /* display: flex;
  flex-direction: column;
  justify-content: space-between;
  overflow-x: hidden;
  overflow-y: scroll;
  flex-wrap: wrap; */
}

.item {
  /* float:left; */
  width: 100%;
  height: auto;
  /* height:200px; */
  text-align: center;
}
.imgItem {
  width: 100%;
  height: auto;
  border-radius: 2px;
}
.imgItem::after {
  content: "";
  width: 100%;
  height: auto;
  /* position: absolute; */
  background-image: url("../../../assets/lazyLoadImg/img_err.png");
  background-size: cover;
}
/* .imgDetail{
  width: 100%;
  height: 100%;
} */
.details {
  position: absolute;
  width: 22%;
  top: 0;
  right: 0;
  /* background-color: antiquewhite; */
}
.button {
  width: 100%;
  /* margin-top: 30px; */
}
#button {
  width: 60%;
  margin-top: 20px;
  margin-left: 15%;
}
#show_button {
  margin-top: 20px;
}
.insect_list {
  width: 90%;
  /* margin-left: 10%; */
  margin-top: 30px;
}
.list_content {
  width: 90%;
  margin-top: 30px;
  height: 370px;
  overflow: auto;
}
.img_container {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  text-decoration: none;
}
.img_container:after {
  content: "";
  height: 0;
  width: 24%;
}
.img_container .item {
  margin-top: 10px;
  width: 24%;
  height: 100%;
  /* border: 1px solid #333333;
  background-color: skyblue; */
}
.fullscreen .top {
  width: 700px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  flex-wrap: wrap;
  text-decoration: none;
}
/* .bottom_button {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
} */
.ml-4 {
  /* position: absolute; */
  /* right: 0;
  top: 50px; */
  margin-top: 5px;
  float: right;
}
</style>