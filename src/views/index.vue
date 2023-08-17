<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>HZAU昆虫管理系统</h2>
        <p>
          
        </p>
        <p>
          <b>当前版本:</b> <span>v{{ version }}</span>
        </p>
        <p>
          <el-tag type="danger">&yen;开发阶段</el-tag>
        </p>
        <p>
          <el-button
            size="mini"
            icon="el-icon-s-home"
            plain
            @click="goTarget('http://81.71.138.29:20002/')"
            >访问主页</el-button
          >
        </p>
      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>技术选型</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>后端技术</h4>
            <ul>
              <li>SpringBoot</li>
              <li>Spring Security</li>
              <li>JWT</li>
              <li>MyBatis</li>
              <li>Druid</li>
              <li>Fastjson</li>
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>前端技术</h4>
            <ul>
              <li>Vue</li>
              <li>Vuex</li>
              <li>Element-ui</li>
              <li>Axios</li>
              <li>Sass</li>
              <li>Quill</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>联系信息</span>
          </div>
          <div class="body">
            <p>
              <i class="el-icon-s-promotion"></i> 官网：<el-link
                href="http://81.71.138.29:20002/"
                target="_blank"
                >http://81.71.138.29:20002/</el-link
              >
            </p>
            <p>
              <i class="el-icon-s-check"></i> 项目负责人：<a
                href="javascript:;"
                >---</a
              >
            </p>
            <p>
              <i class="el-icon-s-opportunity"></i> 项目开发人员：<a
                href="javascript:;"
                class="支付宝信息"
                >------</a
              >
            </p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>更新日志</span>
          </div>
          <el-collapse accordion>
            <!-- <el-collapse-item title="v3.6.0 - 2021-07-12">
              <ol>
                <li>角色管理新增分配用户功能</li>
                <li>用户管理新增分配角色功能</li>
                <li>日志列表支持排序操作</li>
                <li>优化参数&字典缓存操作</li>
                <li>系统布局配置支持动态标题开关</li>
                <li>菜单路由配置支持内链访问</li>
                <li>默认访问后端首页新增提示语</li>
                <li>富文本默认上传返回url类型</li>
                <li>新增自定义弹窗拖拽指令</li>
                <li>全局注册常用通用组件</li>
                <li>全局挂载字典标签组件</li>
                <li>ImageUpload组件支持多图片上传</li>
                <li>FileUpload组件支持多文件上传</li>
                <li>文件上传组件添加数量限制属性</li>
                <li>富文本编辑组件添加类型属性</li>
                <li>富文本组件工具栏配置视频</li>
                <li>封装通用iframe组件</li>
                <li>限制超级管理员不允许操作</li>
                <li>用户信息长度校验限制</li>
                <li>分页组件新增pagerCount属性</li>
                <li>添加bat脚本执行应用</li>
                <li>升级oshi到最新版本v5.7.4</li>
                <li>升级element-ui到最新版本2.15.2</li>
                <li>升级pagehelper到最新版1.3.1</li>
                <li>升级commons.io到最新版本v2.10.0</li>
                <li>升级commons.fileupload到最新版本v1.4</li>
                <li>升级swagger到最新版本v3.0.0</li>
                <li>修复关闭confirm提示框控制台报错问题</li>
                <li>修复存在的SQL注入漏洞问题</li>
                <li>定时任务屏蔽rmi远程调用</li>
                <li>修复用户搜索分页变量错误</li>
                <li>修复导出角色数据范围翻译缺少仅本人</li>
                <li>修复表单构建选择下拉选择控制台报错问题</li>
                <li>优化图片工具类读取文件</li>
                <li>其他细节优化</li>
              </ol>
            </el-collapse-item> -->
            <el-collapse-item title="v1.0.0 - 2022-1-30">
              <ol>
                <li>昆虫管理系统正式发布</li>
              </ol>
            </el-collapse-item>
            <el-collapse-item title="v1.0.1 - 2022-2-21">
              <ol>
                <li>将post和delete请求改为post请求</li>
              </ol>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
      <!-- <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
        </el-card>
      </el-col> -->
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "1.0.0",
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
  },
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: 'Times New Roman', Microsoft YaHei, "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

