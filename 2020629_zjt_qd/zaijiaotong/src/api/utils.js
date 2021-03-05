import Axios from 'axios'
import { getToken } from '@/utils/auth'
import request from '@/utils/request'

/**
 * 下载图片文件
 * @export
 * @param {*} url
 * @returns
 */
export function downloadFile(filepath, filename) {
  return Axios({
    baseURL: process.env.VUE_APP_BASE_API,
    method: 'get',
    url: `/fileDownload?filename=${filename}&filepath=${filepath}`,
    responseType: 'arraybuffer',
    headers: {
      authorization: getToken(),
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}
/**
 * 上传文件
 * @export
 * @param {*} files
 * @returns
 */
export function uploadFiles(files) {
  return request({
    url: 'mFileUpdate/file_upload',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: files
  })
}
// 确认上传文件
export function importExcel(url, excelPath, type) {
  return request({
    url,
    method: 'post',
    data: {
      excelPath,
      type
    },
    timeout: null
  })
}

/**
 * 下载导入模板
 * @export
 * @param {*} files
 * @returns
 */
export function downExcelModule(type) {
  return request({
    url: 'excelWeb/outportExcelModule',
    method: 'post',
    params: { type }
  })
}

