
import { createStore } from 'vuex'

const store = createStore({
  state: {
    isAuthenticated: false,
    user: null,
    token: null
  },
  mutations: {
    setAuth(state, { user, token }) {
      state.isAuthenticated = true
      state.user = user
      state.token = token
    },
    clearAuth(state) {
      state.isAuthenticated = false
      state.user = null
      state.token = null
    }
  },
  actions: {
    login({ commit }, { user, token }) {
      // localStorage
      const expireAt = Date.now() + 24 * 60 * 60 * 1000
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify(user))
      localStorage.setItem('expireAt', expireAt)
      commit('setAuth', { user, token })
    },
    logout({ commit }) {
      // remove localStorage
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      localStorage.removeItem('expireAt')
      commit('clearAuth')
    },
    
    initAuth({ commit }) {
      const token = localStorage.getItem('token')
      const user = localStorage.getItem('user')
      const expireAt = localStorage.getItem('expireAt')

      if (!token || !user || !expireAt) return

      // check if outdate
      if (Date.now() > parseInt(expireAt)) {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        localStorage.removeItem('expireAt')
        return
      }

      try {
        commit('setAuth', {
          token,
          user: JSON.parse(user)
        })
      } catch (error) {
        console.error('Failed to restore login session：', error)
        localStorage.clear()
      }
    }
  },
  getters: {
    isAuthenticated: state => state.isAuthenticated,
    currentUser: state => state.user,
    userId: state => state.user ? state.user.id : null
  }
})

export default store