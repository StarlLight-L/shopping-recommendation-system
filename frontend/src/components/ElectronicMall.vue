<template>
  <div class="mall-container">
    <!-- Navigate Bar -->
    <nav class="nav-bar">
      <div class="nav-content">
        <div class="logo" @click="router.push('/')">iMall</div>
        <div class="nav-links">
          <a href="#" class="active" @click.prevent="router.push('/')">Home</a>
          <a href="#" @click.prevent="router.push('/laptop')">Laptop</a>
          <a href="#" @click.prevent="router.push('/phone')">Phone</a>
          <a href="#" @click.prevent="router.push('/tablet')">Tablet</a>
          <a href="#" @click.prevent="router.push('/watch')">SmartWatch</a>
          <a href="#" @click.prevent="router.push('/headphone')">HeadPhone</a>
        </div>
        <div class="nav-actions">
          <div class="search-box">
            <input type="text" v-model="searchQuery" @keyup.enter="handleSearch" placeholder="Search..." class="search-input">
            <span class="material-icons search-icon" @click="handleSearch">search</span>
          </div>
          <a href="#" @click.prevent="handleFavoriteClick"><span class="material-icons">favorite</span></a>
          <a href="#" @click.prevent="handleCartClick"><span class="material-icons">shopping_cart</span></a>
          <a href="#" @click.prevent="handleUserClick"><span class="material-icons">person</span></a>
        </div>
      </div>
    </nav>

    <!-- Main Content -->
    <main>
      <!-- Hero Content -->
      <section class="hero-section">
        <div class="hero-content">
          <h1>Experience Next-Gen Technology</h1>
          <p>Discover the latest in electronic innovation</p>
          <button class="primary-btn" @click="router.push('/shopping')">Shop Now</button>
        </div>
      </section>

      <!-- Product Classification -->
      <section class="categories">
        <h2>Featured Categories</h2>
        <div class="category-grid">
          <div class="category-card" @click="router.push('/phone')">
            <span class="material-icons">smartphone</span>
            <h3>Smartphones</h3>
          </div>
          <div class="category-card" @click="router.push('/laptop')">
            <span class="material-icons">laptop</span>
            <h3>Laptops</h3>
          </div>
          <div class="category-card" @click="router.push('/headphone')">
            <span class="material-icons">headphones</span>
            <h3>Audio</h3>
          </div>
          <div class="category-card" @click="router.push('/tablet')">
            <span class="material-icons">tablet</span>
            <h3>Tablets</h3>
          </div>
        </div>
      </section>

      <!-- Special Product -->
      <section class="featured-products">
        <h2>NewArrivals</h2>
        <div class="product-grid">
          <div v-for="product in newProducts" :key="product.id" class="product-card" @click="handleProductClick(product)">
            <div class="product-image">
              <img :src="product.image" :alt="product.name">
            </div>
            <div class="product-info">
              <h3>{{ product.name }}</h3>
              <p>{{ product.description }}</p>
              <span class="price">¥{{ product.price }}</span>
              <button class="buy-btn" @click.stop="handleAddToCart(product)">Add to Cart</button>
            </div>
          </div>
        </div>
      </section>
    </main>
    <!-- FootPrint -->
    <footer>
      <div class="footer-content">
        <div class="footer-section">
          <h4>About iMall</h4>
          <p>iMall is your trusted electronics shopping platform, committed to providing you with high-quality products and services.</p>
        </div>
        <div class="footer-section">
          <h4>QuickLink</h4>
          <a href="#">Introduction</a>
          <a href="#">Contact Us</a>
          <a href="#">Delivery Information</a>
          <a href="#">Help</a>
        </div>
        <div class="footer-section">
          <h4>Shopping Guide</h4>
          <a href="#">New Arrivals</a>
          <a href="#">Special Offers</a>
          <a href="#">After-sales Service</a>
          <a href="#">Member Benefits</a>
        </div>
        <div class="footer-section">
          <h4>About Us</h4>
          <p>Follow us on social media to get the latest offers and promotions</p>
          <div class="social-links">
            <a href="#" class="social"><span class="material-icons">facebook</span></a>
            <a href="#" title="微博"><span class="material-icons">public</span></a>
            <a href="#" class="social"><span class="material-icons">link</span></a>
          </div>
        </div>
      </div>
    </footer>
  </div>

</template>

<script>
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ref, onMounted } from 'vue'
import {search} from '../api/search.js'

export default {
  name: 'ElectronicMall',
  setup() {
    const router = useRouter()
    const store = useStore()
    const searchQuery = ref('')

    const handleSearch = () => {
      if (searchQuery.value.trim()) {
        //Add Search Record
        search.getSearchContent(store.getters.userId,searchQuery.value.trim())
        router.push(`/search?q=${encodeURIComponent(searchQuery.value.trim())}`)
      }
    }
    // Example
    const newProducts = ref([
      {
        id: 1,
        name: "iPhone 16 Pro",
        description: "A brand-new smartphone powered by the A18 Pro chip",
        price: 7999,
        image: "/pictures/iphone16pro.jpg"
      },
      {
        id: 2,
        name: "MacBook Pro 14",
        description: "A pro-level laptop powered by the M3 Pro chip",
        price: 14999,
        image: "/pictures/macbook14.png"
      },
      {
        id: 3,
        name: "iPad Pro",
        description: "A tablet with an edge-to-edge display powered by the M2 chip",
        price: 8999,
        image: "/pictures/ipad-pro.jpg"
      },
      {
        id: 4,
        name: "Apple Watch S9",
        description: "A next-generation smartwatch powered by the new S10 chip",
        price: 2999,
        image: "/pictures/apple-watch-S10.png"
      },
    ])
    const cartItems = ref([])

    const handleUserClick = () => {
      if (store.getters.isAuthenticated) {
        router.push('/profile')
      } else {
        router.push('/login')
      }
    }

    const handleFavoriteClick = () => {
      if (store.getters.isAuthenticated) {
        router.push('/favorite')
      } else {
        router.push('/login')
      }
    }

    const handleCartClick = () => {
      if (store.getters.isAuthenticated) {
        router.push('/cart')
      } else {
        router.push('/login')
      }
    }

    const handleProductClick = (product) => {
      if (store.getters.isAuthenticated) {
        router.push(`/product/${product.id}`)
      } else {
        router.push('/login')
      }
    }

    const handleAddToCart = async (product) => {
      if (store.getters.isAuthenticated) {
        try {
          await fetch('http://localhost:5000/api/user/cart/add', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${store.state.token}`
            },
            body: JSON.stringify({ productId: product.id })
          })
          await fetchCartItems()
        } catch (error) {
          console.error('Failed to add to cart. Please try again later', error)
        }
      } else {
        router.push('/login')
      }
    }

    const fetchCartItems = async () => {
      if (store.getters.isAuthenticated) {
        try {
          const cartResponse = await fetch('http://localhost:5000/api/user/cart', {
            headers: {
              'Authorization': `Bearer ${store.state.token}`
            }
          })
          const cartData = await cartResponse.json()
          cartItems.value = cartData.cartItems
        } catch (error) {
          console.error('Failed to fetch cart data: ', error)
        }
      }
    }

    onMounted(() => {
      fetchCartItems()
    })

    return {
      handleUserClick,
      handleFavoriteClick,
      handleCartClick,
      router,
      newProducts,
      cartItems,
      handleProductClick,
      handleAddToCart,
      handleSearch,
      searchQuery
    }
  }
}
</script>

<style scoped>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  background: #f6f5f7;
  font-family: 'Montserrat', sans-serif;
  width: 1440px;
  min-height: 100vh;
  margin-left: 0;
}


.mall-container {
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f6f5f7;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
}

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
  transition: color 0.3s ease;
}

.search-icon:hover {
  color: #FF416C;
}

.nav-actions a {
  color: #333;
  font-size: 1.4rem;
  transition: color 0.3s ease;
}

.nav-actions a:hover {
  color: #FF416C;
}

.nav-actions a {
  color: #333;
  font-size: 1.4rem;
}

main {
  padding-top: 80px;
  width: 100%;
}

.logo {
  font-size: 2.5rem;
  font-weight: bold;
  background: linear-gradient(45deg, #FF4B2B, #FF416C);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.logo:hover {
  transform: scale(1.1);
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 3rem;
}

.nav-links a {
  color: #333;
  text-decoration: none;
  font-size: 1.2rem;
  font-weight: 500;
  transition: color 0.3s ease;
}

.nav-actions {
  display: flex;
  gap: 2rem;
}

.nav-actions a {
  color: #333;
  font-size: 1.4rem;
}

main {
  padding-top: 80px;
  width: 100%;
}

.hero-section {
  background: linear-gradient(45deg, #FF4B2B, #FF416C);
  color: white;
  padding: 120px 40px;
  text-align: center;
  min-height: 600px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.hero-content {
  max-width: 1200px;
  margin: 0 auto;
}

.hero-content h1 {
  font-size: 4.5rem;
  margin-bottom: 1.5rem;
  line-height: 1.2;
}

.hero-content p {
  font-size: 1.8rem;
  margin-bottom: 3rem;
  opacity: 0.9;
}

.primary-btn {
  background: white;
  color: #FF416C;
  border: none;
  padding: 1.2rem 4rem;
  border-radius: 50px;
  font-size: 1.3rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}

.categories {
  padding: 100px 40px;
  background: #f8f9fa;
}

.categories h2 {
  text-align: center;
  margin-bottom: 60px;
  font-size: 3rem;
  color: #333;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 40px;
  max-width: 1600px;
  margin: 0 auto;
  padding: 0 40px;
}

.category-card {
  background: white;
  padding: 40px;
  border-radius: 20px;
  text-align: center;
  transition: all 0.3s ease;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
  min-height: 250px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.category-card:active {
  transform: translateY(0);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.category-card i {
  font-size: 3.5rem;
  color: #FF416C;
  margin-bottom: 1.5rem;
}

.category-card h3 {
  font-size: 1.5rem;
  color: #333;
}

.featured-products {
  padding: 100px 40px;
}

.featured-products h2 {
  text-align: center;
  margin-bottom: 60px;
  font-size: 3rem;
  color: #333;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30px;
  max-width: 1600px;
  margin: 0 auto;
  padding: 0 40px;
}

.product-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);

  display: flex;
  flex-direction: column;
  align-items: center;
}


.product-image {
  height: 260px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.product-info {
  padding: 30px;

  display: flex;
  flex-direction: column;
  align-items: center;

  text-align: center;
}

.product-info p {
  min-height: 50px;
}


.product-info h3 {
  font-size: 1.8rem;
  margin-bottom: 1rem;
  color: #333;
}

.price {
  color: #FF416C;
  font-size: 2rem;
  font-weight: bold;
  display: block;
  margin: 1.5rem 0;
}

.buy-btn {
  background: linear-gradient(45deg, #FF4B2B, #FF416C);
  color: white;
  border: none;
  padding: 1rem 3rem;
  border-radius: 50px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  width: 80%;
}

footer {
  background: #2c3e50;
  color: white;
  padding: 0;
  margin-top: 0;
  width: 100%;
}

.footer-content {
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 40px;
  padding: 40px 0;
}

.footer-section {
  padding: 0 15px;
}

.footer-section h4 {
  font-size: 1.4rem;
  margin-bottom: 1.2rem;
  color: #fff;
  position: relative;
  padding-bottom: 10px;
}

.footer-section h4:after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 50px;
  height: 2px;
  background: linear-gradient(45deg, #FF4B2B, #FF416C);
}

.footer-section p {
  font-size: 1rem;
  line-height: 1.6;
  opacity: 0.8;
  margin-bottom: 1.2rem;
}

.footer-section a {
  color: #fff;
  text-decoration: none;
  display: block;
  margin: 0.6rem 0;
  font-size: 1rem;
  opacity: 0.8;
  transition: all 0.3s ease;
}

.footer-section a:hover {
  opacity: 1;
  color: #FF416C;
  transform: translateX(5px);
}

.social-links {
  display: flex;
  gap: 1rem;
  margin-top: 1.2rem;
}

.social-links a {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1rem;
  transition: all 0.3s ease;
  margin: 0;
}

.social-links a:hover {
  background: #FF416C;
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(255, 65, 108, 0.3);
}

.primary-btn:hover,
.buy-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(255, 75, 43, 0.3);
}

.category-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.product-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.social-links a:hover {
  background: #FF416C;
  transform: translateY(-3px);
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

@media (max-width: 1280px) {
  .category-grid,
  .product-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 1200px) {
  .hero-content h1 {
    font-size: 3.5rem;
  }

  .footer-content {
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  }
}
</style>
