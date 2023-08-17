<template>
  <div class="container">
    <!-- 地图 -->
    <el-row :gutter="30">
      <el-col :span="16" :xs="24" :sm="20" :md="16">
        <div
          id="main"
          ref="mainMap"
          style="min-width: 400px; min-height: 600px; backgroundColor: rgba(0,0,0,.2); overflow: hidden">
        </div>
      </el-col>
      <el-col :span="8" :xs="8" :sm="8" :md="8">
        <div id="sideRight" style="min-width: 220px;" size="mini">
          <br>
          <el-button @click="cancel" :disabled="isDisabled" type="primary" plain class="el-icon-back">返回上一级</el-button>
          <br>
          <br>
          <el-descriptions class="margin-top" title="设备详细信息" :column="1"  border>
            <el-descriptions-item :span="2" :labelStyle="{'width': '70px', 'font-size': '16px'}" :contentStyle="{'width': '80px', 'text-align': 'center', 'font-size': '16px'}">
              <template slot="label">
                <i class="el-icon-switch-button"></i>
                设备工作状态
              </template>
              <template>
                <p class="eWorkStatus"></p>
                <!-- <el-tag type='success' class="eWorkStatus" v-if="workStatusType === '1'"></el-tag>
                <el-tag type='info' class="eWorkStatus" v-else></el-tag> -->
              </template>
            </el-descriptions-item>
            <el-descriptions-item :span="2" :labelStyle="{'width': '70px', 'font-size': '16px'}" :contentStyle="{'width': '80px', 'text-align': 'center', 'font-size': '16px'}">
              <template slot="label">
                <i class="el-icon-s-operation"></i>
                电量
              </template>
              <template>
                <p class="eElectricity"></p>
                <!-- <el-tag  class="eElectricity"></el-tag> -->
              </template>
            </el-descriptions-item>
            <el-descriptions-item :span="2" :labelStyle="{'width': '70px', 'font-size': '16px'}" :contentStyle="{'width': '80px', 'text-align': 'center', 'font-size': '16px'}">
              <template slot="label">
                <i class="el-icon-sunny"></i>
                天气
              </template>
              <template>
                <p class="eWeather"></p>
                <!-- <el-tag  class="eWeather"></el-tag> -->
              </template>
            </el-descriptions-item>
            <el-descriptions-item :span="2" :labelStyle="{'width': '70px', 'font-size': '16px'}" :contentStyle="{'width': '80px',  'text-align': 'center', 'font-size': '16px'}">
              <template slot="label">
                <i class="el-icon-open"></i>
                挡雨板状态
              </template>
              <!-- <el-tag :type="weathershieldStatusType" class="eWeathershieldStatus"></el-tag> -->
              <!-- <el-tag :type="weathershieldStatusType==='1'?'success':'info'" class="eWeathershieldStatus"></el-tag> -->
              <!-- <el-tag>{{ descriptionList.workStatus }}  </el-tag> -->
              <p class="eWeathershieldStatus"></p>

            </el-descriptions-item>
            <el-descriptions-item :span="2" :labelStyle="{'width': '70px', 'font-size': '16px'}" :contentStyle="{'text-align': 'center', 'font-size': '16px'}" >
              <template slot="label">
                <i class="el-icon-location"></i>
                设备所在地
              </template>
              <template>
                <p class="eAddress"></p>
                <!-- <el-tag  class="eAddress"></el-tag> -->
              </template>
            </el-descriptions-item>
            <el-descriptions-item :span="2" :labelStyle="{'width': '70px', 'font-size': '16px'}" :contentStyle="{'text-align': 'center', 'font-size': '16px'}" >
              <template slot="label">
                <i class="el-icon-map-location"></i>
                详细地址
              </template>
              <template>
                <p class="eAddressDetail"></p>
                <!-- <el-tag class="eAddressDetail"></el-tag> -->
              </template>
            </el-descriptions-item>
          </el-descriptions>
          <!-- 设备工作状态：<p class="eWorkStatus"></p> -->
        </div>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import {listAllEquipment} from "@/api/insectApi/equipment"
import echarts from "echarts";
import axios from "axios";
import {
  provinces,
  provincesText,
  arrCityCodeConcat,
  arrCityName,
  arrDisCode,
  arrDisName,
  allName,
  allCode
} from "../../../../public/mapJson/test";

var cities = arrCityCodeConcat;
var citiesText = arrCityName;

var districts = arrDisCode;
var districtsText = arrDisName;
const chinaJson = require("../../../../public/mapJson/china.json");

export default {
  data() {
    return {
      myChart: "",
      distributionOptions: "",
      name: "",
      status: "",
      weather: "",
      historyData: ['100000',],
      initList: [],
      allList: [],
      // 正常工作设备
      onEquipmentList: [],
      // 停机设备
      offEquipmentList: [],
      workStatusType: "",
      weathershieldStatusType: "",
      // descriptionList: { workStatus: "------", weathershieldStatus: "------", electricity: "-------", weather: "------", address: "------", addressDetail: "------"},
      descriptionList: { workStatus: "------", weathershieldStatus: undefined, electricity: undefined, weather: undefined, address: undefined, detail: undefined},
      workStatusOptions:[{value: "0",label: "停用"},{value: "1",label: "正常"}],
      weathershieldStatusOptions:[{value: "0",label: "停用"},{value: "1",label: "正常"}],
      screenWidth: '',
      screenHeight: ''
    };
  },
  computed:{
    isDisabled: function(){
      if(this.historyData.length > 1){
        return false
      }
      else{ return true }
    }
  },
  watch:{
    offEquipmentList(){
      this.$nextTick(()=>{
        //此时就可以获取到在created赋值后的dataList了
        //console.log("watched");
      })
    }
  },

  created() {
  },
  beforeRouteEnter(to, from, next) {
    listAllEquipment({workStatus: "1"}).then(resOn => {
      //console.log(resOn);
      listAllEquipment({workStatus: "0"}).then(resOff => {
        //console.log(resOff);
        next(vm => {
          vm.onEquipmentList = resOn.rows
          vm.offEquipmentList = resOff.rows
          vm.onEquipmentList.forEach(ele => {
            ele.value = [Number(ele.longitude),Number(ele.latitude)]
            ele.value.push(1)})
          vm.offEquipmentList.forEach(ele => {
            ele.value = [Number(ele.longitude),Number(ele.latitude)]
            ele.value.push(0)})
          //console.log(vm.onEquipmentList);//执行完created之后打印
          //console.log(vm.offEquipmentList);
        })
      })
    })
  },

  mounted() {
    //console.log("mounted");
    this.getHeight()
    this.init();
    // setTimeout(() => {
    //   window.addEventListener('resize',this.myChart.resize)
    // }, 100);
    // window.addEventListener('resize',this.myChart.resize)
    var myChart = echarts.init(document.getElementById("main"))
    window.addEventListener("resize", this.resizeFunc(function(){
        //console.log("resize");
        myChart.resize()
      },
      500))
    var eWorkStatus = document.querySelector(".eWorkStatus")
    var eElectricity = document.querySelector(".eElectricity")
    var eWeather = document.querySelector(".eWeather")
    var eWeathershieldStatus = document.querySelector(".eWeathershieldStatus")
    var eAddressDetail = document.querySelector(".eAddressDetail")
    var eAddress = document.querySelector(".eAddress")

    listAllEquipment().then(res => {
      this.allList = res.rows
      //console.log(this.allList);
      // let list = []
      // this.allList.forEach(ele=>{
      //   list.push(ele)
      // })
      // console.log(list);
      // console.log(list.findIndex((ele)=>{ele.workStatus=='0'}));
      // console.log(list.filter(ele=>{ele.workStatus=='0'}));
      // console.log([1,2,3,-1].findIndex( (n)=> n<0 ) )
      let foundRes = this.allList.find(this.findOne)
      // console.log(this.allList.find(this.findOne));
      if(foundRes != undefined){
        if(foundRes.workStatus === "0"){
          eWorkStatus.innerText = "停用";
          this.workStatusType = "0";
        }
        else{
          eWorkStatus.innerText = "正常";
          this.workStatusType = "1";
        }
        eElectricity.innerText = foundRes.electricity;
        eWeather.innerText = foundRes.weather;
        eAddressDetail.innerText = foundRes.detail;
        eAddress.innerText = foundRes.province + foundRes.city + foundRes.county;
        if(foundRes.weathershieldStatus === "0"){
          eWeathershieldStatus.innerText = "停用";
          this.weathershieldStatusType = '0';
        }
        else{
          eWeathershieldStatus.innerText = "正常";
          this.weathershieldStatusType = '1';
        }
      }
      else{
        if(this.allList[0].workStatus === "0"){
          eWorkStatus.innerText = "停用";
          this.workStatusType = "0";
        }
        else{
          eWorkStatus.innerText = "正常";
          this.workStatusType = "1";
        }
        eElectricity.innerText = this.allList[0].electricity;
        eWeather.innerText = this.allList[0].weather;
        eAddressDetail.innerText = this.allList[0].detail;
        eAddress.innerText = this.allList[0].province + this.allList[0].city + this.allList[0].county;
        if(this.allList[0].weathershieldStatus === "0"){
          eWeathershieldStatus.innerText = "停用";
          this.weathershieldStatusType = '0';
        }
        else{
          eWeathershieldStatus.innerText = "正常";
          this.weathershieldStatusType = '1';
        }
      }

    })
  },

  methods: {
    resizeFunc(func, time){
      let timer = null
      return function(){
        clearTimeout(timer)
        timer = setTimeout(()=>{
          func.apply(this,arguments)
        }, time)
      }
    },
    getHeight() { //计算高度
      let mapBox = document.getElementById("main");
      let sideRight = document.getElementById("sideRight")
      mapBox.style.height = (window.innerHeight - 130) + "px"
      sideRight.style.height = (window.innerHeight - 130) + "px"
      //console.log(mapBox.style.height);
    },
    findOne(ele) {
      return ele.county === '洪山区'|| ele.city === '武汉市' || ele.province === '湖北省';
    },

    //显示各省地图
    getProvinceMapOpt(provinceAlphabet, name) {
      var path = "/mapJson/province/" + provinceAlphabet + ".json";
      if (provinceAlphabet === 100000) {
        path = "/mapJson/china.json";
      }
      axios.get(path).then((s) => {
        echarts.registerMap(name, s.data);
        this.changeOptions(name);
        this.myChart.setOption(this.distributionOptions, true);
      });
    },

    //显示各市地图
    getCityMapOpt(cityAlphabet, name) {
      var path = "/mapJson/cities/" + cityAlphabet + ".json";
      if (cityAlphabet === 100000) {
        path = "/mapJson/china.json";
      }
      axios.get(path).then((s) => {
        echarts.registerMap(name, s.data);
        this.changeOptions(name);
        this.myChart.setOption(this.distributionOptions, true);
      });
    },

    //显示各区县地图
    getDistrictMapOpt(districtAlphabet, name) {
      var path = "/mapJson/districts/" + districtAlphabet + ".json";
      if (districtAlphabet === 100000) {
        path = "/mapJson/china.json";
      }
      axios.get(path).then((s) => {
        echarts.registerMap(name, s.data);
        this.changeOptions(name);
        this.myChart.setOption(this.distributionOptions, true);
      });
    },
    cancel(){
      let item
      if(this.historyData.length !== 1){
        this.historyData.pop();//本级
        // console.log(this.historyData.length);
        item = this.historyData.pop()
        // console.log(item);
        this.historyData.push(item)
      }
      else{
        item = '100000'//只剩下全国级别
      }
      // console.log(this.historyData);
      var path = "/mapJson/all/" + item + ".json";
      this.myChart = echarts.init(document.getElementById("main"));
      if (item === '100000') {
        path = "/mapJson/china.json";
      }
      axios.get(path).then((s) => {
        echarts.registerMap(item, s.data);
        this.changeOptions(item);
        this.myChart.setOption(this.distributionOptions, true);
      });
    },

    init() {
      echarts.registerMap("china", chinaJson);
      this.changeOptions("china");
      this.myChart = echarts.init(document.getElementById("main"));
      this.myChart.setOption(this.distributionOptions);
      this.myChart.on("click", (chinaParam) => {
        // console.log(chinaParam.name);
        // console.log(provinces[provincesText.indexOf(chinaParam.name)]);
        // console.log(cities[citiesText.indexOf(chinaParam.name)]);
        // console.log(districts[districtsText.indexOf(chinaParam.name)]);
        let preItem = allCode[allName.indexOf(chinaParam.name)]
        // console.log(preItem);
        // console.log(typeof(preItem));
        // console.log(this.historyData.includes(preItem))//false时表示无，添加进historyData
        if(preItem !== undefined && this.historyData.includes(preItem) === false){
          this.historyData.push(preItem)
          //console.log(this.historyData);
        }
        if (provinces[provincesText.indexOf(chinaParam.name)]) {
          let code = provinces[provincesText.indexOf(chinaParam.name)];
          this.getProvinceMapOpt(
            code,
            provincesText[provincesText.indexOf(chinaParam.name)]
          );
        } else if (cities[citiesText.indexOf(chinaParam.name)]) {
          let code = cities[citiesText.indexOf(chinaParam.name)];
          this.getCityMapOpt(code, citiesText.indexOf(chinaParam.name));
        } else if (
          districts[districtsText.indexOf(chinaParam.name) || 100000]
        ) {
          let code =
            districts[districtsText.indexOf(chinaParam.name)] || 100000;
          this.getDistrictMapOpt(code, districtsText.indexOf(chinaParam.name));
        }
      });
      var eWorkStatus = document.querySelector(".eWorkStatus")
      var eElectricity = document.querySelector(".eElectricity")
      var eWeather = document.querySelector(".eWeather")
      var eWeathershieldStatus = document.querySelector(".eWeathershieldStatus")
      var eAddress = document.querySelector(".eAddress")
      var eAddressDetail = document.querySelector(".eAddressDetail")

      this.myChart.on('click', function(param){
        //console.log(param);

        if(param.componentType === 'series'&& param.componentSubType === 'scatter'){
          // this.name = param.data.name;
          // this.status = param.data.status;
          // this.weather = param.data.weather;
          this.descriptionList = param.data
          //console.log(this.descriptionList);
          // this.descriptionList.workStatus = param.data.workStatus
          // this.descriptionList.weathershieldStatus = param.data.weathershieldStatus
          // this.descriptionList.electricity = param.data.electricity
          // this.descriptionList.weather = param.data.weather
          this.descriptionList.address = param.data.province + param.data.city + param.data.county;
          // this.descriptionList.addressDetail = param.data.detail
          //console.log(this.descriptionList);
          if(param.data.workStatus === "0"){
            eWorkStatus.innerText = "停用";
            this.workStatusType = "0";
          }
          else{
            eWorkStatus.innerText = "正常";
            this.workStatusType = "1";
          }
          eElectricity.innerText = param.data.electricity;
          eWeather.innerText = param.data.weather;
          eAddressDetail.innerText = param.data.detail;
          eAddress.innerText = param.data.province + param.data.city + param.data.county;
          if(param.data.weathershieldStatus === "0"){
            eWeathershieldStatus.innerText = "停用";
            this.weathershieldStatusType = '0';
          }
          else{
            eWeathershieldStatus.innerText = "正常";
            this.weathershieldStatusType = '1';
          }
        }
      })
    },

    changeOptions(name) {
      // 获取经纬度数据
      const seriesList = [
        {
          name: "启用",
          color: 'green',
          label: {
            normal: {
              formatter: function (params) {
                return  "设备编号：" + params.data.id + "<br />" + "坐标：" + "北纬 " +params.data.value[1].toFixed(2) + ", 东经 " + params.data.value[0].toFixed(2)
              },
              position: "right",
              show: true,
            }
          },
          data: this.onEquipmentList
        },
        {
          name: "禁用",
          color: 'gray',
          label: {
            normal: {
              formatter: function (params) {
                return  "设备编号：" + params.data.id + "<br />" + "坐标：" + "北纬 " +params.data.value[1].toFixed(2) + ", 东经 " + params.data.value[0].toFixed(2)
              },
              position: "right",
              show: true,
            },
          },
          data: this.offEquipmentList
        }
      ];

      // 自定义图标
      const series = seriesList.map(v => {
        return {
          type: "scatter", //配置显示方式为用户自定义
          coordinateSystem: "geo",
          data: v.data,
          itemStyle: {
            normal: {
              color: v.color
            }
          }
        };
      });


      // options
      this.distributionOptions = {
        tooltip: {
          // 提示框组件
          show: true, // 显示提示框组件
          trigger: "item", // 触发类型
          triggerOn: "mousemove", // 触发条件
          formatter: function (params) {
            return (
              // params.data
              "设备编号：" + params.data.id + "<br />" + "坐标：" + "北纬 " +params.data.value[1].toFixed(2) + ", 东经 " + params.data.value[0].toFixed(2)
            );
          },
        },
        visualMap: {	// 视觉映射组件
          type: 'piecewise',
          min: 0,
          max: 1,
          pieces: [
            { value: 0, label: "禁用", color: "gray" },// 0
            { value: 1, label: "启用", color: "green" } // 1
          ],
          left:"left",                              //组件离容器左侧的距离,'left', 'center', 'right','20%'
          top:"top",                                   //组件离容器上侧的距离,'top', 'middle', 'bottom','20%'
          right:"auto",                               //组件离容器右侧的距离,'20%'
          bottom:"auto",                              //组件离容器下侧的距离,'20%'
          orient:"vertical",                        //图例排列方向
          padding:5,                                   //图例内边距，单位px  5  [5, 10]  [5,10,5,10]
          backgroundColor:"transparent",            //标题背景色
          borderColor:"#ccc",                         //边框颜色
          borderWidth:0,                               //边框线宽
          hoverLink: true,//鼠标悬浮到 visualMap 组件上时，鼠标位置对应的数值 在 图表中对应的图形元素，会高亮
          textStyle: {
            color: 'black',
            fontSize: 16,
          }
        },
        series, // 数据
        geo: {
          map: name || "china", // 引入地图
          layoutCenter: ["50%", "58%"], //设置后left/right/top/bottom等属性无效
          layoutSize: "45%",
          roam: true, //开启鼠标缩放和漫
          zoom: 2.5,
          label: {
            normal: {
              //静态的时候展示样式
              show: true, //是否显示地图省份的名称
              textStyle: {
                color: "#fff",
                fontSize: 10,
                fontFamily: "Arial",
              },
            },
            emphasis: {
              // 高亮状态下的样式
              //动态展示的样式
              color: "#fff",
            },
          },
          itemStyle: {
            // 地图区域的多边形 图形样式。
            normal: {
              borderColor: "#07919e", // 边框颜色
              areaColor: "#1c2f59", //  区域颜色
              textStyle: {
                // 文字颜色
                color: "#fff",
              },
              shadowBlur: 10, // 图形阴影的模糊大小
              shadowOffsetX: 10, // 阴影水平方向上的偏移距离。
            },
            emphasis: {
              areaColor: "#1c2f59",
              color: "#fff",
            },
          },
        },
      };
    },
  },
};
</script>
<style scoped>
.container{
  height: calc(100vh-200px);
}
</style>
