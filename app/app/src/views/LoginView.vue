<template>
  <div class="page">
    <div class="left-image">
      <img src="@/assets/logo.png" width="80%" />
    </div>
    <div class="login">
      <div class="content">
        <div class="title">
          <span
            style="
              font-family: Helvetica Neue;
              color: rgb(51, 51, 51);
              font-size: 24px;
            "
            >Welcome
          </span>
        </div>
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
          <el-form-item label prop="username">
            <el-input
              placeholder="Please input your account"
              class
              style="width: 300px"
              size="large"
              v-model="ruleForm.username"
              icon="el-icon-user-solid"
              prefix-icon="el-icon-user"
            ></el-input>
          </el-form-item>
          <el-form-item label prop="password">
            <el-input
              placeholder="Please input your password"
              show-password
              class="mt-5"
              style="width: 300px"
              size="large"
              prefix-icon="el-icon-info"
              v-model="ruleForm.password"
            ></el-input>
          </el-form-item>
          <el-form-item label>
            <el-select
              placeholder="Please select your permissions"
              size="large"
              style="width: 300px"
              v-model="ruleForm.permission"
            >
              <el-option label="user" value="user"></el-option>
              <el-option label="admin" value="admin"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <el-button
          style="width: 150px; height: 40px"
          type="primary"
          @click="handleAdd"
        >
          <span style="font-family: Helvetica Neue; font-size: 20px">Register</span>
        </el-button>
        <el-button
          style="width: 150px; height: 40px"
          type="primary"
          @click="handleLogin"
        >
          <span style="font-family: Helvetica Neue; font-size: 20px">Login</span>
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import { mapState, mapMutations, mapActions, mapGetters } from "vuex";
export default {
  name: "LoginView",
  data() {
    return {
      ruleForm: {
        username: "",
        password: "",
        // permission: "",
      },
      rules: {
        username: {
          required: true,
          message: "Please input your name",
          trigger: "blur",
        },
        password: { required: true, message: "Please input your password", trigger: "blur" },
        checkPwd: { required: true, message: "Please confirm your password", trigger: "blur" },
      },
    };
  },
  rules: {
    username: {
      required: true,
      message: "Please input your name",
      trigger: "blur",
    },
    password: { required: true, message: "Please input your password", trigger: "blur" },
    checkPwd: { required: true, message: "Please confirm your password", trigger: "blur" },
  },
  computed: {
    // ...mapState(['count','list']),
    // ...mapGetters(['filterList']),
  },
  methods: {
    // ...mapMutations(['addCount']),
    // ...mapActions(['asyncAddCount']),
    async handleLogin() {
      let form = this.ruleForm;
      await this.$store.dispatch("user/login", form);
      this.successMes("login success");
      setTimeout(() => {
        this.$router.push("/filesView");
      }, 1000);
    },
    handleAdd() {
      this.successMes("welcome");
      setTimeout(() => {
        this.$router.push("/register");
      }, 1000);
    },
    successMes(msg) {
      this.$message({
        message: msg,
        type: "success",
        duration: 2000
      });
    },
    errorMes(msg) {
      this.$message.error(msg);
    },
  },
};
</script>

<style>
</style>
