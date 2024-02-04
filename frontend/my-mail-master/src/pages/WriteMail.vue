<template>
  <div class="topper">
    <div class="background"></div>
    <div class="header">
      <i
        class="el-icon-arrow-left"
        style="font-size: 20px; margin-left:20px; margin-top:1px"
        @click="tomenu"
      ></i>
      <div style="font-size: 16px">Write Email</div>
      <i
        class="el-icon-s-promotion"
        style="font-size: 20px; margin-right:20px; margin-top:1px"
        @click="send"
      ></i>
    </div>
    <el-divider></el-divider>
    <div class="form">
      <el-form ref="form" :model="form">
        <el-form-item prop="to">
          <div
            style="display: flex;justify-content: center;margin-left: 15px;margin-right: 15px;"
          >
            <div style="padding-top:5px;font-weight: bold;">收件人：</div>
            <div class="test" style="width:260px">
              <el-tag
                :key="tag"
                v-for="tag in form.to"
                closable
                :disable-transitions="false"
                @close="handleClose(tag)"
              >
                {{ tag }}
              </el-tag>
              <div
                style="display:inline-block;"
                v-if="role == 1 || (role == 2 && form.to.length < 1)"
              >
                <el-input
                  class="input-new-tag"
                  v-if="inputVisible"
                  v-model="inputValue"
                  ref="saveTagInput"
                  size="medium"
                  @keyup.enter.native="handleInputConfirm"
                  @blur="handleInputConfirm"
                >
                </el-input>
                <el-button
                  v-else
                  class="button-new-tag"
                  size="medium"
                  @click="showInput" style="font-weight: bold;"
                  >+ 添加收件人</el-button
                >
              </div>
            </div>
          </div>
          <div class="mydiv2"><el-divider></el-divider></div>
        </el-form-item>

        <el-form-item prop="subject">
          <div
            style="display: flex;justify-content:center;margin-left: 15px;margin-right: 20px;"
          >
            <div style="padding-top:5px;word-spacing:10px;font-weight: bold;">主 题：</div>
            <div class="inputDeep">
              <el-input v-model="form.subject" style="width: 60%;"> </el-input>
            </div>
          </div>
          <div class="mydiv"><el-divider></el-divider></div>
        </el-form-item>
        <el-form-item prop="content">
          <div>
            <div
              class="inputDeep2"
              style="margin-top:10px;margin-left: 5px;margin-right: 5px;"
            >
              <el-input
                v-model="form.content"
                type="textarea"
                :autosize="{ minRows: 16 }"
                style="max-width: 80vw;min-width: 50vw;"
              >
              </el-input>
            </div>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import Rules from "../utils/rules";
export default {
  name: "WriteMail",
  data() {
    return {
      tableData: [],
      form: {
        subject: "",
        content: "",
        to: []
      },
      inputVisible: false,
      inputValue: "",
      role: "",
      username: "",
      params: null
    };
  },
  mounted() {
    this.params = this.$route.params.data;
    console.log(this.params);
    if (this.params) {
      this.form.to = this.params.to;
      this.form.content = this.params.content;
      this.form.subject = this.params.subject;
    }
    this.username = localStorage.getItem("username");
    this.role = localStorage.getItem("role");
  },
  methods: {
    handleClick(row, column, event) {
      var name = row.name;
      if (name == "【管理员】用户管理") {
        this.$router.push("/UserManage");
      }
    },
    handleClose(tag) {
      this.form.to.splice(this.form.to.indexOf(tag), 1);
    },

    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue) {
        this.form.to.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = "";
    },
    tomenu() {
      this.$confirm("文本不保存！<br/>是否返回？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        center: true,
        showClose: false,
        dangerouslyUseHTMLString: true,
        type: "warning"
      }).then(() => {
        if (
          this.form.to.length == 0 &&
          this.form.subject == "" &&
          this.form.subject == ""
        ) {
          this.$router.push("/HomePage");
          return;
        }
        this.$confirm("要保存到草稿箱？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          closeOnClickModal: false,
          center: true,
          showClose: false,
          type: "warning"
        })
          .then(() => {
            var saved = JSON.parse(
              localStorage.getItem(this.username + "save")
            );
            if (this.params) {
              saved.forEach(item => {
                if (item.id == this.params.id) {
                  console.log("asdasd");
                  item.content = this.form.content;
                  item.to = this.form.to;
                  item.subject = this.form.subject;
                  console.log(item);
                  return;
                }
              });
            } else {
              if (saved == null) {
                saved = [];
              }
              saved.push(this.form);
            }
            localStorage.setItem(this.username + "save", JSON.stringify(saved));
            this.$router.push("/HomePage");
          })
          .catch(() => {
            this.$router.push("/HomePage");
          });
      });
    },
    send() {
      if (this.form.to.length < 1) {
        this.$message({
          message: "请先输入收件人！",
          type: "error",
          duration: 1000
        });
        return;
      }
      if (this.form.subject == null || this.form.subject == "") {
        this.$message({
          message: "邮件主题不能为空！",
          type: "error",
          duration: 1000
        });
        return;
      }
      var tos = new URLSearchParams();
      this.form.to.forEach(item => {
        tos.append("to", item);
      });
      // console.log(this.form);
      // var newform = {
      //   to: this.form.to[0],
      //   subject: this.form.subject,
      //   content: this.form.content
      // };
      tos.append("subject", this.form.subject);
      tos.append("content", this.form.content);
      this.$axios
        .post("/api/mail", tos)
        .then(response => {
          if (response.data.code === 0) {
            if (this.params) {
              var saved = JSON.parse(
                localStorage.getItem(this.username + "save")
              );
              for (var i = 0; i < saved.length; i++) {
                if (saved[i].id == this.params.id) {
                  saved.splice(i, 1);
                  break;
                }
              }
              localStorage.setItem(
                this.username + "save",
                JSON.stringify(saved)
              );
            }
            this.$message({
              message: "发送成功！",
              type: "success",
              duration: 800
            });
            this.$router.push("/HomePage");
          } else {
            console.log(response);
            this.$message({
              message: response.data.msg,
              type: "error"
            });
          }
        });
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.inputDeep >>> .el-input__inner {
  border: 0;
  width: 260px;
  padding: 0;
}
.inputDeep2 >>> .el-textarea__inner {
  border: 0;
  font-size: 16px;
  padding-top: 0;
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
  height: 400px;
}
.form >>> .el-form-item__content {
  line-height: 30px;
}
.mydiv >>> .el-divider {
  margin-top: 0px;
  margin-left: 15px;
  margin-bottom: 0px;
  width: 90%;
  border-top: 1px solid #e6ebf5;
}
.mydiv2 >>> .el-divider {
  margin-top: 5px;
  margin-left: 15px;
  margin-bottom: 0px;
  width: 90%;
  border-top: 1px solid #e6ebf5;
}
</style>
<style>
@media screen and (max-width: 750px) {
  .el-message-box {
    width: 80% !important;
  }
}
.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  min-width: 100px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
