<template>
  <div>
    <el-upload
      :action="uploadUrl"
      list-type="picture-card"
      :headers="headers"
      :multiple="false"
      :on-preview="handlePictureCardPreview"
      :on-success="handleImageSuccess"
      :on-remove="handleRemove"
      :class="{hide:hideUpload}"
      :before-upload="beforeUpload"
      :file-list="fileList"
      :limit="limitCount"
      :disabled="disabled"
    >
      <i class="el-icon-plus" />
      <div slot="tip" class="el-upload__tip">只能上传jpg/jpeg/png文件，且不超过1M</div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl">
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from '../../utils/auth'
export default {
  name: 'Upload',
  props: {
    value: {
      type: Array,
      default() {
        return []
      }
    },
    limitCount: {
      type: Number,
      default: 1
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      uploadUrl: `${process.env.VUE_APP_BASE_API}/mFileUpdate/file_upload/`,
      dialogImageUrl: '',
      dialogVisible: false,
      hideUpload: false,
      headers: { authorization: getToken() }
    }
  },
  computed: {
    emitFileList() {
      return this.value
    },
    fileList() {
      const imgData = []
      this.value.map(item => {
        const n = {
          name: 'img',
          url: `${process.env.VUE_APP_BASEIMG_API}/${item}`
        }
        imgData.push(n)
      })
      return imgData
    }
  },
  watch: {
    value: function(val) {
      console.log(val)
      this.hideUpload = val.length >= this.limitCount
    }
  },
  methods: {
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    beforeUpload(file) {
      const size = file.size / 1024
      if (
        (file.type.indexOf('jpg') !== -1 ||
          file.type.indexOf('jpeg') !== -1 ||
          file.type.indexOf('png') !== -1) &&
        size < 1000
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
    handleRemove(file, fileList) {
      const { url } = file
      const id = this.value.indexOf(url)
      this.value.splice(id, 1)
      this.emitInput()
    },
    handleImageSuccess(response, file, fileList) {
      console.log(response, 'response')
      console.log(file, 'file')
      console.log(fileList, 'fileList')
      const { data } = response
      this.value.push(`${data.subPath}/${data.fileName}`)
      this.$message({
        type: 'success',
        message: '上传成功'
      })
      this.emitInput()
    },
    emitInput() {
      this.$emit('input', this.emitFileList)
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
