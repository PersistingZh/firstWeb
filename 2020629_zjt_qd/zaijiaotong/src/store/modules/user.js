import { login, login2, logout, getInfo, updatePassword, upDateToken } from '@/api/user'
import {
  getToken,
  setToken,
  removeToken,
  setRefToken,
  getRefToken,
  removeRefToken
} from '@/utils/auth'
import { resetRouter } from '@/router'

const state = {
  token: getToken(),
  refToken: getRefToken(),
  userPath: '',
  userPathName: '',
  userPathList: [],
  name: '',
  avatar: '',
  role: '',
  userInfo: {},
  menus: [],
  permissions: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_REFTOKEN: (state, token) => {
    state.refToken = token
  },
  SET_USER: (state, userData) => {
    state.userPathName = userData.userPathName
    state.userPath = userData.userPath
    state.name = userData.userInfo.username
    state.userInfo = userData.userInfo
    state.menus = userData.menus
    state.permissions = userData.sysPermissions
    state.userPathList = userData.pathList
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_USERPATH: (state, userPath) => {
    state.userPath = userPath
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login(username, password)
        .then(response => {
          console.log(response)
          const { data } = response
          commit('SET_TOKEN', data.accessToken)
          commit('SET_REFTOKEN', data.refreshToken)
          setToken(data.accessToken)
          setRefToken(data.refreshToken)
          resolve()
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // 手机验证码
  login2({ commit }, userInfo) {
    const { iphone, verificationCode } = userInfo
    return new Promise((resolve, reject) => {
      login2(iphone, verificationCode)
        .then(response => {
          console.log(response)
          const { data } = response
          commit('SET_TOKEN', data.accessToken)
          commit('SET_REFTOKEN', data.refreshToken)
          setToken(data.accessToken)
          setRefToken(data.refreshToken)
          resolve()
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  upDateToken({ commit }) {
    return new Promise((resolve, reject) => {
      upDateToken()
        .then(response => {
          const { data } = response
          commit('SET_TOKEN', data)
          // commit('SET_REFTOKEN', data.refreshToken)
          setToken(data)
          // setRefToken(data.refreshToken)
          resolve()
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token)
        .then(response => {
          const {
            userInfo,
            menus,
            sysPermissions,
            path,
            pathName,
            mySelfPath,
            userPathList
          } = response.data
          // 生成多权限列表
          const pathList = []
          // // 父节点
          // const paterNode = {
          //   label: mySelfPath.sdName || '',
          //   value: mySelfPath.sdCode || '',
          //   children: []
          // }
          // userPathList.map(item => {
          //   const { pathName, code } = item
          //   const pathNameArr = pathName.slice(1, -1).split('#')
          //   const childrenNode = {
          //     label: pathNameArr[pathNameArr.length - 1],
          //     value: code
          //   }
          //   // 子节点
          //   paterNode.children.push(childrenNode)
          // })
          // pathList.push(paterNode)
          // 存储用户信息
          commit('SET_USER', {
            userInfo,
            menus,
            sysPermissions,
            userPath: path,
            userPathName: pathName,
            pathList
          })
          commit(
            'SET_AVATAR',
            'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'
          )

          resolve(menus)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token)
        .then(() => {
          removeRefToken() // must remove  token  first
          removeToken() // must remove  token  first
          resetRouter()
          commit('RESET_STATE')
          resolve()
        })
        .catch(error => {
          reject(error)
        })
    })
  },
  updatePassword({ commit }, userPassword) {
    console.log(userPassword)
    return new Promise((resolve, reject) => {
      updatePassword(userPassword)
        .then(result => {
          console.log(result)
          removeRefToken() // must remove  token  first
          removeToken() // must remove  token  first
          resetRouter()
          commit('RESET_STATE')
          resolve(result)
        })
        .catch(err => {
          console.error()
          reject(err)
        })
    })
  },
  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeRefToken() // must remove  token  first
      removeToken() // must remove  token  first
      // commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
