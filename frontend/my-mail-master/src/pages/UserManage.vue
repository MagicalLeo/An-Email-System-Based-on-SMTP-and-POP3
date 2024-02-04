<template>
  <div class="topper">
    <div class="background"></div>
    <div class="header">
      <i
        class="el-icon-arrow-left"
        style="font-size: 20px; margin-left:20px; margin-top:1px"
        @click="tomenu"
      ></i>
      <div style="font-size: 16px;padding-bottom:1px;">User Manage</div>
      <i
        class="el-icon-plus"
        style="font-size: 20px; margin-right:20px; margin-top:1px"
        @click="toAddUser"
      ></i>
    </div>
    <el-divider></el-divider>
    <el-table
      :data="tableData"
      style="width: 100% "
      max-height="560px"
      v-el-table-loadmore="loadMore"
      @row-click="toEdit"
      ><el-table-column prop="username" label="账号" style="font-size: 20px;">
        <template slot-scope="scope"
          ><div style="display:flex;justify-content:space-between">
            <div>{{ scope.row.username }}</div>
            <div>
              <i
                class="el-icon-arrow-right"
                style="margin-left:20px;margin-right:5px;"
              ></i>
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "UserManage",
  data() {
    return {
      tableData: [],
      total: 0,
      pageSize: 12,
      current: 1,
      loading: false,
      nomore: false
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
    this.onSearchUsers();
  },
  methods: {
    handleClick(row, column, event) {
      var name = row.name;
      if (name == "【管理员】用户管理") {
        this.$router.push("/UserManage");
      }
    },
    loadMore() {
      if (this.current * this.pageSize >= this.total) {
        //当前页码数等于总页数的时候,提示没有更多数据了
        this.loading = false;
        this.nomore = true;
      } else {
        //当数据没有加载完的时候,继续加载数据
        this.loading = true;
        this.current++; //当前页数字加一
        this.$axios
          .get("/api/user", {
            params: {
              pageSize: this.pageSize,
              current: this.current
            }
          })
          .then(response => {
            if (response.data.code === 0) {
              console.log(response.data);
              this.tableData = [...this.tableData, ...response.data.data.items];
              this.total = response.data.data.total;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
          });
      }
    },
    onSearchUsers() {
      //this.$axios.get("/user").then(response => {
      this.$axios
        .get("/api/user", {
          params: {
            pageSize: this.pageSize
          }
        })
        .then(response => {
          // this.$message({
          //   message: "测试",
          //   type: "success"
          // });
          if (response.data.code === 0) {
            // this.$message({
            //   message: "成功",
            //   type: "success"
            // });
            console.log(response.data);
            this.tableData = response.data.data.items;
            this.total = response.data.data.total;
          } else {
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
    toAddUser() {
      this.$router.push("/AddUser");
    },
    toEdit(row) {
      console.log(row.username);
      this.$router.push({
        name: "EditUser",
        params: {
          data: row
        }
      });
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
</style>
<style>
.mybox {
  width: 250px;
  position: absolute;
  left: 72px;
  top: 200px;
}
</style>
