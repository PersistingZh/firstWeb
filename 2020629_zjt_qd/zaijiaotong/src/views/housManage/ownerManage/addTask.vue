<template>
  <div class="editBox">
    <div class="titleBar">
      <el-button @click="$router.go(-1)">返回</el-button>
    </div>
    <div class="contentBox">
      <el-form ref="postData" :rules="rules" :model="postData">
        <el-form-item prop="taskName" class="inputBox" label="任务名称">
          <el-input v-model="postData.taskName" placeholder="任务名称" clearable />
        </el-form-item>

        <el-form-item prop="taskDesc" class="inputBox" label="任务描述">
          <el-input v-model="postData.taskDesc" placeholder="任务描述" clearable />
        </el-form-item>

        <el-form-item prop="effectiveTime" class="inputBox" label="任务生效时间">
          <el-date-picker
            v-model="postData.effectiveTime"
            type="date"
            placeholder="年/月/日"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <el-form-item prop="failureTime" class="inputBox" label="任务结束时间">
          <el-date-picker
            v-model="postData.failureTime"
            type="date"
            placeholder="年/月/日"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <el-form-item prop="startTime" class="inputBox" label="定位核验开始时间">
          <el-time-picker v-model="postData.startTime" placeholder="任意时间点" value-format="HH:mm:ss" />
        </el-form-item>

        <el-form-item prop="endTime" class="inputBox" label="定位核验结束时间">
          <el-time-picker v-model="postData.endTime" placeholder="任意时间点" value-format="HH:mm:ss" />
        </el-form-item>

        <el-form-item prop="taskRange" class="inputBox" label="任务定位核验范围 (米)">
          <el-input v-model.number="postData.taskRange" placeholder="任务定位核验范围" clearable />
        </el-form-item>

        <el-form-item class="inputBox locationBox" label="所在经纬度" required>
          <div class="locationBtn">
            <el-input v-model="postData.taskLatitude" placeholder="请输入纬度" clearable />
            <el-input v-model="postData.taskLongitude" placeholder="请输入经度" clearable />
            <el-button icon="el-icon-search" @click="dialogTableVisible = true" />
          </div>
        </el-form-item>

        <el-form-item class="inputBox" label="所在地址" required>
          <el-input v-model="postData.taskAddress" placeholder="请输入所在地址" clearable />
        </el-form-item>

        <div class="subBtn">
          <el-button :loading="isLockBtn" type="primary" size="small" @click="sub('postData')">保存</el-button>
        </div>
      </el-form>
    </div>

    <!-- 弹出层 -->
    <el-dialog title="获取经纬度" :visible.sync="dialogTableVisible">
      <div
        class="searcbar"
        style="width:100%;margin-top: -35px;padding-bottom: 10px;border-bottom: 3px solid #3c9cFF;"
      >
        <el-select v-model="mapdz" placeholder="请选择" @change="selectOption">
          <el-option
            v-for="item in mapOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <div v-if="!isShowtwo">
          <el-autocomplete
            v-model="mapLocation.address"
            :fetch-suggestions="querySearch"
            placeholder="请输入地址信息"
            style="width: 100%"
            :trigger-on-focus="false"
            @select="handleSelect"
          />
        </div>
        <div>
          经度:
          <el-input v-model.number="mapCenter.lng" placeholder="请输入经度" type="text" clearable />纬度:
          <el-input v-model.number="mapCenter.lat" placeholder="请输入纬度" type="text" clearable />
        </div>
      </div>
      <div class="inputBox" style="width: 100%;padding:0;padding-top: 5px;">
        <baidu-map
          class="map"
          :center="mapCenter"
          :zoom="mapZoom"
          :scroll-wheel-zoom="true"
          :auto-resize="true"
          ak="baidu-ak"
          @ready="handlerBMap"
          @moving="syncCenterAndZoom"
          @moveend="syncCenterAndZoom"
          @zoomend="syncCenterAndZoom"
          @click="clickBidu"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTableVisible = false">取 消</el-button>
        <el-button type="primary" @click="btnDialogclose">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  doorInfoListByCondition,
  // housListManageAdd,
  relateUserViewList
} from '@/api/housManage'
import { addZjtTask, updateZjtTask } from '@/api/jztList'
// var coordtransform=require('coordtransform');
import coordtransform from 'coordtransform'

// import idcard from 'idcard'

// import BMap from 'BMap'
// eslint-disable-next-line prefer-const
// let nodeId = 440499000000

export default {
  name: 'AddTask',
  components: {},
  data() {
    return {
      isLockBtn: false,
      cascaderProps: {
        expandTrigger: 'hover'
        // lazy: true,
        // lazyLoad: this.lazyLoad
      },

      rules: {
        taskName: [
          {
            type: 'string',
            required: true,
            message: '请输入任务名称',
            trigger: 'blur'
          }
        ],
        effectiveTime: [
          {
            type: 'string',
            required: true,
            message: '请选择任务生效时间',
            trigger: 'change'
          }
        ],
        failureTime: [
          {
            type: 'string',
            required: true,
            message: '请选择任务结束时间',
            trigger: 'change'
          }
        ],
        startTime: [
          {
            type: 'string',
            required: true,
            message: '请选择打卡开始时间',
            trigger: 'change'
          }
        ],
        endTime: [
          {
            type: 'string',
            required: true,
            message: '请选择打卡结束时间',
            trigger: 'change'
          }
        ],
        taskRange: [
          {
            required: true, message: '打卡范围不能为空'
          },
          {
            type: 'number', message: '打卡范围必须是数字'
          }
        ]
      },
      optionszjlx: [],
      urlPaths: [],
      qyName: '',
      mapdz: '地址',
      mapOptions: [
        {
          value: '地址',
          label: '地址'
        },
        {
          value: '经纬度',
          label: '经纬度'
        }
      ],
      isshow: true,
      isShowtwo: false,
      mapLocation: {
        address: undefined,
        coordinate: undefined
      },
      mapCenter: { lng: 0, lat: 0 },
      mapZoom: 15,
      dialogTableVisible: false,
      postData: {
        taskName: '',
        effectiveTime: '',
        failureTime: '',
        startTime: '',
        endTime: '',
        id: '',
        taskLatitude: '',
        taskLongitude: '',
        taskAddress: '',
        cycle: 1,
        taskDesc: '',
        taskRange: 30
      },
      qyOptions: [],
      options: [],
      optionslc: [],
      // optionsQy: { children: [] },
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
      this.$route.meta.title = '添加任务管理信息'
    } else {
      this.$route.meta.title = '修改任务管理信息'
    }
  },
  async created() {
    this.postData.taskName = this.$route.params.rwmc
    this.postData.effectiveTime = this.$route.params.rwsxsj
    this.postData.failureTime = this.$route.params.rwjssj
    this.postData.startTime = this.$route.params.dkkssj
    this.postData.endTime = this.$route.params.dajssj
    this.postData.taskLatitude = this.$route.params.taskLatitude
    this.postData.taskLongitude = this.$route.params.taskLongitude
    this.postData.taskAddress = this.$route.params.dkdd
    this.postData.id = this.$route.params.id
    this.postData.taskDesc = this.$route.params.taskDesc
    this.postData.taskRange = this.$route.params.taskRange
  },
  methods: {
    handlerBMap({ BMap, map }) {
      this.BMap = BMap
      this.map = map
      if (this.mapLocation.coordinate && this.mapLocation.coordinate.lng) {
        this.mapCenter.lng = this.mapLocation.coordinate.lng
        this.mapCenter.lat = this.mapLocation.coordinate.lat
        this.mapZoom = 15
        map.addOverlay(new this.BMap.Marker(this.mapLocation.coordinate))
      } else {
        this.mapCenter.lng = 113.271429
        this.mapCenter.lat = 23.135336
        this.mapZoom = 10
      }
    },
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
      var effectiveTimeTest = this.postData.effectiveTime
      var failureTime = this.postData.failureTime
      var startTimeTest = this.postData.startTime
      var endTime = this.postData.endTime
      if (this.postData.taskLatitude === undefined || this.postData.taskLongitude === undefined || this.postData.taskAddress === undefined || this.postData.taskLatitude === '' || this.postData.taskLongitude === '' || this.postData.taskAddress === '') {
        this.$message({
          message: '请选择经纬度和地址'
        })
        return
      } else if (effectiveTimeTest === undefined || failureTime === undefined || effectiveTimeTest > failureTime) {
        this.$message({
          message: '任务结束时间不能大于任务生效时间'
        })
        return
      } else if (startTimeTest === undefined || endTime === undefined || startTimeTest > endTime) {
        this.$message({
          message: '结束时间要小于开始时间'
        })
      } else {
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.isLockBtn = true
            if (this.$route.params.id === 0) {
              addZjtTask(this.postData)
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
              updateZjtTask(this.postData)
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
      }
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
    },
    querySearch(queryString, cb) {
      var that = this
      var myGeo = new this.BMap.Geocoder()
      myGeo.getPoint(
        queryString,
        function(point) {
          if (point) {
            that.mapLocation.coordinate = point
            that.makerCenter(point)
          } else {
            that.mapLocation.coordinate = null
          }
        },
        this.locationCity
      )
      var options = {
        onSearchComplete: function(results) {
          if (local.getStatus() === 0) {
            // 判断状态是否正确
            var s = []
            for (var i = 0; i < results.getCurrentNumPois(); i++) {
              var x = results.getPoi(i)
              var item = { value: x.address + x.title, point: x.point }
              s.push(item)
              cb(s)
            }
          } else {
            cb()
          }
        }
      }
      var local = new this.BMap.LocalSearch(this.map, options)
      local.search(queryString)
    },
    handleSelect(item) {
      var { point } = item
      this.mapLocation.coordinate = point
      this.makerCenter(point)
    },
    makerCenter(point) {
      if (this.map) {
        this.map.clearOverlays()
        this.map.addOverlay(new this.BMap.Marker(point))
        this.mapCenter.lng = point.lng
        this.mapCenter.lat = point.lat
        this.mapZoom = 15
      }
    },
    syncCenterAndZoom(e) {
      const { lng, lat } = e.target.getCenter()
      this.mapCenter.lng = lng
      this.mapCenter.lat = lat
      this.zoom = e.target.getZoom()
    },
    selectOption(val) {
      if (val === '地址') {
        this.isshow = true
        this.isShowtwo = false
        this.mapLocation.address = ''
      } else if (val === '经纬度') {
        this.isshow = false
        this.isShowtwo = true
      }
    },
    btnDialogclose() {
      this.dialogTableVisible = false
      // 换算坐标系
      const bd09togcj02 = coordtransform.bd09togcj02(
        this.mapCenter.lng,
        this.mapCenter.lat
      )
      console.log('bd09togcj02', bd09togcj02)

      const gcj02towgs84 = coordtransform.gcj02towgs84(
        bd09togcj02[0],
        bd09togcj02[1]
      )
      console.log('gcj02towgs84', gcj02towgs84)
      this.mapCenter.lng = gcj02towgs84[0]
      this.mapCenter.lat = gcj02towgs84[1]
      // 给坐标系赋值
      this.postData.taskLatitude = this.mapCenter.lat
      this.postData.taskLongitude = this.mapCenter.lng
      this.postData.taskAddress = this.mapLocation.address

      console.log(12312321)

      console.log(this.mapLocation.address)
    },
    clickBidu(type, target, point, pixel, overlay) {
      console.log(type.Ag.lng, type.Ag.lat)

      this.mapCenter.lng = type.Ag.lng
      this.mapCenter.lat = type.Ag.lat
      console.log('点击')
    }
  }
}
</script>
<style lang="scss">
.map {
  width: 100%;
  height: 450px;
}
.locationBox {
  .el-form-item__label {
    float: none;
  }
  .locationBtn {
    display: flex;
    align-items: center;
    .el-input {
      margin-right: 5px;
    }
  }
}
.searcbar {
  padding: 10px 0;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  .el-input,
  .el-select {
    width: 200px;
    margin-right: 10px;
  }
}
.titleBar {
  padding: 6px 12px;
  border-bottom: 3px solid #3c9cff;
}
.contentBox {
  padding: 5px 30px;
}
// .isshow {
//   display: block;
// }
.text_p {
  margin-bottom: 0;
}
</style>
