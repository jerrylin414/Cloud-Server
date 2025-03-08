<template>
  <div>
    <div class="md-5"></div>

    <div style="margin: 10px 0">
      <el-input
        class="fl"
        style="width: 200px"
        placeholder="請輸入你的文件名"
        suffix-icon="el-icon-search"
        v-model="username"
      ></el-input>
      <el-button class="fl ml-5" type="primary" @click="handleSearch">搜索</el-button>
    </div>

    <div style="clear: both; margin: 10px" class="fl"></div>

    <el-table
      :data="tableData"
      stripe
      style="width: 100%"
      @cell-mouse-enter="handleMouseEnter"
      @cell-mouse-leave="handleMouseLeave"
      ref="taskTable"
    >
      <el-table-column prop="name" label="文件名" width="400"></el-table-column>

      <el-table-column prop="date" label width="120">
        <template slot-scope="scope">
          <div
            @mouseenter="handleMouseEnter(scope.row, scope.column, scope.$index)"
            @mouseleave="handleMouseLeave(scope.row, scope.column, scope.$index)"
          >
            <div v-if="scope.row.isRevealed">
              <el-link
                type="primary"
                @click="handleShare(scope.row)"
                :underline="false"
                class="mr-5"
              >
                <i class="el-icon-share"></i>
              </el-link>
              <el-link
                type="primary"
                @click="handleShare(scope.row)"
                :underline="false"
                class="mr-5"
              >
                <i class="el-icon-download"></i>
              </el-link>
              <el-link
                type="primary"
                @click="handleShare(scope.row)"
                :underline="false"
                class="mr-5"
              >
                <i class="el-icon-delete"></i>
              </el-link>
              <el-link
                type="primary"
                @click="handleShare(scope.row)"
                :underline="false"
                class="mr-5"
              >
                <i class="el-icon-edit"></i>
              </el-link>
              <el-link
                type="primary"
                @click="handleShare(scope.row)"
                :underline="false"
                class="mr-5"
              >
                <i class="el-icon-right"></i>
              </el-link>
            </div>
            <div v-else></div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="date" label="修改日期" width="500"></el-table-column>

      <el-table-column prop="address" label="大小" sortable></el-table-column>
    </el-table>

    <div class="fl">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 5, 10, 15]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "ZipFileView",
  data() {
    const item = [
      {
        id: "1",
        date: "2016-05-02",
        name: "王小虎1",
        address: "广州",
        isRevealed: true
      },
      {
        id: "2",
        date: "2016-05-02",
        name: "王小虎2",
        address: "佛山",
        isRevealed: false
      }
    ];

    return {
      username: "",
      tableData: item,
      collapseBtn: "el-icon-s-fold",
      isCollapse: false,
      sideWidth: 200,
      isShowText: true,
      total: 0,
      pageNum: 1,
      pageSize: 5,
      dialogFormVisible: false,
      formLabelWidth: "120px",
      form: {
        username: "",
        mobile: "",
        email: "",
        gender: ""
      },
      resetForm: {
        username: "",
        mobile: "",
        email: "",
        gender: ""
      },
      showAction: false,
      isRevealed: true,
      hoveredRowId: ""
    };
  },
  created() {
    // this.load();
  },
  methods: {
    revealRow(rowId) {
      const obj = this.tableData.find(item => item.id == rowId);
      obj.isRevealed = true;
    },
    hideRow(rowId) {
      const obj = this.tableData.find(item => item.id == rowId);
      obj.isRevealed = false;
    },
    handleMouseLeave(row, column, rowIndex) {
      this.hoveredRowId = null; // 清除悬停行的id
      this.hideRow(row.id); // 隐藏该行数据
    },
    handleMouseEnter(row, column, rowIndex) {
      this.hoveredRowId = row.id; // 设置当前悬停行的id
      this.revealRow(row.id); // 显示该行数据
      // 在这里，你可以根据 row, column, rowIndex 等参数来获取和处理特定单元格的数据
    },
    handleShare(row) {
      console.log("row", row);
    },
    handleCommand(command) {
      this.$message("click on item " + command);
    },
    callcoll() {
      debugger;
      this.isCollapse = !this.isCollapse;
      if (this.isCollapse) {
        this.sideWidth = 64;
        this.collapseBtn = "el-icon-s-unfold";
        this.isShowText = false;
      } else {
        this.sideWidth = 200;
        this.collapseBtn = "el-icon-s-fold";
        this.isShowText = true;
      }
    },
    successMes() {
      this.$message({
        message: "添加成功",
        type: "success",
        duration: 2000
      });
    },
    errorMes() {
      this.$message.error("服務器出錯");
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.load();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.load();
    },
    load() {
      request
        .get("http://localhost:80/ums/selectPage", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize
          }
        })
        .then(res => {
          this.tableData = res.res.data;
          this.total = res.total;
        });
    },
    findByName() {
      request
        .get("http://localhost:80/ums/selectByName", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            username: this.username
          }
        })
        .then(res => {
          this.tableData = res.res.data;
          this.total = res.total;
        });
    },
    submitForm() {
      request.post("http://localhost:80/ums/insert", this.form).then(res => {
        if (res.code == 200) {
          this.successMes();
          this.dialogFormVisible = false;
        } else {
          this.errorMes();
          this.dialogFormVisible = false;
        }
        this.dialogFormVisible = false;
        this.form = this.resetForm;
      });
    },
    handleSearch() {
      this.pageNum = 1;
      this.findByName();
    },
    handleReset() {
      this.username = "";
      this.load();
    }
  }
};
</script>

<style>
</style>