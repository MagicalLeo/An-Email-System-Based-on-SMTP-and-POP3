<template>
  <div class="background">
    <div class="content">
      <div class="titleLine">
        <div class="icon" />
        <div class="title">邮箱管理员登录</div>
      </div>
      <div class="form">
        <el-form :model="formLogin" ref="formLogin">
          <el-form-item prop="username">
            <el-input
              v-model="formLogin.username"
              prefix-icon="el-icon-user"
              placeholder="username@test.com"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="formLogin.password"
              prefix-icon="el-icon-lock"
              placeholder="密码"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="login" style="width: 100%;" type="primary"
              >登录
            </el-button>
          </el-form-item>
          <el-form-item>
            <router-link to="/ResetPassword">注册新邮箱</router-link>
            |
            <router-link to="/">邮箱用户登录</router-link>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AdminLogin",
  data() {
    return {
      formLogin: {
        username: "",
        password: ""
      }
    };
  },
  methods: {
    login() {
      console.log(this.formLogin);
      this.$axios.post("/api/login", this.formLogin).then(response => {
        console.log(response);
        if (response.data.code === 0) {
          this.$message({
            message: "登录成功",
            type: "success"
          });
          localStorage.setItem("token", "mail " + response.data.data.token); //get  localStorage.getItem("token")
          localStorage.setItem("username", response.data.data.nickname); //get  localStorage.getItem("username")
          localStorage.setItem("role", response.data.data.role);
          this.$router.push("/HomePage");
        } else {
          this.$message({
            message: response.data.msg,
            type: "error"
          });
          this.resetForm("formLogin");
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
};
</script>

<style scoped>
.background {
  display: flex;
  width: 100%;
  height: 100vh;
  align-items: center;
  justify-content: center;
  background-attachment: fixed;
  background-image: url("../assets/login/background.png");
  background-position: center center;
  background-repeat: no-repeat no-repeat;
  background-size: cover;
}

.content {
  width: 360px;
  height: 400px;
  box-sizing: content-box;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  box-shadow: 3px 3px 15px 1px rgba(22, 33, 22, 0.2);
}

.titleLine {
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: center;
  margin-top: 30px;
  margin-bottom: 20px;
}

.icon {
  width: 40px;
  height: 40px;
  border-radius: 100%;
  margin-top: 6px;
  background-image: url("../assets/logo.png");
  background-size: 100% 100%;
}

.title {
  margin-left: 5px;
  color: rgb(42, 42, 42);
  font-size: 24px;
  text-align: center;
}

.form {
  width: 80%;
  margin: 40px auto 20px;
}
</style>
