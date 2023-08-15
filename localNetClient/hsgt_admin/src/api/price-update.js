import request from '@/utils/request'

export function fetchOfferList() {
  return request({
    url: '/offer/metro/selectAll',
    method: 'get'
  })
}

export function fetchAllConfiguration() {
  return request({
    url: '/pricing/metro/conf',
    method: 'get'
  })
}

export function fetchProductPage(productKey) {
  return request({
    url: '/offer/metro/productpage',
    method: 'get',
    params: { productKey: productKey }
  })
}

export function fetchProductPageList(productIdList) {
  return request({
    url: '/offer/metro/productpageList',
    method: 'post',
    data: productIdList
  })
}

