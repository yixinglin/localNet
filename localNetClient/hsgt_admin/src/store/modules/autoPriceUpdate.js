import { getToken } from '@/utils/auth'
import { pricing, fetchOfferList, fetchShippingGroups, fetchProductPage, fetchAllConfiguration, fetchSuggestion, updateConfiguration } from '@/api/price-update'

const state = {
  token: getToken(),
  list: null,
  conf: null,
  suggest: null,
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
  'UPDATE_SUGGEST_OF_ROW': (state, data) => {
    var { productId } = data
    var row = state.list.filter(a => a.id === productId)[0]
    row.suggest = data
  },
  'UPDATE_SELLERS_OF_ROW': (state, data) => {
    var { code, competitors, self } = data
    var row = state.list.filter(a => a.id === code)[0]
    row.sellers = competitors
    state.selfName = self.shopName
  },
  'UPDATE_CONF_OF_ROW': (state, data) => {
    var { productId } = data
    var row = state.list.filter(a => a.id === productId)[0]
    row.conf = data
  }
}

const actions = {
  // Fetch offer data
  generateList({ commit }) {
    return fetchOfferList().then((response) => {
      var list_ = response.data
      list_.forEach(a => {
        a.sellers = []
        a.conf = []
        a.suggest = {}
      })
      return list_
    }).then(resp => {
      commit('SET_TABLE', resp)
      return resp
    })
  },
  // Fetch configuration for dynamic pricing
  generateConfigurations({ commit }) {
    return fetchAllConfiguration().then(resp => {
      var conf = resp.data
      commit('SET_CONFIGURE', conf)
      return conf
    })
  },
  // Fetch list of shipping groups
  generateShipmentList({ commit }) {
    return fetchShippingGroups().then(resp => {
      var ship = resp.data
      return ship
    })
  },
  // Update configuration for dynamic pricing
  uploadConfiguration({ commit }, conf) {
    return updateConfiguration(conf).then(resp => {
      commit('UPDATE_CONF_OF_ROW', conf)
      return resp.data
    })
  },
  // Update information of other sellers
  updateSellersById({ commit }, id) {
    var p = null
    p = fetchProductPage(id).then(resp => {
      var page = resp.data
      commit('UPDATE_SELLERS_OF_ROW', page)
      return page
    })
    return p
  },
  // Update suggested price for competition.
  updateSuggestById({ commit }, id) {
    var p = null
    p = fetchSuggestion(id).then(resp => {
      var suggest = resp.data
      commit('UPDATE_SUGGEST_OF_ROW', suggest)
      return suggest
    })
    return p
  },
  // Request for Pricing
  requestPricing({ commit }, offer) {
    return pricing(offer).then(resp => {
      var newOffer = resp.data
      return newOffer
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
