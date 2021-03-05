<template>
  <div>
    <el-dialog title="数据导入" :visible.sync="exportInDialogVisible" width="70%">
      <el-form label-width="150px" label-position="top">
        <input
          ref="excel-upload-input"
          class="excel-upload-input"
          type="file"
          accept=".xlsx, .xls"
          @change="handleClick"
        >
        <el-form-item label="Excel模板下载">
          <el-button type="primary" @click="templateDownload">下载</el-button>
        </el-form-item>
        <el-form-item label="上传文件">
          <el-button type="primary" @click="handleUpload">上传文件</el-button>
        </el-form-item>
        <el-form-item label="预览">
          <el-table
            :data="importTableData"
            border
            highlight-current-row
            height="250px"
            style="width: 100%;margin-top:20px;"
          >
            <el-table-column
              v-for="item of importTableHeader"
              :key="item"
              min-width="150px"
              :prop="item"
              :label="item"
            />
          </el-table>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="exportInDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="importData">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import XLSX from 'xlsx'
import { saveFiles } from '../../utils/utils'
import {
  downExcelModule,
  downloadFile,
  importExcel,
  uploadFiles
} from '../../api/utils'
import { Loading } from 'element-ui'

export default {
  name: 'TeamplatImport',
  data() {
    return {
      importTableData: [],
      importTableHeader: [],
      uploadFile: Blob,
      exportInDialogVisible: false,
      templateName: '',
      excelPath: '',
      url: ''
    }
  },
  methods: {
    showtemplatImport(templateName, url) {
      console.log('templateName, funName,', templateName, url)
      this.templateName = templateName
      this.url = url
      this.exportInDialogVisible = true
    },
    // 拉起上传按钮事件
    handleUpload() {
      this.$refs['excel-upload-input'].click()
    },
    // 选择完excel文件后
    handleClick(e) {
      const files = e.target.files
      const rawFile = files[0] // only use files[0]
      this.uploadFile = files[0]
      if (!rawFile) return
      this.$refs['excel-upload-input'].value = null // fix can't select the same excel

      console.log(rawFile)
      // 打开二进制读取Excel文件
      const reader = new FileReader()
      reader.onload = e => {
        const data = e.target.result
        const workbook = XLSX.read(data, { type: 'array' })
        const firstSheetName = workbook.SheetNames[0]
        const worksheet = workbook.Sheets[firstSheetName]
        const header = this.getHeaderRow(worksheet)
        const results = XLSX.utils.sheet_to_json(worksheet)
        this.importTableData = results
        this.importTableHeader = header
      }
      // 开始读取
      reader.readAsArrayBuffer(rawFile)
      const formData = new FormData()
      formData.append(rawFile.name, rawFile)
      uploadFiles(formData).then(res => {
        console.log(res)
        const { data } = res
        this.excelPath = `${data.subPath}/${data.fileName}`
      })
    },
    getHeaderRow(sheet) {
      const headers = []
      const range = XLSX.utils.decode_range(sheet['!ref'])
      let C
      const R = range.s.r
      /* start in the first row */
      for (C = range.s.c; C <= range.e.c; ++C) {
        /* walk every column in the range */
        const cell = sheet[XLSX.utils.encode_cell({ c: C, r: R })]
        /* find the cell in the first row */
        let hdr = 'UNKNOWN ' + C // <-- replace with your desired default
        if (cell && cell.t) hdr = XLSX.utils.format_cell(cell)
        headers.push(hdr)
      }
      return headers
    },
    // 上传提交
    importData() {
      const options = {
        lock: true,
        text: '上传中，请稍等...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      }
      console.log(this.$refs)
      console.log(this.uploadFile)
      console.log(this.url, this.excelPath, this.templateName)
      if (this.uploadFile && this.uploadFile.length !== 0) {
        if (this.importTableData.length === 0 && this.importTableData) {
          this.$message({
            type: 'error',
            message: '请填写上传文件内容'
          })
          return
        }
        const loadingInstance = Loading.service(options)

        importExcel(this.url, this.excelPath, this.templateName)
          .then(result => {
            console.log(result)
            this.exportInDialogVisible = false
            this.importTableData = []
            this.importTableHeader = []
            this.uploadFile = null
            this.$message({
              type: 'success',
              message: result.data
            })
            this.$emit('refresh', true)
            this.$nextTick(() => {
              // 以服务的方式调用的 Loading 需要异步关闭
              loadingInstance.close()
            })
          })
          .catch(err => {
            console.error(err)
            this.$message({
              type: 'error',
              message: '导入失败'
            })
            this.$nextTick(() => {
              // 以服务的方式调用的 Loading 需要异步关闭
              loadingInstance.close()
            })
          })
      } else {
        this.$message({
          type: 'error',
          message: '请选择上传文件'
        })
      }
    },
    // 导入
    exportIn(templateName) {
      this.templateName = templateName
      this.exportInDialogVisible = true
    },
    // 下载模板
    templateDownload() {
      console.log(this.templateName)
      downExcelModule(this.templateName).then(res => {
        console.log(res)
        const { data } = res
        downloadFile(data[2], data[1]).then(downRes => {
          console.log(downRes)
          if (downRes.status === 200) {
            const blob = new Blob([downRes.data], {
              type: 'application/vnd.ms-excel'
            })
            const fileName = data[1]
            console.log(blob, fileName)
            saveFiles(blob, fileName)
          } else {
            this.$message({
              type: 'error',
              message: '下载失败'
            })
          }
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
