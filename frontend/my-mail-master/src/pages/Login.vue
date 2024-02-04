<template>
  <div class="topper">
  <div class="background"></div>
      <div class="board">
      <div class="">
        <div class="icon" />
        <div class="title">我们的邮件系统</div>
      </div>


      <div class="form">
        <el-form :model="formLogin" ref="formLogin">
          <el-form-item prop="username" >
            <el-input
              v-model="formLogin.username"
              prefix-icon="el-icon-user"
              placeholder="username@test.com"
              class="inputZone"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password" >
            <el-input
              v-model="formLogin.password"
              prefix-icon="el-icon-lock"
              placeholder="密码"
              show-password
              class="inputZone"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="login" style="width: 100%;" type="primary" class="loginButt"
              >登录
            </el-button>
          </el-form-item>
          <el-form-item>
            <router-link to="/Register">注册新邮箱</router-link>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>



<script>
import axios from 'axios'
export default {
  name: "Login",
  mounted() {
    this.checkstatus();
  },
  data() {
    return {
      formLogin: {
        username: "",
        password: ""
      }
    };
  },
  methods: {
    checkstatus() {
      var token = localStorage.getItem("token");
      if (token != null) {
        this.$router.push("/HomePage");
      }
    },
    login() {
      console.log(this.formLogin);
      axios.post('/api/login',this.formLogin)
        .then(response => {
          console.log(response);
          console.log(this.formLogin);
          if (response.data.code === 0) {
            this.$message({
              message: "登录成功",
              type: "success",
              duration: 500
            });
            localStorage.setItem("token", "mail " + response.data.data.token); //get  localStorage.getItem("token")
            localStorage.setItem("id", response.data.data.id);
            localStorage.setItem("username", response.data.data.username); //get  localStorage.getItem("username")
            localStorage.setItem("nickname", response.data.data.nickname); //get  localStorage.getItem("username")
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
<!----background-image: url("../assets/login/background.png");--->
<!----  background-image: url("../assets/logo2.jpg");-->
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



.icon {
  width: 40px;
  height: 40px;
  margin-top: 6px;

  background-size: 100% 100%;
}

.title {
  color: rgb(42, 42, 42);
  font-size: 24px;
  text-align: center;
  font-weight: bolder;
}

.form {
  width: 80%;
  margin: 40px auto 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.loginButt {
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

.loginButt:disabled {
  pointer-events: none;
}

.loginButt:hover {
  color: #fff;
  background-color: #1A1A1A;
  box-shadow: rgba(0, 0, 0, 0.25) 0 8px 15px;
  transform: translateY(-2px);
}

.loginButt:active {
  box-shadow: none;
  transform: translateY(0);
}
.inputZone{
  border: 2px solid #1A1A1A;
  width: 15vw;
  min-width: 200px;
}



</style>