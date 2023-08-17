<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="用户昵称" prop="nickName">
      <el-input v-model="user.nickName" maxlength="30" />
    </el-form-item> 
    <el-form-item label="手机号码" prop="phonenumber">
      <el-input v-model="user.phonenumber" maxlength="11" />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="user.email" maxlength="50" />
    </el-form-item>
    <el-form-item label="单位地址" prop="unitAddress" v-model="user.unitAddress">
      <el-input disabled v-model="user.unitAddress"></el-input>
        <!-- <el-button type="primary" plain @click="modifyAddress()" v-if="!notAdmin">修改单位地址</el-button>
        <el-button type="primary" plain @click="cancelModify()" v-if="!notAdmin">取消</el-button>
        <v-distpicker v-if="isModify"
          @selected="onSelected">
        </v-distpicker> -->
    </el-form-item>
    <el-form-item>
      <el-button type="primary" plain @click="modifyAddress()" v-if="!notAdmin">修改单位地址</el-button>
      <el-button type="primary" plain @click="cancelModify()" v-if="!notAdmin">取消</el-button>
      <v-distpicker v-if="isModify"
        @selected="onSelected">
      </v-distpicker>
    </el-form-item>
    <el-form-item label="单位名称" prop="unitName">
      <el-input :disabled="notAdmin" v-model="user.unitName"></el-input>
    </el-form-item>
 <!--    <el-form-item label="性别">
      <el-radio-group v-model="user.sex">
        <el-radio label="0">男</el-radio>
        <el-radio label="1">女</el-radio>
      </el-radio-group>
    </el-form-item> -->
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile, getUserProfile } from "@/api/system/user";
import VDistpicker from 'v-distpicker'
export default {
  components: { VDistpicker },
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      isModify: false,
            // 表单校验
      rules: {
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        email: [
          { required: true, message: "邮箱地址不能为空", trigger: "blur" },
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
        unitAddress: [
          { required: true, message: "单位地址不能为空", trigger: "blur"}
        ],
        unitName: [
          { required: true, message: "单位名称不能为空", trigger: "blur"}
        ]
      }
    };
  },
  computed:{
    notAdmin(){
      return this.user.admin === false
      }
  },
  methods: {
/*     onSelect(data){
      this.user.unitAddress = data.province.value + data.city.value + data.area.value
    }, */
    submit() {
      this.$refs["form"].validate(valid => {
        // console.log(this.user);
        if (valid) {
          updateUserProfile(this.user).then(response => {
            this.msgSuccess("修改成功");
          });
        }
      });
    },
    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/index" });
    },
    modifyAddress(){
      this.isModify = true;
    },
    onSelected(data){
      // console.log(data);
      this.user.unitAddress = data.province.value + "-"  + data.city.value + "-"  + data.area.value
    },
    cancelModify(){
      this.isModify = false
      getUserProfile().then(response => {
        // console.log(response.data.unitAddress);
        this.user.unitAddress = response.data.unitAddress
      })
    }
  }
};
</script>
