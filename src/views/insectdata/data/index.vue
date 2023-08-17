<template>
  <div class="divContainer" ref="container">
    <el-container style="height:100%">
      <el-aside class="left-container" style="width: 25%; min-width:300px;backgroundColor: #fff">
        <treeselect 
        v-model="selectedInsectId"
        :options="insectOptions" :flat="true" :multiple="false" :always-open="true" :default-expand-level="2" :disable-branch-nodes="true" :normalizer="normalizer" 
        @input="inputChange"
        @select="handleNodeClick" placeholder="选择种类" />
      </el-aside>
      <!--昆虫数量曲线-->

      <el-container style="width: 60%" >
        <el-header style=" padding-top: 20px; min-height: 100px; " class="hidden-xs-only">
          <el-row>
            <el-col :span="4" :sm="4">
              <div>消长动态：</div>
            </el-col>
            <el-col :span="16" :sm="16">
              <el-date-picker
                v-model="time"
                type="daterange"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @input="daterangeChanged"
              >
              </el-date-picker>
            </el-col>
            <el-col :span="4" :sm="4">
              <el-button type="primary" @click="searchLineByDaterange" :disabled="daterangeSelected" >确定</el-button>
            </el-col>
          </el-row>
          <el-row>
            <br>
          </el-row>
          <el-row>
            <el-col :span="4" :xs="6">
              上灯节律：
            </el-col>
            <el-col :span="16" :xs="18">
              <el-date-picker
                v-model="someday"
                type="date"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                @input="somedayChanged">
              </el-date-picker>
            </el-col>
            <el-col :span="4" :xs="4">
              <el-button type="primary" @click="searchLineByDate" :disabled="somedaySelected" >确定</el-button>
            </el-col>
          </el-row>
        </el-header>
        <el-main class="lineChart">
          <div id="myChart" style="min-width: 700px; min-height: 420px;width: 100%; height: 100%;"></div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script>
// 引入 ECharts 主模块
var echarts = require("echarts/lib/echarts");
// 引入折线图
require("echarts/lib/chart/line");
// 引入提示框、标题组件、图例
require("echarts/lib/component/tooltip");
require("echarts/lib/component/title");
require("echarts/lib/component/legend");

import { listInsect, listSpecies} from "@/api/insectApi/insect";
import { listByDate, listByDatePeriod } from '@/api/insectApi/insectLine.js'
// 引入日期相关工具
import dateUtil from "@/libs/dateUtil.js";
import dateFormat from "@/libs/dateFormat.js";

// 引入折线图组件
import chart from "@/components/LineChart/index.vue";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import "@/assets/myStyles/vue-treeselect.css";
import 'element-ui/lib/theme-chalk/display.css';

export default {
  components: { chart, Treeselect },
  data(){
    return{
      // open: false,
      // title: "",
      form: {},
      chartName: "",
      lineChartStyle: {},
      selectedInsectId: undefined,
      numArray: [],
      insectOptions:[],
      insectName: "",
      insectDefault: {},
      dateArray: 
      dateUtil.getDateArray(
        new Date().setTime(new Date().getTime() - 3600 * 1000 * 24 * 7),
        new Date(),
        3600 * 1000 * 24 * 1,
        "yyyy-MM-dd"
      ),
      time: [],
      defaultTime:[],
      someday: "",
      daterangeSelected: true,
      somedaySelected: true,
      submitPeriodParams:{
        insectId: undefined,
        start: undefined,
        end: undefined
      },
      submitDateParams:{
        insectId: undefined,
        date: undefined,
      },
      list:[],
      allInsect:[],
      allSpecies: [],
      // 折线图
    }
  },
  watch: {
    dateArray: function(val){
      this.renderChart();
    },
    series: function(val) {
      this.renderChart();
    }
  },
  computed: {
    series: function () {
      let arr = [
        {
          name: this.chartName+"昆虫数量",
          data: this.numArray,
          type: "line",
          color: "#036bc8",
          symbolSize: 6,//折线图上的空心圆点
        },
      ];
      return arr;
    },
  },
  created(){
    this.getDefaultInsect();
    this.getInsect();
    // this.getDefaultLine()
  },
  mounted(){
    this.renderChart();
  },
  methods: {
    getInsect(){
      listInsect().then(response=>{
        // 转换成树形结构
        this.allInsect = response.data;
        this.allInsect.forEach( item => {
          if(item.type != '2'){
            item.isDisabled = true
          }
          else{
            item.isDisabled = false
          }
        })
        ////console.log(this.allInsect);
        ////console.log(this.insectDefault);
        this.insectOptions = this.handleTree(response.data, "insectId");
        this.getDefaultLine();
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
    getDefaultInsect(){
      listSpecies({type: '2'}).then( res => {
        ////console.log(res.data);
        this.allSpecies = res.data
        this.insectDefault = this.allSpecies[0]
        this.selectedInsectId = this.insectDefault.insectId
        this.chartName = this.insectDefault.insectName
      })
    },

    resetPeriodParam(){
      this.submitPeriodParams = {
        insectId: undefined,
        start: undefined,
        end: undefined
      }
    },
    resetDateParam(){
      this.submitDateParams = {
        insectId: undefined,
        date: undefined
      }
    },
    inputChange(data){
      ////console.log("input",data);
      ////console.log(this.selectedInsectId);
      this.resetPeriodParam();
      this.resetDateParam();
      this.submitDateParams.insectId = data
      this.submitPeriodParams.insectId = data
      for(let i=0;i<this.allSpecies.length;i++){
        if(this.allSpecies[i].insectId===data){
          if(this.allSpecies[i].type==="2"){
            break;
          }
        }
      }
    },
    // 必须先选时间再选昆虫种类
    handleNodeClick(data){
      // console.log("select",data);
      // console.log(data.insectId);
/*       this.queryParams.insectId = data.insectId
      this.submitDateParams.insectId = data.insectId
      this.submitPeriodParams.insectId = data.insectId
      console.log(this.queryParams); */
    },
    daterangeChanged(){
      //console.log(this.time);
      if(this.time==null||this.time==undefined){
        this.daterangeSelected = true;
      }
      else{
        this.daterangeSelected = false;
      }
    },
    somedayChanged(){
      //console.log(this.someday);
      if(this.someday==null||this.someday==undefined){
        this.somedaySelected = true;
      }
      else{
        this.somedaySelected = false;
      }
    },
    getDefaultLine(){
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      //console.log(start);
      //console.log(end);
      //console.log(typeof(dateFormat.formatDate(start,'yyyy-MM-dd')));
      //console.log(dateFormat.formatDate(end,'yyyy-MM-dd'));
      // //console.log(this.defaultTime)
      this.submitPeriodParams.start = dateFormat.formatDate(start,'yyyy-MM-dd')
      this.submitPeriodParams.end = dateFormat.formatDate(end,'yyyy-MM-dd')
      this.time.push(this.submitPeriodParams.start);
      this.time.push(this.submitPeriodParams.end);
      this.daterangeSelected = false
      this.selectedInsectId = String(this.insectDefault.insectId)
      this.dateArray = dateUtil.getDateArray(dateFormat.formatDate(start,'yyyy-MM-dd'),dateFormat.formatDate(end,'yyyy-MM-dd'), 1000*3600*24*1, "yyyy-MM-dd" )
      // this.dateArray.pop()
      //console.log(this.dateArray);
      this.submitPeriodParams.insectId = this.insectDefault.insectId;
      //console.log("---------");
      //console.log(this.submitPeriodParams);
      this.chartName = this.insectDefault.insectName;
      listByDatePeriod(this.submitPeriodParams).then(response=>{
          //console.log(response);
          //console.log(typeof(response.msg));
          let str = response.msg.slice(1,response.msg.length-1)
          //console.log(str);
          this.numArray = str.split(",")
          //console.log(this.numArray);
      })
    },
    searchLineByDaterange(){
      //console.log(this.time);
      this.submitPeriodParams.start = this.time[0]
      this.submitPeriodParams.end = this.time[1]
      this.dateArray = dateUtil.getDateArray( this.time[0], this.time[1], 1000*3600*24*1, "yyyy-MM-dd" )
      // this.dateArray.pop()
      //console.log(this.dateArray);
      if(this.submitPeriodParams.insectId == undefined){
        this.$message({
          type: 'error',
          message: '请选择昆虫类别！'
        })
      }
      else{
        for(let i=0;i<this.allSpecies.length;i++){
          if(this.allSpecies[i].insectId===Number(this.selectedInsectId)){
            this.chartName = this.allSpecies[i].insectName
            break;
          }
        }
        // this.chartName = this.selectedInsectId
        listByDatePeriod(this.submitPeriodParams).then(response=>{
          //console.log(response);
          //console.log(typeof(response.msg));
          let str = response.msg.slice(1,response.msg.length-1)
          //console.log(str);
          this.numArray = str.split(",")
          //console.log(this.numArray);
        })
      }
    },
    searchLineByDate(){
      //console.log(this.someday);
      this.submitDateParams.date = this.someday
      let d = new Date(this.someday).getTime()
      let d1=new Date(d)
      //console.log(d);
      //console.log(d1);
      // this.dateArray = dateUtil.getDateArray( this.someday+1000*3600, this.someday + 1000*3600*24*1, 1000*3600, "MM-dd:hh" )
      this.dateArray = dateUtil.getHourArray( d-1000*3600*7, d + 1000*3600*16*1, 1000*3600, "yyyy-MM-dd hh时" )
      //console.log(this.dateArray);
      if(this.submitDateParams.insectId == undefined){
        this.$message({
          type: 'error',
          message: '请选择昆虫类别！'
        })
      }
      else{
        for(let i=0;i<this.allSpecies.length;i++){
          if(this.allSpecies[i].insectId===Number(this.selectedInsectId)){
            this.chartName = this.allSpecies[i].insectName
            break;
          }
        }
        listByDate(this.submitDateParams).then(response=>{
          //console.log(response);
          let str = response.msg.slice(1,response.msg.length-1)
          //console.log(str);
          this.numArray = str.split(",")
          //console.log(this.numArray);
        })
      }   
    },
    resizeFunc(func, time){
      let timer = null
      return function(){
        clearTimeout(timer)
        timer = setTimeout(()=>{
          func.apply(this,arguments)
        }, time)
      }
    },
        // 渲染折线图
    renderChart() {
      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(document.getElementById("myChart"));
      //console.log(myChart);
      window.addEventListener("resize", this.resizeFunc(function(){
        //console.log("resize");
        myChart.resize()
        },
      500))
      // 绘制图表

      myChart.setOption({
        title: {
          text: this.chartName+"昆虫数量/只",
          textStyle: {
            color: "#323232",
            fontSize: 16,
            fontWeight: "normal"
          }
        },
        // legend: this.legend,
        //设置canvas内部表格的内距
        grid: {
          x: 40,
          y: 20,
          x2: 40,
          y2: 60,
          borderWidth: 10,
          top: "8%",
        },
        toolbox: {
          show: true,
          feature: {
            mark: { show: true },
            dataView: { show: true, readOnly: false },
            magicType: { show: true, type: ["line", "bar"] },
            restore: { show: true },
            saveAsImage: { show: true }
          },
          trigger: "axis",
          formatter: '{b0}<br />{a0}:{c0}'
        },
        tooltip: {
          trigger: "axis",
          formatter: '{b0}<br />{a0}:{c0}'
        },
        xAxis: {
          type: "category",
          name: "时间",
          nameTextStyle: {
            fontSize: 16,
            align: 'left'
          },
          nameGap: 6,
          data: this.dateArray,
          axisLine: {
            show: true
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            show: true,
            interval: 3
          }
        },
        yAxis: {
          type: "value",
          axisLine: {
            show: true
          },
          axisTick: {
            show: false
          }
        },
        series: this.series
      });
    },
  }
}
</script>
<style scoped>
  .divContainer{
    height: calc(90vh);
    /* background-color: pink; */
  }
  .el-aside{
    /* height: calc(90vh); */
    /* overflow: -moz-hidden-unscrollable; */
    overflow: hidden;
    /* background-color: skyblue; */
  }
  .el-main{
    overflow: hidden;
    /* height: calc(80vh); */
    /* background-color: pink; */
  }
</style>