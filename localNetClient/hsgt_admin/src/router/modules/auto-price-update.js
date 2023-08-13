/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/layout'

const autoPriceUpdateRouter = {
  path: '/auto-price-update',
  component: Layout,
  redirect: 'noRedirect',
  name: '自动比价',
  meta: {
    title: '自动比价',
    icon: 'chart', roles: ['admin', 'admin1']
  },
  children: [
    {
      path: 'update',
      component: () => import('@/views/price/auto-price-update-table'),
      name: 'Supervise',
      meta: { title: 'Supervise', noCache: true }
    },
    {
      path: 'settings',
      component: () => import('@/views/price/auto-price-update-table'),
      name: 'Settings',
      meta: { title: 'Settings' }
    }
  ]
}

export default autoPriceUpdateRouter
