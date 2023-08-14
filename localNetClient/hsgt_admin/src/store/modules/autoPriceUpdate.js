import { getToken } from '@/utils/auth'
import { fetchOfferList, fetchProductPageList } from '@/api/price-update'

const state = {
  token: getToken(),
  list: null
}

const mutations = {
  'SET_ROW_TO_LIST': (state, data) => {
    state.list.push(data)
  },
  'SET_TABLE': (state, date) => {
    state.list = date
  },
  'UPDATE_TABLEROW': (state, data) => {
    data.forEach(page => {
      var { code, competitors } = page
      var row = state.list.filter(a => a.id === code)[0]
      row.sellers = competitors
    })
  }
}

const actions = {
  generateList({ commit }) {
    return fetchOfferList().then((response) => {
      var list_ = response.data
      list_.forEach(a => { a.sellers = [] })
      console.log('aa', list_)
      commit('SET_TABLE', list_)
      return list_
    }).then(resp => {
      var productIds = resp.map(a => a.id)
      fetchProductPageList(productIds).then(r => {
        var productPages = r.data
        commit('UPDATE_TABLEROW', productPages)
      })
      return resp
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
