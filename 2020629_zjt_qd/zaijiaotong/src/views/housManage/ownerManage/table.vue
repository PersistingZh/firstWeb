<template>
  <div class="content">
    <div class="adminBox">
      <div ref="searchBar" class="searcbar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item>
            <el-button type="primary" icon="el-icon-document-add" plain @click="pushAdd(0)">新增</el-button>
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
        highlight-current-row
        style="width:100%"
        @row-click="handleSelectionChange"
      >
        <el-table-column prop="rwmc" align="center" label="任务名称" width="170px" />
        <el-table-column prop="dkdd" align="center" label="定位核验地点" width="150px" />
        <el-table-column prop="rwsxsj" align="center" label="任务生效时间" width="120px" />
        <el-table-column prop="rwjssj" align="center" label="任务结束时间" width="120px" />
        <el-table-column prop="dkkssj" align="center" label="定位核验开始时间" width="140px" />
        <el-table-column prop="dajssj" align="center" label="定位核验结束时间" width="140px" />
        <el-table-column prop="taskDesc" align="center" label="任务描述" width="120px" />
        <el-table-column prop="taskRange" align="center" label="任务定位核验范围 (米)" width="170px" />
        <el-table-column prop="jwq" align="center" label="操作" width="200px">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="openEdit(scope, '编辑')">编辑</el-button>
            <el-button type="danger" size="mini" @click="openEdit(scope, '删除')">删除</el-button>
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
    </div>

    <div class="leverBox">
      <div ref="searchBar" class="searcbar">
        <el-form :inline="true" :model="searchForm3">
          <el-form-item label="手机号码">
            <el-input v-model="searchForm3.phone" type="text" placeholder="请输入手机号码" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" plain @click="selectJzryIDandPhone">查询</el-button>
            <el-button type="primary" icon="el-icon-refresh" plain @click="resetPhone">重置</el-button>
            <el-button type="primary" icon="el-icon-document-add" plain @click="addRwglpz">新增</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        ref="leverTable"
        v-loading="loading"
        element-loading-text="拼命加载中"
        :data="tableData2"
        border
        highlight-current-row
        style="width:100%"
        height="100"
      >
        <el-table-column prop="yhxm" align="center" label="用户姓名" width="150px" />
        <el-table-column prop="zjlx" align="center" label="证件类型" width="120px" />
        <el-table-column prop="zjhm" align="center" label="证件号码" width="170px" />
        <el-table-column prop="sjh" align="center" label="手机号" width="170px" />
        <el-table-column prop="jwq" align="center" label="操作" width="100px">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="openEdit2(scope, '删除')">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        background
        :page-sizes="[20, 40, 60, 80]"
        :page-size="100"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange2"
        @current-change="handleCurrentChange2"
      />
    </div>

    <!-- 弹出层矫正人员 -->
    <el-dialog title="配置矫正人员信息" :visible.sync="dialogTableVisible">
      <div
        class="searcbar"
        style="width:100%;margin-top: -35px;padding-bottom: 10px;border-bottom: 3px solid #3c9cFF;"
      />
      <el-table
        ref="table"
        v-loading="loading"
        element-loading-text="拼命加载中"
        class="tabelpzrw"
        :height="tableHeight"
        :data="tojzryDatatk"
        border
        @selection-change="onTableSelect"
      >
        <el-table-column type="selection" width="80" />
        <el-table-column prop="yhxm" align="center" label="用户姓名" width="300px" />
        <el-table-column prop="zjlx" align="center" label="证件类型" width="120px" />
        <el-table-column prop="zjhm" align="center" label="证件号码" width="170px" />
        <el-table-column prop="sjh" align="center" label="手机号" width="170px" />
      </el-table>
      <el-button type="danger" size="mini" @click="pzjzryxx">确定配置</el-button>
    </el-dialog>

    <!-- 嵌套的dialog -->
    <el-dialog width="60%" title="配置任务" :visible.sync="dialogTableVisible2">
      <div
        class="searcbar"
        style="width:100%;margin-top: -35px;padding-bottom: 10px;border-bottom: 3px solid #3c9cFF;"
      />
      <el-table
        ref="table"
        v-loading="loading"
        element-loading-text="拼命加载中"
        class="tabelpzrw2"
        :height="tableHeight"
        :data="taskConfiguration"
        border
        style="width:100%"
      >
        <el-table-column prop="rwmc" align="center" label="任务名称" width="170px" />
        <el-table-column prop="dkdd" align="center" label="打开地点" width="120px" />
        <el-table-column prop="rwsxsj" align="center" label="任务生效时间" width="120px" />
        <el-table-column prop="rwjssj" align="center" label="任务结束时间" width="120px" />
        <el-table-column prop="dkkssj" align="center" label="打卡开始时间" width="120px" />
        <el-table-column prop="dajssj" align="center" label="打卡结束时间" width="120px" />
        <el-table-column prop="jwq" align="center" label="操作" width="500px">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="openEdit2(scope, '配置')">配置</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import {
  getZjtZjry,
  getZjtTask,
  deleteZjtTask,
  pzZjtTaskUserRelate,
  selectZjtTaskUserRelate,
  selectZjtTaskUserRelatetaskId,
  deleteZjtTaskUserRelate
} from '@/api/jztList'
export default {
  name: 'OwnerManageTable',
  // props: {
  //   value: {
  //     type: String,
  //     required: true
  //   }
  // },
  data() {
    return {
      dialogTableVisible: false,
      dialogTableVisible2: false,
      qyOptions: [],
      searchForm: {
        limit: 20,
        page: 1,
        phone: ''
      },
      searchForm2: {
        limit: 20,
        page: 1
      },
      tableData: [
      ],
      tableData2: [
      ],
      tableHeight: 100,
      total: 0,
      type: '1',
      loading: false,
      tojzryDatatk: [],
      taskConfiguration: [],
      // 接收矫正人员的id
      searchForm3: {
        taskId: '',
        userId: '',
        limit: 20,
        page: 1,
        phone: ''
      },
      taskIdArr: [],
      userIdArr: [],
      multipleSelection: []
    }
  },
  computed: {
  },
  mounted() {},
  activated() {
    this.getList()
    this.getList2()
    this.getList3()
    // this.getList4()
    this.searchForm3.taskId = ''
  },
  methods: {
    pzjzryxx() {
      if(this.userIdArr.length <= 0 ) {
        this.$message({
          message: '请选择要配置的人员'
        })
      } else {
        pzZjtTaskUserRelate(this.taskIdArr, this.userIdArr).then(res => {
          this.$message({
            type: 'success',
            message: '配置成功'
          })
        // this.getList()
          selectZjtTaskUserRelatetaskId(this.searchForm3.taskId, this.searchForm3.phone).then(data => {
            this.tableData2 = []
            for (var i = 0; i < data.data.length; i++) {
              this.tableData2.push({
                sjh: data.data[i].phone,
                yhxm: data.data[i].userName,
                zjlx: data.data[i].cardType,
                zjhm: data.data[i].cardNumber,
                id: data.data[i].id
              })
            }
          })
          this.dialogTableVisible = false
        })
      }
    },
    onTableSelect(val) {
      this.userIdArr = []
      this.multipleSelection = val
      for (var i = 0; i < this.multipleSelection.length; i++) {
        this.userIdArr.push(this.multipleSelection[i].id)
      }
    },
    handleSelectionChange(val) {
      this.taskIdArr = []
      this.searchForm3.taskId = val.id
      this.taskIdArr.push(val.id)
      selectZjtTaskUserRelatetaskId(this.searchForm3.taskId, this.searchForm3.phone).then(data => {
        this.tableData2 = []
        // this.tojzryDatatk = []
        for (var i = 0; i < data.data.length; i++) {
          this.tableData2.push({
            sjh: data.data[i].phone,
            yhxm: data.data[i].userName,
            zjlx: data.data[i].cardType,
            zjhm: data.data[i].cardNumber,
            id: data.data[i].id
          })
        }
      })
    },
    selectJzryIDandPhone() {
      selectZjtTaskUserRelatetaskId(this.searchForm3.taskId, this.searchForm3.phone).then(data => {
        this.tableData2 = []
        for (var i = 0; i < data.data.length; i++) {
          this.tableData2.push({
            sjh: data.data[i].phone,
            yhxm: data.data[i].userName,
            zjlx: data.data[i].cardType,
            zjhm: data.data[i].cardNumber,
            id: data.data[i].id
          })
        }
      })
    },
    getList() {
      // this.loading = true
      getZjtZjry(
        this.searchForm.limit,
        this.searchForm.page,
        this.searchForm.phone
      ).then(result => {
        // console.log(result, '矫正人员')
        this.tableData2 = []
        const { data } = result
        if (data) {
          const list = data.records
          this.total = data.total
          list.map((item, id) => {
            // const n = {
            //   sjh: item.phone,
            //   yhxm: item.userName,
            //   zjlx: item.cardType,
            //   zjhm: item.cardNumber,
            //   id: item.id
            // }
            // this.tableData2.push(n)
          })
        }
      })
    },
    getList2() {
      // this.loading = true
      getZjtTask(this.searchForm2.limit, this.searchForm2.page).then(result => {
        console.log(result, '任务管理')
        this.tableData = []
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
              taskLongitude: item.taskLongitude,
              taskDesc: item.taskDesc,
              taskRange: item.taskRange
            }
            this.tableData.push(n)
          })
        }
      })
    },
    getList3() {
      getZjtZjry(this.searchForm.limit, this.searchForm.page).then(result => {
        this.tojzryDatatk = []
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
              id: item.id
            }
            this.tojzryDatatk.push(n)
          })
        }
      })
    },
    getList4() {
      selectZjtTaskUserRelate(this.searchForm3.userId).then(result => {
        // console.log(result, '查询任务管理配置任务的接口')
        this.taskConfiguration = []
        const { data } = result
        if (data) {
          const list = data
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
          })
        }
      })
    },
    handleSizeChange(val) {
      this.searchForm.limit = val
      this.getList2()
    },
    handleSizeChange2(val) {
      this.searchForm.limit = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.searchForm.page = val
      this.getList2()
    },
    handleCurrentChange2(val) {
      this.searchForm.page = val
      this.getList()
    },
    btnDialogclose() {
      this.dialogTableVisible = false
    },
    btnDialogclose2() {
      this.dialogTableVisible2 = false
    },
    resetPhone() {
      this.searchForm3.phone = ''
      selectZjtTaskUserRelatetaskId(this.searchForm3.taskId, this.searchForm3.phone).then(data => {
        this.tableData2 = []
        for (var i = 0; i < data.data.length; i++) {
          this.tableData2.push({
            sjh: data.data[i].phone,
            yhxm: data.data[i].userName,
            zjlx: data.data[i].cardType,
            zjhm: data.data[i].cardNumber,
            id: data.data[i].id
          })
        }
      })
    },
    pushAdd(id) {
      this.$router.push({
        name: 'AddTask',
        params: {
          id
        }
      })
    },
    pushAdd2(id) {
      this.$router.push({
        name: 'addJzPerson',
        params: {
          id
        }
      })
    },
    pushupdate(
      id,
      dajssj,
      dkkssj,
      rwjssj,
      rwmc,
      rwsxsj,
      taskLatitude,
      taskLongitude,
      dkdd,
      taskDesc,
      taskRange
    ) {
      this.$router.push({
        name: 'AddTask',
        params: {
          id,
          dajssj,
          dkkssj,
          rwjssj,
          rwmc,
          rwsxsj,
          taskLatitude,
          taskLongitude,
          dkdd,
          taskDesc,
          taskRange
        }
      })
    },
    // 矫正人员配置任务
    onjzrypzrw(taskId, userId) {
      pzZjtTaskUserRelate(taskId, userId).then(res => {
        this.$message({
          type: 'success',
          message: '配置成功'
        })
        this.dialogTableVisible2 = false
      })
    },
    addRwglpz() {
      if (this.searchForm3.taskId.length < 1) {
        this.$message({
          showClose: true,
          message: '请选择要配置的任务'
        })
      } else {
        this.getList3()
        this.dialogTableVisible = true
      }
    },
    openEdit({ row }, objName) {
      const id = row.id
      switch (objName) {
        case '编辑':
          this.pushupdate(
            id,
            row.dajssj,
            row.dkkssj,
            row.rwjssj,
            row.rwmc,
            row.rwsxsj,
            row.taskLatitude,
            row.taskLongitude,
            row.dkdd,
            row.taskDesc,
            row.taskRange
          )
          break
        case '删除':
          this.$confirm('此操作将永久删除该任务信息, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deleteZjtTask([id])
              .then(result => {
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                })
                this.getList2()
                this.tableData2 = []
                this.searchForm3.taskId = ''
              })
              .catch(err => {
                console.error(err)
                this.$message({
                  type: 'error',
                  message: '删除失败'
                })
              })
          })
          break

        default:
          break
      }
    },
    openEdit2({ row }, objName) {
      const id = row.id
      switch (objName) {
        case '配置':
          console.log(row, '矫正人员的id', this.searchForm3.userId)
          this.searchForm3.taskId = row.id
          this.onjzrypzrw(this.searchForm3.userId, this.searchForm3.taskId)
          break
        case '删除':
          this.$confirm('此操作将永久删除配置了该任务的人员, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deleteZjtTaskUserRelate([id])
              .then(result => {
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                })
                // this.getList()
                selectZjtTaskUserRelatetaskId(this.searchForm3.taskId).then(
                  data => {
                    this.tableData2 = []
                    for (var i = 0; i < data.data.length; i++) {
                      this.tableData2.push({
                        sjh: data.data[i].phone,
                        yhxm: data.data[i].userName,
                        zjlx: data.data[i].cardType,
                        zjhm: data.data[i].cardNumber,
                        id: data.data[i].id
                      })
                    }
                  }
                )
              })
              .catch(err => {
                console.error(err)
                this.$message({
                  type: 'error',
                  message: '删除失败'
                })
              })
          })
          break
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
    .el-form-item {
      margin: 5px;
    }
    .el-input {
      width: 200px;
      margin-right: 10px;
    }
  }
  .el-table {
    flex: 1;
  }
  .el-table--border {
    border-top: 5px solid #3c9cff;
  }
  .el-pagination {
    padding: 10px;
    display: flex;
    justify-content: center;
  }
}
.content {
  display: flex;
  height: 100%;
  width: 100%;
  padding: 0 10px;
  .adminBox,
  .leverBox {
    display: flex;
    flex-direction: column;
    width: 48%;
    margin-right: 20px;
    .el-table {
      flex: 1;
    }
    .el-pagination {
      padding: 10px;
      display: flex;
      justify-content: center;
    }
  }
}
div.searcbar > .el-form.el-form--inline {
  padding-top: 10px;
}
.searcbar {
  padding: 10px 0;
  padding-bottom: 0px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  .el-input,
  .el-select {
    width: 200px;
    margin-right: 10px;
  }
}
.el-dialog__footer {
  padding-top: 30px;
}
.tabelpzrw {
  height: 300px !important;
  width: 100%;
}
.tabelpzrw2 {
  height: 400px !important;
  width: 100%;
  // display: none;
}
</style>
