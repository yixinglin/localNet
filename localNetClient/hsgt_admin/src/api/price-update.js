import request from '@/utils/request'

// Fetch offers from API and store it to database
export function fetchOfferList() {
  return request({
    url: '/offer/metro/selectAll',
    method: 'get'
  })
}

// Fetch configurations from databse
export function fetchAllConfiguration() {
  return request({
    url: '/pricing/metro/conf',
    method: 'get'
  })
}

// Fetch a product page from API and store it to database
export function fetchProductPage(productId) {
  return request({
    url: '/offer/metro/productpage',
    method: 'get',
    params: { productId: productId }
  })
}

// Fetch product page from database quickly.
export function fetchProductPageList(productIdList) {
  return request({
    url: '/offer/metro/productpageList',
    method: 'post',
    data: productIdList
  })
}

// Fetch product page from database quickly.
export function fetchSuggestion(productId) {
  return request({
    url: '/pricing/metro/suggest',
    method: 'get',
    params: { productId: productId }
  })
}

// Fetch product page from database quickly.
export function updateConfiguration(conf) {
  return request({
    url: '/pricing/metro/conf',
    method: 'post',
    data: conf
  })
}
