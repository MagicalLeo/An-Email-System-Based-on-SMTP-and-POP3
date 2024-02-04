<template>
  
  <div class="topper">
    <div class="background"></div>
    <div class="header">
      <i
        class="el-icon-arrow-left"
        style="font-size: 20px;margin-left:20px; margin-top:1px;position: absolute; left: 0;"
        @click="logout"
      ></i>
      <div style="display: flex;font-size: 16px;margin-top:1px;font-weight: bolder;justify-content: end;margin-right: 20px;">{{ username }}</div>
      <i
        class="el-icon-edit-outline"
        style="font-size: 20px;margin-right:20px; margin-top:1px"
        @click="toWrite"
      ></i>
    </div>
    <el-divider></el-divider>
    <el-table
      :data="tableData"
      :show-header="false"
      style="width: 100%"
      @row-click="handleClick"
      ><el-table-column prop="name" style="font-size: 20px;display: flex;flex-direction: row;">
        <template slot-scope="scope">
          <div style="display:flex;justify-content:center">
            <div style="width:200px;display:flex;">
              <div>
                <i
                  :class="getIcon(scope.row.name)"
                  style="margin-left:20px;margin-right:15px"
                ></i>
              </div>
              <div style="font-weight: bold;">{{ scope.row.name }}</div>
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-table
      v-if="role == 1"
      :data="tableData2"
      :show-header="false"
      style="width: 100%"
      @row-click="handleClick"
      ><el-table-column prop="name" style="font-size: 20px;display: flex;flex-direction: row;">
        <template slot-scope="scope"
          ><div style="display:flex;justify-content:center;">
            <div style="width:200px;display:flex;">
              <div>
                <i
                  :class="getIcon(scope.row.name)"
                  style="margin-left:20px;margin-right:10px;"
                ></i>
              </div>
              <div style="font-weight: bold;">{{ scope.row.name }}</div>
            </div>
            <div>
              <i
                class="el-icon-arrow-right"
                style="margin-left:20px;margin-right:15px;"
              ></i>
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-button
      style="width: 80%; margin-top:30px; margin-left:0px;"
      type="primary"
      @click="logout"
      class="loginButt"
      >登出</el-button
    >
  </div>
</template>

<script>
export default {
  name: "HomePage",
  data() {
    return {
      username: "Welcome to Your Vue.js App",
      tableData: [{ name: "收件箱" }, { name: "草稿箱" }, { name: "个人设置" }],
      tableData2: [{ name: "【管理员】用户管理" }],
      role: null,
      totalunread: 0,
      mailData: [],
      timer: null
    };
  },
  mounted() {
    this.getInfo();
    this.refresh();
  },
  methods: {
    handleClick(row, column, event) {
      var name = row.name;
      if (name == "【管理员】用户管理") {
        this.$router.push("/UserManage");
      } else if (name == "个人设置") {
        this.$router.push("/EditMyself");
      } else if (name == "收件箱") {
        this.$router.push("/MailBox");
      } else if (name == "草稿箱") {
        this.$router.push("/SaveBox");
      }
    },
    getInfo() {
      this.username = localStorage.getItem("username");
      this.role = localStorage.getItem("role");
      var total = localStorage.getItem(this.username + "total");
      this.mailData = JSON.parse(localStorage.getItem(this.username + "email"));
      if (total != null && total == 0) {
        total = null;
      }
      this.totalunread = total;
      console.log(total);
    },
    refresh() {
      this.onSearchMail();
      this.timer = setTimeout(() => {
        this.refresh();
      }, 10000);
    },
    onSearchMail() {
      console.log("test");
      this.$axios.get("/api/mail").then(response => {
        if (response.data.code === 0) {
          var total = response.data.data.total;
          var tempdata = response.data.data.items;
          tempdata.reverse();
          tempdata.forEach(item => {
            item.isread = false;
            item.headtime = item.sentDate.split("T")[0];
            if (item.from[0] != "<") {
              item.fromname = item.from.split("<")[0];
              item.fromaddress = item.from.split("<")[1];
            } else {
              item.fromname = item.from;
              item.fromaddress = item.from.split("<")[1];
            }
          });
          if (this.mailData != null && this.mailData.length >= 1) {
            this.totalunread = 0;
            tempdata.forEach(item => {
              this.mailData.forEach(titem => {
                if (item.messageId == titem.messageId) {
                  item.isread = titem.isread;
                  return;
                }
              });
              if (!item.isread) {
                this.totalunread++;
              }
            });
          } else {
            this.totalunread = total;
          }
          this.mailData = tempdata;
          localStorage.setItem(
            this.username + "email",
            JSON.stringify(this.mailData)
          );
          localStorage.setItem(this.username + "total", this.totalunread);
        } else {
        }
      });
    },
    getIcon(name) {
      if (name == "收件箱") {
        return "el-icon-folder-checked";
      }
      if (name == "已发送") {
        return "el-icon-s-promotion";
      }
      if (name == "【管理员】用户管理") {
        return "el-icon-user-solid";
      }
      if (name == "草稿箱") {
        return "el-icon-document";
      }
      return "el-icon-setting";
    },
    onSearchUsers() {
      this.$axios.get("/api/user").then(response => {
        this.$message({
          message: "测试",
          type: "success"
        });
        if (response.data.code === 0) {
          this.$message({
            message: "成功",
            type: "success"
          });
          console.log(response.data);
        } else {
          this.$message({
            message: response.data.username,
            type: "error"
          });
        }
      });
    },
    toWrite() {
      this.$router.push("/WriteMail");
    },
    logout() {
      this.$confirm("是否退出当前邮箱账号？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        center: true,
        showClose: false,
        type: "warning"
      }).then(() => {
        localStorage.removeItem("token");
        localStorage.removeItem("username");
        localStorage.removeItem("nickname");
        localStorage.removeItem("role");
        localStorage.removeItem("id");
        this.$router.push("/");
      });
    }
  },
  beforeDestroy() {
    clearTimeout(this.timer);
    this.timer = null;
  }
};
</script>

<style scoped>
.header {
  margin-top: 20px;
  display: flex;
justify-content:end;
}
.el-divider {
  margin-bottom: 0px;
}
</style>
<style>
@media screen and (max-width: 750px) {
  .el-message-box {
    width: 80% !important;
  }
}

td{
  height: 150px;
  font-size:medium;
  background: linear-gradient(310deg, rgba(0, 183, 255, 0.5) 0%, rgba(255, 215, 104, 0.5) 90%);
}
.background {
  display: flex;
  position: absolute;
  top: 0;
  width: 100%;
  height: 120vh;
  align-items: center;
  justify-content: center;
  background-attachment: fixed; 
  background-position: center center;
  background-repeat: no-repeat no-repeat;
  background-size: cover;
  background: linear-gradient(310deg, rgba(0, 183, 255, 0.5) 0%, rgba(255, 215, 104, 0.5) 100%);
  mix-blend-mode: color-burn;
  opacity: 0.4;
  z-index: -5;
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

</style>
