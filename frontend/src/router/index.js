import { createRouter, createWebHistory } from 'vue-router'
import LoginRegister from '../components/LoginRegister.vue'
import ElectronicMall from '../components/ElectronicMall.vue'
import UserProfile from '../components/UserProfile.vue'
import ShoppingPage from '../components/ShoppingPage.vue'
import LaptopPage from '../components/LaptopPage.vue'
import PhonePage from '../components/PhonePage.vue'
import TabletPage from '../components/TabletPage.vue'
import WatchPage from '../components/WatchPage.vue'
import HeadphonePage from '../components/HeadphonePage.vue'
import CartPage from '../components/CartPage.vue'
import OrderPage from '../components/OrderManagement.vue'
import FavoritePage from '../components/FavoritePage.vue'
import ProductDetail from '../components/ProductDetail.vue'
import OrderCheckout from '../components/OrderCheckout.vue'
import SearchPage from '../components/SearchPage.vue'
import PaymentPage from '../components/PaymentPage.vue'
import PaymentResult from '../components/PaymentResult.vue'
import store from '../store'

const routes = [
  {
    path: '/payment',
    name: 'Payment',
    component: PaymentPage
  },
  {
    path: '/',
    name: 'Home',
    component: ElectronicMall
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('../components/SearchPage.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginRegister
  },
  {
    path: '/profile',
    name: 'Profile',
    component: UserProfile,
    meta: { requiresAuth: true }
  },
  {
    path: '/shopping',
    name: 'Shopping',
    component: ShoppingPage
  },
  {
    path: '/laptop',
    name: 'Laptop',
    component: LaptopPage
  },
  {
    path: '/phone',
    name: 'Phone',
    component: PhonePage
  },
  {
    path: '/tablet',
    name: 'Tablet',
    component: TabletPage
  },
  {
    path: '/watch',
    name: 'Watch',
    component: WatchPage
  },
  {
    path: '/headphone',
    name: 'Headphone',
    component: HeadphonePage
  },
  {
    path: '/cart',
    name: 'Cart',
    component: CartPage,
    meta: { requiresAuth: true }
  },
  {
    path: '/order',
    name: 'Order',
    component: OrderPage,
    meta: { requiresAuth: true }
  },
  {
    path: '/favorite',
    name: 'Favorite',
    component: FavoritePage,
    meta: { requiresAuth: true }
  },
  {
    path: '/product/:id',
    name: 'ProductDetail',
    component: ProductDetail
  },
  {
    path:'/ordercheckout',
    name: OrderCheckout,
    component: OrderCheckout
  },
  {
    path:'/paymentresult',
    name: PaymentResult,
    component: PaymentResult
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})


router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // Check if the user is logged in
    if (!store.getters.isAuthenticated) {
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router