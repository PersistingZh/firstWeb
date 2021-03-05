<template>
  <div class="table">
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
      style="width:100%"
    >
      <el-table-column prop="gzryxm" align="center" label="工作人员姓名" width="150px" />
      <el-table-column prop="zjlx" align="center" label="证件类型" width="170px" />
      <el-table-column prop="zjhm" align="center" label="证件号码" width="170px" />
      <el-table-column prop="ssdw" align="center" label="所属单位" width="150px" />
      <el-table-column prop="bm" align="center" label="部门" width="120px" />
      <el-table-column prop="zw" align="center" label="职位" width="120px" />
      <el-table-column prop="sjh" align="center" label="手机号" width="120px" />
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
    <!-- <templat-import ref="templatImport" @refresh="getList" /> -->
  </div>
</template>

<script>
import { getZjtGzry, deleteZjtGzry } from '@/api/jztList'
export default {
  name: 'TenantManageTable',
  // props: {
  //   value: {
  //     type: String,
  //     required: true
  //   }
  // },
  data() {
    return {
      qyOptions: [],
      tableData: [
      ],
      tableHeight: 100,
      relateId: '',
      total: 0,
      jzdw: '',
      type: '2',
      searchForm: {
        limit: 20,
        page: 1
      },
      loading: false,
      path: ''
    }
  },
  computed: {
  },
  mounted() {
  },
  activated() {
    this.getList()
    setInterval(() => {
      this.getList()
    }, 60000)
  },
  methods: {
    getList() {
      // this.loading = true
      getZjtGzry(
        this.searchForm.limit,
        this.searchForm.page,
      ).then(result => {
        // console.log(result, '工作人员')
        this.tableData = []
        const { data } = result
        if (data) {
          const list = data.records
          this.total = data.total
          list.map((item, id) => {
            const n = {
              gzryxm: item.name,
              zjlx: item.cardType,
              zjhm: item.cardNumber,
              ssdw: item.units,
              bm: item.department,
              zw: item.position,
              sjh: item.phone,
              id: item.id
            }
            this.tableData.push(n)
          })
        }
      })
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
        name: 'AddgzPerson',
        params: {
          id
        }
      })
    },
    pushupdate(id, bm, gzryxm, sjh, ssdw, zjhm, zjlx, zw) {
      this.$router.push({
        name: 'AddgzPerson',
        params: {
          id,
          bm,
          gzryxm,
          sjh,
          ssdw,
          zjhm,
          zjlx,
          zw
        }
      })
    },
    openEdit({ row }, objName) {
      const id = row.id
      switch (objName) {
        case '编辑':
          this.pushupdate(id, row.bm, row.gzryxm, row.sjh, row.ssdw, row.zjhm, row.zjlx, row.zw)
          break
        case '删除':
          this.$confirm('此操作将永久删除该工作人员, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deleteZjtGzry([id])
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
</style>
