import { getToken } from '@/utils/auth'
import { fetchOfferList, fetchProductPage, fetchAllConfiguration } from '@/api/price-update'

const state = {
  token: getToken(),
  list: null,
  conf: null,
  selfName: ''
}

const mutations = {
  'SET_ROW_TO_LIST': (state, data) => {
    state.list.push(data)
  },
  'SET_TABLE': (state, data) => {
    state.list = data
  },
  'SET_CONFIGURE': (state, data) => {
    state.conf = data
    state.list.forEach(el => {
      var res = data.filter(c => c.productId === el.id)
      el.conf = res[0]
    })
  },
  'UPDATE_SELLERS_OF_ROW': (state, data) => {
    var { code, competitors, self } = data
    var row = state.list.filter(a => a.id === code)[0]
    row.sellers = competitors
    state.selfName = self.shopName
  }
}

const actions = {
  generateList({ commit }) {
    return fetchOfferList().then((response) => {
      var list_ = response.data
      list_.forEach(a => {
        a.sellers = []
        a.conf = []
      })
      console.log('aa', list_)
      commit('SET_TABLE', list_)
      return list_
    }).then(resp => {
      return resp
    })
  },
  generateConfigruations({ commit }) {
    return fetchAllConfiguration().then(resp => {
      var conf = resp.data
      console.log('bb', conf)
      commit('SET_CONFIGURE', conf)
      return conf
    })
  },
  updateSellersById({ commit, state }, id) {
    var p = null
    // var conf = state.list.filter(a => a.productKey === id)[0].conf
    // if (conf.enabled) {
    p = fetchProductPage(id).then(resp => {
      var page = resp.data
      console.log('cc', page)
      commit('UPDATE_SELLERS_OF_ROW', page)
      return page
    })
    return p
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
