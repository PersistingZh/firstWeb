<template>
  <div class="table">
    <el-tabs v-model="activeName">
      <el-tab-pane label="离线预警" name="first">
        <el-table
          ref="table"
          v-loading="loading"
          element-loading-text="拼命加载中"
          :height="tableHeight"
          :data="tableData"
          border
          style="width:100%;"
        >
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
            width="150px"
          />
          <el-table-column
            prop="sjh"
            align="center"
            label="手机号"
            width="150px"
          />
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
        </el-table>
        <el-pagination
          background
          :page-sizes="[10, 20, 30, 40]"
          :page-size="100"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </el-tab-pane>
      <el-tab-pane label="位置预警" name="second">
        <div ref="searchBar" class="searcbar">
          <el-form :inline="true" :model="searchForm">
            <el-form-item>
              <el-date-picker
                v-model="searchForm2.createTime"
                type="date"
                placeholder="选择日期"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" plain @click="selectcreateTime">查询</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table
          ref="table"
          v-loading="loading"
          element-loading-text="拼命加载中"
          :height="tableHeight2"
          :data="tableDatadwyj"
          border
          style="width:100%;"
        >
          <el-table-column prop="warningType" align="center" label="预警类型" width="120" />
          <el-table-column prop="warningName" align="center" label="预警名称" width="120" />
          <el-table-column prop="warningContent" align="center" label="预警内容" width="150" />
          <el-table-column prop="createTime" align="center" label="预警时间" width="200" />
          <el-table-column prop="warningDesc" align="center" label="预警地点" width="150px" />
          <el-table-column prop="warningRange" align="center" label="预警范围 (米)" width="150px" />
          <el-table-column prop="userName" align="center" label="用户姓名" width="150px" />
          <el-table-column prop="phone" align="center" label="手机号码" width="150px" />
          <el-table-column prop="address" align="center" label="联系地址" width="150px" />
          <el-table-column prop="createLongitude" align="center" label="经度" width="150px" />
          <el-table-column prop="createLatitude" align="center" label="纬度" width="150px" />
        </el-table>
        <el-pagination
          background
          :page-sizes="[10, 20, 30, 40]"
          :page-size="100"
          :total="total2"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange2"
          @current-change="handleCurrentChange2"
        />
      </el-tab-pane>
      <el-tab-pane label="定位核验预警" name="third">
        <div ref="searchBar" class="searcbar">
          <el-form :inline="true" :model="searchForm">
            <el-form-item>
              <el-date-picker
                v-model="searchForm3.createTime"
                type="date"
                placeholder="选择日期"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" plain @click="selectcreateTime2">查询</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table
          ref="table"
          v-loading="loading"
          element-loading-text="拼命加载中"
          :height="tableHeight3"
          :data="tableDatakqyj"
          border
          style="width:100%;"
        >
          <el-table-column prop="userName" align="center" label="用户姓名" width="120" />
          <el-table-column prop="phone" align="center" label="手机号码" width="120" />
          <el-table-column prop="taskAddress" align="center" label="任务地址" width="120" />
          <el-table-column prop="taskDesc" align="center" label="任务说明" width="120" />
          <el-table-column prop="taskLongitude" align="center" label="经度" width="120" />
          <el-table-column prop="taskLatitude" align="center" label="纬度" width="120" />
          <el-table-column prop="taskRange" align="center" label="预警范围 (米)" width="150px" />
          <el-table-column prop="createTime" align="center" label="考勤时间" width="150px" />
          <el-table-column prop="clockEffective" align="center" label="考勤状态" width="150px" />
        </el-table>
        <el-pagination
          background
          :page-sizes="[10, 20, 30, 40]"
          :page-size="100"
          :total="total3"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange3"
          @current-change="handleCurrentChange3"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { getzjtZjryendisOnline, getWarningRecordView, getTaskResultViewKqyj } from '@/api/jztList'
export default {
  name: 'InformationTable',
  data() {
    return {
      loading: false,
      tableData: [],
      tableDatadwyj: [],
      tableDatakqyj: [],
      total: 0,
      total2: 0,
      total3: 0,
      tableHeight: 100,
      tableHeight2: 100,
      tableHeight3: 100,
      searchForm: {
        limit: 10,
        page: 1,
        isOnline: 0
      },
      searchForm2: {
        limit: 20,
        page: 1,
        createTime: ''
      },
      activeName: 'first',
      searchForm3: {
        limit: 20,
        page: 1,
        isFinish: 1,
        clockEffective: 1,
        createTime: ''
      }
    }
  },
  mounted() {},
  activated() {
    const d = new Date()
    const resDate = d.getFullYear() + '-' + this.p((d.getMonth() + 1)) + '-' + this.p(d.getDate())
    this.searchForm3.createTime = resDate
    this.searchForm2.createTime = resDate
    this.getList()
    this.getList2()
    this.getList3()
    setInterval(() => {
      this.getList()
      this.getList2()
      this.getList3()
    }, 60000)
  },
  methods: {
    getList() {
      getzjtZjryendisOnline(
        this.searchForm.limit,
        this.searchForm.page,
        this.searchForm.isOnline
      ).then(result => {
        console.log(result, '离线预警')
        this.tableData = []
        const { data } = result
        if (data) {
          const list = data.records
          this.total = data.total
          list.map((item, id) => {
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
              sfzx: item.isOnline === 0 ? '离线' : '在线',
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
    getList2() {
      getWarningRecordView(this.searchForm2.limit, this.searchForm2.page, this.searchForm2.createTime).then(result => {
        this.tableDatadwyj = []
        // console.log(result, '定位预警')
        const { data } = result
        if (data) {
          const list = data.records
          this.total2 = data.total
          list.map((item, id) => {
            const n = {
              warningType: item.warningType === 1 ? '定位预警' : '离线预警',
              warningName: item.warningName,
              warningContent: item.warningContent,
              createTime: item.createTime,
              warningDesc: item.warningDesc,
              warningRange: item.warningRange,
              userName: item.userName,
              phone: item.phone,
              address: item.address,
              createLongitude: item.createLongitude,
              createLatitude: item.createLatitude
            }
            this.tableDatadwyj.push(n)
          })
        }
      })
    },
    getList3() {
      getTaskResultViewKqyj(this.searchForm3.limit, this.searchForm3.page, this.searchForm3.isFinish, this.searchForm3.clockEffective, this.searchForm3.createTime).then(result => {
        this.tableDatakqyj = []
        const { data } = result
        // console.log(result, '考勤预警')
        if (data) {
          const list = data.records
          this.total3 = data.total
          list.map((item, id) => {
            const n = {
              userName: item.userName,
              phone: item.phone,
              taskAddress: item.taskAddress,
              taskDesc: item.taskDesc,
              taskLongitude: item.taskLongitude,
              taskLatitude: item.taskLatitude,
              taskRange: item.taskRange,
              createTime: item.createTime,
              clockEffective: item.clockEffective === '1' ? '正常' : '异常'
            }
            this.tableDatakqyj.push(n)
          })
        }
      })
    },
    p(s) {
      return s < 10 ? '0' + s : s
    },
    selectcreateTime() {
      if (this.searchForm2.createTime === null || this.searchForm2.createTime === '') {
        this.searchForm2.createTime = ''
      } else {
        const d = new Date(this.searchForm2.createTime)
        const resDate = d.getFullYear() + '-' + this.p((d.getMonth() + 1)) + '-' + this.p(d.getDate())
        this.searchForm2.createTime = resDate
      }
      this.getList2()
    },
    selectcreateTime2() {
      if (this.searchForm3.createTime === null || this.searchForm3.createTime === '') {
        this.searchForm3.createTime = ''
      } else {
        const d = new Date(this.searchForm3.createTime)
        const resDate = d.getFullYear() + '-' + this.p((d.getMonth() + 1)) + '-' + this.p(d.getDate())
        this.searchForm3.createTime = resDate
      }
      this.getList3()
    },
    handleSizeChange(val) {
      this.searchForm.limit = val
      this.getList()
    },
    handleSizeChange2(val) {
      this.searchForm2.limit = val
      this.getList2()
    },
    handleSizeChange3(val) {
      this.searchForm3.limit = val
      this.getList3()
    },
    handleCurrentChange(val) {
      this.searchForm.page = val
      this.getList3()
    },
    handleCurrentChange2(val) {
      this.searchForm2.page = val
      this.getList2()
    },
    handleCurrentChange3(val) {
      this.searchForm3.page = val
      this.getList3()
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
    flex-wrap: wrap;
    align-items: center;
    .el-form-item {
      margin: 5px;
    }
    .el-input,
    .el-select {
      width: 200px;
      margin-right: 10px;
    }
  }
  .el-table {
    flex: 1;
  }
  .el-table--border {
    border-top: 5px solid #3c9cff;
    height: 750px !important;
  }
  .el-pagination {
    padding: 10px;
    display: flex;
    justify-content: center;
  }
}
</style>
