import request from '@/utils/request'

/**
 * 查询矫正人员列表分页数据
 * @export
 * @param {Number} limit 一页多少条
 * @param {Number} page 第几页
 * @param {String} phone 手机号码
 * @returns
 */
export function getZjtZjry(limit, page, phone) {
  return request({
    url: 'zjtZjry/listByPage',
    method: 'post',
    data: {
      limit,
      page,
      phone
    }
  })
}
/**
 * 添加矫正人员数据
 * @export
 * @param {*} postData
 * @returns
 */
export function addZjtZjry(postData) {
  return request({
    url: '/zjtZjry/add',
    method: 'post',
    data: postData
  })
}
/**
 * 删除矫正人员信息
 * @export
 * @param {*} ids
 * @returns
 */
export function deleteZjtZjry(ids) {
  return request({
    url: '/zjtZjry/delete',
    method: 'delete',
    data: ids
  })
}
/**
 * 编辑矫正人员信息
 * @export
 * @param {*} postData
 * @returns
 */
export function updatezjtZjry(postData) {
  return request({
    url: '/zjtZjry/update',
    method: 'PUT',
    data: postData
  })
}
/**
 * 查询工作人员列表分页数据
 * @export
 * @param {Number} limit 一页多少条
 * @param {Number} page 第几页
 * @returns
 */
export function getZjtGzry(limit, page) {
  return request({
    url: '/zjtGzry/listByPage',
    method: 'post',
    data: {
      limit,
      page
    }
  })
}
/**
 * 添加工作人员信息
 * @export
 * @param {*} postData
 * @returns
 */
export function addZjtGzry(postData) {
  return request({
    url: '/zjtGzry/add',
    method: 'post',
    data: postData
  })
}
/**
 * 删除工作人员信息
 * @export
 * @param {*} ids
 * @returns
 */
export function deleteZjtGzry(ids) {
  return request({
    url: '/zjtGzry/delete',
    method: 'delete',
    data: ids
  })
}
/**
 * 编辑工作人员信息
 * @export
 * @param {*} postData
 * @returns
 */
export function updateZjtGzry(postData) {
  return request({
    url: '/zjtGzry/update',
    method: 'PUT',
    data: postData
  })
}
/**
 * 查询考勤管理列表分页数据
 * @export
 * @param {Number} limit 一页多少条
 * @param {Number} page 第几页
 * @returns
 */
export function taskResultView(limit, page) {
  return request({
    url: '/taskResultView/listByPage',
    method: 'post',
    data: {
      limit,
      page
    }
  })
}
// /**
//  * 添加考勤管理信息
//  * @export
//  * @param {*} postData
//  * @returns
//  */
// export function addTaskResultView(postData) {
//   return request({
//     url: '/taskResultView/add',
//     method: 'post',
//     data: postData
//   })
// }
/**
 * 查询预警列表分页数据
 * @export
 * @param {Number} limit 一页多少条
 * @param {Number} page 第几页
 * @returns
 */
export function getZjtWarning(limit, page) {
  return request({
    url: '/zjtWarning/listByPage',
    method: 'post',
    data: {
      limit,
      page
    }
  })
}
/**
 * 添加预警信息
 * @export
 * @param {*} postData
 * @returns
 */
export function addZjtWarning(postData) {
  return request({
    url: '/zjtWarning/add',
    method: 'post',
    data: postData
  })
}
/**
 * 删除预警信息
 * @export
 * @param {*} ids
 * @returns
 */
export function deletezjtWarning(ids) {
  return request({
    url: '/zjtWarning/delete',
    method: 'delete',
    data: ids
  })
}
/**
 * 编辑预警信息
 * @export
 * @param {*} postData
 * @returns
 */
export function updateZjtWarning(postData) {
  return request({
    url: '/zjtWarning/update',
    method: 'PUT',
    data: postData
  })
}
/**
 * 查询任务管理分页数据
 * @export
 * @param {Number} limit 一页多少条
 * @param {Number} page 第几页
 * @returns
 */
export function getZjtTask(limit, page) {
  return request({
    url: '/zjtTask/listByPage',
    method: 'post',
    data: {
      limit,
      page
    }
  })
}
/**
 * 添加任务信息
 * @export
 * @param {*} postData
 * @returns
 */
export function addZjtTask(postData) {
  return request({
    url: '/zjtTask/add',
    method: 'post',
    data: postData
  })
}
/**
 * 删除任务信息
 * @export
 * @param {*} ids
 * @returns
 */
export function deleteZjtTask(ids) {
  return request({
    url: '/zjtTask/delete',
    method: 'delete',
    data: ids
  })
}
/**
 * 编辑任务信息
 * @export
 * @param {*} postData
 * @returns
 */
export function updateZjtTask(postData) {
  return request({
    url: '/zjtTask/update',
    method: 'PUT',
    data: postData
  })
}
/**
 * 矫正人员配置任务
 * @export
 * @param {String} taskId 任务ID
 * @param {String} userId 矫正人员用户ID
 * @returns
 */
export function pzZjtTaskUserRelate(taskId, userId) {
  return request({
    url: '/zjtTaskUserRelate/add',
    method: 'post',
    data: { 'taskIds': taskId, 'userIds': userId }
  })
}
/**
 * 矫正人员配置任务2
 * @export
 * @param {String} taskId 任务ID
 * @param {String} userId 矫正人员用户ID
 * @returns
 */
export function plpzZjtZjry(taskId, userId) {
  return request({
    url: 'zjtZjry/configTask',
    method: 'post',
    data: { 'taskIds': taskId, 'userIds': userId }
  })
}
/**
 * 查询任务管理配置任务的接口
 * @export
 * @param {String} userId 矫正人员用户ID
 * @returns
 */
export function selectZjtTaskUserRelate(userId) {
  return request({
    url: '/zjtTaskUserRelate/listByUserIdAndNotConfig',
    method: 'post',
    data: { userId }
  })
}
/**
 * 查询任务分页接口
 * @export
 * @param {String} limit limit
 * @param {String} page page
 * @returns
 */
export function selectTaskUserRelateView(limit, page) {
  return request({
    url: '/taskUserRelateView/listByPage',
    method: 'post',
    data: { limit, page }
  })
}
/**
 * 查询用户任务接口 传userId
 * @export
 * @param {String} userId limit
 * @returns
 */
export function selectzjtTaskUserRelate(userId) {
  return request({
    url: '/zjtTaskUserRelate/listByUserId',
    method: 'post',
    data: { userId }
  })
}
/**
 * 查询用户任务接口 传taskId
 * @export
 * @param {String} taskId 任务ID
 * @param {Number} phone 手机号码
 * @returns
 */
export function selectZjtTaskUserRelatetaskId(taskId, phone) {
  return request({
    url: '/zjtTaskUserRelate/listByUserId',
    method: 'post',
    data: { taskId, phone }
  })
}
/**
 * 查询任务分页接口 传taskId
 * @export
 * @param {String} limit limit
 * @param {String} page page
 * @param {String} taskId 传taskId
 * @returns
 */
export function selectRwtaskUserRelateView(limit, page, taskId) {
  return request({
    url: '/taskUserRelateView/listByPage',
    method: 'post',
    data: { limit, page, taskId }
  })
}

/**
 * 配置预警
 * @export
 * @param {String} userIds 矫正人员ID
 * @param {String} warningIds 预警ID
 * @returns
 */
export function pzyjZjtWarningUserRelate(userIds, warningIds) {
  return request({
    url: '/zjtWarningUserRelate/add',
    method: 'post',
    data: { 'userIds': userIds, 'warningIds': warningIds }
  })
}
/**
 * 配置预警2
 * @export
 * @param {String} userIds 矫正人员ID
 * @param {String} warningIds 预警ID
 * @returns
 */
export function plpzyjZjtZjry(userIds, warningIds) {
  return request({
    url: 'zjtZjry/configWarning',
    method: 'post',
    data: { 'userIds': userIds, 'warningIds': warningIds }
  })
}
/**
 * 查询所有预警列表view
 * @export
 * @param {String} limit limit
 * @param {String} page page
 * @returns
 */
export function selectWarningUserRelateView(limit, page) {
  return request({
    url: '/warningUserRelateView/listByPage',
    method: 'post',
    data: { limit, page }
  })
}
/**
 * 查询查询预警与用户关系表分页数据 传warningId
 * @export
 * @param {String} limit limit
 * @param {String} page page
 * @param {String} warningId 传warningId
 * @returns
 */
export function selectwarningUserRelateViewwarningId(limit, page, warningId) {
  return request({
    url: '/warningUserRelateView/listByPage',
    method: 'post',
    data: { limit, page, warningId }
  })
}
/**
 * 取消用户匹配任务
 * @export
 * @param {*} ids
 * @returns
 */
export function deleteZjtTaskUserRelate(ids) {
  return request({
    url: '/zjtTaskUserRelate/delete',
    method: 'delete',
    data: ids
  })
}
/**
 * 删除预警与用户关系表
 * @export
 * @param {*} ids
 * @returns
 */
export function deleteZjtWarningUserRelate(ids) {
  return request({
    url: '/zjtWarningUserRelate/delete',
    method: 'delete',
    data: ids
  })
}
/**
 * 预警信息 离线预警
 * @export
 * @param {Number} limit 一页多少条
 * @param {Number} page 第几页
 * @param {Number} isOnline 状态
 * @returns
 */
export function getzjtZjryendisOnline(limit, page, isOnline) {
  return request({
    url: 'zjtZjry/listByPage',
    method: 'post',
    data: {
      limit,
      page,
      isOnline
    }
  })
}
/**
 * 预警信息 定位预警
 * @export
 * @param {Number} limit 一页多少条
 * @param {Number} page 第几页
 * @param {String} createTime 筛选时间
 * @returns
 */
export function getWarningRecordView(limit, page, createTime) {
  return request({
    url: 'warningRecordView/listByPage',
    method: 'post',
    data: {
      limit,
      page,
      createTime
    }
  })
}
/**
 * 预警信息 考勤预警
 * @export
 * @param {Number} limit 一页多少条
 * @param {Number} page 第几页
 * @param {String} isFinish isFinish
 * @param {Number} clockEffective clockEffective
 * @param {String} createTime 时间
 * @returns
 */
export function getTaskResultViewKqyj(limit, page, isFinish, clockEffective, createTime) {
  return request({
    url: 'taskResultView/listByPage',
    method: 'post',
    data: {
      limit,
      page,
      isFinish,
      clockEffective,
      createTime
    }
  })
}
/**
 * 设置离线预警时间
 * @export
 * @param {String} parameterName 离线预警时间名称
 * @param {Number} parameterVal 离线预警时间隔（分钟）
 * @returns
 */
export function setSysParameter(parameterName, parameterVal) {
  return request({
    url: 'sysParameter/add',
    method: 'post',
    data: { 'parameterName': parameterName, 'parameterVal': parameterVal }
  })
}
/**
 * 回填离线预警时间
 * @export
 * @param {String} parameterName 离线预警时间名称
 * @returns
 */
export function selectSysParameter(parameterName) {
  return request({
    url: 'sysParameter/getSysParameterByName',
    method: 'post',
    data: { 'parameterName': parameterName }
  })
}
