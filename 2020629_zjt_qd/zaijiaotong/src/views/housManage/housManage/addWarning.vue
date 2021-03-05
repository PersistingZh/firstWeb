<template>
  <div class="editBox">
    <div class="titleBar">
      <el-button @click="$router.go(-1)">返回</el-button>
    </div>
    <div class="contentBox">
      <el-form ref="postData" :rules="rules" :model="postData">
        <el-form-item class="inputBox" label="预警类型">
          <el-select v-model="postData.warningType" placeholder="定位预警">
            <el-option
              v-for="item in optionsyjlx"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item prop="warningName" class="inputBox" label="预警名称">
          <el-input
            v-model="postData.warningName"
            placeholder="预警名称"
            clearable
          />
        </el-form-item>

        <el-form-item prop="warningContent" class="inputBox" label="预警内容">
          <el-input
            v-model="postData.warningContent"
            placeholder="预警内容"
            clearable
          />
        </el-form-item>

        <el-form-item prop="startTime" class="inputBox" label="预警生效时间">
          <el-date-picker
            v-model="postData.startTime"
            type="date"
            placeholder="年/月/日"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <el-form-item prop="endTime" class="inputBox" label="预警结束时间">
          <el-date-picker
            v-model="postData.endTime"
            type="date"
            placeholder="年/月/日"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <el-form-item prop="warningRange" class="inputBox" label="预警范围单位 (米)">
          <el-input
            v-model.number="postData.warningRange"
            placeholder="预警范围单位"
            clearable
          />
        </el-form-item>

        <el-form-item class="inputBox locationBox" label="所在精纬度" required>
          <div class="locationBtn">
            <el-form-item>
              <el-input
                v-model="postData.warningLatitude"
                placeholder="请输入纬度"
                clearable
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="postData.warningLongitude"
                placeholder="请输入经度"
                clearable
              />
            </el-form-item>
            <el-button
              icon="el-icon-search"
              @click="dialogTableVisible = true"
            />
          </div>
        </el-form-item>

        <el-form-item required class="inputBox" label="所在地址">
          <el-input
            v-model="postData.warningDesc"
            placeholder="请输入所在地址"
            clearable
          />
        </el-form-item>

        <div class="subBtn">
          <el-button
            :loading="isLockBtn"
            type="primary"
            size="small"
            @click="sub('postData')"
          >保存</el-button>
        </div>
      </el-form>

      <!-- 弹出层 -->
      <el-dialog title="获取经纬度" :visible.sync="dialogTableVisible">
        <div
          class="searcbar"
          style="width:100%;margin-top: -35px;padding-bottom: 10px;border-bottom: 3px solid #3c9cFF;"
        >
          <el-select
            v-model="mapdz"
            placeholder="请选择"
            @change="selectOption"
          >
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
            <el-input
              v-model.number="mapCenter.lng"
              placeholder="请输入经度"
              type="text"
              clearable
            />纬度:
            <el-input
              v-model.number="mapCenter.lat"
              placeholder="请输入纬度"
              type="text"
              clearable
            />
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
          />
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogTableVisible = false">取 消</el-button>
          <el-button type="primary" @click="btnDialogclose">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { doorInfoListByCondition, relateUserViewList } from '@/api/housManage'
import { addZjtWarning, updateZjtWarning } from '@/api/jztList'
import coordtransform from 'coordtransform'

// import { Row } from 'element-ui'
// import idcard from 'idcard'

// import BMap from 'BMap'
// eslint-disable-next-line prefer-const
// let nodeId = 440499000000

export default {
  name: 'AddWarning',
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
        warningName: [
          {
            type: 'string',
            required: true,
            message: '请输入预警名称',
            trigger: 'blur'
          }
        ],
        startTime: [
          {
            type: 'string',
            required: true,
            message: '请选择预警生效时间',
            trigger: 'change'
          }
        ],
        endTime: [
          {
            type: 'string',
            required: true,
            message: '请选择预警结束时间',
            trigger: 'change'
          }
        ],
        warningRange: [
          {
            required: true, message: '预警范围单位不能为空'
          },
          {
            type: 'number', message: '预警范围单位不能是数字'
          }
        ]
      },
      optionsyjlx: [
        {
          label: '定位预警',
          value: 1
        }
      ],
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
        warningType: '',
        warningName: '',
        startTime: '',
        endTime: '',
        id: '',
        warningLatitude: '',
        warningLongitude: '',
        warningDesc: '',
        warningRange: 30,
        warningContent: ''
      },
      qyOptions: [],
      options: [],
      optionslc: [],
      // optionsQy: { children: [] },
      optionqy: [],
      optionsh: []
    }
  },
  beforeCreate() {
    if (this.$route.params.id === 0) {
      this.$route.meta.title = '添加预警管理信息'
    } else {
      this.$route.meta.title = '修改预警管理信息'
    }
  },
  async created() {
    this.postData.warningType = this.$route.params.yjlx === 1 ? 1 : 1
    this.postData.warningName = this.$route.params.yjmc
    this.postData.startTime = this.$route.params.yjsxsj
    this.postData.endTime = this.$route.params.yjjssj
    this.postData.warningLatitude = this.$route.params.warningLatitude
    this.postData.warningLongitude = this.$route.params.warningLongitude
    this.postData.warningDesc = this.$route.params.yjaqfw
    this.postData.id = this.$route.params.id
    this.postData.warningRange = this.$route.params.warningRange
    this.postData.warningContent = this.$route.params.warningContent
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
      var startTimeTest = this.postData.startTime
      var endTimeTest = this.postData.endTime
      console.log(this.postData.warningLatitude, this.postData.warningLongitude, this.postData.warningDesc)
      if (this.postData.warningLatitude === undefined || this.postData.warningLongitude === undefined || this.postData.warningDesc === undefined || this.postData.warningLatitude === '' || this.postData.warningLongitude === '' || this.postData.warningDesc === '') {
        this.$message({
          message: '请选择经纬度和地址'
        })
        return
      } else if (startTimeTest === undefined || endTimeTest === undefined || startTimeTest > endTimeTest) {
        this.$message({
          message: '预警结束时间不能大于预警生效时间'
        })
      } else {
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.isLockBtn = true
            if (this.$route.params.id === 0) {
              addZjtWarning(this.postData)
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
              updateZjtWarning(this.postData)
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
      // console.log('bd09togcj02', bd09togcj02)

      const gcj02towgs84 = coordtransform.gcj02towgs84(
        bd09togcj02[0],
        bd09togcj02[1]
      )
      // console.log('gcj02towgs84', gcj02towgs84)
      this.mapCenter.lng = gcj02towgs84[0]
      this.mapCenter.lat = gcj02towgs84[1]
      // 给坐标系赋值

      this.postData.warningLatitude = this.mapCenter.lat
      this.postData.warningLongitude = this.mapCenter.lng
      this.postData.warningDesc = this.mapLocation.address
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
