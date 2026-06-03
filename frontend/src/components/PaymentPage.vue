<template>
  <div class="payment-container">
    <!-- order information -->
    <section class="payment-card order-info">
      <h2>Order Details</h2>
      <div class="order-details">
        <div class="order-item">
          <span>ID：</span>
          <span>{{ orderId }}</span>
        </div>
        <div class="order-item">
          <span>OrderAmount：</span>
          <span class="amount">¥{{ orderAmount }}</span>
        </div>
      </div>
    </section>

    <!-- Payment Method -->
    <section class="payment-card payment-methods">
      <h2>Choose Payment Method</h2>
      <div class="methods-list">
        <div
          class="method-item"
          :class="{ active: selectedMethod === 'alipay' }"
          @click="selectPaymentMethod('alipay')"
        >
          <img src="E:\shopping_master\frontend\public\pictures\alipay.png" alt="支付宝" />
          <span>Alipay</span>
        </div>
        <div
          class="method-item"
          :class="{ active: selectedMethod === 'wechat' }"
          @click="selectPaymentMethod('wechat')"
        >
          <img src="E:\shopping_master\frontend\public/pictures/wechat.png" alt="微信支付" />
          <span>Wechat</span>
        </div>
      </div>
    </section>

    <!-- Pay -->
    <div class="payment-action">
      <button
        class="pay-button"
        :disabled="!selectedMethod"
        @click="handlePayment"
      >
        PayNow
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {cart} from '../api/cart.js'

const route = useRoute()
const router = useRouter()

const orderId = ref('')
const orderAmount = ref(0)

const selectedMethod = ref('')
const showQRCode = ref(false)
const qrCodeUrl = ref('')

// Select Payment Method
const selectPaymentMethod = (method) => {
  selectedMethod.value = method
}

// Alipay
const handlePayment = async () => {
  if (!orderId.value) {
    ElMessage.error('Missing order information');
    return;
  }

  try {
    // Construct the Alipay payment request URL
    const paymentUrl = `http://localhost:8087/api/alipay/pay?orderId=${orderId.value}`;

    // Redirect directly to the backend payment API
    window.location.href = paymentUrl;

    // Clear cart items
    cart.clearCart()

  } catch (error) {
    ElMessage.error('Payment request failed. Please try again later');
    console.error('Payment request failed:', error);
  }
};


// Initialise Data
onMounted(() => {
  const { id, amount } = route.query
  if (!id || !amount) {
    ElMessage.error('Incomplete order information')
    router.push('/orders')
    return
  }

  orderId.value = id
  orderAmount.value = parseFloat(amount)

})
</script>

<style scoped>
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  background-color: #f5f5f5;
  font-family: Arial, sans-serif;
}
.payment-container {
  min-width: 100vw;
  min-height: 100vh;
  padding: 40px 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #f5f5f5;
  box-sizing: border-box;
}

.payment-card {
  min-width: 1200px;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.payment-card h2 {
  margin: 0 0 20px;
  font-size: 20px;
  color: #333;
  font-weight: 500;
}

.order-details {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  color: black;
}

.order-item:last-child {
  border-bottom: none;
}

.amount {
  color: #ff4d4f;
  font-size: 24px;
  font-weight: bold;
}

.methods-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-top: 16px;
}

.method-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  border: 2px solid #e8e8e8;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.method-item:hover {
  border-color: #1890ff;
  background: #f0f7ff;
}

.method-item.active {
  border-color: #1890ff;
  background: #f0f7ff;
}

.method-item img {
  width: 32px;
  height: 32px;
}

.payment-action {
  text-align: center;
  margin-top: 32px;
}

.pay-button {
  padding: 12px 48px;
  font-size: 18px;
  color: white;
  background: #1890ff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.pay-button:hover {
  background: #40a9ff;
}

.pay-button:disabled {
  background: #d9d9d9;
  cursor: not-allowed;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  padding: 24px;
  width: 90%;
  max-width: 400px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.close-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
}

.qrcode-container {
  text-align: center;
}

.qrcode {
  width: 200px;
  height: 200px;
  margin: 0 auto 16px;
  padding: 8px;
  border: 1px solid #e8e8e8;
}

.qrcode img {
  width: 100%;
  height: 100%;
}

.hint {
  color: #666;
  margin-top: 12px;
}
</style>