<template>
  <div class="shopping-container">

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

    <div class="carousel-section">
      <div class="carousel-container">
        <div class="carousel-slide" v-for="(slide, index) in slides" :key="index" :class="{ active: currentSlide === index }">
          <img :src="slide.image" :alt="slide.title">
          <div class="slide-content">
            <h2>{{ slide.title }}</h2>
            <p>{{ slide.description }}</p>
            <button class="shop-btn">Buy Now</button>
          </div>
        </div>
        <button class="carousel-btn prev" @click="prevSlide"><span class="material-icons">chevron_left</span></button>
        <button class="carousel-btn next" @click="nextSlide"><span class="material-icons">chevron_right</span></button>
        <div class="carousel-dots">
          <span v-for="(_, index) in slides" :key="index" 
                :class="{ active: currentSlide === index }" 
                @click="setSlide(index)"></span>
        </div>
      </div>
    </div>

    <section class="featured-products">
      <h2>Recommendations</h2>
      <div class="product-grid">
        <div class="product-card" v-for="products in productDetails" :key="products.id">
          <div class="product-image">
            <img :src="products.image_url" :alt="products.name">
          </div>
          <div class="product-info">
            <h3>{{ products.name }}</h3>
            <p>{{ products.description }}</p>
            <span class="price">¥{{ products.price }}</span>
            <button class="buy-btn">Add to Cart</button>
          </div>
        </div>
      </div>
    </section>
  </div>

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
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import axios from 'axios';
import {product} from '../api/product.js'

export default {
  name: 'ShoppingPage',
  setup() {
    const router = useRouter()
    const store = useStore()
    const currentSlide = ref(0)
    const searchQuery = ref('')
    const recommendations = ref([])
    const productDetails = ref([])

    const handleSearch = () => {
      if (searchQuery.value.trim()) {
        router.push(`/search?q=${encodeURIComponent(searchQuery.value.trim())}`)
      }
    }

    const getProductById = async (id) => {
      try {
        const response = await product.getProductById(id)
        const product = response.data;
        console.log(product);
        return product;
      } catch (error) {
        console.error('Failed to fetch product information:', error);
        return null;
      }
    };
    //example
    const slides = ref([
      {
        image: '/public/pictures/gbz56hft.png',
        title: 'The New MacBook Pro',
        description: 'Unleash creativity with breakthrough performance'
      },
      {
        image: '/public/pictures/gnm7x7es.png',
        title: 'iPhone 16 Pro',
        description: 'Powerful performance for a professional photography experience'
      },
      {
        image: '/public/pictures/octa3xs6.png',
        title: 'iPad Air',
        description: 'Lightweight design for limitless creativity'
      }
    ])

    const featuredProducts = ref([
      {
        id: 1,
        name: 'MacBook Pro 14"',
        description: 'Professional-grade performance powered by the M2 Pro chip',
        price: '14999',
        image: 'https://via.placeholder.com/300'
      },
      {
        id: 2,
        name: 'iPhone 14 Pro',
        description: 'Innovative technology for an exceptional experience',
        price: '8999',
        image: 'https://via.placeholder.com/300'
      },
      {
        id: 3,
        name: 'iPad Air',
        description: 'Thin and lightweight design with powerful performance',
        price: '4799',
        image: 'https://via.placeholder.com/300'
      },
      {
        id: 4,
        name: 'AirPods Pro',
        description: 'Immersive sound with active noise cancellation',
        price: '1999',
        image: 'https://via.placeholder.com/300'
      },
      {
        id: 5,
        name: 'Apple Watch',
        description: 'Smart health monitoring for your active lifestyle',
        price: '3299',
        image: 'https://via.placeholder.com/300'
      },
      {
        id: 6,
        name: 'iMac 24"',
        description: 'Powered by the M1 chip with stunning visuals',
        price: '12999',
        image: 'https://via.placeholder.com/300'
      },
      {
        id: 7,
        name: 'Mac mini',
        description: 'Small in size, big on performance',
        price: '5999',
        image: 'https://via.placeholder.com/300'
      },
      {
        id: 8,
        name: 'Studio Display',
        description: '5K Retina Display',
        price: '13999',
        image: 'https://via.placeholder.com/300'
      }
    ])

    const nextSlide = () => {
      currentSlide.value = (currentSlide.value + 1) % slides.value.length
    }

    const prevSlide = () => {
      currentSlide.value = (currentSlide.value - 1 + slides.value.length) % slides.value.length
    }

    const setSlide = (index) => {
      currentSlide.value = index
    }

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

    onMounted(async () => {

      store.dispatch('initAuth')
      // Autoplay
      setInterval(nextSlide, 5000)
      try {
        const response = await axios.get(`http://localhost:8000/recommend?user_id=${store.getters.userId}`);
        const recommendations = response.data.recommendations;
        console.log(recommendations)

        const details = await Promise.all(recommendations.map(id => product.getProductById(id)));
        productDetails.value = details
        console.log('Product Details：', productDetails)

      } catch (error) {
        console.error('Failed to fetch recommendations:', error);
      }


    })

    return {
      currentSlide,
      slides,
      recommendations,
      featuredProducts,
      nextSlide,
      prevSlide,
      setSlide,
      handleUserClick,
      handleFavoriteClick,
      handleCartClick,
      router,
      getProductById,
      productDetails
    }
  }
}
</script>

<style scoped>
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

.nav-actions a span.material-icons {
  color: #333;
  transition: all 0.3s ease;
  display: inline-block;
}

.nav-actions a:hover span.material-icons {
  color: #FF416C;
  transform: scale(1.2) rotate(10deg);
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
.shopping-container {
  min-height: 100vh;
  background: #f6f5f7;
}

/* 页脚样式 */
footer {
  background: #2c3e50;
  color: white;
  padding: 60px 0;
  margin-top: 0;
}

.footer-content {
  max-width: 1200px;
  width: 90%;
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
  transform: translateX(5px);
  color: #FF416C;
}


/* Carousel Ad */
.carousel-section {
  margin-top: 80px;
  position: relative;
  height: 600px;
  overflow: hidden;
}

.carousel-container {
  position: relative;
  height: 100%;
}

.carousel-slide {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  transition: opacity 0.5s ease-in-out;
}

.carousel-slide.active {
  opacity: 1;
}

.carousel-slide img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.slide-content {
  position: absolute;
  top: 50%;
  left: 10%;
  transform: translateY(-50%);
  color: white;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.slide-content h2 {
  font-size: 3.5rem;
  margin-bottom: 1rem;
}

.slide-content p {
  font-size: 1.5rem;
  margin-bottom: 2rem;
}

.shop-btn {
  background: white;
  color: #FF416C;
  border: none;
  padding: 1rem 3rem;
  border-radius: 30px;
  font-size: 1.2rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}

.shop-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(255, 65, 108, 0.3);
}

.carousel-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.3);
  border: none;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  transition: all 0.3s ease;
}

.carousel-btn:hover {
  background: rgba(255, 255, 255, 0.5);
}

.prev {
  left: 20px;
}

.next {
  right: 20px;
}

.carousel-dots {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
}

.carousel-dots span {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  transition: all 0.3s ease;
}

.carousel-dots span.active {
  background: white;
  transform: scale(1.2);
}

/* 推荐产品样式 */
.featured-products {
  padding: 80px 40px;
  max-width: 1920px;
  margin: 0 auto;
}

.featured-products h2 {
  text-align: center;
  font-size: 2.5rem;
  color: #333;
  margin-bottom: 50px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30px;
  padding: 0 30px;
  max-width: 1600px;
  margin: 0 auto;
}

.product-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.product-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  padding-top: 75%;
  position: relative;
  overflow: hidden;
}

.product-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 30px;
  text-align: center;
}

.product-info h3 {
  font-size: 1.8rem;
  color: #333;
  margin-bottom: 1rem;
}

.product-info p {
  font-size: 1.1rem;
  color: #666;
  margin-bottom: 1.5rem;
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
  border-radius: 30px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  width: 80%;
}

.buy-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(255, 65, 108, 0.3);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .slide-content h2 {
    font-size: 2.5rem;
  }

  .slide-content p {
    font-size: 1.2rem;
  }
}

@media (max-width: 768px) {
  .nav-links {
    display: none;
  }

  .carousel-section {
    height: 400px;
  }

  .slide-content {
    left: 5%;
    width: 90%;
  }

  .product-grid {
    grid-template-columns: 1fr;
    padding: 0 20px;
    gap: 20px;
  }

  .product-card {
    max-width: 400px;
    margin: 0 auto;
  }

  .nav-actions {
    gap: 1rem;
  }

  .search-box {
    display: none;
  }

  .footer-content {
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
  }
}

@media (max-width: 480px) {
  .nav-content {
    padding: 0 20px;
  }

  .logo {
    font-size: 2rem;
  }

  .carousel-section {
    height: 300px;
  }

  .slide-content h2 {
    font-size: 2rem;
  }

  .slide-content p {
    font-size: 1rem;
    margin-bottom: 1rem;
  }

  .shop-btn {
    padding: 0.8rem 2rem;
    font-size: 1rem;
  }

  .featured-products {
    padding: 40px 20px;
  }

  .featured-products h2 {
    font-size: 2rem;
  }

  .product-info h3 {
    font-size: 1.5rem;
  }

  .product-info p {
    font-size: 1rem;
  }

  .price {
    font-size: 1.5rem;
  }

  .buy-btn {
    padding: 0.8rem 2rem;
    font-size: 1rem;
    width: 100%;
  }
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
.social-links {
  display: flex;
  gap: 20px;
  margin-top: 15px;
}

.social-links a {
  margin: 0 10px;
  color: #666;
  text-decoration: none;
  transition: all 0.3s ease;
}

.social-links a:hover {
  color: #FF416C;
  transform: scale(1.2);
}

.social-links .material-icons {
  font-size: 24px;
}
</style>