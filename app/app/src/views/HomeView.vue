<template>
  <div style="height: 100vh">
    <el-container style="height: 100%">
      <el-aside :width="sideWidth + 'px'" style="height: 100%">
        <AsideView
          :isCollapse="isCollapse"
          :collapse-transition="false"
          :isShowText="isShowText"
          style="height: 100%"
        ></AsideView>
      </el-aside>

      <el-container>
        <el-header
          style="
            /* text-align: right; */
            border-bottom: 1px solid #ccc;
            background-color: #F2F6FC;
            
          "
        >
          <HeaderView :collapseBtn="collapseBtn" :callcoll="callcoll"></HeaderView>
        </el-header>

        <el-main>
            <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import AsideView from "@/views/Aside.vue";
import HeaderView from "@/views/Header.vue";

export default {
  name: "HomeView",
  components: {
    AsideView,
    HeaderView,
  },
  data() {
    const item = {
      date: "2016-05-02",
      name: "王小虎",
      address: "上海市普陀区金沙江路 1518 弄",
    };
    return {
      username: "",
      tableData: Array(10).fill(item),
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
        gender: "",
      },
      resetForm: {
        username: "",
        mobile: "",
        email: "",
        gender: "",
      },
    };
  },

  methods: {
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
    }
  },
};
</script>

<style>
.el-header {
  background-color: #b3c0d1;
  color: #b3c0d1;
  line-height: 60px;
}

.el-aside {
  color: #b3c0d1;
  overflow: hidden;
}
</style>