import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import menu from './modules/menu'
import createPersistedState from 'vuex-persistedstate'


Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  modules: {
    user,
    menu
  },
  plugins: [
    createPersistedState({
      paths: [
        'user'
      ]
    })
  ]
})
