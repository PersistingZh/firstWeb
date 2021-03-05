<template>
  <div class="content table-container">
    <!-- 搜索 -->
    <div ref="searchBar" class="searcbar">
      <el-form :inline="true">
        <el-form-item>
          <el-input v-model="searchForm.versionCode" placeholder="请输入版本号" type="text" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" plain @click="getApkList">搜索</el-button>
          <el-button
            type="primary"
            plain
            icon="el-icon-document-add"
            @click="dialogVisible = true"
          >新增</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 表格 -->
    <el-table
      ref="table"
      v-loading="loading"
      element-loading-text="拼命加载中"
      :height="tableHeight"
      :data="tableData"
      highlight-current-row
      border
      style="width:100%"
    >
      <el-table-column align="center" prop="isOnline" label="是否上线">
        <template slot-scope="scope">
          <p>{{ scope.row.isOnline?'已上线':'未上线' }}</p>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="appName" label="app名称" />
      <el-table-column align="center" prop="versionCode" label="版本号" />
      <el-table-column align="center" prop="versionName" label="版本名" />
      <el-table-column align="center" prop="packages" label="包名" />
      <el-table-column align="center" prop="createTime" label="创建时间" />
      <el-table-column align="center" label="操作" width="260">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openEdit(scope, '编辑')">编辑</el-button>
          <el-button type="primary" size="mini" @click="downloadApk(scope)">下载apk</el-button>
          <el-button type="danger" size="mini" @click="apkDelelte(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
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
    <!-- 弹出层 -->
    <el-dialog title="上传APK文件" :visible.sync="dialogVisible" width="60%">
      <!-- :before-close="handleClose" -->
      <el-form ref="dialongForm" :rules="rules" :model="uploadApkData">
        <el-form-item label="APP名称">
          <el-input v-model="uploadApkData.appName" disabled type="text" />
        </el-form-item>
        <el-form-item prop="versionName" label="版本名">
          <el-input v-model="uploadApkData.versionName" type="text" />
        </el-form-item>
        <el-form-item prop="versionCode" label="版本">
          <el-input v-model="uploadApkData.versionCode" type="text" />
        </el-form-item>
        <el-form-item prop="packages" label="包名">
          <el-input v-model="uploadApkData.packages" type="text" />
        </el-form-item>
        <el-form-item label="是否上线">
          <!-- <el-input v-model="uploadApkData.isOnline" /> -->
          <el-radio v-model="uploadApkData.isOnline" label="0">否</el-radio>
          <el-radio v-model="uploadApkData.isOnline" label="1">是</el-radio>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="uploadApkData.remark" type="textarea" :rows="2" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item v-if="!uploadApkData.id" label="apk文件">
          <el-upload
            ref="apkUpload"
            :action="uploadUrl"
            :headers="headers"
            :multiple="false"
            :on-remove="handleRemove"
            :on-success="uploadSuccess"
            :limit="1"
            :file-list="fileList"
            :on-exceed="handleExceed"
            :on-change="fileChange"
            :before-upload="beforeUpload"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传apk文件，且不超过30mb</div>
          </el-upload>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialongUploadApk">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import AppInfoParser from 'app-info-parser'
import { getApkList, addOrUpdate, appDelete } from '@/api/SystemManagement'
import { getToken } from '../../../utils/auth'
// const AppInfoParser = require('app-info-parser')

export default {
  name: 'UploadApk',
  data() {
    return {
      // 列表数据
      tableData: [],
      loading: false,
      tableHeight: 100,
      total: 0,
      // 🔍搜索数据
      searchForm: {
        limit: 20,
        page: 1,
        versionCode: null
      },
      // 弹出层数据
      dialogVisible: false,
      uploadApkData: {
        id: '',
        appName: '在矫通', // app名称
        versionCode: null, // 版本名
        versionName: '', // 版本名称
        packages: '', // 包名
        isOnline: '0', // 是否上线
        remark: '', // 备注
        versionPath: '', // apk路径
        appType: 'zjtAPK' // app类型
      },
      // 上传数据
      uploadUrl: `${process.env.VUE_APP_BASE_API}/mFileUpdate/apk_upload`,
      fileList: [],
      headers: { authorization: getToken() },
      // dialong必填项规则
      rules: {
        versionName: [
          {
            type: 'string',
            required: true,
            message: '请输入版本名称',
            trigger: 'blur'
          }
        ],
        versionCode: [
          {
            // type: 'number',
            required: true,
            message: '请输入版本名',
            trigger: 'blur'
          }
        ],
        packages: [
          {
            type: 'string',
            required: true,
            message: '请输入包名',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  watch: {
    dialogVisible(newVal, oldVal) {
      if (!newVal) {
        this.uploadApkData.versionCode = null
        this.uploadApkData.versionName = ''
        this.uploadApkData.packages = ''
        this.uploadApkData.isOnline = '0'
        this.uploadApkData.remark = ''
        this.uploadApkData.versionPath = ''
        this.uploadApkData.versionPath = ''
        this.uploadApkData.id = ''
        if (this.fileList.length !== 0) {
          this.$refs.apkUpload.abort(this.fileList[0])
          this.$refs.apkUpload.clearFiles()
          this.fileList = []
        }
      }
    }
  },
  mounted() {
    this.getApkList()
  },
  methods: {
    // 获取apk列表
    getApkList() {
      this.loading = true
      getApkList(this.searchForm)
        .then(result => {
          this.loading = false
          // 结构data出来并赋值
          const { data } = result
          this.tableData = data.records
          console.log(this.tableData)
        })
        .catch(err => {
          this.loading = false
          console.error(err)
          this.$message({
            type: 'error',
            message: '获取列表失败'
          })
        })
    },
    // 编辑操作
    openEdit({ row }) {
      console.log(row)

      this.dialogVisible = true
      this.uploadApkData.id = row.id
      this.uploadApkData.versionCode = row.versionCode
      this.uploadApkData.versionName = row.versionName
      this.uploadApkData.packages = row.packages
      this.uploadApkData.isOnline = row.isOnline.toString()
      this.uploadApkData.remark = row.remark
      this.uploadApkData.versionPath = row.versionPath
      console.log(this.uploadApkData)
    },
    downloadApk({ row }) {
      const { versionPath } = row
      const downloadPath = `${process.env.VUE_APP_BASEIMG_API}/${versionPath}`
      console.log(downloadPath)
      const aLink = document.createElement('a')
      aLink.href = downloadPath
      document.body.appendChild(aLink)
      aLink.click()
      aLink.remove()
      console.log(aLink)
    },
    apkDelelte({ row }) {
      console.log(row)
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          appDelete([row.id])
            .then(result => {
              console.log(result)
              this.$message({
                type: 'success',
                message: '删除成功!'
              })
              this.getApkList()
            })
            .catch(err => {
              console.error(err)
              this.$message({
                type: 'error',
                message: '删除失败'
              })
            })
        })
        .catch(() => {})
    },
    // dialong提交apk
    dialongUploadApk() {
      this.$refs.dialongForm.validate(valid => {
        if (valid) {
          if (this.uploadApkData.versionPath) {
            addOrUpdate(this.uploadApkData)
              .then(result => {
                console.log(result)
                this.getApkList()

                console.log(123)
                this.dialogVisible = false
                console.log(this.uploadApkData)
              })
              .catch(err => {
                console.error(err)
                this.$message({
                  type: 'error',
                  message: '提交版本失败'
                })
              })
          } else {
            this.$message({
              type: 'error',
              message: '请上传apk文件！'
            })
          }
        } else {
          return false
        }
      })
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done()
        })
        .catch(_ => {})
    },
    // 翻页
    handleSizeChange(val) {
      this.searchForm.limit = val
      this.getApkList()
    },
    handleCurrentChange(val) {
      this.searchForm.page = val
      this.getApkList()
    },
    // 上传事件
    beforeUpload(file) {
      console.log(file)
      const size = file.size / 1024
      if (
        file.type === 'application/vnd.android.package-archive' &&
        size < 30000
      ) {
        this.$message({
          type: 'info',
          message: '正在上传'
        })
        return true
      } else {
        this.$message({
          type: 'error',
          message: '上传文件有误'
        })
        return false
      }
    },
    fileChange(file, fileList) {
      console.log(file, fileList)
      this.fileList = fileList
    },
    handleRemove(file, fileList) {
      console.log(file, fileList)
      this.fileList = fileList
      this.uploadApkData.versionPath = ''
    },
    // 超出文件限制时候提示
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${
          files.length
        } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      )
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    // 上传成功
    uploadSuccess(response, file, fileList) {
      console.log(response, file, fileList)
      this.fileList = fileList
      // 设置文件路径
      const { subPath, fileName } = response
      this.uploadApkData.versionPath = subPath + '/' + fileName
      // 自动设置apk信息
      const parser = new AppInfoParser(file.raw) // or xxx.ipa
      parser
        .parse()
        .then(result => {
          console.log('app info ----> ', result)
          const { versionCode, versionName, package: packName } = result
          this.uploadApkData.versionName = versionName
          this.uploadApkData.versionCode = versionCode
          this.uploadApkData.packages = packName
        })
        .catch(err => {
          console.log('err ----> ', err)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.content {
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
  // align-items: flex-start;
}
</style>
