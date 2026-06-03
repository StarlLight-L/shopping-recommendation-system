<template>
  <div class="cart-container">
    <!-- Navigation Bar -->
    <nav class="nav-bar">
      <div class="nav-content">
        <div class="logo" @click="router.push('/')">iMall</div>
        <div class="nav-links">
          <a href="#" class="nav-link" @click.prevent="router.push('/shopping')">Home</a>
          <a href="#" class="nav-link" @click.prevent="router.push('/laptop')">Laptop</a>
          <a href="#" class="nav-link" @click.prevent="router.push('/phone')">Phone</a>
          <a href="#" class="nav-link" @click.prevent="router.push('/tablet')">Tablet</a>
          <a href="#" class="nav-link" @click.prevent="router.push('/watch')">SmartWatch</a>
          <a href="#" class="nav-link" @click.prevent="router.push('/headphone')">Headphone</a>
        </div>
        <div class="nav-actions">
          <div class="search-box">
            <input type="text" placeholder="Search..." class="search-input">
            <span class="material-icons search-icon">search</span>
          </div>
          <a href="#"><span class="material-icons">favorite</span></a>
          <a href="#" @click.prevent="router.push('/cart')"><span class="material-icons">shopping_cart</span></a>
          <a href="#" @click.prevent="handleUserClick"><span class="material-icons">person</span></a>
        </div>
      </div>
    </nav>

    <div class="main-content" v-if="isLoggedIn">
      <!-- Sidebar Navigation Menu -->
      <div class="side-menu">
        <div class="menu-item active">
          <span class="material-icons">shopping_cart</span>
          <span>Cart</span>
        </div>
        <div class="menu-item" @click="router.push('/favorite')">
          <span class="material-icons">favorite</span>
          <span>Favorites</span>
        </div>
        <div class="menu-item" @click="router.push('/orders')">
          <span class="material-icons">receipt_long</span>
          <span>My Order</span>
        </div>
      </div>

      <!-- Cart Content Section -->
      <div class="cart-content">
        <h2>My Cart</h2>
        <div class="cart-items" v-if="cartItems.length > 0">
          <table class="cart-table">
            <thead>
              <tr>
                <th class="product-info">Product</th>
                <th class="product-price">Price</th>
                <th class="product-quantity">Quantity</th>
                <th class="product-total">Subtotal</th>
                <th class="product-action">Action</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in cartItems" :key="item.id" class="cart-item">
                <td class="product-info">
                  <div class="product-image">
                    <img :src="item.image" :alt="item.name">
                  </div>
                  <div class="product-details">
                    <h3>{{ item.name }}</h3>
                    <p>{{ item.description }}</p>
                  </div>
                </td>
                <td class="product-price">¥{{ item.price }}</td>
                <td class="product-quantity">
                  <div class="quantity-controls">
                    <button @click="decreaseQuantity(item)" :disabled="item.quantity <= 1">
                      <span class="material-icons">remove</span>
                    </button>
                    <span class="quantity-value">{{ item.quantity }}</span>
                    <button @click="increaseQuantity(item)">
                      <span class="material-icons">add</span>
                    </button>
                  </div>
                </td>
                <td class="product-total">¥{{ item.price * item.quantity }}</td>
                <td class="product-action">
                  <button class="remove-btn" @click="removeItem(item)">
                    <span class="material-icons">delete</span>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
          <div class="cart-summary">
            <div class="summary-info">
              <div class="summary-row">
                <span>Total Items:</span>
                <span>{{ totalItems }}件</span>
              </div>
              <div class="summary-row total">
                <span>Total Price:</span>
                <span class="total-price">¥{{ totalPrice }}</span>
              </div>
            </div>
            <button class="checkout-btn" @click="checkout">Checkout</button>
          </div>
        </div>
        <div class="empty-cart" v-else>
          <span class="material-icons">shopping_cart</span>
          <p>Your cart is empty</p>
          <button class="continue-shopping" @click="router.push('/')">Continue Shopping</button>
        </div>
      </div>
    </div>
    <div class="login-prompt" v-else>
      <p>Please log in first</p>
      <button @click="handleUserClick">Go to Login</button>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import axios from 'axios'
import {cart} from '../api/cart.js'

export default {
  name: 'CartPage',
  setup() {
    const router = useRouter()
    const store = useStore()

    const isLoggedIn = computed(() => store.state.isAuthenticated)
    const cartItems = ref([])

    const totalItems = computed(() => {
      return cartItems.value.reduce((total, item) => total + item.quantity, 0)
    })

    const totalPrice = computed(() => {
      return cartItems.value.reduce((total, item) => total + item.price * item.quantity, 0)
    })

    const loadCartItems = async () => {
      try {
        if (!store.state.user || !store.state.user.id) {
          console.error('User is not logged in or user ID does not exist')
          return
        }
        const response = await cart.getCartItems(store.getters.userId)
        if (!response) {
          console.error('Failed to retrieve cart data: Server returned empty data')
          return
        }
        console.log(response)

        cartItems.value = response.map(item => ({
          id: item.id,
          name: item.product.name,
          description: item.product.description,
          price: item.product.price,
          quantity: item.quantity,
          image: item.product.image_url
        }))
      } catch (error) {
        console.error('Failed to load cart:', error)
        if (error.response && error.response.status === 401) {
          store.dispatch('logout')
          router.push('/login')
        }
      }
    }

    // Increase Product Quantity
    const increaseQuantity = async (item) => {
      try {
        await cart.updateCartItem(
            store.getters.userId,
            item.id,
            item.quantity + 1
        )
        item.quantity++
      } catch (error) {
        console.error('Failed to update quantity:', error)
      }
    }
    // Decrease Product Quantity
    const decreaseQuantity = async (item) => {
      if (item.quantity > 1) {
        try {
          await cart.updateCartItem(
              store.getters.userId,
              item.id,
              item.quantity - 1
          )
          item.quantity--
        } catch (error) {
          console.error('Failed to remove item:', error)
        }
      }
    }

    const removeItem = async (item) => {
      try {
        await cart.removeFromCart(
            store.getters.userId,
            item.id
        )
        const index = cartItems.value.indexOf(item)
        if (index > -1) {
          cartItems.value.splice(index, 1)
        }
      } catch (error) {
        console.error('Failed to remove item:', error)
      }
    }
    // Checkout Process
    const checkout = async () => {
      try {
        if (cartItems.value.length === 0) {
          alert('Your cart is empty. Unable to proceed to checkout')
          return
        }

        const orderData = {
          items: cartItems.value.map(item => ({
            productId: item.id,
            productName: item.name,
            productImage: item.image,
            productPrice: item.price,
            quantity: item.quantity,
            totalPrice: item.price * item.quantity
          }))
        }
        //items=[{"id":5,"name":"联想小新Pro16","description":"AMD+R7处理器，
        // 16寸2.5K屏，高性能独显","price":6499,"quantity":1,"image":"/public/pictures/xiaoxinpro.jpg"}]

        router.push({
          path: '/ordercheckout',
          query: {
            source: 'cart',
            items: JSON.stringify(orderData.items)
          }
        })

      } catch (error) {
        console.error('Checkout failed:', error)
        alert('Checkout failed. Please try again later')
      }
    }

    const handleUserClick = () => {
      router.push('/profile')
    }

    onMounted(() => {
      if (isLoggedIn.value) {
        loadCartItems()
      }
    })

    return {
      router,
      isLoggedIn,
      cartItems,
      totalItems,
      totalPrice,
      increaseQuantity,
      decreaseQuantity,
      removeItem,
      checkout,
      handleUserClick
    }
  }
}
</script>

<style scoped>
.nav-bar {
  background-color: rgba(255, 255, 255, 0.98);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  height: 80px;
}

.nav-content {
  max-width: 1920px;
  width: 90%;
  height: 100%;
  margin: 0 auto;
  padding: 0 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 2rem;
  font-weight: bold;
  color: #FF416C;
  cursor: pointer;
  text-decoration: none;
  transition: color 0.3s ease;
}

.logo:hover {
  color: #ff1f4b;
}

.nav-links {
  display: flex;
  gap: 30px;
  align-items: center;
}

.nav-link {
  color: #333;
  text-decoration: none;
  font-size: 1rem;
  transition: color 0.3s ease;
}

.nav-link:hover,
.nav-link.active {
  color: #FF416C;
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.search-box {
  position: relative;
  display: flex;
  align-items: center;
}

.search-input {
  padding: 8px 35px 8px 15px;
  border: 1px solid #ddd;
  border-radius: 20px;
  font-size: 0.9rem;
  width: 200px;
  transition: all 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: #FF416C;
  box-shadow: 0 0 5px rgba(255, 65, 108, 0.3);
}

.search-icon {
  position: absolute;
  right: 12px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
}

.search-icon:hover {
  color: #FF416C;
  transform: scale(1.2) rotate(-10deg);
}

.nav-actions a {
  color: #333;
  font-size: 1.4rem;
  transition: color 0.3s ease;
  text-decoration: none;
  display: flex;
  align-items: center;
}

.nav-actions a:hover {
  color: #FF416C;
}

.nav-actions a span.material-icons {
  color: #333;
  transition: all 0.3s ease;
  display: inline-block;
}

.nav-actions a:hover span.material-icons {
  color: #FF416C;
  transform: scale(1.2) rotate(10deg);
}

.nav-actions .material-icons {
  font-size: 1.6rem;
}

.cart-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.cart-content {
  background: white;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.cart-content h2 {
  font-size: 1.8rem;
  color: #333;
  margin-bottom: 30px;
}

.cart-table {
  width: 100%;
  border-collapse: collapse;
}

thead th {
  padding: 15px;
  text-align: left;
  border-bottom: 2px solid #eee;
  color: #666;
}

.cart-item td {
  padding: 20px 15px;
  border-bottom: 1px solid #eee;
}

.product-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.product-image img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 8px;
}

.product-details h3 {
  font-size: 1.1rem;
  margin-bottom: 5px;
  color: #333;
}

.product-details p {
  color: #666;
  font-size: 0.9rem;
}

.quantity-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.quantity-controls button {
  background: #f5f5f5;
  border: none;
  border-radius: 4px;
  padding: 5px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.quantity-controls button:hover {
  background: #e0e0e0;
}

.quantity-controls button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.quantity-value {
  min-width: 30px;
  text-align: center;
  color: #020202;
}

.product-price {
  min-width: 30px;
  text-align: center;
  color: #020202;
}

.product-total {
  min-width: 30px;
  text-align: center;
  color: #020202;
}

.remove-btn {
  background: none;
  border: none;
  color: #FF416C;
  cursor: pointer;
  padding: 5px;
  transition: color 0.3s ease;
}

.remove-btn:hover {
  color: #ff1f4b;
}

.cart-summary {
  margin-top: 30px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.summary-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.summary-row {
  display: flex;
  gap: 10px;
  align-items: center;
  color: #666;
}

.summary-row.total {
  font-size: 1.2rem;
  color: #333;
}

.checkout-btn {
  background: linear-gradient(135deg, #FF4B2B, #FF416C);
  color: white;
  border: none;
  padding: 12px 24px;
  font-size: 18px;
  border-radius: 8px;
  cursor: pointer;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}
</style>