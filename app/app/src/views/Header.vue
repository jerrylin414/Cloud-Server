<template>
  <div style="font-size: 12px; display: flex">
    <div style="flex: 1; font-size: 18px; text-align: left; cursor: pointer">
      <span :class="collapseBtn" @click="callcoll"></span>
      MyCloud
      <!-- <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>
          <a href="/">用户管理</a>
        </el-breadcrumb-item>
      </el-breadcrumb> -->
    </div>

    <el-dropdown @command="handleCommand">
      <span style="color: #000; cursor: pointer">
        {{ username }}
        <i class="el-icon-arrow-down"></i>
      </span>

      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="person">Personal Settings</el-dropdown-item>
        <el-dropdown-item command="logout">Quit</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';

export default {
  name: "HeaderView",
  props: {
    collapseBtn: String,
    callcoll: Function,
  },
  data() {
    return {
      username: "",
    };
  },
  computed:{
    ...mapGetters('user',['getUser'])
  },
  created() {
    this.username = this.getUser.username;
  },
  methods: {
    handleCommand(command) {
      if (command == "person") {

      } else {
        this.$store.dispatch('user/logout')
        this.$router.push("/")

      }
    },
  },
};
</script>

<style>
</style>