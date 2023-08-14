import request from '@/utils/request'

export function fetchOfferList() {
  return request({
    url: '/offer/metro/selectAll',
    method: 'get'
  })
}

export function fetchProductPage(id) {
  return request({
    url: '/offer/metro/productpage',
    method: 'get',
    params: { productId: id }
  })
}

export function fetchProductPageList(productIdList) {
  return request({
    url: '/offer/metro/productpageList',
    method: 'post',
    data: productIdList
  })
}
