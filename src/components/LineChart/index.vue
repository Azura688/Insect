<template>
  <div :id="chartId" style="width: 100%; height: 100%;"></div>
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

export default {
  name: "chart",
  props: {
    chartId: String,
    name: String,
    xData: Array,
    itemData: Array,
    series: Array,
    // legend: Object
  },
  data() {
    return {};
  },
  watch: {
    xData: function(val){
      this.renderChart();
    },
    series: function(val) {
      this.renderChart();
    }
  },
  mounted() {
    this.renderChart();
  },
  methods: {
    // 渲染折线图
    renderChart() {
      var lineChart = document.getElementById(this.chartId)
      //console.log(lineChart.clientWidth);
      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(document.getElementById(this.chartId));
      // 绘制图表
      myChart.clientWidth = lineChart.clientWidth
      //console.log(myChart.clientWidth);
      myChart.setOption({
        title: {
          text: this.name+"昆虫数量/只",
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
          data: this.xData,
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
    }
  }
};
</script>
