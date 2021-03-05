<template>
  <div class="editBox">
    <div class="titleBar">
      <el-button @click="$router.go(-1)">返回</el-button>
    </div>
    <div class="contentBox">
      <el-form ref="formData" :rules="rules" :model="postData">

        <el-form-item label="姓名" prop="userName" class="inputBox">
          <el-input v-model="postData.userName" type="text" placeholder="姓名" clearable />
        </el-form-item>

        <el-form-item label="证件号码" class="inputBox">
          <el-input
            v-model="postData.cardNumber"
            type="text"
            placeholder="证件号码"
            clearable
          />
        </el-form-item>

        <el-form-item label="证件类型" class="inputBox">
          <el-select v-model="postData.cardType" placeholder="二代居民身份证">
            <el-option
              v-for="item in optionszjlx"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="手机号码" prop="phone" class="inputBox">
          <el-input v-model="postData.phone" type="text" placeholder="手机号码" clearable />
        </el-form-item>

        <el-form-item label="联系地址" class="inputBox">
          <el-input v-model="postData.address" type="text" placeholder="请输入联系地址" clearable />
        </el-form-item>

        <div class="inputBox">
          <p>用户照片</p>
          <upload-file v-model="urlPaths" :limit-count="1" />
        </div>

        <div class="subBtn">
          <el-button :loading="isLockBtn" type="primary" size="small" @click="sub">保存</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { addZjtZjry, updatezjtZjry } from '@/api/jztList'
import uploadFile from '@/components/UploadFile/index'
export default {
  name: 'AddJzPerson',
  components: { uploadFile },
  data() {
    return {
      isLockBtn: false,
      isBackFill: false,
      rules: {
        userName: [
          {
            type: 'string',
            required: true,
            message: '请输入姓名',
            trigger: 'blur'
          }
        ],
        phone: [
          {
            type: 'string',
            required: true,
            message: '请输入手机号码',
            pattern: /^1[3456789]\d{9}$/,
            trigger: 'blur'
          }
        ]
      },
      urlPaths: [],
      dialogImageUrl: '',
      dialogVisible: false,
      hideUpload: false,

      optionszjlx: [
        { label: '二代居民身份证', value: '二代居民身份证' }, { label: '港澳居民往来内地通行证', value: '港澳居民往来内地通行证' }, { label: '台胞证', value: '台胞证' }, { label: '香港永久性居民身份证', value: '香港永久性居民身份证' }, { label: '护照', value: '护照' }, { label: '其他', value: '其他' }
      ],
      postData: {
        address: '',
        cardNumber: '',
        cardType: '',
        phone: '',
        photoUrl: '',
        userName: '',
        id: ''
      }
    }
  },
  beforeCreate() {
    if (this.$route.params.id === 0) {
      this.$route.meta.title = '添加矫正人员信息'
    } else {
      this.$route.meta.title = '修改矫正人员信息'
    }
  },
  created() {
    this.postData.userName = this.$route.params.userName
    this.postData.cardNumber = this.$route.params.cardNumber
    this.postData.cardType = this.$route.params.cardType
    this.postData.phone = this.$route.params.phone
    this.postData.address = this.$route.params.lxdz
    this.postData.photoUrl = this.$route.params.photoUrl
    this.postData.id = this.$route.params.id
  },
  mounted() {
  },
  methods: {
    changeUploda(file, fileList) {
      this.hideUpload = fileList.length >= 1
    },
    handleRemove(file, fileList) {
      this.hideUpload = fileList.length >= 1
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    sub() {
      this.$refs.formData.validate(val => {
        if (val) {
          this.isLockBtn = true
          this.postData.photoUrl = this.urlPaths.join(',')
          if (this.$route.params.id === 0) {
            addZjtZjry(this.postData)
              .then(res => {
                this.$message({
                  type: 'success',
                  message: '保存成功'
                })
                this.$router.go(-1)
                this.isLockBtn = false
              })
              .catch(err => {
                console.error(err)
                this.isLockBtn = false
              })
          } else {
            updatezjtZjry(this.postData)
              .then(res => {
                this.$message({
                  type: 'success',
                  message: '修改成功'
                })
                this.$router.go(-1)
                this.isLockBtn = false
              })
              .catch(err => {
                console.error(err)
                this.isLockBtn = false
              })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.titleBar {
  padding: 6px 12px;
  border-bottom: 3px solid #3c9cff;
}
.contentBox {
  padding: 5px 30px;
}
.isshow {
  display: block;
}
</style>
