<template>
  <div class="editBox">
    <div class="titleBar">
      <el-button @click="$router.go(-1)">返回</el-button>
    </div>
    <div class="contentBox">
      <el-form ref="postData" :rules="rules" :model="postData">

        <el-form-item prop="name" class="inputBox" label="姓名">
          <el-input v-model="postData.name" placeholder="姓名" clearable />
        </el-form-item>

        <el-form-item prop="cardType" class="inputBox" label="证件类型">
          <el-select v-model="postData.cardType" placeholder="二代居民身份证">
            <el-option
              v-for="item in optionszjlx"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item prop="cardNumber" class="inputBox" label="证件号码">
          <el-input v-model="postData.cardNumber" placeholder="证件号码" clearable />
        </el-form-item>

        <el-form-item prop="units" class="inputBox" label="所属单位">
          <el-input v-model="postData.units" placeholder="所属单位" clearable />
        </el-form-item>

        <el-form-item prop="department" class="inputBox" label="部门">
          <el-input v-model="postData.department" placeholder="请输入部门" clearable />
        </el-form-item>

        <el-form-item prop="position" class="inputBox" label="职位">
          <el-input v-model="postData.position" placeholder="职位" clearable />
        </el-form-item>

        <el-form-item prop="phone" class="inputBox" label="手机号码">
          <el-input v-model="postData.phone" placeholder="手机号码" clearable />
        </el-form-item>

        <div class="subBtn">
          <el-button :loading="isLockBtn" type="primary" size="small" @click="sub('postData')">保存</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import {
  doorInfoListByCondition,
  relateUserViewList
} from '@/api/housManage'
import { addZjtGzry, updateZjtGzry } from '@/api/jztList'
// import idcard from 'idcard'

// import BMap from 'BMap'
// eslint-disable-next-line prefer-const
// let nodeId = 440499000000

export default {
  name: 'AddgzPerson',
  components: { },
  data() {
    return {
      isLockBtn: false,
      cascaderProps: {
        expandTrigger: 'hover'
        // lazy: true,
        // lazyLoad: this.lazyLoad
      },

      rules: {
        name: [
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
            trigger: 'blur'
          }
        ]
      },
      optionszjlx: [
        { label: '二代居民身份证', value: '二代居民身份证' }, { label: '港澳居民往来内地通行证', value: '港澳居民往来内地通行证' }, { label: '台胞证', value: '台胞证' }, { label: '香港永久性居民身份证', value: '香港永久性居民身份证' }, { label: '护照', value: '护照' }, { label: '其他', value: '其他' }
      ],
      optionsbm: [
      ],
      urlPaths: [],
      qyName: '',
      postData: {
        name: '',
        cardType: '',
        cardNumber: '',
        units: '',
        position: '',
        department: '',
        // password: '',
        phone: '',
        id: ''
      },
      qyOptions: [],
      options: [],
      optionslc: [],
      optionqy: [],
      optionsh: [],
      optionszjsex: [
        {
          value: '男',
          label: '男'
        },
        {
          value: '女',
          label: '女'
        }
      ]
    }
  },
  beforeCreate() {
    if (this.$route.params.id === 0) {
      this.$route.meta.title = '添加工作人员信息'
    } else {
      this.$route.meta.title = '修改工作人员信息'
    }
  },
  async created() {
    this.postData.name = this.$route.params.gzryxm
    this.postData.cardType = this.$route.params.zjlx
    this.postData.cardNumber = this.$route.params.zjhm
    this.postData.units = this.$route.params.ssdw
    this.postData.department = this.$route.params.bm
    this.postData.position = this.$route.params.zw
    this.postData.phone = this.$route.params.sjh
    this.postData.id = this.$route.params.id
  },
  methods: {
    // lazyLoad(node, resolve) {
    //   console.log(node, 'node')
    //   const { level } = node
    //   if (level === 0) {
    //     getSysDict(this.$store.getters.userPath)
    //       .then(result => {
    //         const { data } = result
    //         console.log(data, 123)
    //         const node = []
    //         data.map(item => {
    //           const n = {
    //             value: item.sdCode,
    //             label: item.sdName,
    //             leaf: item.sdLevel === 3
    //           }
    //           node.push(n)
    //         })
    //         resolve(node)
    //       })
    //       .catch(err => {
    //         console.error(err)
    //       })
    //   } else {
    //     const path = node.data.value
    //     const leaf = node.data.leaf
    //     if (leaf) {
    //       resolve([])
    //       return
    //     }
    //     getSysChildDict(path)
    //       .then(result => {
    //         const { data } = result
    //         console.log(data, 123)
    //         const node = []
    //         data.map(item => {
    //           const n = {
    //             value: item.sdCode,
    //             label: item.sdName,
    //             leaf: item.sdLevel === 3
    //           }
    //           node.push(n)
    //         })
    //         resolve(node)
    //       })
    //       .catch(err => {
    //         console.error(err)
    //       })
    //   }
    // },
    // async getDic(id, parent) {
    //   await getSysDict(id, parent).then(res => {
    //     const { data } = res
    //     data.map(dataItem => {
    //       const n = {
    //         value: dataItem.sdCode,
    //         label: dataItem.sdName,
    //         children: dataItem.sdLevel !== 3 ? [] : null
    //       }
    //       parent.children.push(n)
    //       if (dataItem.sdLevel !== 3) {
    //         this.getChildDic(dataItem.sdCode, n)
    //       }
    //     })
    //   })
    // },
    // async getChildDic(id, parent) {
    //   await getSysChildDict(id, parent).then(res => {
    //     const { data } = res
    //     data.map(dataItem => {
    //       const n = {
    //         value: dataItem.sdCode,
    //         label: dataItem.sdName,
    //         children: dataItem.sdLevel !== 3 ? [] : null
    //       }
    //       parent.children.push(n)
    //       if (dataItem.sdLevel !== 3) {
    //         this.getChildDic(dataItem.sdCode, n)
    //       }
    //     })
    //   })
    // },
    sub(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.isLockBtn = true
          if (this.$route.params.id === 0) {
            addZjtGzry(this.postData)
              .then(res => {
                this.$message({
                  type: 'success',
                  message: '保存成功'
                })
                this.isLockBtn = false
                this.$router.go(-1)
              })
              .catch(err => {
                console.error(err)
                this.isLockBtn = false
              })
          } else {
            updateZjtGzry(this.postData)
              .then(res => {
                this.$message({
                  type: 'success',
                  message: '修改成功'
                })
                this.isLockBtn = false
                this.$router.go(-1)
              })
              .catch(err => {
                console.error(err)
                this.isLockBtn = false
              })
          }
        } else {
          return false
        }
      })
    },
    async getInfo(id, path) {
      await relateUserViewList(id)
        .then(res => {
          let paths = []
          paths = path.split('#')
          paths.shift()
          paths.shift()
          paths.pop()
          this.postData.qyName = paths
          this.postData.qyCode = paths[path.length - 1]
          this.changeQu(paths)
          this.changeJzxq(res.data[0].fwId)

          this.postData.yzId = res.data[0].yzId
          this.postData.id = res.data[0].yzId
          this.postData.xm = res.data[0].xm
          this.postData.zjhm = res.data[0].zjhm
          this.postData.zjlx = res.data[0].zjlx
          this.postData.sjhm = res.data[0].sjhm
          this.postData.xb = res.data[0].xb
          this.postData.sfzdz = res.data[0].sfzdz
          this.postData.csrq = res.data[0].csrq
          this.postData.kssj = res.data[0].kssj
          this.postData.jssj = res.data[0].jssj
          this.postData.doorId = res.data[0].doorId
          this.postData.filePath = res.data[0].filePath
          if (this.postData.filePath) {
            this.urlPaths.push(this.postData.filePath)
          }

          return res
        })
        .catch(e => {})

      this.postData.floorId = this.$route.params.floorId
      this.postData.relateId = this.$route.params.relateId

      await doorInfoListByCondition(this.postData.fwId, this.postData.floorId)
        .then(res => {
          for (var i = 0; i < res.data.length; i++) {
            this.optionsh.push({
              value: res.data[i].id,
              label: res.data[i].mph
            })
          }
        })
        .catch(e => {})
    }
  }
}
</script>
<style lang="scss">
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
.text_p {
  margin-bottom: 0;
}
</style>
