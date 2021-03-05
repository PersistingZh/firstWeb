import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 * url                            在最下以一层必须设置，不设置就会出现，你没这权限也会出现在菜单中
 * hidden: true                   当设置 true 的时候该路由不会在侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect           当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    roles: ['admin','editor']    设置该路由进入的权限，支持多个权限叠加
    title: 'title'               设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             设置该路由的图标
    breadcrumb: false            如果设置为false，则该项将隐藏在breadcrumb中（默认为true）
    activeMenu: '/example/list'  如果设置路径，sidebar将突出显示您设置的路径
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    hidden: true,
    redirect: '/housList',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index'),
        // component: () => import('@/views/housManage/housList/index'),
        meta: { title: '首页', icon: 'dashboard' }
      }
    ]
  }
]

const createRouter = () =>
  new Router({
    // mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
  })

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router

export const asyncRouter = [
  {
    path: '/housInfo',
    redirect: '/housInfo/housList',
    component: Layout,
    name: 'HousInfo',
    meta: { title: '在矫通管理', icon: 'gzrygl' },
    children: [
      {
        name: 'HousManageIndex',
        path: '/housList',
        component: () => import('@/views/housManage/housList/index'),
        meta: { title: '矫正人员管理', keepAlive: true, icon: 'jzrygl' },
        menu: 'HousInfo',
        url: 'housInfo/housList'
      },
      {
        path: '/addjzryxx/:id',
        hidden: true,
        name: 'addJzPerson',
        component: () => import('@/views/housManage/housList/addJzPerson'),
        meta: { title: '添加矫正人员信息', icon: '' },
        menu: 'addJzPerson'
      },
      {
        path: '/tenantManage',
        name: 'TenantManage',
        component: () => import('@/views/housManage/tenantManage/index'),
        meta: { title: '工作人员管理', icon: 'gzrygl', keepAlive: true },
        menu: 'HousInfo',
        url: 'housInfo/tenantManage'
      },
      {
        path: '/addPerson/:id/0',
        hidden: true,
        name: 'AddgzPerson',
        component: () => import('@/views/housManage/tenantManage/AddgzPerson'),
        meta: { title: '添加工作人员信息', icon: '' },
        menu: 'addgzPerson'
      },
      {
        name: 'OwnerManage',
        path: '/ownerManage',
        component: () => import('@/views/housManage/ownerManage/index'),
        meta: { title: '任务管理', icon: 'rwgl', keepAlive: true },
        menu: 'HousInfo',
        url: 'housInfo/ownerManage'
      },
      {
        path: '/addTask/:id/0',
        hidden: true,
        name: 'AddTask',
        component: () => import('@/views/housManage/ownerManage/addTask'),
        meta: { title: '添加任务管理', icon: '' },
        menu: 'addTask'
      },
      {
        path: '/housManage',
        name: 'HousManage',
        component: () => import('@/views/housManage/housManage/index'),
        meta: { title: '预警管理', icon: 'yjgl', keepAlive: true },
        menu: 'HousInfo',
        url: 'housInfo/housManage'
      },
      {
        path: '/addWarning/:id/0',
        hidden: true,
        name: 'AddWarning',
        component: () => import('@/views/housManage/housManage/addWarning'),
        meta: { title: '添加预警管理', icon: '' },
        menu: 'addWarning'
      },
      {
        name: 'TenantExamine',
        path: '/tenantExamine',
        component: () => import('@/views/housManage/tenantExamine/index'),
        meta: { title: '定位核验管理', keepAlive: true, icon: 'kqrygl' },
        menu: 'HousInfo',
        url: 'housInfo/tenantExamine'
      },
      {
        path: '/addAttendance/:id/0',
        hidden: true,
        name: 'AddAttendance',
        component: () =>
          import('@/views/housManage/tenantExamine/addAttendance'),
        meta: { title: '添加考勤管理', icon: '' },
        menu: 'addAttendance'
      },
      {
        name: 'Information',
        path: '/information',
        component: () => import('@/views/housManage/information/index'),
        meta: { title: '预警信息', keepAlive: true, icon: 'yjxx' },
        menu: 'HousInfo',
        url: 'HousInfo/information'
      }
    ]
  },
  {
    path: '/SystemManagement',
    redirect: '/SystemManagement/uploadApk',
    component: Layout,
    name: 'HousInfo',
    meta: { title: '系统管理', icon: 'system' },
    children: [
      {
        name: 'UploadApk',
        path: '/uploadApk',
        component: () => import('@/views/SystemManagement/uploadApk/index'),
        meta: { title: 'apk管理', keepAlive: true, icon: 'system' },
        menu: 'HousInfo',
        url: 'HousInfo/information'
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]
