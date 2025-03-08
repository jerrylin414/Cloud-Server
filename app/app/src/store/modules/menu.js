
const state = {
  folderStack: [],
  curFolder: '',
}

const mutations = {
  insert(state, data) {
    let stack = state.folderStack
    stack.push(data)
    state.folderStack = stack
  },
  remove(state) {
    let stack = state.folderStack
    stack.pop()
    state.curFolder = stack[stack.length - 1]
    state.folderStack = stack
  },
  setCurId(state, data) {
    state.curFolder = data
  },
  initStack(state) {
    state.folderStack = []
    let stack = state.folderStack
    stack.push(0)
    state.folderStack = stack
    state.curFolder = '0'
  },
  clearStack(state) {
    state.folderStack = []
    state.curFolder = ''
  },
}

const actions = {
  async clearStack(context, obj) {
    context.commit('clearStack')
  }
}

const getters = {
  getLastEl(state) {
    let stack = state.folderStack
    return stack[stack.length - 1]
  },
  getStack(state){
    return state.folderStack
  },
  getCurId(state){
    return state.curFolder
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
}