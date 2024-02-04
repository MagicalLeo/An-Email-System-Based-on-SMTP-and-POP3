<template>
  <div>
    <div class="background"></div>
    <div class="board">
    <div class="content">
      <div class="titleLine">
        <div class="title" style="font-weight: bold;">Register</div>
      </div>
      <div class="form" >
        <el-form ref="form" :model="form" :rules="formRules">
          <el-form-item prop="username">
            <el-input
            style="border: 2px solid #1A1A1A;"
              v-model="form.username"
              placeholder="邮箱账号名，例：test"
              prefix-icon="el-icon-user"
            >
              <template slot="append">@mymail.com</template>
            </el-input>
          </el-form-item>
          <el-form-item prop="newPassword" >
            <el-input
            style="border: 2px solid #1A1A1A;"
              v-model="form.newPassword"
              placeholder="密码"
              prefix-icon="el-icon-lock"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item prop="repeatPassword">
            <el-input
            style="border: 2px solid #1A1A1A;"
              v-model="form.repeatPassword"
              placeholder="重复密码"
              prefix-icon="el-icon-lock"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 100%;" type="primary" @click="register" class="butt"
              >注册</el-button
            >
          </el-form-item>
          <el-form-item>
            <router-link to="/">返回登录</router-link>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import Rules from "../utils/rules";

export default {
  name: "Register",
  data() {
    const repeatPassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.form.newPassword) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      loading: false,
      count: 60,
      show: "发送验证码",
      form: {
        username: "",
        verification: "",
        newPassword: "",
        repeatPassword: ""
      },
      formRules: {
        verification: [
          {
            required: true,
            message: "请输入验证码",
            trigger: "blur"
          }
        ],
        username: [
          {
            validator: Rules.checkIllegal("请输入正确的邮箱名称，4-13位"),
            trigger: "blur"
          }
        ],
        newPassword: [
          {
            validator: Rules.passwordCheck(),
            trigger: "blur"
          }
        ],
        repeatPassword: [
          {
            validator: repeatPassword,
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    sendVerification() {
      this.$refs.form.validateField("phone", errMsg => {
        if (!errMsg) {
          this.$axios
            //.post("/user/forgot_password/auth_code", {
            .post("/api/user/forgot_password/auth_code", {
              phoneNumber: this.form.phone
            })
            .then(response => {
              if (response.data.code === 0) {
                localStorage.setItem("uuid", response.data.data.uuid);
                this.loading = true;
                this.setCountDown();
                this.$message({
                  message: response.data.msg,
                  type: "success"
                });
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error"
                });
              }
            });
        }
      });
    },
    register() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$axios
            //.post("/user/register", {
            .post("/api/user/register", {
              username: this.form.username + "@mymail.com",
              password: this.form.newPassword
            })
            .then(response => {
              if (response.data.code === 0) {
                this.$router.push("/");
                this.$message({
                  message: "注册成功！",
                  type: "success"
                });
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
    setCountDown() {
      if (this.count === 0) {
        this.show = "发送验证码";
        this.loading = false;
        this.count = 60;
      } else {
        this.count--;
        this.show = this.count + "s";
        setTimeout(() => this.setCountDown(), 1000);
      }
    }
  }
};
</script>

<style scoped>
.background {
  display: flex;
  width: 100%;
  height: 200vh;
  align-items: center;
  justify-content: center;
  background-attachment: fixed; 
  background-position: center center;
  background-repeat: no-repeat no-repeat;
  background-size: cover;
   background: linear-gradient(310deg, rgba(0, 183, 255, 0.5) 0%, rgba(255, 215, 104, 0.5) 100%);
  mix-blend-mode: color-burn;
  z-index: -5;
  position: absolute;
}

.board{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.content {
  margin-top: 50px;
  width: 360px;
  height: auto;
  box-sizing: content-box;
  padding: 20px;
}

.titleLine {
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: center;
  margin-top: 20px;
  margin-bottom: 20px;
}

.title {
  margin-left: 5px;
  color: rgb(42, 42, 42);
  font-size: 24px;
  text-align: center;
}

.form {
  width: 90%;
  margin: 40px auto 20px;
}
.inputZone{
  border: 2px solid #1A1A1A;
  width: 15vw;
  min-width: 200px;
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

.butt:active {
  box-shadow: none;
  transform: translateY(0);
}
</style>
