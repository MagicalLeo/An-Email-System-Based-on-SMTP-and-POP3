<template>
  <div class="hello">
    <div class="header">
      <i
        class="el-icon-arrow-left"
        style="font-size: 20px; margin-left:20px"
        @click="touser"
      ></i>
      <div style="font-size: 16px;margin-right:172px">添加用户</div>
    </div>
    <el-divider></el-divider>
    <div class="form">
      <el-form ref="form" :model="form" :rules="formRules">
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="邮箱账号名，例：test@mymail.com（必填）"
          >
          </el-input>
        </el-form-item>
        <el-form-item prop="password" style="margin-top:25px">
          <el-input
            v-model="form.password"
            placeholder="初始密码（必填）"
          ></el-input>
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input v-model="form.nickname" placeholder="昵称（必填）">
          </el-input>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="form.phone" placeholder="电话（选填）"> </el-input>
        </el-form-item>
        <el-row
          ><el-col :span="11"
            ><el-form-item prop="sex">
              <el-select v-model="form.sex" placeholder="性别（可选）">
                <el-option
                  v-for="item in sexOption"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select> </el-form-item></el-col
          ><el-col :span="13"
            ><el-form-item prop="roleId">
              <el-select
                v-model="form.roleId"
                placeholder="用户权限（必选）"
                style="margin-left:30px"
              >
                <el-option
                  v-for="item in roleOption"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select> </el-form-item></el-col
        ></el-row>
        <el-form-item>
          <el-button
            style="width: 100%;margin-top:35px"
            type="primary"
            @click="addUser"
            >添加</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import Rules from "../utils/rules";
export default {
  name: "AddUser",
  data() {
    return {
      tableData: [],
      form: {
        username: "",
        password: "",
        sex: null,
        nickname: null,
        roleId: null,
        phone: null
      },
      sexOption: [
        { name: "男", id: 0 },
        { name: "女", id: 1 }
      ],
      roleOption: [
        { name: "管理员", id: 1 },
        { name: "普通用户", id: 2 }
      ],
      formRules: {
        username: [
          {
            validator: Rules.checkIllegal2("请输入正确的邮箱地址"),
            trigger: "blur"
          }
        ],
        password: [
          { required: true, message: "请输入初始密码", trigger: "blur" }
        ],
        nickname: [{ required: true, message: "请输入昵称", trigger: "blur" }],
        roleId: [{ required: true, message: "请选择用户权限", trigger: "blur" }]
      }
    };
  },
  mounted() {},
  methods: {
    handleClick(row, column, event) {
      var name = row.name;
      if (name == "【管理员】用户管理") {
        this.$router.push("/UserManage");
      }
    },
    addUser() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          var newform = {
            username: this.form.username,
            password: this.form.password,
            nickname: this.form.nickname,
            roleId: this.form.roleId
          };
          if (this.form.phone != null && this.form.phone != "") {
            newform.phone = this.form.phone;
          }
          if (this.form.sex != null && this.form.sex != "") {
            newform.sex = this.form.sex;
          }
          this.$axios
            .post("/api/user", { ...newform })
            .then(response => {
              if (response.data.code === 0) {
                this.$message({
                  message: "添加成功！",
                  type: "success",
                  duration: 800
                });
                this.$router.push("/UserManage");
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error"
                });
              }
            });
        } else {
          return false;
        }
      });
    },
    touser() {
      this.$router.push("/UserManage");
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.header {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
}
.el-divider {
  margin-bottom: 0px;
}
.form {
  margin: 20px;
  margin-top: 30px;
}
</style>
<style>
.mybox {
  width: 250px;
  position: absolute;
  left: 72px;
  top: 200px;
}
</style>
