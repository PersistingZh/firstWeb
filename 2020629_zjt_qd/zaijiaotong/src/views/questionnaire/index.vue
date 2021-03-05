<template>
  <div>
    <input
      ref="excel-upload-input"
      class="excel-upload-input"
      type="file"
      accept=".xlsx, .xls"
      @change="handleClick"
    >
    <div class="drop">
      Drop excel file here or
      <el-button style="margin-left:16px;" size="mini" type="primary" @click="handleUpload">Browse</el-button>
    </div>
  </div>
</template>

<script>
import XLSX from 'xlsx'

export default {
  name: 'Questionnaire',
  data() {
    return {
    }
  },
  methods: {
    handleUpload() {
      this.$refs['excel-upload-input'].click()
    },
    handleClick(e) {
      const files = e.target.files
      const rawFile = files[0] // only use files[0]
      if (!rawFile) return
      this.upload(rawFile)
    },
    upload(rawFile) {
      this.$refs['excel-upload-input'].value = null // fix can't select the same excel
      // console.log(rawFile)

      const reader = new FileReader()
      reader.onload = e => {
        // console.log(e)
        const data = e.target.result
        // console.log('data', data)
        const workbook = XLSX.read(data, { type: 'array' })
        const firstSheetName = workbook.SheetNames[0]
        const worksheet = workbook.Sheets[firstSheetName]
        // const header = this.getHeaderRow(worksheet)
        const results = XLSX.utils.sheet_to_json(worksheet)
        // console.log('workbook', workbook)
        // console.log('firstSheetName', firstSheetName)
        console.log('results', results)
        // console.log('header', header)
        // console.log('worksheet', worksheet)
      }
      reader.readAsArrayBuffer(rawFile)
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
