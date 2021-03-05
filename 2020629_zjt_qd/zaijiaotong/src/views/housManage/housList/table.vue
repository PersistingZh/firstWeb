<template>
  <div class="table">
    <div ref="searchBar" class="searcbar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-document-add"
            plain
            @click="pushAdd(0)"
          >
            新增
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" plain @click="plpzrw">
            批量配置任务
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" plain @click="plpzyjfw">
            批量配置预警范围
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      ref="table"
      v-loading="loading"
      element-loading-text="拼命加载中"
      :height="tableHeight"
      :data="tableData"
      border
      style="width:100%"
      @selection-change="onTableSelectplpzrw"
    >
      <el-table-column type="selection" width="80" />
      <el-table-column
        prop="huxp"
        align="center"
        label="用户相片"
        width="150px"
      >
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            :disabled="!scope.row.photoUrl"
            @click="openImg(scope, 'photoUrl')"
          >
            预览
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="yhxm"
        align="center"
        label="用户姓名"
        width="150px"
      />
      <el-table-column
        prop="zjlx"
        align="center"
        label="证件类型"
        width="150px"
      />
      <el-table-column
        prop="zjhm"
        align="center"
        label="证件号码"
        width="160px"
      />
      <el-table-column prop="sjh" align="center" label="手机号" width="150px" />
      <el-table-column
        prop="rylx"
        align="center"
        label="人员类型"
        width="150px"
      />
      <el-table-column
        prop="lxdz"
        align="center"
        label="联系地址"
        width="200px"
      />
      <el-table-column
        prop="zhycwz"
        align="center"
        label="最后一次位置"
        width="200px"
      />
      <el-table-column
        prop="zhycjrwzsj"
        align="center"
        label="最后一次计入位置时间"
        width="180px"
      />
      <el-table-column
        prop="sfzx"
        align="center"
        label="是否在线"
        width="100px"
      />
      <el-table-column
        prop="zhyczxsj"
        align="center"
        label="最后一次在线时间"
        width="180px"
      />
      <el-table-column prop="jwq" align="center" label="操作" width="380px">
        <template slot-scope="scope">
          <el-button
            type="danger"
            size="mini"
            @click="openEdit(scope, '预警范围')"
          >
            预警范围
          </el-button>
          <el-button
            type="danger"
            size="mini"
            @click="openEdit(scope, '配置任务')"
          >
            配置任务
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="openEdit(scope, '编辑')"
          >
            编辑
          </el-button>
          <el-button type="danger" size="mini" @click="openEdit(scope, '删除')">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      ref="pageBar"
      background
      :page-sizes="[20, 40, 60, 80]"
      :page-size="100"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <templat-import ref="templatImport" @refresh="getList" />

    <!-- 配置任务弹框 -->
    <el-dialog width="60%" title="任务" :visible.sync="dialogTableVisible">
      <!-- <div
        class="searcbar"
        style="width:100%;margin-top: -35px;padding-bottom: 10px;border-bottom: 3px solid #3c9cFF;"
      /> -->
      <el-table
        ref="table"
        v-loading="loading"
        element-loading-text="拼命加载中"
        :height="tableHeight"
        :data="taskConfiguration"
        class="tabelpzrw2"
        border
        highlight-current-row
        style="width:100%"
        @selection-change="onTableSelect"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
          prop="rwmc"
          align="center"
          label="任务名称"
          width="170px"
        />
        <el-table-column
          prop="dkdd"
          align="center"
          label="打开地点"
          width="120px"
        />
        <el-table-column
          prop="rwsxsj"
          align="center"
          label="任务生效时间"
          width="120px"
        />
        <el-table-column
          prop="rwjssj"
          align="center"
          label="任务结束时间"
          width="120px"
        />
        <el-table-column
          prop="dkkssj"
          align="center"
          label="打卡开始时间"
          width="120px"
        />
        <el-table-column
          prop="dajssj"
          align="center"
          label="打卡结束时间"
          width="120px"
        />
      </el-table>
      <el-button type="danger" size="mini" @click="pzrw">确定配置</el-button>
    </el-dialog>

    <!-- 预警范围弹框 -->
    <el-dialog width="60%" title="预警" :visible.sync="dialogTableVisible2">
      <!-- <div
        class="searcbar"
        style="width:100%;margin-top: -35px;padding-bottom: 10px;border-bottom: 3px solid #3c9cFF;"
      /> -->
      <el-table
        ref="table"
        v-loading="loading"
        element-loading-text="拼命加载中"
        :height="tableHeight"
        :data="yjfwData"
        class="tabelpzrw2"
        border
        highlight-current-row
        style="width:100%"
        @selection-change="onTableSelect2"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
          prop="yjlx"
          align="center"
          label="预警类型"
          width="170px"
        />
        <el-table-column
          prop="yjmc"
          align="center"
          label="预警名称"
          width="120px"
        />
        <el-table-column
          prop="yjsxsj"
          align="center"
          label="预警生效时间"
          width="120px"
        />
        <el-table-column
          prop="yjjssj"
          align="center"
          label="预警结束时间"
          width="120px"
        />
        <el-table-column
          prop="yjaqfw"
          align="center"
          label="预警安全范围"
          width="120px"
        />
      </el-table>
      <el-button type="danger" size="mini" @click="yjfwpz">确定配置</el-button>
    </el-dialog>

    <!-- 批量配置任务弹框 -->
    <el-dialog width="60%" title="所有任务" :visible.sync="dialogTableVisible3">
      <!-- <div
        class="searcbar"
        style="width:100%;margin-top: -35px;padding-bottom: 10px;border-bottom: 3px solid #3c9cFF;"
      /> -->
      <el-table
        ref="table"
        v-loading="loading"
        element-loading-text="拼命加载中"
        :height="tableHeight"
        :data="taskDataPl"
        class="tabelpzrw2"
        border
        highlight-current-row
        style="width:100%"
        @selection-change="onTableSelect3"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
          prop="rwmc"
          align="center"
          label="任务名称"
          width="170px"
        />
        <el-table-column
          prop="dkdd"
          align="center"
          label="打开地点"
          width="120px"
        />
        <el-table-column
          prop="rwsxsj"
          align="center"
          label="任务生效时间"
          width="120px"
        />
        <el-table-column
          prop="rwjssj"
          align="center"
          label="任务结束时间"
          width="120px"
        />
        <el-table-column
          prop="dkkssj"
          align="center"
          label="打卡开始时间"
          width="120px"
        />
        <el-table-column
          prop="dajssj"
          align="center"
          label="打卡结束时间"
          width="120px"
        />
      </el-table>
      <el-button type="danger" size="mini" @click="qdpzpl">确定配置</el-button>
    </el-dialog>

    <!-- 批量配置预警弹框 -->
    <el-dialog width="60%" title="预警" :visible.sync="dialogTableVisible4">
      <el-table
        ref="table"
        v-loading="loading"
        element-loading-text="拼命加载中"
        :height="tableHeight"
        :data="yjpzDataPl"
        class="tabelpzrw2"
        border
        highlight-current-row
        style="width:100%"
        @selection-change="onTableSelect4"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
          prop="yjlx"
          align="center"
          label="预警类型"
          width="170px"
        />
        <el-table-column
          prop="yjmc"
          align="center"
          label="预警名称"
          width="120px"
        />
        <el-table-column
          prop="yjsxsj"
          align="center"
          label="预警生效时间"
          width="120px"
        />
        <el-table-column
          prop="yjjssj"
          align="center"
          label="预警结束时间"
          width="120px"
        />
        <el-table-column
          prop="yjaqfw"
          align="center"
          label="预警安全范围"
          width="120px"
        />
      </el-table>
      <el-button type="danger" size="mini" @click="yjqdpzpl">
        确定配置
      </el-button>
    </el-dialog>
  </div>
</template>

<script>
import templatImport from '@/components/TemplatImport'
import {
  getZjtZjry,
  deleteZjtZjry,
  getZjtTask,
  selectTaskUserRelateView,
  selectzjtTaskUserRelate,
  getZjtWarning,
  plpzZjtZjry,
  plpzyjZjtZjry
} from '@/api/jztList'
export default {
  name: 'HouseListTable',
  components: { templatImport },
  data() {
    return {
      qyOptions: [],
      importTableData: [],
      importTableHeader: [],
      uploadFile: Blob,
      exportInDialogVisible: false,
      templateName: '',

      tableData: [],
      tableHeight: 100,
      total: 0,

      searchForm: {
        limit: 20,
        page: 1,
        phone: ''
      },
      loading: false,
      // 接收矫正人员的id
      searchForm2: {
        taskId: '',
        userId: '',
        limit: 20,
        page: 1
      },
      taskIdArr: [],
      userIdArr: [],
      taskConfiguration: [],
      dialogTableVisible: false,
      multipleSelection: [],
      // 预警范围
      yjfwData: [],
      dialogTableVisible2: false,
      multipleSelection2: [],
      // 接收预警的id
      warningIdArr: [],
      // 用于接收批量配置任务的 矫正人员ID
      alluserIdData: [],
      // 用于接收批量配置任务的 所有的任务ID
      alltaskIdData: [],
      // 用于接收批量配置预警的 预警ID
      allwarningIdData: [],
      // 用于接收批量配置预警的 矫正人员ID
      alluserIdYjData: [],
      dialogTableVisible3: false,
      taskDataPl: [],
      yjpzDataPl: [],
      multipleSelection3: [],
      multipleSelection4: [],
      dialogTableVisible4: false
    }
  },
  computed: {
    // imgDisable(scope) {
    //   console.log(scope, 'row')
    //   return !scope.photoUrl
    // }
  },
  mounted() {},
  activated() {
    this.getList()
    setInterval(() => {
      this.getList()
    }, 60000)
  },
  methods: {
    // 批量配置任务
    plpzrw() {
      if (this.alluserIdData.length <= 0) {
        this.$message({
          message: '请选择要配置的人员'
        })
      } else {
        this.getListplpzselect()
        this.dialogTableVisible3 = true
      }
    },
    qdpzpl() {
      if (this.alltaskIdData.length <= 0) {
        this.$message({
          message: '请选择任务'
        })
      } else {
        plpzZjtZjry(this.alltaskIdData, this.alluserIdData).then(res => {
          this.$message({
            type: 'success',
            message: '批量配置成功'
          })
        })
        this.dialogTableVisible3 = false
      }
    },
    yjqdpzpl() {
      if (this.allwarningIdData.length <= 0) {
        this.$message({
          message: '请选择预警'
        })
      } else {
        plpzyjZjtZjry(this.alluserIdYjData, this.allwarningIdData).then(res => {
          this.$message({
            type: 'success',
            message: '批量配置预警成功'
          })
        })
        this.dialogTableVisible4 = false
      }
    },
    // 批量配置预警范围
    plpzyjfw() {
      if (this.alluserIdYjData.length <= 0) {
        this.$message({
          message: '请选择要配置的人员'
        })
      } else {
        this.getListplpzselectYj()
        this.dialogTableVisible4 = true
      }
    },
    // 任务配置
    pzrw() {
      console.log(this.taskIdArr, 'this.userIdArr')
      if (this.taskIdArr <= 0) {
        this.$message({
          message: '请选择要配置的任务'
        })
      } else {
        plpzZjtZjry(this.taskIdArr, this.userIdArr).then(res => {
          this.$message({
            type: 'success',
            message: '配置成功'
          })
        })
        this.dialogTableVisible = false
      }
    },
    // 预警配置
    yjfwpz() {
      if (this.warningIdArr <= 0) {
        this.$message({
          message: '请选择要配置的预警'
        })
      } else {
        plpzyjZjtZjry(this.userIdArr, this.warningIdArr).then(res => {
          this.$message({
            type: 'success',
            message: '预警配置成功'
          })
        })
        this.dialogTableVisible2 = false
      }
    },
    changeFun(val) {},
    onTableSelect(val) {
      this.taskIdArr = []
      this.multipleSelection = val
      for (var i = 0; i < this.multipleSelection.length; i++) {
        this.taskIdArr.push(this.multipleSelection[i].id)
      }
    },
    onTableSelect2(val) {
      this.warningIdArr = []
      this.multipleSelection2 = val
      for (var i = 0; i < this.multipleSelection2.length; i++) {
        this.warningIdArr.push(this.multipleSelection2[i].id)
      }
    },
    onTableSelect3(val) {
      this.alltaskIdData = []
      this.multipleSelection3 = val
      for (var i = 0; i < this.multipleSelection3.length; i++) {
        this.alltaskIdData.push(this.multipleSelection3[i].id)
      }
    },
    onTableSelect4(val) {
      this.allwarningIdData = []
      this.multipleSelection4 = val
      for (var i = 0; i < this.multipleSelection4.length; i++) {
        this.allwarningIdData.push(this.multipleSelection4[i].id)
      }
    },
    onTableSelectplpzrw(val) {
      this.alluserIdData = []
      this.alluserIdYjData = []
      for (var i = 0; i < val.length; i++) {
        this.alluserIdData.push(val[i].id)
        this.alluserIdYjData.push(val[i].id)
      }
    },
    getList() {
      getZjtZjry(
        this.searchForm.limit,
        this.searchForm.page,
        this.searchForm.phone
      ).then(result => {
        console.log(result, '矫正人员')
        this.tableData = []
        const { data } = result
        if (data) {
          const list = data.records
          this.total = data.total
          list.map((item, id) => {
            if (item.isOnline === null) {
              item.isOnline = '未打卡'
            } else if (item.isOnline === 1) {
              item.isOnline = '在线'
            } else if (item.isOnline === 0) {
              item.isOnline = '离线'
            }
            const n = {
              sjh: item.phone,
              yhxm: item.userName,
              zjlx: item.cardType,
              zjhm: item.cardNumber,
              lxdz: item.address,
              zhycwz:
                item.lastLongitude === null && item.lastLatitude === null
                  ? ''
                  : item.lastLongitude + ', ' + item.lastLatitude,
              zhycjrwzsj: item.lastRecordTime,
              sfzx: item.isOnline,
              zhyczxsj: item.lastOnlineTime,
              photoUrl: item.photoUrl,
              id: item.id,
              password: item.password,
              rylx: '矫正人员'
            }
            this.tableData.push(n)
          })
        }
      })
    },
    // 点击配置任务
    getList2() {
      getZjtTask(this.searchForm2.limit, this.searchForm2.page).then(result => {
        // console.log(result, '任务管理')
        this.taskConfiguration = []
        const { data } = result
        if (data) {
          const list = data.records
          this.total = data.total
          list.map((item, id) => {
            const n = {
              rwmc: item.taskName,
              rwsxsj: item.effectiveTime,
              rwjssj: item.failureTime,
              dkkssj: item.startTime,
              dajssj: item.endTime,
              dkdd: item.taskAddress,
              id: item.id,
              taskLatitude: item.taskLatitude,
              taskLongitude: item.taskLongitude
            }
            this.taskConfiguration.push(n)
            // console.log(n, 'n')
            this.getSelectzjtTaskUserRelate()
            this.$refs.table.toggleRowSelection(0)
          })
        }
        selectTaskUserRelateView(
          this.searchForm2.limit,
          this.searchForm2.page
        ).then(data => {
          for (var i = 0; i < result.data.records.length; i++) {
            for (var j = 0; j < data.data.records.length; j++) {
              if (
                this.searchForm2.userId === data.data.records[j].userId &&
                result.data.records[i].id === data.data.records[j].taskId
              ) {
                console.log(this.taskConfiguration[i])
              }
            }
          }
        })
      })
    },
    getListplpzselect() {
      getZjtTask(this.searchForm2.limit, this.searchForm2.page).then(result => {
        this.taskDataPl = []
        const { data } = result
        if (data) {
          const list = data.records
          this.total = data.total
          list.map((item, id) => {
            const n = {
              rwmc: item.taskName,
              rwsxsj: item.effectiveTime,
              rwjssj: item.failureTime,
              dkkssj: item.startTime,
              dajssj: item.endTime,
              dkdd: item.taskAddress,
              id: item.id,
              taskLatitude: item.taskLatitude,
              taskLongitude: item.taskLongitude
            }
            this.taskDataPl.push(n)
          })
        }
      })
    },
    getListplpzselectYj() {
      getZjtWarning(this.searchForm2.limit, this.searchForm2.page).then(
        result => {
          this.yjpzDataPl = []
          const { data } = result
          if (data) {
            const list = data.records
            this.total = data.total
            list.map((item, id) => {
              const n = {
                yjlx: item.warningType === 1 ? '定位预警' : '离线预警',
                yjmc: item.warningName,
                yjsxsj: item.startTime,
                yjjssj: item.endTime,
                yjaqfw: item.warningDesc,
                id: item.id
              }
              this.yjpzDataPl.push(n)
            })
          }
        }
      )
    },
    // 查询用户任务接口
    getSelectzjtTaskUserRelate() {
      selectzjtTaskUserRelate(this.searchForm2.userId).then(data => {
        // console.log(data, '查询用户任务接口', this.taskConfiguration)
        for (var i = 0; i < data.data.length; i++) {
          for (var j = 0; j < this.taskConfiguration.length; j++) {
            if (data.data[i].taskId === this.taskConfiguration[j].id) {
              // console.log('一样')
            }
          }
        }
      })
    },
    // 查询预警配置弹框的数据展示
    getList3() {
      getZjtWarning(this.searchForm2.limit, this.searchForm2.page).then(
        result => {
          // console.log(result, '预警管理数据')
          this.yjfwData = []
          const { data } = result
          if (data) {
            const list = data.records
            this.total = data.total
            list.map((item, id) => {
              const n = {
                yjlx: item.warningType === 1 ? '定位预警' : '离线预警',
                yjmc: item.warningName,
                yjsxsj: item.startTime,
                yjjssj: item.endTime,
                yjaqfw: item.warningDesc,
                id: item.id
              }
              this.yjfwData.push(n)
            })
          }
        }
      )
    },
    handleSizeChange(val) {
      this.searchForm.limit = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.searchForm.page = val
      this.getList()
    },
    openImg(row, objName) {
      console.log(row.row[objName])
      this.$confirm(
        `<img style="margin:0 auto;display:block;width:100%;height:100%;" src="${
          process.env.VUE_APP_BASEIMG_API
        }/${row.row[objName].substring(24)}" >`,
        '预览',
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: '取消',
          cancelButtonText: '确定'
        }
      )
        .then(() => {})
        .catch(() => {})
    },
    pushAdd(id) {
      this.$router.push({
        name: 'addJzPerson',
        params: {
          id
        }
      })
    },
    pushupdate(id, userName, cardNumber, cardType, phone, lxdz, photoUrl) {
      this.$router.push({
        name: 'addJzPerson',
        params: {
          id,
          userName,
          cardNumber,
          cardType,
          phone,
          lxdz,
          photoUrl
        }
      })
    },
    openEdit({ row }, objName) {
      const id = row.id
      const userName = row.yhxm
      switch (objName) {
        case '预警范围':
          this.userIdArr = []
          this.userIdArr.push(row.id)
          this.dialogTableVisible2 = true
          this.getList3()
          break
        case '配置任务':
          console.log(row, '人员ID')
          this.userIdArr = []
          this.searchForm2.userId = row.id
          this.userIdArr.push(row.id)
          this.dialogTableVisible = true
          this.getList2()
          break
        case '编辑':
          this.pushupdate(
            id,
            userName,
            row.zjhm,
            row.zjlx,
            row.sjh,
            row.lxdz,
            row.photoUrl
          )
          break
        case '删除':
          this.$confirm('此操作将永久删除该人员, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deleteZjtZjry([id])
              .then(result => {
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                })
                this.getList()
              })
              .catch(err => {
                console.error(err)
                this.$message({
                  type: 'error',
                  message: '删除失败'
                })
              })
          })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.table {
  width: 100%;
  display: flex;
  flex-direction: column;

  .searcbar {
    padding: 10px 0;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    .el-form-item {
      margin: 5px;
    }
    &:nth-of-type() {
      margin-bottom: 10px;
    }
    .el-input {
      width: 200px;
    }
  }
  .el-table {
    flex: 1;
  }
  .el-table--border {
    border-top: 5px solid #3c9cff;
  }
}
.tabelpzrw2 {
  height: 400px !important;
  width: 100%;
  // display: none;
}
</style>
