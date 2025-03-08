<template>
  <div class="register">
    <div class="content">
      <div class="title">
        <span
          style="
            font-family: Helvetica Neue;
            color: rgb(51, 51, 51);
            font-size: 24px;
          "
          >Welcome to register</span
        >
      </div>

      <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
        <el-form-item label="" prop="username">
          <el-input
            placeholder="Please input your account"
            class=""
            style="width: 300px"
            size="large"
            v-model="ruleForm.username"
            icon="el-icon-user-solid"
            prefix-icon="el-icon-user"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            placeholder="Please input your passwrod"
            show-password
            class="mt-5"
            style="width: 300px"
            size="large"
            prefix-icon="el-icon-info"
            v-model="ruleForm.password"
            prop="password"
          ></el-input>
        </el-form-item>
        <el-form-item prop="checkPwd">
          <el-input
            placeholder="Please confirm your passwrod"
            show-password
            class="mt-5"
            style="width: 300px"
            size="large"
            prefix-icon="el-icon-info"
            v-model="ruleForm.checkPwd"
            prop="checkPwd"
          ></el-input>
        </el-form-item>
      </el-form>

      <div>
        <el-button
          style="width: 300px; height: 40px"
          type="primary"
          @click="handleAdd"
          ><span style="font-family: Helvetica Neue; font-size: 20px"
            >Register</span
          ></el-button
        >
      </div>
    </div>
  </div>
</template>

<script>
import { register } from "@/api/user";
import request from "@/utils/request";

export default {
  name: "LoginView",
  data() {
    var testValid = (rule, value, callback) => {
      if (value == "") {
        callback(new Error("Please input your password"));
      } else {
        if (this.ruleForm.checkPass !== "") {
          this.$refs.ruleForm.validateField("checkPass");
        }
        callback();
      }
    };
    return {
      ruleForm: {
        username: "",
        password: "",
        checkPwd: "",
      },
      rules: {
        username: {
          required: true,
          message: "Please input your name",
          trigger: "blur",
        },
        password: {
          required: true,
          message: "Please input your password",
          trigger: "blur",
        },
        checkPwd: {
          required: true,
          message: "Please confirm your password",
          trigger: "blur",
        },
      },
    };
  },
  methods: {
    async handleAdd() {
      console.log("thisform", this.ruleForm);
      if (this.validateFrom(this.ruleForm)) {
        const param = {
          username: this.ruleForm.username,
          password: this.ruleForm.password,
        };
        const res = await register(param);
        this.$message({
          message: res.message,
          type: "success",
          duration: 2000,
        });
        setTimeout(() => {
          this.$router.push("/login");
        }, 1000);
      }
    },
    validateFrom(form) {
      // 注意这里 true 的拼写
      if (form.username == "" || form.password == "" || form.checkPwd == "") {
        this.$message({
          message: "Please input your account name and password",
          type: "error",
          duration: 2000,
        });
        return false;
      } else if (form.password != form.checkPwd) {
        this.$message({
          message: "Please ensure that the passwords you enter twice are the same",
          type: "error",
          duration: 2000,
        });
        return false;
      } else {
        return true;
      }
    },
  },
};
</script>

<style>
</style>