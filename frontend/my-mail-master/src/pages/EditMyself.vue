<template>
  <div class="topper">
    <div class="background"></div>
    <div class="header">
      <i class="el-icon-arrow-left"
        style="font-size: 20px; margin-left:20px"
        @click="tomenu"
      ></i>
      <div style="font-size: 24px;font-weight: bolder;">Settings</div>
      <div></div>
    </div>
    <el-divider></el-divider>
    <div class="form">

      <el-form ref="form" :model="form" class="temp" >
        <el-form-item label="账号：">
          <el-input v-model="form.username" style="width:260px" disabled>
          </el-input>
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
        <el-form-item
          prop="smtpHost"
          style="margin-top:20px"
          label="SMTP地址："
        >
          <el-input
            v-model="form.smtpHost"
            placeholder="SMTP地址（选填）"
            style="width:220px"
          ></el-input>
        </el-form-item>
        <el-form-item
          prop="smtpPort"
          style="margin-top:20px"
          label="SMTP端口："
        >
          <el-input
            v-model.number="form.smtpPort"
            placeholder="SMTP端口（选填）"
            style="width:220px"
          ></el-input>
        </el-form-item>
        <el-form-item
          prop="pop3Host"
          style="margin-top:20px"
          label="POP3地址："
        >
          <el-input
            v-model="form.pop3Host"
            placeholder="POP3地址（选填）"
            style="width:220px"
          ></el-input>
        </el-form-item>
        <el-form-item
          prop="pop3Port"
          style="margin-top:20px"
          label="POP3端口："
        >
          <el-input
            v-model.number="form.pop3Port"
            placeholder="POP3端口（选填）"
            style="width:220px"
          ></el-input>
        </el-form-item>
        <el-form-item prop="authCode" style="margin-top:20px" label="授权码：">
          <el-input
            v-model.number="form.authCode"
            placeholder="SMTP、POP3授权码（选填）"
            style="width:220px;margin-left:20px"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            style="width: 98%;margin-top:5px;margin-right:15px"
            type="primary"
            @click="editUser"
            class="butt"
            >确认修改</el-button
          >
        </el-form-item>
      </el-form>
      <div class="mydiv">
        <el-divider></el-divider>
      </div>
      <el-form
        ref="passform"
        :model="passform"
        :rules="formRules2"
        :hide-required-asterisk="true"
      >
        <el-form-item prop="oldPassword" label="原密码：">
          <el-input
            v-model="passform.oldPassword"
            style="width:220px;margin-left:15px"
            placeholder="请输入原密码"
            :show-password="true"
          >
          </el-input>
        </el-form-item>
        <el-form-item prop="newPassword" label="新密码：">
          <el-input
            v-model="passform.newPassword"
            style="width:220px;margin-left:15px"
            placeholder="请输入新密码"
            :show-password="true"
          >
          </el-input>
        </el-form-item>
        <el-form-item
          prop="repeat"
          style="margin-top:20px"
          label="重复密码："
          :show-password="true"
        >
          <el-input
            v-model="passform.repeat"
            placeholder="请再次输入新密码"
            style="width:220px"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            style="width: 98%;margin-top:5px;margin-right:15px"
            type="primary"
            @click="editPassword"
            class="butt"
            >修改密码</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import Rules from "../utils/rules";
export default {
  name: "EditMyself",
  data() {
    const repeatPassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.passform.newPassword) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      formLabelWidth: "82px",
      tableData: [],
      id: null,
      form: {
        username: "",
        password: "",
        nickname: "",
        sex: "",
        roleId: "",
        smtpHost: "",
        smtpPort: null,
        pop3Host: "",
        pop3Port: null,
        authCode: ""
      },
      passform: {
        oldPassword: "",
        newPassword: "",
        repeat: ""
      },
      formRules: {
        username: [
          {
            validator: Rules.checkIllegal2("请输入正确的邮箱"),
            trigger: "blur"
          }
        ]
      },
      formRules2: {
        oldPassword: [
          { required: true, message: "请输入原密码", trigger: "blur" }
        ],
        newPassword: [
          {
            validator: Rules.passwordCheck("请输入正确的新密码"),
            trigger: "blur"
          }
        ],
        repeat: [
          {
            validator: repeatPassword,
            trigger: "blur"
          }
        ]
      },
      sexOption: [
        { name: "男", id: 0 },
        { name: "女", id: 1 }
      ]
    };
  },
  mounted() {
    this.id = localStorage.getItem("id");
    this.getInfo();
  },
  methods: {
    getInfo() {
      this.$axios
        .get("/api/user", {
          params: {
            id: this.id
          }
        })
        .then(response => {
          if (response.data.code === 0) {
            this.form = response.data.data.items[0];
          } else {
            this.$message({
              message: response.data.msg,
              type: "error"
            });
          }
        });
    },
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
          if (this.form.nickname != null) {
            newform.nickname = this.form.nickname;
          }
          if (this.form.sex != null) {
            newform.sex = this.form.sex;
          }
          if (this.form.phone != null) {
            newform.phone = this.form.phone;
          }
          if (this.form.smtpHost != null && this.form.smtpHost != "") {
            newform.smtpHost = this.form.smtpHost;
          }
          if (this.form.pop3Host != null && this.form.pop3Host != "") {
            newform.pop3Host = this.form.pop3Host;
          }
          if (this.form.authCode != null && this.form.authCode != "") {
            newform.authCode = this.form.authCode;
          }
          if (this.form.pop3Port != null) {
            newform.pop3Port = this.form.pop3Port;
          }
          if (this.form.smtpPort != null) {
            newform.smtpPort = this.form.smtpPort;
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
                localStorage.setItem("nickname", newform.nickname);
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
    editPassword() {
      console.log(this.passform);
      this.$refs["passform"].validate(valid => {
        if (valid) {
          var newform = {};
          newform.oldPassword = this.passform.oldPassword;
          newform.newPassword = this.passform.newPassword;
          this.$axios
            //.post("/user", {
            .put("/api/user/change_password", {
              ...newform
            })
            .then(response => {
              if (response.data.code === 0) {
                this.$message({
                  message: "修改成功！",
                  type: "success",
                  duration: 800
                });
                localStorage.setItem("password", newform.password);
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
    tomenu() {
      this.$router.push("/HomePage");
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.header {
  margin-top: 20px;
  margin-right: 30px;
  display: flex;
  justify-content: space-between;
}
.el-divider {
  margin-bottom: 0px;
  border-top: 1px solid #e6ebf5;
}
.mydiv >>> .el-divider {
  margin-bottom: 20px;
  border-top: 1px solid #e6ebf5;
}
.form {
  margin: 20px;
  display: flex;
  flex-direction: column;
  align-self: center;
  justify-self: center;
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
.el-form-item{
  display: flex;
  justify-content: center;
}


.butt {
  appearance: none;
  background-color: transparent;
  border: 2px solid #1A1A1A;
  border-radius: 15px;
  box-sizing: border-box;
  color: #3B3B3B;
  cursor: pointer;
  display: inline-block;
  font-family: Roobert,-apple-system,BlinkMacSystemFont,"Segoe UI",Helvetica,Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol";
  font-size: 16px;
  font-weight: 600;
  line-height: normal;
  margin: 0;
  min-width: 0;
  outline: none;
  padding: 16px 24px;
  text-align: center;
  text-decoration: none;
  transition: all 300ms cubic-bezier(.23, 1, 0.32, 1);
  user-select: none;
  -webkit-user-select: none;
  touch-action: manipulation;
  width: 100%;
  will-change: transform;
}

.butt:disabled {
  pointer-events: none;
}

.butt:hover {
  color: #fff;
  background-color: #1A1A1A;
  box-shadow: rgba(0, 0, 0, 0.25) 0 8px 15px;
  transform: translateY(-2px);
}

.butt:active .butt:focus{
  box-shadow: none;
  transform: translateY(0);
  color: #fff;
}
</style>
