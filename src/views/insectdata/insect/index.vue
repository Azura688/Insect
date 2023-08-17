<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <!-- 昆虫管理 -->
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAddOrder"
        >新增昆虫目</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAddFamily"
        >新增昆虫科</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAddSpecies"
        >新增昆虫种</el-button>
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
    </el-row>

    <el-table
      v-loading="loading"
      :data="insectList"
      row-key="insectId"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="parentName" label="上一级昆虫" ></el-table-column>
      <!-- <el-table-column prop="insectId" label="昆虫id" width="200"></el-table-column> -->
      <!-- <el-table-column prop="type" label="type" width="200"></el-table-column> -->
      <el-table-column prop="insectName" label="昆虫名称"></el-table-column>
      <!-- <el-table-column prop="parentName" label="上一级昆虫" width="180"></el-table-column> -->
      <!-- <el-table-column prop="orderNum" label="排序" align="center"></el-table-column> -->
      <el-table-column label="创建时间" align="center" prop="createTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.type!=='0'"
            size="mini" 
            type="text" 
            icon="el-icon-edit" 
            @click="handleUpdateBySelectType(scope.row)"
          >修改</el-button>
          <el-button v-else
            size="mini" 
            type="text" 
            icon="el-icon-edit" 
            @click="handleUpdateOrder(scope.row)"
          >修改</el-button>
          <el-button 
            v-if="scope.row.type != '2'"
            size="mini" 
            type="text" 
            icon="el-icon-plus" 
            @click="handleAddBySelectType(scope.row)"
          >新增</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 新增昆虫目 -->
    <el-dialog :title="addOrderTitle" :visible.sync="openAddOrder" width="600px" append-to-body>
      <el-form ref="addOrderForm" :model="addOrderForm" :rules="addOrderRules" label-width="80px">
        <el-row>
          <el-col :span="18">
            <el-form-item label="昆虫目名称" prop="insectName" label-width="100px">
              <el-input v-model="addOrderForm.insectName" placeholder="请输入昆虫名称" />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="addOrderForm.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>          -->
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAddOrderForm">确 定</el-button>
        <el-button @click="cancelAddOrder">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 新增昆虫科 -->
    <el-dialog :title="addFamilyTitle" :visible.sync="openAddFamily" width="600px" append-to-body>
      <el-form ref="addFamilyForm" :model="addFamilyForm" :rules="addFamilyRules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="上级种类" prop="parentId">
              <el-select v-model="addFamilyForm.parentId" placeholder="请选择目种类">
                <el-option v-for="item in this.orderOptions" :label="item.label" :value="item.value" :key="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="昆虫科名称" prop="insectName" label-width="100px">
              <el-input v-model="addFamilyForm.insectName" placeholder="请输入昆虫名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-row>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="addFamilyForm.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>  
        </el-row> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAddFamilyForm">确 定</el-button>
        <el-button @click="cancelAddFamily">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 新增昆虫种 -->
    <el-dialog :title="addSpeciesTitle" :visible.sync="openAddSpecies" width="600px" append-to-body>
      <el-form ref="addSpeciesForm" :model="addSpeciesForm" :rules="addSpeciesRules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="上级种类" prop="parentId">
              <el-select v-model="addSpeciesForm.parentId" placeholder="请选择科种类">
                <el-option v-for="item in this.familyOptions" :label="item.label" :value="item.value" :key="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="昆虫种名称" prop="insectName" label-width="100px">
              <el-input v-model="addSpeciesForm.insectName" placeholder="请输入昆虫名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-row>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="addSpeciesForm.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>  
        </el-row> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAddSpeciesForm">确 定</el-button>
        <el-button @click="cancelAddSpecies">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { listInsect,  getInsect, delInsect, addInsect, updateInsect, exportInsect } from "@/api/insectApi/insect";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import VDistpicker from 'v-distpicker'
export default {
  components: { Treeselect, VDistpicker },
  data() {
    return {
      // 遮罩层
      loading: false,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 弹出层标题
      addOrderTitle: "",
      addFamilyTitle: "",
      addSpeciesTitle: "",
      // 是否显示弹出层
      openAddOrder: false,
      openAddFamily: false,
      openAddSpecies: false,
      // 查询参数
      queryParams: {
        insectName: undefined,
      },
      // 表单参数
      addOrderForm: {},
      addFamilyForm: {},
      addSpeciesForm: {},
      // 表单校验
      addOrderRules: {
        insectName: [ { required: true, message: "请输入昆虫名称", trigger: "blur" }]
      },
      addFamilyRules: {
        parentId: [ { required: true, message: "请选择上级昆虫", trigger: "blur" }],
        insectName: [ { required: true, message: "请输入昆虫名称", trigger: "blur" }],
      },
      addSpeciesRules: {
        parentId: [ { required: true, message: "请选择上级昆虫", trigger: "blur" }],
        insectName: [ { required: true, message: "请输入昆虫名称", trigger: "blur" }],
      },
      orderOptions: [],
      familyOptions: [],
      speciesOptions: [],
      insectOptions:[],
      insectList: [],
      allInsect: [],
      typeOptions: [{value: "0",label: "目"},{value: "1",label: "科"},{value: "2",label: "种"},],
      typeForm: {},
      typeList: ["0", "1", "2"]
    }
  },
  created(){
    this.getList();
  },
  methods: {
    getList(){
      listInsect(this.queryParams).then(response=>{
        //console.log(response);
        // 转换成树形结构
        this.allInsect = response.data;
        let newItem = {};
        this.orderOptions = [],
        this.familyOptions = [],
        this.speciesOptions = [];
        this.allInsect.forEach( item => {
          newItem = {};
          if(item.type === '0'){
            newItem.label = item.insectName
            newItem.value = item.insectId
            this.orderOptions.push(newItem)
          }
          else if(item.type === '1'){
            newItem.label = item.insectName
            newItem.value = item.insectId
            this.familyOptions.push(newItem)           
          }
          else{
            newItem.label = item.insectName
            newItem.value = item.insectId
            this.speciesOptions.push(newItem)
          }
        })
        this.allInsect.forEach( item => {
          //console.log(typeof(item.parentId));
          if(item.parentId === 0){
            item.parentName = "------"
          }
          else{
            for(let i=0;i<this.allInsect.length;i++){
              if(item.parentId === this.allInsect[i].insectId){
                item.parentName = this.allInsect[i].insectName
                break
              }
            }
          }
        })
        this.insectList = this.handleTree(response.data, "insectId");
        //console.log(this.insectList);
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
    handleAddBySelectType(row){
      if(row.type === '0'){
        this.handleAddFamily(row)
      }
      else if(row.type === '1'){
        this.handleAddSpecies(row)
      }
    },
    handleUpdateBySelectType(row){
      if(row.type === '1'){
        this.handleUpdateFamily(row)
      }
      else if(row.type === '2'){
        this.handleUpdateSpecies(row)
      }
    },
    // 表单重置
    resetAddOrderForm(){
      this.addOrderForm = {
        insectName: undefined,
        // orderNum: undefined,
      };
      this.resetForm("addOrderForm");
    },

    resetAddFamilyForm(){
      this.addFamilyForm = {
        parentId: undefined,
        insectName: undefined,
        // orderNum: undefined
      };
      this.resetForm("addFamilyForm");
    },

    resetAddSpeciesForm(){
      this.addSpeciesForm = {
        parentId: undefined,
        insectName: undefined,
        // orderNum: undefined,
      };
      this.resetForm("addSpeciesForm");
    },

    // handleAdd(row){
    //   this.reset();
    //   if (row != undefined) {
    //     this.form.parentId = row.insectId;
    //   }
    //   this.open = true
    //   this.addFamilyTitle = "新增昆虫种类"
    //   listInsect(this.queryParams).then(response=>{
    //     console.log(response);
    //     this.allInsect = response.data;
    //     let newItem = {};
    //     this.allInsect.forEach( item => {
    //       newItem = {};
    //       if(item.type === '0'){
    //         newItem.label = item.insectName
    //         newItem.value = item.insectId
    //         this.orderOptions.push(newItem)
    //       }
    //       else if(item.type === '1'){
    //         newItem.label = item.insectName
    //         newItem.value = item.insectId
    //         this.familyOptions.push(newItem)           
    //       }
    //       else{
    //         newItem.label = item.insectName
    //         newItem.value = item.insectId
    //         this.speciesOptions.push(newItem)
    //       }
    //     })
    //     // 转换成树形结构
    //     this.insectOptions = this.handleTree(response.data, "insectId");
    //     console.log(this.insectOptions);
    //   })
    // },

// 新增目
    handleAddOrder(){
      this.resetAddOrderForm();
      this.openAddOrder = true
      this.addOrderTitle = "新增昆虫目"
    },
// 更新目
    handleUpdateOrder(row){
      this.resetAddOrderForm();
      getInsect(row.insectId).then(response=>{
        //console.log(response);
        this.addOrderForm= response.data
        this.openAddOrder = true
        this.addOrderTitle = "修改昆虫目信息"
      })
    },
// 提交目表单
    submitAddOrderForm(){
      this.$refs["addOrderForm"].validate(valid => {
        if (valid) {
          //console.log(this.addOrderForm);
          ////console.log(this.addOrderForm.insectId);
          if (this.addOrderForm.insectId != undefined) {
            updateInsect(this.addOrderForm).then(response => {
              if(response.code===200&&response.msg==="操作成功"){
                this.msgSuccess("修改成功");
                this.openAddOrder = false;
                this.getList();
              }
            });
          } else {
            this.addOrderForm.parentName = "-----";
            this.addOrderForm.parentId = 0;
            this.addOrderForm.type = "0";
            addInsect(this.addOrderForm).then(response => {
              if(response.code===200&&response.msg==="操作成功"){ 
                this.msgSuccess("新增成功");
                this.openAddOrder = false;
                this.getList();
              }
            });
          }
        }
      });
    },
// 关闭新增目对话框
    cancelAddOrder(){
      this.openAddOrder = false
    },

// 新增科
    handleAddFamily(row){
      this.resetAddFamilyForm();
      if (row != undefined) {
        this.addFamilyForm.parentId = row.insectId;
      }
      this.openAddFamily = true
      this.addFamilyTitle = "新增昆虫科"
    },
// 更新科
    handleUpdateFamily(row){
      this.resetAddFamilyForm();
      getInsect(row.insectId).then(response=>{
        //console.log(response);
        this.addFamilyForm= response.data
        this.openAddFamily = true
        this.addFamilyTitle = "修改昆虫科信息"
      })
    },
// 提交科表单
    submitAddFamilyForm(){
      this.$refs["addFamilyForm"].validate(valid => {
        if (valid) {
          //console.log(this.addFamilyForm);
          //console.log(this.addFamilyForm.insectId);
          this.addFamilyForm.type = '1';
          if (this.addFamilyForm.insectId != undefined) {
            updateInsect(this.addFamilyForm).then(response => {
              if(response.code===200&&response.msg==="操作成功"){
                this.msgSuccess("修改成功");
                this.openAddFamily = false;
                this.getList();
              }
            });
          } else {
            addInsect(this.addFamilyForm).then(response => {
              if(response.code===200&&response.msg==="操作成功"){ 
                this.msgSuccess("新增成功");
                this.openAddFamily = false;
                this.getList();
              }
            });
          }
        }
      });
    },
// 关闭新增科对话框
    cancelAddFamily(){
      this.openAddFamily = false
    },

// 新增种
    handleAddSpecies(row){
      this.resetAddSpeciesForm();
      if (row != undefined) {
        this.addSpeciesForm.parentId = row.insectId;
      }
      this.openAddSpecies = true
      this.addSpeciesTitle = "新增昆虫种"
    },
// 更新种
    handleUpdateSpecies(row){
      this.resetAddSpeciesForm();
      getInsect(row.insectId).then(response=>{
        //console.log(response);
        this.addSpeciesForm= response.data
        this.openAddSpecies = true
        this.addSpeciesTitle = "修改昆虫种信息"
      })
    },
// 提交种表单
    submitAddSpeciesForm(){
      this.$refs["addSpeciesForm"].validate(valid => {
        if (valid) {
          //console.log(this.addSpeciesForm);
          //console.log(this.addSpeciesForm.insectId);
          this.addSpeciesForm.type = '2';
          if (this.addSpeciesForm.insectId != undefined) {
            updateInsect(this.addSpeciesForm).then(response => {
              if(response.code===200&&response.msg==="操作成功"){
                this.msgSuccess("修改成功");
                this.openAddSpecies = false;
                this.getList();
              }
            });
          } else {
            addInsect(this.addSpeciesForm).then(response => {
              if(response.code===200&&response.msg==="操作成功"){ 
                this.msgSuccess("新增成功");
                this.openAddSpecies = false;
                this.getList();
              }
            });
          }
        }
      });
    },
// 关闭新增种对话框
    cancelAddSpecies(){
      this.openAddSpecies = false
    },

    handleDelete(row){
      /* delInsect(row.insectId).then(response=>{
        console.log(response);
        if(response.code===200&&response.msg==="操作成功"){
          this.msgSuccess("删除成功");
          this.getList();
        }
      }) */
      this.$confirm('是否确认删除名称为"' + row.insectName + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delInsect(row.insectId);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
  /** 导出按钮操作 */
    handleExport() {
      this.$confirm('是否确认导出所有昆虫数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportInsect();
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
}
</script>