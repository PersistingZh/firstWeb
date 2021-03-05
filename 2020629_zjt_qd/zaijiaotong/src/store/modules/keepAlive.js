const state = {
  keepAliveComponents: [] // 缓存数组
}

const mutations = {
  ADD_KEEP_ALIVE(state, component) {
    // 注：防止重复添加（当然也可以使用Set）
    !state.keepAliveComponents.includes(component) &&
      state.keepAliveComponents.push(component)
  },
  DEL_KEEP_ALIVE(state, component) {
    const index = state.keepAliveComponents.indexOf(component)
    index !== -1 && state.keepAliveComponents.splice(index, 1)
  }
}

const actions = {
  addKeepAlive({ commit }, data) {
    commit('ADD_KEEP_ALIVE', data)
  },
  delKeepAlive({ commit }, data) {
    commit('DEL_KEEP_ALIVE', data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
