import { constantRoutes, asyncRouter } from '@/router'

/**
 * 递归吧所有的路由url调出来
 * @param {*} menus
 * @returns
 */
const menusListUrl = []
function getRouterMenus(menus) {
  menus.map(router => {
    if (router.children && router.children.length) {
      menusListUrl.concat(getRouterMenus(router.children))
    } else {
      menusListUrl.push(router.url)
    }
  })
  return menusListUrl
}

/**
 * 判断用户是否拥有此菜单
 * @param menus
 * @param route
 */
function hasPermission(menus, route) {
  if (route.url) {
    // 如果这个路由有menu属性,就需要判断用户是否拥有此menu权限
    return menus.indexOf(route.url) !== -1
  } else {
    return true
  }
}

/**
 * 递归过滤异步路由表，返回符合用户菜单权限的路由表
 * @param {*} routes
 * @param {*} userMenus
 */
function filterAsyncRoutes(routes, userMenus) {
  const accessedRouters = routes.filter(route => {
    if (hasPermission(userMenus, route)) {
      if (route.children && route.children.length) {
        // 如果这个路由下面还有下一级的话,就递归调用
        route.children = filterAsyncRoutes(route.children, userMenus)
        // 如果过滤一圈后,没有子元素了,这个父级菜单就也不显示了
        return route.children && route.children.length
      }
      return true
    }
    return false
  })
  return accessedRouters
}

const state = {
  routers: constantRoutes, // 本用户所有的路由,包括了固定的路由和下面的addRouters
  addRouters: [] // 本用户的角色赋予的新增的动态路由
}

const mutations = {
  SET_ROUTERS: (state, routers) => {
    state.addRouters = routers
    // 将固定路由和新增路由进行合并, 成为本用户最终的全部路由信息
    state.routers = constantRoutes.concat(routers)
  }
}

const actions = {
  generateRoutes({ commit }, userMenus) {
    return new Promise(resolve => {
      getRouterMenus(userMenus)
      // const accessedRoutes = filterAsyncRoutes(asyncRouter, menusListUrl)
      // 判断是不是管理员是的话返回不用过滤路由
      // if (userPermissions.includes('admin')) {
      const accessedRoutes = asyncRouter || []
      // } else {
      //   accessedRoutes = filterAsyncRoutes(asyncRouter, userMenus)
      // }

      commit('SET_ROUTERS', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
