<template>
  <div class="login-container">
    <div
      style="margin-top: 200px;text-align: right;width: 60%;color: white;height:500px;float:left;"
    >
      <div style="width:41%;margin-top:90px;float:left;">
        <!-- <img src="../../assets/404_images/logo1.png"> -->
      </div>
      <div style="width:58%;margin-top:140px;float:left;text-align:left;padding-left:30px;">
        <span style="font-size: 49px;font-weight: bolder;">在 矫 通 后 台 管 理</span>
        <br>
        <span
          style="font-size: 28px;font-weight: bold;display: inline-block;margin-top: -10px;padding-top:15px;"
        >ZAI JIAO TONG HOU TAI GUANG LI</span>
        <br>
        <br>
        <br>
      </div>
    </div>
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
      auto-complete="on"
      label-position="left"
    >
      <div style="height:70px;">
        <div
          id="zhmmdl"
          class="title-container"
          style="float: left;width:50%;border:1px solid #409EFF;background: lightseagreen;margin-top:27px;"
        >
          <h3
            class="title"
            style="margin-top: 10px;margin-bottom: 10px;font-size:18px;"
            @click="zhmmdl"
          >账号密码登录</h3>
        </div>
        <div
          id="sjhmdl"
          class="title-container"
          style="float:left;width:50%;border:1px solid #409EFF;margin-top:27px;"
        >
          <h3
            class="title"
            style="margin-top: 10px;margin-bottom: 10px;font-size:18px;"
            @click="sjhmdl"
          >手机号码登录</h3>
        </div>
      </div>

      <div id="userLogin">
        <el-form-item prop="username">
          <span class="svg-container">
            <svg-icon icon-class="user" />
          </span>
          <el-input
            ref="username"
            v-model="loginForm.username"
            placeholder="用户名"
            name="username"
            type="text"
            tabindex="1"
            auto-complete="on"
          />
        </el-form-item>

        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="passwordType"
            ref="password"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="密码"
            name="password"
            tabindex="2"
            auto-complete="on"
            @keyup.enter.native="handleLogin"
          />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>
      </div>

      <div id="sjhmlogin" style="display:none;">
        <el-form-item prop="iphone">
          <span class="svg-container">
            <svg-icon icon-class="iphone" />
          </span>
          <el-input
            ref="iphone"
            v-model="loginForm2.iphone"
            placeholder="手机号码"
            name="iphone"
            type="text"
            tabindex="1"
            auto-complete="on"
          />
        </el-form-item>
        <el-form-item prop="verificationCode">
          <span class="svg-container">
            <svg-icon icon-class="yzm" />
          </span>
          <el-input
            ref="verificationCode"
            v-model="loginForm2.verificationCode"
            placeholder="验证码"
            name="verificationCode"
            type="text"
            tabindex="1"
            auto-complete="on"
            style="width:65%;"
          />
          <el-button type="primary" :disabled="isDisabled" style="display: inline-block;" @click="sendoutCode">{{yzmtime}}</el-button>
        </el-form-item>
      </div>

      <el-button
        id="zhmmbuttom"
        :loading="loading"
        type="primary"
        style="width:100%;margin-bottom:30px;"
        @click="handleLogin"
      >登陆</el-button>

      <el-button
        id="sjhmbuttom"
        :loading="loading2"
        type="primary"
        style="width:100%;margin-bottom:30px;margin-left:0;display:none;"
        @click="handleLogin2"
      >登陆</el-button>

      <div class="tips">
        <!-- <span style="margin-right:20px;"></span>
        <span></span>-->
      </div>
    </el-form>
  </div>
</template>

<script>
import { sendoutVerificationCode } from '@/api/user'
import addAttendanceVue from '../housManage/tenantExamine/addAttendance.vue'
export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入账户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginForm2: {
        iphone: '',
        verificationCode: '',
        type: 2
      },
      loginRules: {
        username: [
          { required: true, trigger: 'blur', validator: validateUsername }
        ],
        password: [
          { required: true, trigger: 'blur', validator: validatePassword }
        ]
      },
      loading: false,
      loading2: false,
      passwordType: 'password',
      redirect: undefined,
      yzmtime: '发送验证码',
      isDisabled: false,
      showTime: 60
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    sjhmdl() {
      var userLogin = document.getElementById('userLogin')
      var sjhmlogin = document.getElementById('sjhmlogin')
      var zhmmdl = document.getElementById('zhmmdl')
      var sjhmdl = document.getElementById('sjhmdl')
      var zhmmbuttom = document.getElementById('zhmmbuttom')
      var sjhmbuttom = document.getElementById('sjhmbuttom')
      userLogin.style.display = 'none'
      sjhmlogin.style.display = 'block'
      zhmmdl.style.background = 'none'
      sjhmdl.style.background = 'lightseagreen'
      zhmmbuttom.style.display = 'none'
      sjhmbuttom.style.display = 'block'
    },
    zhmmdl() {
      var userLogin = document.getElementById('userLogin')
      var sjhmlogin = document.getElementById('sjhmlogin')
      var zhmmdl = document.getElementById('zhmmdl')
      var sjhmdl = document.getElementById('sjhmdl')
      var zhmmbuttom = document.getElementById('zhmmbuttom')
      var sjhmbuttom = document.getElementById('sjhmbuttom')
      userLogin.style.display = 'block'
      sjhmlogin.style.display = 'none'
      zhmmdl.style.background = 'lightseagreen'
      sjhmdl.style.background = 'none'
      zhmmbuttom.style.display = 'block'
      sjhmbuttom.style.display = 'none'
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$store
        .dispatch('user/login', this.loginForm)
        .then(() => {
          this.$router.push({ path: '/housList' })
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    handleLogin2() {
      this.$store
        .dispatch('user/login2', this.loginForm2)
        .then(() => {
          this.$router.push({ path: '/housList' })
          this.loading = false
        })
        .catch(() => {
          this.loading = false
        })
    },
    sendoutCode() {
      var _this = this;
      // _this.isDisabled = true
      // setInterval(function(){
      //   console.log(_this.showTime)
      //   if(_this.showTime > 0) {
      //     _this.yzmtime =  parseInt(_this.showTime--) + 's'
      //     _this.isDisabled = true
      //   } else {
      //     _this.isDisabled = false
      //     _this.yzmtime = '发送验证码'
      //   }
      //   if(_this.isDisabled === false) {
      //       _this.showTime = 60
      //   }
      // },1000)
      _this.showTime = 60
      function settime() { 
        if (_this.showTime > 0) { 
          _this.isDisabled = true
          _this.yzmtime = '重新发送' + parseInt(_this.showTime--)
        } else { 
          _this.isDisabled = false
          _this.yzmtime = '获取验证码'
          _this.showTime = 0; 
          clearInterval(myVar)
        } 
         console.log(_this.showTime)
      }
      var myVar =setInterval(function(){
        settime()
      },1000)
      sendoutVerificationCode(this.loginForm2.iphone).then(res => {
      })
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  // display: flex;
  background-image: url('../../assets/404_images/bg.jpg');
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 220px 35px 0;
    margin: 0 auto;
    overflow: hidden;
    margin-left: -50px;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
