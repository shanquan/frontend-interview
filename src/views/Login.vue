<!--
@author: wang.weili
@since : 2020/11/12
-->
<template>
  <div class="login">
    <el-form :model="form" status-icon :rules="rules" ref="form" label-width="65px">
      <el-form-item label="用户名" prop="user">
        <el-input type="text" v-model="form.user" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="pwd">
        <el-input type="password" v-model="form.pwd" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('form')">登录</el-button>
        <el-button @click="resetForm('form')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "login",
  created() {
    Window.isAuthenticated = false;
    this.$root.$children[0].isAuthenticated = false;
    this.$root.logout();
    // or auto login
    // try{
    //   let session = JSON.parse(localStorage.getItem('aSession'));
    //   if(session.token){
    //     this.$root.initSession(session.user,session.token);
    //     this.$root.$children[0].isAuthenticated=true;
    //     this.$router.push({'path':"/"});
    //   }
    // }catch(e){}
  },
  data() {
    var validate = (rule, value, callback) => {
      if (value === "") {
        let tips = "请输入内容";
        switch (rule.field) {
          case "user":
            tips = "请输入用户名";
            break;
          case "pwd":
            tips = "请输入密码";
            break;
        }
        callback(new Error(tips));
      } else {
        callback();
      }
    };
    return {
      form: {
        user: "",
        pwd: ""
      },
      rules: {
        user: [{ validator: validate, trigger: "blur" }],
        pwd: [{ validator: validate, trigger: "blur" }]
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.login();
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.login();
    },
    login() {
      this.$http
        .login({
          account: this.form.user || 2327665, //2327665
          password: this.form.pwd || 12345678 //12345678
        })
        .then(response => {
          let session = {
            user: response.DATA.USER,
            token: response.DATA.TOKEN
          };
          this.$root.initSession(session.user, session.token);
          localStorage.setItem("aSession", JSON.stringify(session));
          this.$root.$children[0].isAuthenticated = true;
          this.$router.push({ path: "/" });
        });
    }
  }
};
</script>

<style>
.login {
  padding: 0 10px;
  margin: 20px auto;
  max-width: 500px;
}
</style>