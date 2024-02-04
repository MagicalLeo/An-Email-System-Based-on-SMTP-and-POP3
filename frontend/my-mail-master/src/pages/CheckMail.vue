<template>
  <div class="topper">
    <div class="background"></div>
    <div class="header">
      <i
        class="el-icon-arrow-left"
        style="font-size: 20px; margin-left:20px; margin-top:1px"
        @click="tobox"
      ></i>
      <div style="font-size: 16px; margin-right:170px; font-weight:bold">Check mail</div>
    </div>
    <el-divider></el-divider>
    <div class="form" style="display: flex; flex-direction: column;">
      <el-form ref="form" :model="form">
        <el-form-item>
          <div style="text-align:left; margin-left:20px;font-weight:bold">
            发件人：{{ form.fromaddress }}
          </div>
        </el-form-item>
        <el-form-item>
          <div class="inputDeep">
            {{ form.subject }}
          </div>
          <div class="time" style="display: flex;justify-content: end;margin-right: 160px;">{{ form.sentDate }}</div>
        </el-form-item>
        <el-form-item prop="content">
          <div class="inputDeep2">
            <el-input readonly
              v-model="form.content"
              type="textarea"
              :autosize="{ minRows: 15 }"
              style="min-width: 50vw;"
            >
            </el-input>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "CheckMail",
  data() {
    return {
      tableData: [],
      form: {
        to: "",
        subject: "",
        content: ""
      }
    };
  },
  mounted() {
    this.getInfo();
  },
  methods: {
    getInfo() {
      this.form = this.$route.params.data;
    },
    handleClick(row, column, event) {
      var name = row.name;
      if (name == "【管理员】用户管理") {
        this.$router.push("/UserManage");
      }
    },
    tobox() {
      this.$router.push("/MailBox");
    },
    send() {}
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.background {
  display: flex;
  position: absolute;
  top: 0;
  width: 100%;
  height: 100vh;
  align-items: center;
  justify-content: center;
  background-attachment: fixed; 
  background-position: center center;
  background-repeat: no-repeat no-repeat;
  background-size: cover;
  background: linear-gradient(310deg, rgba(0, 183, 255, 0.5) 0%, rgba(255, 215, 104, 0.5) 100%);
  mix-blend-mode: color-burn;
  opacity: 0.4;
  z-index: -1;
}
.time {
  text-align: left;
  margin-left: 20px;
  line-height: 10px;
  margin-top: 10px;
}
.inputDeep {
  text-align: left;
  margin-left: 20px;
  font-size: 18px;
  font-weight: 600;
  width: 90%;
  line-height: 25px;
}
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
  margin-left: 0px;
  margin-right: 0px;
}
.inputDeep2 >>> .el-textarea__inner {
  border: 0;
  padding: 10px;
  margin-left: 10px;
  width: 94%;
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
