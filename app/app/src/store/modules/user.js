import { login } from "@/api/user"
import { getToken, setToken, removeToken } from "@/utils/auth"

const state = {
  userInfo: {},
  token: getToken(),
}

const mutations = {
  setToken(state, token) {
    state.token = token
    setToken(token)
  },
  removeToken(state) {
    state.userInfo = {}
    state.token = ''
    removeToken()
  },
  setUserInfo(state, userInfo) {
    state.userInfo = userInfo
  },
}

const actions = {
  async login(context, data) {
    return new Promise((resolve, reject) => {
      login(data)
        .then(res => {
          context.commit('setUserInfo', res.data)
          context.commit('setToken', res.data.token)
          resolve(res.data)
        })
        .catch(error => {
          reject(error)
        });
    });
  },
  async logout(context) {
    context.commit('removeToken')
    context.commit('setUserInfo', {})
    context.commit('setToken', '')
  },
}

const getters = {
  getUser(state){
    return state.userInfo
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
}