<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="父昆虫ID" prop="parentId">
        <el-input
          v-model="queryParams.parentId"
          placeholder="请输入父昆虫ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="祖级列表" prop="ancestors">
        <el-input
          v-model="queryParams.ancestors"
          placeholder="请输入祖级列表"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="昆虫名称" prop="insectName">
        <el-input
          v-model="queryParams.insectName"
          placeholder="请输入昆虫名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="显示顺序" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入显示顺序"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类别： 0 目 1 科 2 种" prop="type">
        <el-input
          v-model="queryParams.type"
          placeholder="请输入类别： 0 目 1 科 2 种"
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
          v-hasPermi="['insectdata:insect:add']"
        >新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="insectList"
      row-key="insectId"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="父昆虫ID" prop="parentId" />
      <el-table-column label="祖级列表" align="center" prop="ancestors" />
      <el-table-column label="昆虫名称" align="center" prop="insectName" />
      <el-table-column label="显示顺序" align="center" prop="orderNum" />
      <el-table-column label="类别： 0 目 1 科 2 种" align="center" prop="type" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['insectdata:insect:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['insectdata:insect:add']"
          >新增</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['insectdata:insect:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改昆虫对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="父昆虫ID" prop="parentId">
          <treeselect v-model="form.parentId" :options="insectOptions" :normalizer="normalizer" placeholder="请选择父昆虫ID" />
        </el-form-item>
        <el-form-item label="昆虫名称" prop="insectName">
          <el-input v-model="form.insectName" placeholder="请输入昆虫名称" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入显示顺序" />
        </el-form-item>
        <el-form-item label="类别： 0 目 1 科 2 种" prop="type">
          <el-input v-model="form.type" placeholder="请输入类别： 0 目 1 科 2 种" />
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
import { listInsect, getInsect, delInsect, addInsect, updateInsect, exportInsect } from "@/api/insectdata/insect";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Insect",
  components: {
    Treeselect
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 昆虫表格数据
      insectList: [],
      // 昆虫树选项
      insectOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        parentId: null,
        ancestors: null,
        insectName: null,
        orderNum: null,
        type: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        insectName: [
          { required: true, message: "昆虫名称不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "类别： 0 目 1 科 2 种不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询昆虫列表 */
    getList() {
      this.loading = true;
      listInsect(this.queryParams).then(response => {
        this.insectList = this.handleTree(response.data, "insectId", "parentId");
        this.loading = false;
      });
    },
    /** 转换昆虫数据结构 */
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
	/** 查询昆虫下拉树结构 */
    getTreeselect() {
      listInsect().then(response => {
        this.insectOptions = [];
        const data = { insectId: 0, insectName: '顶级节点', children: [] };
        data.children = this.handleTree(response.data, "insectId", "parentId");
        this.insectOptions.push(data);
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
        insectId: null,
        parentId: null,
        ancestors: null,
        insectName: null,
        orderNum: null,
        type: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.getTreeselect();
      if (row != null && row.insectId) {
        this.form.parentId = row.insectId;
      } else {
        this.form.parentId = 0;
      }
      this.open = true;
      this.title = "添加昆虫";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      if (row != null) {
        this.form.parentId = row.insectId;
      }
      getInsect(row.insectId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改昆虫";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.insectId != null) {
            updateInsect(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInsect(this.form).then(response => {
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
      this.$confirm('是否确认删除昆虫编号为"' + row.insectId + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delInsect(row.insectId);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    }
  }
};
</script>
