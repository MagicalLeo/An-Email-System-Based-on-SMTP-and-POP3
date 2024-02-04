<template>
  <div class="topper">
    <div class="background"></div>
    <div class="header">
      <i
        class="el-icon-arrow-left"
        style="font-size: 20px; margin-left:20px"
        @click="touser"
      ></i>
      <div style="font-size: 16px;margin-right:172px">Edit User</div>
    </div>
    <el-divider></el-divider>
    <div class="form">
      <el-form ref="form" :model="form" :rules="formRules">
        <el-form-item prop="username" label="账号：">
          <div class="myinput">
            <el-input
              v-model="form.username"
              placeholder="邮箱账号名，例：test"
              style="width:260px"
            >
            </el-input>
          </div>
        </el-form-item>
        <el-form-item prop="nickname" style="margin-top:20px" label="昵称：">
          <el-input
            v-model="form.nickname"
            placeholder="昵称（选填）"
            style="width:260px"
          ></el-input>
        </el-form-item>
        <el-form-item prop="phone" style="margin-top:20px" label="电话：">
          <el-input
            v-model="form.phone"
            placeholder="电话（选填）"
            style="width:260px"
          ></el-input>
        </el-form-item>
        <el-form-item prop="sex" style="margin-top:20px" label="性别：">
          <el-select
            v-model="form.sex"
            placeholder="请选择性别（选填）"
            style="width:260px"
          >
            <el-option
              v-for="item in sexOption"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="role.id" style="margin-top:20px" label="权限：">
          <el-select
            v-model="form.role.id"
            placeholder="请选择用户权限"
            style="width:260px"
          >
            <el-option
              v-for="item in roleOption"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="disabled" style="margin-top:20px" label="状态：">
          <el-select
            v-model="form.disabled"
            placeholder="请选择用户状态"
            style="width:260px"
          >
            <el-option
              v-for="item in statusOption"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            style="width: 98%;margin-top:5px;margin-right:15px"
            type="primary"
            @click="editUser"
            class="butt"
            >修改</el-button
          >
        </el-form-item>
        <el-form-item>
          <el-button
            style="width: 98%;margin-top:5px;margin-right:15px"
            type="danger"
            @click="deleteUser"
            class="butt"
            >删除该用户</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import Rules from "../utils/rules";
export default {
  name: "EditUser",
  data() {
    return {
      tableData: [],
      form: {
        username: "",
        password: "",
        roleId: "",
        role: {
          id: ""
        }
      },
      formRules: {
        username: [
          {
            validator: Rules.checkIllegal2("请输入正确的邮箱"),
            trigger: "blur"
          }
        ]
      },
      sexOption: [
        { name: "男", id: 0 },
        { name: "女", id: 1 }
      ],
      roleOption: [
        { name: "管理员", id: 1 },
        { name: "普通用户", id: 2 }
      ],
      statusOption: [
        { name: "正常", id: 0 },
        { name: "冻结，禁止登录", id: 1 }
      ]
    };
  },
  mounted() {
    this.form = this.$route.params.data;
    console.log(this.form);
  },
  methods: {
    handleClick(row, column, event) {
      var name = row.name;
      if (name == "【管理员】用户管理") {
        this.$router.push("/UserManage");
      }
    },
    editUser() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          var newform = {};
          newform.id = this.form.id;
          newform.username = this.form.username;
          newform.disabled = this.form.disabled;
          newform.roleId = this.form.role.id;
          if (this.form.nickname != null) {
            newform.nickname = this.form.nickname;
          }
          if (this.form.sex != null) {
            newform.sex = this.form.sex;
          }
          if (this.form.phone != null) {
            newform.phone = this.form.phone;
          }
          this.$axios
            //.post("/user", {
            .put("/api/user", { ...newform })
            .then(response => {
              if (response.data.code === 0) {
                this.$message({
                  message: "修改成功！",
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
    deleteUser() {
      this.$confirm("是否删除该邮箱账号？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        center: true,
        showClose: false,
        type: "warning"
      }).then(() => {
        this.$axios
          .delete("/api/user", {
            params: {
              id: this.form.id
            }
          })
          .then(response => {
            if (response.data.code === 0) {
              this.$message({
                message: "删除成功！",
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
}
/* .myinput >>> .el-input-group__append {
  padding: 0px 10px;
}
.myinput >>> .el-input__inner {
  padding: 0px 10px;
} */
</style>
<style>
.mybox {
  width: 250px;
  position: absolute;
  left: 72px;
  top: 200px;
}
</style>
