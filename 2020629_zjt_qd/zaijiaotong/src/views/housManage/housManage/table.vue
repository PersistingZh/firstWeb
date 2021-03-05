<template>
  <div class="content">
    <div class="adminBox">
      <div ref="searchBar" class="searcbar" style="padding-bottom: 0;">
        <el-form :inline="true" :model="searchForm">
          <el-form-item>
            <el-button type="primary" icon="el-icon-document-add" plain @click="pushAdd(0)">新增</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" plain @click="setlxyjTime">设置离线预警时间</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        ref="table"
        v-loading="loading"
        element-loading-text="拼命加载中"
        :height="tableHeight"
        :data="tableData"
        highlight-current-row
        border
        style="width:100%"
        @row-click="handleSelectionChange"
      >
        <el-table-column
          prop="yjlx"
          align="center"
          label="预警类型"
          width="120"
        />
        <el-table-column
          prop="yjmc"
          align="center"
          label="预警名称"
          width="120"
        />
        <el-table-column
          prop="warningContent"
          align="center"
          label="预警内容"
        />
        <el-table-column
          prop="yjsxsj"
          align="center"
          label="预警生效时间"
          width="200"
        />
        <el-table-column
          prop="yjjssj"
          align="center"
          label="预警结束时间"
          width="200"
        />
        <el-table-column
          prop="yjaqfw"
          align="center"
          label="预警地点"
          width="150px"
        />
        <el-table-column
          prop="warningRange"
          align="center"
          label="预警范围 (米)"
          width="150px"
        />
        <el-table-column prop="jwq" align="center" label="操作" width="200px">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="mini"
              @click="openEdit(scope, '编辑')"
            >编辑</el-button>
            <el-button
              type="danger"
              size="mini"
              @click="openEdit(scope, '删除')"
            >删除</el-button>
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
      <templat-import ref="templatImport" />
    </div>

    <div class="leverBox">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="手机号码">
          <el-input
            v-model="searchForm.phone"
            type="text"
            placeholder="请输入手机号码"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" plain @click="getList">查询</el-button>
          <el-button type="primary" icon="el-icon-document-add" plain @click="addOpen">新增</el-button>
        </el-form-item>
      </el-form>
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
          width="120px"
        />
        <el-table-column
          prop="zjhm"
          align="center"
          label="证件号码"
          width="170px"
        />
        <el-table-column
          prop="sjh"
          align="center"
          label="手机号"
          width="170px"
        />
        <el-table-column prop="jwq" align="center" label="操作" width="100px">
          <template slot-scope="scope">
            <el-button
              type="danger"
              size="mini"
              @click="openEdit2(scope, '删除')"
            >删除</el-button>
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
        <el-table-column
          prop="yhxm"
          align="center"
          label="用户姓名"
          width="300px"
        />
        <el-table-column
          prop="zjlx"
          align="center"
          label="证件类型"
          width="120px"
        />
        <el-table-column
          prop="zjhm"
          align="center"
          label="证件号码"
          width="170px"
        />
        <el-table-column
          prop="sjh"
          align="center"
          label="手机号"
          width="170px"
        />
      </el-table>
      <el-button type="danger" size="mini" @click="pzjzryxx">确定配置</el-button>
    </el-dialog>
  </div>
</template>

<script>
import templatImport from '@/components/TemplatImport'
import {
  getZjtZjry,
  getZjtWarning,
  deletezjtWarning,
  selectwarningUserRelateViewwarningId,
  deleteZjtWarningUserRelate,
  pzyjZjtWarningUserRelate,
  setSysParameter,
  selectSysParameter
} from '@/api/jztList'
export default {
  name: 'HousManageTable',
  components: {
    templatImport
  },
  // props: {
  //   value: {
  //     type: String,
  //     required: true
  //   }
  // },
  data() {
    return {
      qyOptions: [],
      dictZjlx: [],
      searchForm: {
        limit: 20,
        page: 1
      },
      searchForm2: {
        limit: 20,
        page: 1,
        phone: ''
      },
      tableData: [
      ],
      tableData2: [
      ],
      tableHeight: 100,
      total: 0,
      type: '0',
      loading: false,
      // 接收预警类型ID
      warningId: '',
      dialogTableVisible: false,
      tojzryDatatk: [],
      userIdArr: [],
      warningIdArr: [],
      multipleSelection: [],
      // 设置离线预警时间
      // 离线预警时间
      parameterName: '离线预警时间',
      // 离线预警时间隔（分钟）
      parameterVal: 0
    }
  },
  // watch: {
  //   value: function(val) {
  //     this.getList()
  //   }
  // },
  mounted() {},
  async activated() {
    this.getList()
    this.getList2()
    this.getList3()
    this.warningId = ''
  },
  methods: {
    pzjzryxx() {
      if(this.userIdArr.length <= 0) {
        this.$message({
          message: '请选择要配置的人员'
        })
      } else {
        pzyjZjtWarningUserRelate(this.userIdArr, this.warningIdArr).then(res => {
          this.$message({
            type: 'success',
            message: '预警配置成功'
          })
          this.dialogTableVisible = false
          selectwarningUserRelateViewwarningId(
            this.searchForm.limit,
            this.searchForm.page,
            this.warningId
          ).then(data => {
            this.tableData2 = []
            for (var i = 0; i < data.data.records.length; i++) {
              this.tableData2.push({
                sjh: data.data.records[i].phone,
                yhxm: data.data.records[i].userName,
                zjlx: data.data.records[i].cardType,
                zjhm: data.data.records[i].cardNumber,
                id: data.data.records[i].id
              })
            }
          })
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
            //   lxdz: item.address,
            //   zhycwz: item.lastLongitude + ',' + item.lastLatitude,
            //   zhycjrwzsj: item.lastRecordTime,
            //   sfzx: item.isOnline,
            //   zhyczxsj: item.lastOnlineTime,
            //   id: item.id
            // }
            // this.tableData2.push(n)
          })
        }
      })
    },
    getList2() {
      getZjtWarning(this.searchForm2.limit, this.searchForm2.page).then(
        result => {
          console.log(result, '预警人员')
          this.tableData = []
          const { data } = result
          if (data) {
            const list = data.records
            this.total = data.total
            list.map((item, id) => {
              const n = {
                yjlx: item.warningType === 1 ? '定位预警' : '离线预警',
                yjmc: item.warningName,
                warningContent: item.warningContent,
                yjsxsj: item.startTime,
                yjjssj: item.endTime,
                yjaqfw: item.warningDesc,
                id: item.id,
                warningLatitude: item.warningLatitude,
                warningLongitude: item.warningLongitude,
                warningRange: item.warningRange
              }
              this.tableData.push(n)
            })
          }
        }
      )
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
      selectSysParameter(this.parameterName).then(result => {
        console.log(result.data.parameterVal, '回填离线预警时间')
        this.parameterVal = result.data.parameterVal
      })
    },
    handleSelectionChange(val) {
      this.warningIdArr = []
      this.warningIdArr.push(val.id)
      this.warningId = val.id
      selectwarningUserRelateViewwarningId(
        this.searchForm.limit,
        this.searchForm.page,
        this.warningId
      ).then(data => {
        this.tableData2 = []
        for (var i = 0; i < data.data.records.length; i++) {
          this.tableData2.push({
            sjh: data.data.records[i].phone,
            yhxm: data.data.records[i].userName,
            zjlx: data.data.records[i].cardType,
            zjhm: data.data.records[i].cardNumber,
            id: data.data.records[i].id
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
    pushAdd(id) {
      this.$router.push({
        name: 'AddWarning',
        params: {
          id
        }
      })
    },
    addOpen() {
      if (this.warningId.length < 1) {
        this.$message({
          showClose: true,
          message: '请选择要配置的预警'
        })
      } else {
        this.getList3()
        this.dialogTableVisible = true
      }
    },
    pushupdate(
      id,
      yjjssj,
      yjlx,
      yjmc,
      yjsxsj,
      yjaqfw,
      warningLatitude,
      warningLongitude,
      yjnr,
      warningRange,
      warningContent
    ) {
      this.$router.push({
        name: 'AddWarning',
        params: {
          id,
          yjjssj,
          yjlx,
          yjmc,
          yjsxsj,
          yjaqfw,
          warningLatitude,
          warningLongitude,
          yjnr,
          warningRange,
          warningContent
        }
      })
    },
    setlxyjTime() {
      this.getList4()
      setTimeout(() => {
        this.$prompt('请输入离线预警时间隔 (分钟)', '提示', {
          inputValue: this.parameterVal,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /^\d+$|^\d+[.]?\d+$/,
          inputErrorMessage: '格式不正确,只能输入数字',
          closeOnClickModal: false
        })
          .then(({ value }) => {
            setSysParameter(this.parameterName, value).then(result => {
              console.log(this.parameterName, value)
              this.$message({
                type: 'success',
                message: '设置成功'
              })
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '取消输入'
            })
          })
      }, 1000)
    },
    openEdit({ row }, objName) {
      const id = row.id
      switch (objName) {
        case '编辑':
          this.pushupdate(
            id,
            row.yjjssj,
            row.yjlx,
            row.yjmc,
            row.yjsxsj,
            row.yjaqfw,
            row.warningLatitude,
            row.warningLongitude,
            row.yjnr,
            row.warningRange,
            row.warningContent
          )
          break
        case '删除':
          this.$confirm('此操作将永久删除该预警信息, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deletezjtWarning([id])
              .then(result => {
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                })
                this.getList2()
                this.tableData2 = []
                this.warningId = ''
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
      console.log(row)
      const id = row.id
      switch (objName) {
        case '删除':
          this.$confirm('此操作将永久删除该人员, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deleteZjtWarningUserRelate([id])
              .then(result => {
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                })
                // this.getList()
                selectwarningUserRelateViewwarningId(
                  this.searchForm.limit,
                  this.searchForm.page,
                  this.warningId
                ).then(data => {
                  this.tableData2 = []
                  for (var i = 0; i < data.data.records.length; i++) {
                    this.tableData2.push({
                      sjh: data.data.records[i].phone,
                      yhxm: data.data.records[i].userName,
                      zjlx: data.data.records[i].cardType,
                      zjhm: data.data.records[i].cardNumber,
                      id: data.data.records[i].id
                    })
                  }
                })
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
div.leverBox > .el-form.el-form--inline {
  padding-top: 10px;
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
