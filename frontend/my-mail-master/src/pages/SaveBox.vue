<template>
  <div class="hello">
    <div class="header">
      <i
        class="el-icon-arrow-left"
        style="font-size: 20px; margin-left:20px; margin-top:1px"
        @click="tomenu"
      ></i>
      <div style="font-size: 14px">
        草稿箱
      </div>
      <div></div>
    </div>
    <el-divider></el-divider>
    <el-table
      :data="tableData"
      style="width:96%; margin-left:10px"
      max-height="580px"
      @row-click="toCheck"
      :show-header="false"
      ><el-table-column>
        <template slot-scope="scope"
          ><div>
            <div>
              <el-col
                style="width: 300px;white-space: nowrap;text-overflow: ellipsis;overflow: hidden;"
                ><b style="font-size: 18px;">{{
                  scope.row.tostring
                }}</b></el-col
              >
            </div>
            <div style="font-size: 14px; font-weight:600">
              <el-col
                style="width: 300px;white-space: nowrap;text-overflow: ellipsis;overflow: hidden;"
                >{{ scope.row.subjectstring }}</el-col
              >
            </div>
            <div>
              <el-col
                style="width: 350px;white-space: nowrap;text-overflow: ellipsis;overflow: hidden;"
                >{{ scope.row.contentstring }}</el-col
              >
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "SaveBox",
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
  mounted() {
    this.username = localStorage.getItem("username");
    this.tableData = JSON.parse(localStorage.getItem(this.username + "save"));
    var now = 0;
    this.tableData.forEach(item => {
      item.id = now;
      now++;
      if (item.to.length < 1) {
        item.tostring = "空";
      } else if (item.to.length > 1) {
        item.tostring = item.to[0] + "等";
      } else {
        item.tostring = item.to[0];
      }
      if (item.subject == "") {
        item.subjectstring = "空";
      } else {
        item.subjectstring = item.subject;
      }
      if (item.content == "") {
        item.contentstring = "空";
      } else {
        item.contentstring = item.content;
      }
    });
    this.tableloading = false;
    console.log(this.tableData);
    localStorage.setItem(
      this.username + "save",
      JSON.stringify(this.tableData)
    );
  },
  methods: {
    tomenu() {
      this.$router.push("/HomePage");
    },
    toCheck(row) {
      this.$router.push({
        name: "WriteMail",
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
