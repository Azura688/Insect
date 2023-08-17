<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="设备工作状态" prop="workStatus">
        <!-- 设备管理 -->
        <el-select v-model="queryParams.workStatus" placeholder="请选择设备工作状态">
          <el-option
            v-for="item in workStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="设备挡雨板状态" prop="weathershieldStatus"> 
        <el-select v-model="queryParams.weathershieldStatus" placeholder="请选择设备挡雨板工作状态">
          <el-option
            v-for="item in weathershieldStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <br>
      <el-form-item label="设备地址">
        <v-distpicker 
          :province="queryParams.province"
          :city="queryParams.city"
          :county="queryParams.county"
          @province="queryProvinceChange"
          @city="queryCityChange"
          @area="queryAreaChange"
          @selected="querySelected"></v-distpicker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="equipmentList" @selection-change="handleSelectionChange" :row-style="{height:'80px'}"
:cell-style="{padding:'0px'}" >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="设备编号" align="center" prop="equipmentId" min-width="80"/>
      <el-table-column label="设备地址" align="center" prop="equipmentLocation" min-width="160"/>
      <el-table-column label="详细地址" align="center" prop="addressDetail" class="detail" min-width="200"/>
      <el-table-column label="工作状态" align="center" prop="workStatus">
        <template slot-scope="scope">
          <el-tag :type="scope.row.workStatus==='1'?'success':'danger'">{{ getStatusTag(scope.row.workStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="电量" align="center" prop="electricity"/>
      <el-table-column label="挡雨板状态" align="center" prop="weathershieldStatus">
        <template slot-scope="scope">
          <el-tag :type="scope.row.weathershieldStatus==='1'?'success':'danger'">{{ getShieldTag(scope.row.weathershieldStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="所在地气候" align="center" prop="weather"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-folder-add"
            @click="handleUploadImgs(scope.row)"
          >上传图片</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改设备对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body v-if="open">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备工作状态:" prop="workStatus" label-width="120px">
              <!-- <el-input v-model="form.workStatus" placeholder="请输入设备工作状态" /> -->
              <el-select v-model="form.workStatus" placeholder="请选择设备工作状态">
                <el-option
                  v-for="item in workStatusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="挡雨板状态:" prop="weathershieldStatus" label-width="100px">
              <!-- <el-input v-model="form.weathershieldStatus" placeholder="请输入设备挡雨板状态" /> -->
              <el-select v-model="form.weathershieldStatus" placeholder="请输入设备挡雨板状态">
                <el-option
                  v-for="item in weathershieldStatusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="所在地气候:" prop="weather" label-width="120px">
              <el-input v-model="form.weather" placeholder="请输入设备所在地气候" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备电量:" prop="electricity" label-width="100px">
              <el-input-number v-model="form.electricity" :min="0" :max="100" placeholder="请输入设备电量" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="设备地址:" prop="equipmentLocation" placeholder="请输入设备地址" label-width="120px">
              <v-distpicker @selected="onSelected"
                :province="form.province"
                :city="form.city"
                :area="form.county"
                @province="provinceChange"
                @city="cityChange"
                @area="areaChange"></v-distpicker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="详细地址：" prop="addressDetail" label-width="120px">
             <el-autocomplete
               v-model="form.addressDetail"
               style="width: 100%"
               popper-class="autoAddressClass"
               :fetch-suggestions="getAddressList"
               :trigger-on-focus="false"
               placeholder="详细地址"
               clearable
               @select="handleSelect"
             >
               <!-- 双向数据绑定 -->
               <template slot-scope="{ item }">
                 <i class="el-icon-search fl mgr10" />
                 <div style="overflow: hidden">
                   <div class="title">{{ item.name }}</div>
                   <span class="address ellipsis">{{ item.address }}</span>
                 </div>
               </template>
             </el-autocomplete>
           </el-form-item>
          </el-col>
        </el-row>
 

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 上传设备拍摄图片对话框 -->
    <el-dialog :title="captureTitle" :visible.sync="captureOpen" width="600px" append-to-body v-if="captureOpen" max-height="800px" class="captureUploadDialog">
      <el-form ref="captureForm" :model="captureForm" :rules="captureFormRules" label-width="80px">
        <el-form-item label="图片列表：" prop="originalPictures" label-width="100px">
          <my-file-upload v-model="captureForm.originalPictures" v-if="captureOpen" :limit="50"></my-file-upload>
        </el-form-item>
        <el-form-item label="拍摄时间：" prop="photoTime" label-width="100px">
          <div>
            <el-date-picker
            v-model="captureForm.photoTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:00"
            format="yyyy-MM-dd HH:mm:00">
          </el-date-picker>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCaptureForm">确 定</el-button>
        <el-button @click="cancelUpload">取 消</el-button>
      </div>      
    </el-dialog>
  </div>
</template>
<script>
import {listAllEquipment, getEquipmentById, addEquipment, updateEquipment, delEquipment, exportEquipment, importCapture} from "@/api/insectApi/equipment"
import VDistpicker from 'v-distpicker'
import Pagination from '@/components/Pagination'
import loadBMap from '@/utils/loadBMap.js'
import MyFileUpload from "@/components/MyFileUpload"
import dateUtil from "@/libs/dateUtil"
import request from "@/utils/request"
export default {
  components: { VDistpicker, Pagination, MyFileUpload},
  data() {
    return {
      workStatusOptions:[{value: "0", label: "停用"},{value: "1", label: "启用"}],
      weathershieldStatusOptions:[{value: "0", label: "停用"},{value: "1", label: "启用"}],
      // 遮罩层
      loading: false,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 设备表格数据
      equipmentList: [
        // { equipmentId: }
      ],
      // 弹出层标题
      title: "",
      captureTitle: "上传设备拍摄图片",
      // 是否显示弹出层
      open: false,
      captureOpen: false,
      // 状态数据字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        workStatus: undefined,
        weathershieldStatus: undefined,
        province: undefined,
        county: undefined,
        city: undefined
      },
      // queryLocationFlag: false,
      // 表单参数
      form: {},
      captureForm: {
        originalPictures: undefined,
        equipmentId: undefined,
        photoTime: undefined,
        photoArea: undefined
      },
      map: "",
      mk: "",
      // 表单校验
      rules: {
        workStatus: [
          { required: true, message: "设备工作状态不能为空", trigger: "blur" }
        ],
        weathershieldStatus: [
          { required: true, message: "挡雨板状态不能为空", trigger: "blur" }
        ],
        weather: [
          { required: true, message: "设备所在地气候不能为空", trigger: "blur" }
        ],
        electricity: [
          { required: true, message: "设备电量不能为空", trigger: "blur" }
        ],
        equipmentLocation: [
          { required: true, message: "设备地址不能为空", trigger: "change" }
        ],
        addressDetail: [
          { required: true, message: "设备详细地址不能为空", trigger: "blur" }
        ]
      },
      captureFormRules:{
        originalPictures: [
          { required: true, message: "图片列表不能为空，请至少选择一张图片", trigger: "blur" }
        ],
        photoTime: [
          { required: true, message: "请选择图片拍摄时间", trigger: "blur" }
        ]
      },
      location:"",
      myPoint: {},
      areaCode: "",
      dropdownSelected: false,
      resList:[],
      resArray: {},
      submitFormatForm:{
        // id: undefined,
        province: undefined,
        city: undefined,
        county: undefined,
        detail: undefined,
        latitude: undefined,
        longitude:undefined,
        workStatus: undefined,
        weather: undefined,
        weathershieldStatus: undefined,
        electricity: undefined,
      },
      // workStatusOptions:[{value: "0",label: "停用"},{value: "1",label: "正常"}],
      // weathershieldStatusOptions:[{value: "0",label: "停用"},{value: "1",label: "正常"}],
      tagType: '',
      equipmentLocationSelected: false,
      verifyEquipentLocation: {province: undefined, city: undefined, county: undefined},
      equipmentLocationChange: false,
      latestCounty: "",
    };
  },
  created(){
    this.getList();
  },
  computed: {
/*     getTag(row){
      if(row.workStatus === "0"){
        this.tagType = 'info';
        return '启用';
      }
      else{
        this.tagType = 'success';
        return '停用';
      }
    } */
  },
  methods: {
    getList(){
      this.loading = true;
      this.equipmentList = [];
      // console.log(this.queryParams);
      listAllEquipment(this.queryParams).then(response=>{
        // console.log(response);
        this.resList = response.rows
        this.total = response.total
        this.resList.forEach(ele => {
          let newEle = {}
          newEle.equipmentId =  ele.id
          newEle.province = ele.province
          newEle.city = ele.city
          newEle.county = ele.county
          newEle.equipmentLocation = ele.province + ele.city + ele.county
          newEle.addressDetail = ele.detail
          newEle.workStatus = ele.workStatus
          newEle.weather = ele.weather
          newEle.weathershieldStatus = ele.weathershieldStatus
          newEle.electricity = ele.electricity
          this.equipmentList.push(newEle)
        })
      })
      //console.log(this.equipmentList);
      this.loading = false;
    },
    getStatusTag(status){
      if(status === "0"){
        return '停用';
      }
      else{
        return '启用';
      }
    },
    getShieldTag(status){
      if(status === "0"){
        return '停用';
      }
      else{
        return '启用';
      }
    },
    querySelected(data){
      //console.log("选择了完整的地址！！");
      //console.log(data);
      // 需要三项都选择
      this.queryParams.province = data.province.value
      this.queryParams.city = data.city.value
      this.queryParams.county = data.area.value
    },
    //根据地址查询时，不需要选择完整的
    queryProvinceChange(data){
      //console.log(data);
      //console.log("provinceChange");
      this.queryParams.province = data.value
/*    console.log(this.queryParams.province);
      console.log(this.queryParams.city);
      console.log(this.queryParams.county);
      if(this.queryParams.province === undefined || this.queryParams.city === undefined || this.queryParams.county === undefined){
        console.log("其中一个是undefined");
        this.queryLocationFlag = false;
      } */

    },
    queryCityChange(data){
      // console.log("cityChange");
      this.queryParams.city = data.value
   /*    console.log(this.queryParams.city);
      console.log(this.queryParams.county);
      if(this.queryParams.county === "区" || this.queryParams.province === undefined || this.queryParams.city === undefined || this.queryParams.county === undefined){
        this.queryLocationFlag = false;
      } */
    },
    queryAreaChange(data){
      //console.log("areaChange");
      this.queryParams.county = data.value
      /* console.log(this.queryParams.county);
      if(this.queryParams.city === "市" || this.queryParams.county === "区" || this.queryParams.province === undefined || this.queryParams.city === undefined || this.queryParams.county === undefined){
        this.queryLocationFlag = false;
      } */
    },



    //新增设备时选择地址，必须要完整
    provinceChange(data){
      //console.log(data);
      //console.log("provinceChange");
      // this.form.addressDetail = undefined
      this.form.province = data.value
      //console.log(this.form.province);
      //console.log(this.form.city);
      //console.log(this.form.county);
      if(this.form.province === undefined || this.form.city === undefined || this.form.county === undefined){
        //console.log("其中一个是undefined");
        this.form.addressDetail = undefined
      }

    },
    cityChange(data){
      //console.log("cityChange");
      // this.form.addressDetail = undefined
      this.form.city = data.value
      // this.form.addressDetail = undefined
      //console.log(this.form.city);
      //console.log(this.form.county);
      if(this.form.county === "区" || this.form.province === undefined || this.form.city === undefined || this.form.county === undefined){
        this.form.addressDetail  = undefined
      }
    },
    areaChange(data){
      //console.log("areaChange");
      //console.log("latestCounty",this.latestCounty);
      // this.form.addressDetail = undefined
      this.form.county = data.value
      // this.form.addressDetail = undefined
      //console.log(this.form.county);
      if(this.form.city === "市" || this.form.county === "区" || this.form.county != this.latestCounty || this.form.province === undefined || this.form.city === undefined || this.form.county === undefined){
        this.form.addressDetail  = undefined
      }
      this.latestCounty = data.value
    },
    onSelected(data){
      //console.log("onSelectedChange");
      // //console.log(this.form.equipmentLocation);
      this.equipmentLocationSelected = true
      //console.log(this.equipmentLocationSelected);
      // let equipmentLocationChange = false
      // //console.log(data);
      // if(data.province.value === this.verifyEquipentLocation.province && data.city.value === this.verifyEquipentLocation.city && data.area.value === this.verifyEquipentLocation.county ){
      //   equipmentLocationChange = false
      // }else{
      //   equipmentLocationChange = true
      // }
      // //console.log("equipmentLocationChange:",equipmentLocationChange);
      // if(equipmentLocationChange===true){
      //   this.form.addressDetail = undefined
      // }

      this.location = data.province.value + data.city.value + data.area.value
      this.form.equipmentLocation = data.province.value + data.city.value + data.area.value
      this.areaCode = data.area.code
      //console.log(this.form.equipmentLocation);
      this.submitFormatForm.province = data.province.value
      this.submitFormatForm.city = data.city.value
      this.submitFormatForm.county = data.area.value

      //console.log(this.equipmentLocationSelected);
      
 /*      //console.log(this.areaCode);
      let point 
        this.$jsonp("http://api.map.baidu.com/geocoding/v3/", {
          address: this.form.equipmentLocation,
          output: "json",
          ak: "Y7szrgNy6ZKO4py71vIIfPVoP139Ge8M",
        })
          .then(json => {
            // 得到经纬度
            console.log(json);
            console.log(json.result.location);
            point = new BMap.Point(json.result.location.lng,json.result.location.lat)
            console.log(point);
            // this.map.panTo(point);
          })
          .catch((err) => {
            console.log(err);
          }); */

    },
    // 地址搜索提示
    getAddressList(str, callback){
      this.dropdownSelected = false
      //console.log(this.form.equipmentLocation);
      //console.log(this.equipmentLocationSelected);

      // if(this.form.equipmentLocation === "" || this.form.equipmentLocation === undefined || this.form.equipmentLocation === null){
      //   this.$message({
      //       type: "warning",
      //       message: "请输入设备地址！"
      //     })
      // }
      // if(this.form.province === undefined || this.form.city === undefined || this.form.county === undefined){
      //   console.log("其中一个是undefined");
      // }
      if(this.form.province === "省" || this.form.city === "市" || this.form.county === "区" || this.form.province === undefined || this.form.city === undefined || this.form.county === undefined){
        //console.log("其中一个是undefined");
        this.$message({
          type: "error",
          message: "请完整选择省市区！"
        })
        return false
      }
      if(this.equipmentLocationSelected === false || this.form.equipmentLocation === undefined){
          this.$message({
            type: "error",
            message: "请完整选择省市区！"
          })
          this.form.addressDetail = ""
          return false  
          // callback(null)    
      }
      else{
    this.$jsonp("http://api.map.baidu.com/place/v2/search/", {
            query: str,
            region: this.form.equipmentLocation,
            city_limit: true,
            extensions_adcode: true,
            scope: 2,
            page_size: 20,
            output: "json",
            ak: "Y7szrgNy6ZKO4py71vIIfPVoP139Ge8M",
          })
          .then(res => {
        //console.log("res:",res);
        let arr = res.results.filter( item => {
          return item.adcode === this.areaCode
        })
        //console.log(arr);
        if(arr.length!==0){
          callback(arr)
        }
        else{
          this.$message({
            type: "error",
            message: "未检索到，请检查详细地址输入是否正确！"
          })
        }
      }).catch( err => {
        //console.log(err);
        this.$message({
          type: "warning",
          message: err.statusText
        })
        // if(err.status === 408 && err.statusText === "Request Timeout"){
        //   this.$message({
        //     type: "warning",
        //     message: "网络不太顺畅，请重新检索！"
        //   })
        // }
      });
      }
      // this.$jsonp("http://api.map.baidu.com/place/v2/search/", {
      //       query: str,
      //       region: this.form.equipmentLocation,
      //       city_limit: true,
      //       extensions_adcode: true,
      //       scope: 2,
      //       page_size: 20,
      //       output: "json",
      //       ak: "Y7szrgNy6ZKO4py71vIIfPVoP139Ge8M",
      //     })
      //     .then(res => {
      //   console.log("res:",res);
      //   let arr = res.results.filter( item => {
      //     return item.adcode === this.areaCode
      //   })
      //   console.log(arr);
      //   if(arr.length!==0){
      //     callback(arr)
      //   }
      //   else{
      //     this.$message({
      //       type: "error",
      //       message: "未检索到，请检查详细地址输入是否正确！"
      //     })
      //   }
      // }).catch( err => {
      //   console.log(err);
      // });
    },
    // 选择地址
    handleSelect(item) {
      this.dropdownSelected = true
      this.form.addressDetail = item.address + item.name;
      this.form.addrPoint = item.location;//得到经纬度
      this.submitFormatForm.latitude = item.location.lat
      this.submitFormatForm.longitude = item.location.lng
      // this.map.clearOverlays();
      // this.mk = new BMap.Marker(item.location);
      // this.map.addOverlay(this.mk);
      // this.map.panTo(item.location);
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        // equipmentId: undefined,
        province: undefined,
        city: undefined,
        county: undefined,
        equipmentLocation: undefined,
        addressDetail: undefined,
        addrPoint: undefined,
        workStatus: undefined,
        weather: undefined,
        weathershieldStatus: undefined,
        electricity: undefined,
      };
      this.submitFormatForm = {
        // id: undefined,
        province: undefined,
        city: undefined,
        county: undefined,
        detail: undefined,
        latitude: undefined,
        longitude:undefined,
        workStatus: undefined,
        weather: undefined,
        weathershieldStatus: undefined,
        electricity: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      //console.log(this.queryParams);
      // if(this.queryParams.province === undefined|| this.queryParams.city === undefined || this.queryParams.county === undefined){
      //   this.$message({
      //     type: "warning",
      //     message: "请选择查询地址！"
      //   })
      // this.queryParams.province = undefined
      // this.queryParams.city = undefined
      // this.queryParams.county = undefined
      //   return false
      // }
      this.getList();    
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.province = undefined
      this.queryParams.city = undefined
      this.queryParams.county = undefined
      // this.handleQuery();
      this.queryParams.pageNum = 1;
      this.getList()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.equipmentId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备";
      this.latestCounty = ""
      //console.log(this.latestCounty);
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getEquipmentById(row.equipmentId).then(response => {
        //console.log(response);
        this.form.equipmentId =  response.data.id
        this.form.province = response.data.province
        this.form.city = response.data.city
        this.form.county = response.data.county

        this.verifyEquipentLocation.province = response.data.province
        this.verifyEquipentLocation.city = response.data.city
        this.verifyEquipentLocation.county = response.data.county
        this.latestCounty = response.data.county

        this.form.equipmentLocation = response.data.province + response.data.city + response.data.county
        this.form.addressDetail = response.data.detail
        this.form.workStatus = response.data.workStatus
        this.form.weather = response.data.weather
        this.form.weathershieldStatus = response.data.weathershieldStatus
        this.form.electricity = response.data.electricity
        //console.log(this.form);
      })
      this.dropdownSelected = true
      this.open = true;
      this.title = "修改设备信息";
    },
    /** 提交按钮 */
    submitForm(){
      //console.log(this.form);
      this.$refs["form"].validate(valid =>{
        if(valid){
          if(this.form.province === "省" || this.form.city === "市" || this.form.county === "区" || this.form.province === undefined || this.form.city === undefined || this.form.county === undefined){
            //console.log("其中一个是undefined");
            this.$message({
              type: "error",
              message: "请完整选择省市区！"
            })
            return false
          }
          // 表示是从下拉框里选中的
          if(this.dropdownSelected === false){
            this.$message({
              type: 'error',
              message: '请从推荐地址中选择详细地址!'
            })
          }
          else{
            // alert(this.form.equipmentId+this.form.equipmentLocation+this.form.addrPoint.lat+this.form.addrPoint.lng)
            //console.log(this.form);
            this.submitFormatForm.id = this.form.equipmentId
            this.submitFormatForm.detail = this.form.addressDetail
            this.submitFormatForm.workStatus = this.form.workStatus
            this.submitFormatForm.weather = this.form.weather
            this.submitFormatForm.weathershieldStatus = this.form.weathershieldStatus
            this.submitFormatForm.electricity = this.form.electricity
            //console.log(this.submitFormatForm);
            //console.log(this.submitFormatForm.id);
            if (this.submitFormatForm.id != undefined) {
              updateEquipment(this.submitFormatForm).then(response => {
                //console.log(response);
                this.msgSuccess("修改成功");
                this.open = false;
                // this.getList();
                this.resetQuery();
                this.verifyEquipentLocation = {province: undefined, city: undefined, county: undefined}
              })
            }
            else{
              addEquipment(this.submitFormatForm).then(response => {
              //console.log(response);
              this.msgSuccess("新增成功");
              this.open = false;
              // this.getList();
              this.resetQuery()
            })
            }
          }
        }
        })
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const equipmentIds = row.equipmentId || this.ids;
      this.$confirm('是否确认删除设备编号为"' + equipmentIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delEquipment(equipmentIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有设备数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportEquipment(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    },
    // 上传设备拍摄图片
    handleUploadImgs(row){
      this.resetCaptureForm();
      //console.log(row);
      //console.log(row.equipmentId);
      this.captureOpen = true;
      this.captureForm.equipmentId = row.equipmentId
      // this.captureForm.photoArea  = row.equipmentLocation
      this.captureForm.photoArea = row.province + "-" + row.city + "-" + row.county
      // this.captureForm.photoTime = dateUtil.format(new Date(), 'yyyy-MM-dd hh:mm:00')
      //console.log(this.captureForm.photoTime);
    },
    submitCaptureForm(){
      //console.log(this.captureForm);
      if(this.captureForm.originalPictures.length === 0 ||this.captureForm.originalPictures === undefined || this.captureForm.originalPictures === null || this.captureForm.originalPictures === ""){
        this.$message.error("请至少选择一张图片!")
      }
      else if(this.captureForm.photoTime === undefined || this.captureForm.photoTime === null || this.captureForm.photoTime === ""){
        this.$message.error('请选择图片拍摄时间！')
      }
      else{
/*         importCapture(this.captureForm).then(response => {
          console.log(response);
          this.msgSuccess("上传成功");
          this.captureOpen = false
        }) */
            const formData = new FormData()
            // 因为要传一个文件数组过去，所以要循环append
            this.captureForm.originalPictures.forEach(item => {
              //console.log(item);
              formData.append('file', item)
            })
            formData.append('equipmentId', this.captureForm.equipmentId)
            formData.append('photoArea', this.captureForm.photoArea)
            formData.append('photoTime', this.captureForm.photoTime)
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
            let responseList = []
            // formData.delete('file')
            request({
                method: 'POST',
                url: '/insectdata/data/upLoadAndInsertData',   //填写自己的接口
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
              res.forEach(item => {
                // responseList.push({ name: item.fileName, url: 'http://81.71.138.29:59999'+item.fileName })
                responseList.push({ name: item.fileName, url: item.fileName })
              })
              // console.log("responseList");
              // console.log(responseList);
              this.$message.success('上传成功！')
              this.captureOpen = false
                })
              }
    },
    resetCaptureForm(){
      this.captureForm = {
        originalPictures: undefined,
        equipmentId: undefined,
        photoTime: undefined,
        photoArea: undefined
      }
    },
    cancelUpload(){
      this.captureOpen= false
    },
/*     resetCaptureForm(){
      this.captureForm = {
        equipmentId = undefined,
        originalPicture = undefined,
        photoTime = undefined
      }
    } */
  }
}
</script>
<!-- 样式 -->

<style>

.captureUploadDialog .el-upload-list--picture .el-upload-list__item{
  background-color: #fff;
}
.captureUploadDialog .el-upload-list__item:nth-child(n+11){
  display: none;
}
</style>