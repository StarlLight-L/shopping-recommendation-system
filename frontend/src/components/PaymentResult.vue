<template>
  <div class="flex flex-col items-center justify-center h-screen space-y-4">
    <div class="text-2xl font-bold">
      {{ message }}
    </div>
    <div v-if="status === 'success'" class="text-green-500">Payment successful</div>
    <div v-else-if="status === 'fail'" class="text-red-500">Payment failed or invalid order</div>
    <div v-else class="text-gray-500">Confirming order status...</div>
    <!-- Back to Home Button -->
    <button @click="goHome" class="home-button">
      Back to Home
    </button>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import {order} from '../api/order.js'

const status = ref('pending') // success | fail | pending
const message = ref('Payment result is being confirmed....')
const router = useRouter()

const goToOrders = () => {
  router.push('/profile/orders')
}
const goHome = () => {
  router.push('/')
}

onMounted(async () => {
  const params = new URLSearchParams(window.location.search)
  const orderId = params.get('orderId')
  console.log(orderId)

  if (!orderId) {
    status.value = 'fail'
    message.value = 'Invalid order number'
    return
  }

  try {
    const res = await order.updateOrder(orderId)// Update order payment status
                                                // Note: Do not delete the order here.
                                                // The order should only be removed after delivery is completed
                                                // and the user confirms receipt.

    if (res) {
      status.value = 'success'
      message.value = 'Payment successful. Thank you for your purchase!'
    } else {
      status.value = 'fail'
      message.value = 'The order has not been paid yet. Please check again later or contact the administrator.'
    }
  } catch (err) {
    status.value = 'fail'
    message.value = 'Request failed. Unable to confirm payment status.'
  }
})
</script>
<style scoped>
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  background-color: #ffffff;
}

.flex {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh; /* 全屏高度 */
  background-color: #ffffff;
}

.text-2xl {
  font-size: 2rem;
  font-weight: bold;
  color: #333333;
}

.text-green-500 {
  color: #22c55e;
  font-size: 1.5rem;
}

.text-red-500 {
  color: #ef4444;
  font-size: 1.5rem;
}

.text-gray-500 {
  color: #6b7280;
  font-size: 1.5rem;
}

.space-y-4 > * + * {
  margin-top: 1rem;
}

.home-button {
  margin-top: 1rem;
  padding: 0.75rem 2rem;
  background-color: #FF416C;
  color: #fff;
  font-size: 1rem;
  border: none;
  border-radius: 9999px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.home-button:hover {
  background-color: #e03e5d;
}
</style>
