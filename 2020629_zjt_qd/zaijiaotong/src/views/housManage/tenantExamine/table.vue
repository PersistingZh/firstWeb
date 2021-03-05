<template>
  <div class="table">
    <el-table
      ref="table"
      v-loading="loading"
      element-loading-text="拼命加载中"
      :height="tableHeight"
      :data="tableData"
      border
      style="width:100%"
    >
      <el-table-column prop="yhxm" align="center" label="姓名" width="150px" />
      <el-table-column
        prop="yhzjlx"
        align="center"
        label="证件类型"
        width="120px"
      />>
      <el-table-column
        prop="yhzjhm"
        align="center"
        label="证件号码"
        width="180px"
      />>
      <el-table-column
        prop="yhsjhm"
        align="center"
        label="手机号码"
        width="120px"
      />
      <el-table-column
        prop="kqzt"
        align="center"
        label="考勤状态"
        width="120px"
      />
      <el-table-column
        prop="kqzxsj"
        align="center"
        label="考勤时间"
        width="180px"
      />
      <el-table-column
        prop="rwmc"
        align="center"
        label="任务名称"
        width="150px"
      />
      <el-table-column
        prop="rwms"
        align="center"
        label="任务描述"
        width="200px"
      />
      <el-table-column
        prop="startTime"
        align="center"
        label="开始时间"
        width="200px"
      />
      <el-table-column
        prop="endTime"
        align="center"
        label="结束时间"
        width="200px"
      />
    </el-table>

    <el-pagination
      ref="pageBar"
      background
      :page-sizes="[10, 20, 30, 40]"
      :page-size="100"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { taskResultView } from '@/api/jztList'
export default {
  name: 'TenantExamineTable',
  // props: {
  //   value: {
  //     type: String,
  //     required: true
  //   }
  // },
  data() {
    return {
      pathName: [],
      qyOptions: [],
      tableData: [],
      tableHeight: 100,
      total: 0,
      limit: 20,
      page: 1,
      unitName: '',
      tenantName: '',
      phone: '',
      idCardNum: '',
      loading: false,
      shState: '',
      searchForm: {
        limit: 10,
        page: 1
      }
    }
  },
  // watch: {
  //   value: function(val) {
  //     this.getList()
  //   }
  // },
  mounted() {},
  activated() {
    this.getList()
    setInterval(() => {
      this.getList()
    }, 60000)
  },
  methods: {
    getList() {
      taskResultView(this.searchForm.limit, this.searchForm.page).then(
        result => {
          console.log('考勤管理', result)
          this.tableData = []
          const { data } = result
          if (data) {
            const list = data.records
            this.total = data.total
            list.map((item, id) => {
              if (item.isFinish === '0') {
                item.clockEffective = '未完成'
              } else if (item.isFinish === '1') {
                if (item.clockEffective === '1') {
                  item.clockEffective = '异常'
                } else if (item.clockEffective === '0') {
                  item.clockEffective = '正常'
                }
              }
              const n = {
                yhxm: item.userName,
                yhzjlx: item.cardType,
                yhzjhm: item.cardNumber,
                yhsjhm: item.phone,
                kqzt: item.clockEffective,
                kqzxsj: item.finishTime,
                rwmc: item.taskName,
                rwms: item.taskDesc,
                startTime: item.startTime,
                endTime: item.endTime
              }
              this.tableData.push(n)
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
    pushAdd(id) {
      this.$router.push({
        name: 'AddAttendance',
        params: {
          id
        }
      })
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
  }
  .el-pagination {
    padding: 10px;
    display: flex;
    justify-content: center;
  }
}
</style>
