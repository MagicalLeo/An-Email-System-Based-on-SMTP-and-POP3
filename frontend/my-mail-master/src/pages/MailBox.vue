<template>
  
  <div class="topper">
    <div class="background"></div>
    <div class="header" style="display: flex;justify-content: end;">
      <i
        class="el-icon-arrow-left"
        style="font-size: 20px; margin-left:20px; margin-top:1px;position: absolute;left: 0;"
        @click="tomenu"
      ></i>
      <div style="font-size: 14px;font-weight: bolder;">
        {{ username }}
      </div>
      <div></div>
    </div>
    <el-divider></el-divider>
    <el-table
      :data="tableData"
      style="width:98%; margin-left:10px"
      max-height="600px"
      @row-click="toCheck"
      :show-header="false"
      v-loading="tableloading"
      ><el-table-column>
        <template slot-scope="scope"
          ><div>
            <div>
              <span class="login-cycle" v-if="!scope.row.isread"></span>
              <b style="font-size: 18px;">{{ scope.row.fromname }}</b>
              
            </div>
            <div class="time">{{ scope.row.headtime }}</div>
            <div style="font-size: 14px; font-weight:600">
              <el-col
                style="width: 300px;white-space: nowrap;text-overflow: ellipsis;overflow: hidden;"
                >{{ scope.row.subject }}</el-col
              >
            </div>
            <div>

            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <div  style="font-size: 14px;font-weight: bold;margin-top: 10px;">你有{{ totalunread }}封未读邮件</div>
  </div>

</template>
<!-- 
              
              
              
                            <el-col
                style="width: 350px;white-space: nowrap;text-overflow: ellipsis;overflow: hidden;"
                >{{ scope.row.content }}</el-col
              >---->
<script>
export default {
  name: "MailBox",
  data() {
    return {
      tableData: [],
      total: 0,
      pageSize: 12,
      current: 1,
      loading: false,
      nomore: false,
      username: "",
      tableloading: true,
      totalunread: 0,
      timer: null
    };
  },
  directives: {
    "el-table-loadmore": {
      bind: function(el, binding) {
        console.log("sss");
        const selectWrap = el.querySelector(".el-table__body-wrapper");
        let currentLeft = 0; //初始左滑距离为0
        selectWrap.addEventListener("scroll", function() {
          const scrollDistance =
            this.scrollHeight - this.scrollTop - this.clientHeight; //向下滚动距离
          const scrollL = this.scrollLeft - currentLeft; //左滑距离
          currentLeft = this.scrollLeft;
          // 当纵向滑动到底部，且横向不滑动时，触发滚动事件
          if (scrollDistance <= 0.4 && scrollL == 0) {
            binding.value();
          }
        });
      }
    }
  },
  mounted() {
    this.username = localStorage.getItem("username");
    this.tableData = JSON.parse(localStorage.getItem(this.username + "email"));
    this.totalunread = localStorage.getItem(this.username + "total");
    if (this.totalunread == null) {
      this.totalunread = 0;
    }
    if (this.tableData != null && this.tableData.length > 0) {
      this.tableloading = false;
    }
    console.log(this.tableData);
    this.refresh();
  },
  methods: {
    handleClick(row, column, event) {
      var name = row.name;
      if (name == "【管理员】用户管理") {
        this.$router.push("/UserManage");
      }
    },
    refresh() {
      this.onSearchMail();
      this.timer = setTimeout(() => {
        this.refresh(); // 调用轮询
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
          if (this.tableData != null && this.tableData.length >= 1) {
            this.totalunread = 0;
            tempdata.forEach(item => {
              this.tableData.forEach(titem => {
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
          this.tableData = tempdata;
          localStorage.setItem(
            this.username + "email",
            JSON.stringify(this.tableData)
          );
          localStorage.setItem(this.username + "total", this.totalunread);
          this.tableloading = false;
        } else {
          console.log(response);
          this.tableloading = false;
          this.$message({
            message: response.data.msg,
            type: "error"
          });
        }
      });
    },
    tomenu() {
      this.$router.push("/HomePage");
    },
    toCheck(row) {
      if (!row.isread) {
        row.isread = true;
        this.totalunread--;
        localStorage.setItem(this.username + "total", this.totalunread);
        localStorage.setItem(
          this.username + "email",
          JSON.stringify(this.tableData)
        );
      }
      this.$router.push({
        name: "CheckMail",
        params: {
          data: row
        }
      });
    }
  },
  beforeDestroy() {
    clearTimeout(this.timer);
    this.timer = null;
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
}
.login-cycle {
  width: 8px;
  height: 8px;
  display: flex;
  justify-self: center;
  background-color: rgb(33, 141, 243);
  border-radius: 50%;
  margin-bottom: 2px;
}
.header {
  margin-top: 20px;
  margin-right: 30px;
  display: flex;
  justify-content: space-between;
}
.el-divider {
  margin-bottom: 0px;
}
.time {
  position: absolute;
  top: 12px;
  right: 10px;
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
