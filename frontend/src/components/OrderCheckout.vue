<template>
  <div class="order-checkout">
    <!-- Product Information -->
    <section class="checkout-card">
      <div class="card-header">
        <h3>Product Information</h3>
      </div>
      <div class="product-list">
        <div v-for="item in products" :key="item.productId" class="product-item">
          <img :src="item.productImage" :alt="item.productName" />
          <div class="product-info">
            <h4>{{ item.name }}</h4>
            <p class="product-price">¥{{ item.productPrice }}</p>
            <p class="product-quantity">Number: {{ item.quantity }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Shipping Address Section -->
    <section class="checkout-card">
      <div class="card-header">
        <h3>Shipping Address</h3>
        <button @click="showAddressDialog = true" class="link-button">Select Address</button>
      </div>
      <div v-if="selectedAddress" class="address-info">
        <p><strong>{{ selectedAddress?.consignee }}</strong> {{ selectedAddress?.phone }}</p>
        <p>{{ selectedAddress?.province }} {{ selectedAddress?.city }} {{ selectedAddress?.district }} {{ selectedAddress?.detail }}</p>
      </div>
      <div v-else class="no-address">
        <button @click="showAddressDialog = true">Add Shipping Address</button>
      </div>
    </section>

    <!-- 订单总结 -->
    <section class="checkout-card">
      <div class="order-summary">
        <div class="total-price">
          <span>Total Price：</span>
          <span class="price">¥{{ totalPrice }}</span>
        </div>
        <button @click="submitOrder" :disabled="!selectedAddress">Place Order</button>
      </div>
    </section>

    <!-- Address Selection Dialog -->
    <div class="modal" v-if="showAddressDialog">
      <div class="modal-content">
        <h3>Select Shipping Address</h3>
        <div class="address-list">
          <div
              v-for="address in addresses"
              :key="address.id"
              class="address-item"
              :class="{ 'selected': selectedAddress?.id === address.id }"
              @click="selectAddress(address)">
            <p><strong>{{ address.consignee}}</strong> {{ address.phone }}</p>
            <p>{{ address.province }} {{ address.city }} {{ address.district }} {{ address.detail }}</p>
          </div>
        </div>
        <button @click="showAddressDialog = false">Close</button>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useStore } from 'vuex'
import {user} from '../api/user.js'
import {product} from '../api/product.js'
import {order} from '../api/order.js'
const route = useRoute()
const router = useRouter()
const store = useStore()

// Product Data
const products = ref([])

// Address Data
const addresses = ref([])
const selectedAddress = ref(null)
const showAddressDialog = ref(false)

// Calculate Price
const totalPrice = computed(() => {
  return products.value.reduce((total, item) => total + item.productPrice * item.quantity, 0)
})

// Get Addresses
const fetchAddresses = async () => {
  try {
    const response = await user.getUserAddress(store.getters.userId)
    addresses.value = response
  } catch (error) {
    console.error('Failed to fetch address list:', error)
  }
}

// Select Address
const selectAddress = (address) => {
  selectedAddress.value = address
  showAddressDialog.value = false
}

// Submit
const submitOrder = async () => {
  if (!selectedAddress.value) {
    ElMessage.warning('Please select a shipping address')
    return
  }
  try {
    const orderData = {
      user_id: store.getters.userId,
      items: products.value,
      shipping_address: selectedAddress.value.province + selectedAddress.value.city + selectedAddress.value.district + selectedAddress.value.detail,
      receiver_name:selectedAddress.value.consignee,
      receiver_phone:selectedAddress.value.phone,
      total_amount: totalPrice.value
    }

    const data = await order.createOrder(orderData)
    console.log(data)
    if (data) {
      alert('Order created successfully')
      router.push({ path: '/payment', query: { id: data.order_no, amount: data.total_amount } })
    } else {
      throw new Error(data.message)
    }
  } catch (error) {
    ElMessage.error(error.message || 'Failed to create order')
  }
}


// Initialize Data
onMounted(async () => {
  try {
    const { source, items } = route.query

    if (!source || !items) {
      ElMessage.error('Incomplete product data')
      router.push('/')
      return
    }

    try {
      if (source === 'cart') {
        // // Products from the shopping cart (passed as an array)
        const cartItems = JSON.parse(items)
        if (Array.isArray(cartItems) && cartItems.length > 0) {
          products.value = cartItems
          console.log(products.value)
        } else {
          throw new Error('Shopping cart data format is invalid')
        }


      } else if (source === 'product') {
        const productDataList = JSON.parse(items)
        if (!Array.isArray(productDataList) || productDataList.length === 0) {
          throw new Error('Invalid product data format')
        }
        const productData = productDataList[0]

        //Search info

        const productInfo = await product.getProductById(productData.productId)
        console.log(productInfo)
        products.value = [{
          productId: productInfo.id,
          productName: productInfo.name,
          productImage: productInfo.image_url,
          productPrice: productInfo.price,
          quantity: productData.quantity,
          totalPrice: productInfo.price * productData.quantity
        }]
        if (!productInfo) {
          throw new Error('Product not found')
        }

      } else {
        throw new Error('Unknown product source')
      }
    } catch (parseError) {
      console.error('Failed to parse product data', parseError)
      return
    }
    // FetchAddresses
    await fetchAddresses()
  } catch (error) {
    console.error('Failed to initialize data:', error)
    ElMessage.error('Failed to initialize data')
    router.push('/')
  }
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

.order-checkout {
  width: 100vw;
  min-height: 100vh;
  padding: 40px 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #f5f5f5;
  box-sizing: border-box;
}

.checkout-card {
  width: 100%;
  max-width: 1200px;
  background: #ffffff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.checkout-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e0e0e0;
}

.card-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 500;
  color: #333333;
}

.product-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 16px;
  background: #fafafa;
  border-radius: 12px;
}

.product-item {
  display: flex;
  gap: 24px;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eeeeee;
}

.product-item:last-child {
  border-bottom: none;
}

.product-item img {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
}

.product-info {
  flex: 1;
}

.product-info h4 {
  font-size: 16px;
  margin: 0 0 8px;
  color: #333333;
}

.product-price {
  font-size: 18px;
  font-weight: bold;
  color: #000000;
}

.product-quantity {
  color: #666666;
  margin-top: 4px;
}

.address-info {
  padding: 20px 24px;
  color: black;
}

.no-address {
  padding: 32px;
  text-align: center;
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
  width: 90%;
  max-width: 600px;
  max-height: 80vh;
  background: #ffffff;
  border-radius: 8px;
  padding: 24px;
  overflow-y: auto;
}

.modal h3 {
  margin: 0 0 20px;
  font-size: 18px;
  font-weight: 500;
}

.address-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 20px;
  max-height: 400px;
  overflow-y: auto;
  color: black;
}

.address-item {
  padding: 16px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: gray;
}

.address-item:hover,
.address-item.selected {
  border-color: #1890ff;
  background-color: #f0f7ff;
}

.order-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
}

.total-price {
  font-size: 16px;
  color: black;
}

.total-price .price {
  font-size: 24px;
  font-weight: bold;
  color: #ff4d4f;
  margin-left: 8px;
}

button {
  padding: 10px 24px;
  border: none;
  border-radius: 4px;
  background-color: #1890ff;
  color: #ffffff;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

button:hover {
  background-color: #40a9ff;
}

button:disabled {
  background-color: #d9d9d9;
  cursor: not-allowed;
}

.link-button {
  background: none;
  color: #1890ff;
  padding: 4px 8px;
}

.link-button:hover {
  background-color: black;
}
</style>

