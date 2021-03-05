import request from '@/utils/request'
/**
 *获取apk列表
 *
 * @export
 * @returns
 */
export function getApkList(postData) {
  return request({
    url: 'appVersion/listByPage ',
    method: 'post',
    data: postData
  })
}
/**
 * 提交apk
 * @export
 * @param {*} postData
 * @returns
 */
export function addOrUpdate(postData) {
  return request({
    url: 'appVersion/addOrUpdate ',
    method: 'post',
    data: postData
  })
}
/**
 * 删除apk
 * @export
 * @param {*} postData
 * @returns
 */
export function appDelete(postData) {
  return request({
    url: 'appVersion/delete ',
    method: 'post',
    data: postData
  })
}
