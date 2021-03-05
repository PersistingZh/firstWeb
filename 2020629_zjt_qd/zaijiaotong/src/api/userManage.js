import request from '@/utils/request'
/**
 * 系统管理-数据字典-获取数据字典
 * @export
 * @param {*} postData
 * @returns
 */
export function getDiscLevel(postData) {
  return request({
    url: 'sysDict/getSysDictListByCondition',
    method: 'post',
    data: postData
  })
}
/**
 * 系统管理-数据字典-获取数据字典列表
 * @export
 * @param {*} postData
 * @returns
 */
export function getDiscLevellistByPage(postData) {
  return request({
    url: 'sysDict/listByPage',
    method: 'post',
    data: postData
  })
}
/**
 * 系统管理-数据字典-删除数据字典
 * @export
 * @param {*} postData
 * @returns
 */
export function getDiscLevelDelete(postData) {
  return request({
    url: 'sysDict/delete',
    method: 'post',
    data: postData
  })
}
/**
 * 系统管理-数据字典-新增或修改字典项
 * @export
 * @param {*} postData
 * @returns
 */
export function getDiscAddOrUpdate({ sdPid, sdName, bz, id }) {
  return request({
    url: 'sysDict/addOrUpdate',
    method: 'post',
    data: { sdPid, sdName, bz, id }
  })
}
/**
 * 系统管理-角色管理-获取角色列表
 * @export
 * @param {*} postData
 * @returns
 */
export function getRolesList({ roleName, pageNum, pageSize }) {
  return request({
    url: 'sys/roles',
    method: 'post',
    data: { roleName, pageNum, pageSize }
  })
}
/**
 * 系统管理-角色管理-获取角色权限列表
 * @export
 * @param {*} postData
 * @returns
 */
export function getRolesPowerList({
  permissionName,
  roleId,
  limit,
  page,
  flagDel
}) {
  return request({
    url: 'sysRolePermissionRelate/listByPage',
    method: 'post',
    data: { permissionName, roleId, limit, page, flagDel }
  })
}
/**
 * 系统管理-角色管理-新增角色
 * @export
 * @param {*} postData
 * @returns
 */
export function addRole({ description, name, permissions, status = 1 }) {
  return request({
    url: 'sys/role',
    method: 'post',
    data: { description, name, permissions, status }
  })
}
/**
 * 系统管理-角色管理-删除角色
 * @export
 * @param {*} postData
 * @returns
 */
export function delRole(id) {
  return request({
    url: `sys/role/${id}`,
    method: 'delete'
  })
}
/**
 * 系统管理-角色管理-获取角色详情
 * @export
 * @param {*} id
 * @returns
 */
export function getRole(id) {
  return request({
    url: `sys/role/${id}`,
    method: 'get'
  })
}
/**
 * 获取角色权限
 * @export
 * @param {*} id
 * @returns
 */
export function getsysRolePermissionViewRole(id) {
  return request({
    url: `sysRolePermissionView/listByCondition`,
    method: 'post',
    data: { id }
  })
}
/**
 * 系统管理-角色管理-更新角色
 * @export
 * @param {*} { id, description, name, permissions, status = 1 }
 * @returns
 */
export function updateRole({ id, description, name, permissions, status = 1 }) {
  return request({
    url: 'sys/role',
    method: 'put',
    data: { id, description, name, permissions, status }
  })
}
/**
 * 删除角色某个权限
 * @export
 * @param {*}
 * @returns
 */
export function delRoleInfoItem(permissionId, roleId) {
  return request({
    url: 'sys/role/delete',
    method: 'post',
    data: { permissionId, roleId }
  })
}

/**
 * 查询附加文件分页数据
 * @export
 * @param {Number} limit 一页多少条
 * @param {Number} page 第几页
 * @param {String} ms 筛选条件, 文件描述
 * @returns
 */
export function getFileManage(postData) {
  return request({
    url: '/sysFileFjxx/listByPage',
    method: 'post',
    data: postData
  })
}
/**
 * 删除附加文件管理的数据
 * @export
 * @param {*} ids
 * @returns
 */
export function delfilems(ids) {
  return request({
    url: '/sysFileFjxx/delete',
    method: 'post',
    data: ids
  })
}

/**
 * 添加附加文件管理的数据
 * @export
 * @param {*} postData
 * @returns
 */
export function addFileManage(postData) {
  return request({
    url: '/sysFileFjxx/addOrUpdate',
    method: 'post',
    data: postData
  })
}

/**
 * 修改附加文件管理的数据
 * @export
 * @param {*} postData
 * @returns
 */
export function updateFileManage(postData) {
  return request({
    url: '/sysFileFjxx/update',
    method: 'PUT',
    data: postData
  })
}

/**
 * 查询用户管理分页数据
 * @export
 * @param {Number} limit 一页多少条
 * @param {Number} page 第几页
 * @param {Number} roleName 筛选条件 用户名
 * @param {Number} name 筛选条件  角色名称
 * @returns
 */
export function getSysUserRoleView(postData) {
  return request({
    url: '/sysUserRoleView/listByPage',
    method: 'post',
    data: postData
  })
}

/**
 * 添加用户管理的数据
 * @export
 * @param {*} postData
 * @returns
 */
export function addSysUser(postData) {
  return request({
    url: '/sys/user',
    method: 'post',
    data: postData
  })
}

/**
 * 查询所有角色信息
 * @export
 * @returns
 */
export function getSysRoles() {
  return request({
    url: 'sys/allRoles',
    method: 'post',
    data: {}
  })
}
/**
 * 更新用户信息
 * @export
 * @param {*} postData
 * @returns
 */
export function upDateUser(postData) {
  return request({
    url: 'sys/user/info',
    method: 'put',
    data: postData
  })
}

/**
 * 删除用户管理数据
 * @export
 * @param {*} ids
 * @returns
 */
export function deleteSysUser(ids) {
  return request({
    url: '/sys/user',
    method: 'delete',
    data: ids
  })
}
/**
 * 获取用户管理数据
 * @export
 * @param {*} ids
 * @returns
 */
export function getSysUser(id) {
  return request({
    url: '/sys/getUserById',
    method: 'get',
    params: { id }
  })
}
/**
 * 获取用户下级树节点
 * @export
 * @param {*} sdPid
 * @returns
 */
export function getSysUserTree(sdPid) {
  return request({
    url: 'sys/user/treeHasOneSelf',
    method: 'get',
    params: { sdPid }
  })
}
/**
 * 获取派出所节点
 * @export
 * @param {*} sdPid
 * @returns
 */
export function getSysTree(sdPid) {
  return request({
    url: 'sys/user/tree',
    method: 'get',
    params: { sdPid }
  })
}

/**
 * 修改用户管理的数据
 * @export
 * @param {*} postData
 * @returns
 */
export function updateSysUser(postData) {
  return request({
    url: '/sys/user',
    method: 'PUT',
    data: postData
  })
}

/**
 * 用户管理配置角色的数据
 * @export
 * @param {*} postData
 * @returns
 */
export function updatesysUserRoleView(postData) {
  return request({
    url: 'sysUserRoleView/update',
    method: 'post',
    data: postData
  })
}

/**
 * 查询app超链接管理 超链接类型分页数据
 * @export
 * @param {Number} limit 一页多少条
 * @param {Number} page 第几页
 * @param {Number} hyperlinkType 筛选条件 超链接类型
 * @returns
 */
export function getSysHyperlinkType(postData) {
  return request({
    url: 'sysHyperlinkType/listByPage',
    method: 'post',
    data: postData
  })
}
/**
 * 添加app超链接管理 超链接类型分页数据
 * @export
 * @param {*} postData
 * @returns
 */
export function addSysHyperlinkType(postData) {
  return request({
    url: '/sysHyperlinkType/add',
    method: 'post',
    data: postData
  })
}
/**
 * 修改app超链接管理 超链接类型分页数据
 * @export
 * @param {*} postData
 * @returns
 */
export function updateSysHyperlinkType(postData) {
  return request({
    url: '/sysHyperlinkType/update',
    method: 'post',
    data: postData
  })
}
/**
 * 删除app超链接管理 超链接类型分页数据
 * @export
 * @param {*} ids
 * @returns
 */
export function deleteSysHyperlinkType(ids) {
  return request({
    url: '/sysHyperlinkType/delete',
    method: 'delete',
    data: ids
  })
}
/**
 * 排序app超链接管理 超链接类型分页数据
 * @export
 * @param {*} org1 //第一个id
 * @param {*} org2 //第二个id
 * @returns
 */
export function orderSysHyperlinkType(org1, org2) {
  return request({
    url: '/sysHyperlinkType/updateOrderById',
    method: 'post',
    data: { org1, org2 }
  })
}

/**
 * 查询app超链接管理 超链接标题分页数据
 * @export
 * @param {Number} limit 一页多少条
 * @param {Number} page 第几页
 * @param {Number} hyperlinkTitle 筛选条件 超链接标题
 * @returns
 */
export function getSysHyperlinkContent(postData) {
  return request({
    url: 'sysHyperlinkContent/listByPage',
    method: 'post',
    data: postData
  })
}
/**
 * 添加app超链接管理 超链接标题分页数据
 * @export
 * @param {*} postData
 * @returns
 */
export function addSysHyperlinkContent(postData) {
  return request({
    url: '/sysHyperlinkContent/addOrUpdate',
    method: 'post',
    data: postData
  })
}
/**
 * 删除app超链接管理 超链接标题分页数据
 * @export
 * @param {*} ids
 * @returns
 */
export function deleteSysHyperlinkContent(ids) {
  return request({
    url: '/sysHyperlinkContent/delete',
    method: 'delete',
    data: ids
  })
}
/**
 * 更新用户角色
 * @export
 * @param {*} userId
 * @returns
 */
export function upDateUserRoles(userId, roleIds) {
  return request({
    url: `sys/user/roles/${userId}`,
    method: 'put',
    data: roleIds
  })
}
/**
 * 获取用户角色
 * @export
 * @param {*} userId
 * @returns
 */
export function getUserRole(userId) {
  return request({
    url: `sys/rolesByUserId`,
    method: 'get',
    params: { userId }
  })
}
