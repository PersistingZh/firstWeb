/**
 * 保存文件
 * @export
 * @param {*} blob
 * @param {*} fileName
 */
export function saveFiles(blob, fileName) {
  // 兼容ie
  if (window.navigator && window.navigator.msSaveOrOpenBlob) {
    window.navigator.msSaveOrOpenBlob(blob, fileName)
  } else {
    const objectUrl = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = objectUrl
    link.setAttribute('download', fileName)
    document.body.appendChild(link)
    link.click()
    window.URL.revokeObjectURL(link.href)
  }
}
