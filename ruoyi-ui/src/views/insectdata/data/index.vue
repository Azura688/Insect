<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="拍摄时间" prop="photoTime">
        <el-date-picker clearable size="small"
          v-model="queryParams.photoTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择拍摄时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="拍摄地点" prop="photoArea">
        <el-input
          v-model="queryParams.photoArea"
          placeholder="请输入拍摄地点"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备号" prop="equipmentId">
        <el-input
          v-model="queryParams.equipmentId"
          placeholder="请输入设备号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['insectdata:data:add']"
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
          v-hasPermi="['insectdata:data:edit']"
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
          v-hasPermi="['insectdata:data:remove']"
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
          v-hasPermi="['insectdata:data:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="数据ID" align="center" prop="dataId" />
      <el-table-column label="原始图片" align="center" prop="originalPicture" />
      <el-table-column label="标记图片" align="center" prop="tagPicture" />
      <el-table-column label="拍摄时间" align="center" prop="photoTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.photoTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="拍摄地点" align="center" prop="photoArea" />
      <el-table-column label="设备号" align="center" prop="equipmentId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['insectdata:data:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['insectdata:data:remove']"
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

    <!-- 添加或修改识别数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="原始图片">
          <imageUpload v-model="form.originalPicture"/>
        </el-form-item>
        <el-form-item label="标记图片">
          <imageUpload v-model="form.tagPicture"/>
        </el-form-item>
        <el-form-item label="拍摄时间" prop="photoTime">
          <el-date-picker clearable size="small"
            v-model="form.photoTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择拍摄时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="拍摄地点" prop="photoArea">
          <el-input v-model="form.photoArea" placeholder="请输入拍摄地点" />
        </el-form-item>
        <el-form-item label="设备号" prop="equipmentId">
          <el-input v-model="form.equipmentId" placeholder="请输入设备号" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listData, getData, delData, addData, updateData, exportData } from "@/api/insectdata/data";

export default {
  name: "Data",
  data() {
    return {
      // 遮罩层
      loading: true,
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
      // 识别数据表格数据
      dataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        originalPicture: null,
        tagPicture: null,
        photoTime: null,
        photoArea: null,
        equipmentId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        originalPicture: [
          { required: true, message: "原始图片不能为空", trigger: "blur" }
        ],
        photoTime: [
          { required: true, message: "拍摄时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询识别数据列表 */
    getList() {
      this.loading = true;
      listData(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        dataId: null,
        originalPicture: null,
        tagPicture: null,
        photoTime: null,
        photoArea: null,
        equipmentId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.dataId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加识别数据";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const dataId = row.dataId || this.ids
      getData(dataId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改识别数据";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.dataId != null) {
            updateData(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addData(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const dataIds = row.dataId || this.ids;
      this.$confirm('是否确认删除识别数据编号为"' + dataIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delData(dataIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有识别数据数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportData(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
