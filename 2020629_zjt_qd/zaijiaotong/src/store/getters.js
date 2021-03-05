const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  // user modules
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  menus: state => state.user.menus,
  userPath: state => state.user.userPath,
  userPathName: state => state.user.userPathName,
  userPathList: state => state.user.userPathList,
  // permission modules
  permission_routes: state => state.permission.routers,
  permission_addRoutes: state => state.permission.addRouters,
  // keepAlive views
  keepAliveComponents: state => state.keepAlive.keepAliveComponents
}
export default getters
