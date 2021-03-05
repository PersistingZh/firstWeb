import request from '@/utils/request'
import { getRefToken } from '@/utils/auth'
import Axios from 'axios'
/**
 * 用户登陆
 * @export
 * @param {*} username
 * @param {*} password
 * @param {number} [type=2]
 * @returns
 */
export function login(username, password, type = 2) {
  return request({
    url: '/sys/user/login',
    method: 'post',
    data: {
      username,
      password,
      type
    }
  })
}
/**
 * 更新用户token
 * @export
 * @returns
 */
export function upDateToken() {
  return request({
    url: '/sys/user/token',
    method: 'get',
    headers: {
      refresh_token: getRefToken()
    }
  })
}

export function getInfo(token) {
  return request({
    url: '/sys/home',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/sys/user/logout',
    method: 'get',
    headers: {
      refresh_token: getRefToken()
    }
  })
}
/**
 * 获取子级别字典
 * @export
 * @param {*} sdPid
 * @returns
 */
export function getSysChildDict(sdPid) {
  return request({
    url: 'sysDict/getSysDictListByCondition',
    method: 'post',
    data: { sd_pid: sdPid }
  })
}

/**
 * 获取本级字典
 * @export
 * @param {*} sdPid
 * @returns
 */
export function getSysDict(sdPid) {
  return request({
    url: 'sysDict/getSysDictListByCondition',
    method: 'post',
    data: { sd_code: sdPid }
  })
}

/**
 * 获取字典1
 * @export
 * @param {*} type
 * @param {*} sdLevel
 * @returns
 */
export function getSysDict1(type, sdLevel) {
  return request({
    url: 'sysDict/getSysDictListByCondition',
    method: 'post',
    data: { sd_level: sdLevel, type }
  })
}

/**
 * 获取字典2 证件类型
 * @export
 * @param {*} type
 * @param {*} sdLevel
 * @returns
 */
export function getSysDict2(type, sdLevel) {
  return request({
    url: 'sysDict/getSysDictListByCondition',
    method: 'post',
    data: { sd_level: sdLevel, type }
  })
}
/**
 * 修改用户密码
 * @export
 * @param {*} { newPwd, oldPwd }
 * @returns
 */
export function updatePassword({ newPwd, oldPwd }) {
  return request({
    url: '/sys/user/pwd',
    method: 'put',
    headers: {
      refresh_token: getRefToken()
    },
    data: { newPwd, oldPwd }
  })
}

/**
 * 首页数据展示
 * @export
 * @returns
 */
export function getStatistics(postData) {
  const requestApiList = [
    'mbLogin/getFgCountsData',
    'mbLogin/getZhCountsData',
    'mbLogin/getLyCountsData',
    'mbLogin/getDfjzkCountsData',
    'mbLogin/getDoorCountsData'
  ]
  const apiAll = []
  for (const apiUrl of requestApiList) {
    const api = request({
      url: apiUrl,
      method: 'post',
      data: postData
    })
    apiAll.push(api)
  }

  return Axios.all(apiAll)
  // return
}

/**
 * 矫正通登录页面 发送验证码
 * @export
 * @param {String} phone 手机号码
 * @returns
 */
export function sendoutVerificationCode(phone) {
  return request({
    url: '/sys/getVerify',
    method: 'post',
    data: { 'phone': phone }
  })
}
/**
 * 矫正通登录页面 进行登录
 * @export
 * @param {*} phone 手机号码
 * @param {*} verificationCode 验证码
 * @param {number} [type=2]
 * @returns
 */
export function login2(phone, verificationCode, type = 2) {
  return request({
    url: '/sys/user/login',
    method: 'post',
    data: {
      phone,
      verificationCode,
      type
    }
  })
}
