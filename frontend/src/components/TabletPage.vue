<template>
  <div class="tablet-container">
    <nav class="nav-bar">
      <div class="nav-content">
        <div class="logo" @click="router.push('/')">iMall</div>
        <div class="nav-links">
          <a href="#" class="active" @click.prevent="router.push('/')">Home</a>
          <a href="#" @click.prevent="router.push('/laptop')">Laptop</a>
          <a href="#" @click.prevent="router.push('/phone')">Phone</a>
          <a href="#" @click.prevent="router.push('/tablet')">Tablet</a>
          <a href="#" @click.prevent="router.push('/watch')">SmartPhone</a>
          <a href="#" @click.prevent="router.push('/headphone')">HeadPhone</a>
        </div>
        <div class="nav-actions">
          <div class="search-box">
            <input type="text" placeholder="Search..." class="search-input">
            <span class="material-icons search-icon">search</span>
          </div>
          <a href="#" @click.prevent="handleFavoriteClick"><span class="material-icons">favorite</span></a>
          <a href="#" @click.prevent="handleCartClick"><span class="material-icons">shopping_cart</span></a>
          <a href="#" @click.prevent="handleUserClick"><span class="material-icons">person</span></a>
        </div>
      </div>
    </nav>

    <main class="main-content">
      <div class="filter-section">
        <div class="filter-item">
          <span class="filter-label">价格：</span>
          <a href="#" 
             v-for="option in priceOptions" 
             :key="option.value"
             :class="{ active: priceFilter === option.value }"
             @click.prevent="handlePriceFilter(option.value)"
          >
            {{ option.label }}
          </a>
        </div>
        <div class="filter-item">
          <span class="filter-label">Brand：</span>
          <a href="#" 
             v-for="brand in brands" 
             :key="brand.value"
             :class="{ active: selectedBrand === brand.value }"
             @click.prevent="handleBrandFilter(brand.value)"
          >
            {{ brand.label }}
          </a>
        </div>
      </div>
      <div class="products-section">
        <div class="product-grid">
          <div class="product-card"
               v-for="product in products"
               :key="product.id"
               @click="handleProductClick(product.id)">
            <div class="product-image">
              <img :src="product.image_url" :alt="product.name">
            </div>
            <div class="product-info">
              <h3 class="product-name">{{ product.name }}</h3>
              <p class="product-desc">{{ product.description }}</p>
              <div class="product-price">
                <span class="price">¥{{ product.price }}</span>
                <span class="original-price" v-if="product.originalPrice">¥{{ product.originalPrice }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="pagination" v-if="totalPages > 1">
        <button 
          class="page-btn" 
          :disabled="currentPage === 1" 
          @click="handlePageChange(currentPage - 1)"
        >
          Previous
        </button>
        <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
        <button 
          class="page-btn" 
          :disabled="currentPage === totalPages" 
          @click="handlePageChange(currentPage + 1)"
        >
          Next
        </button>
      </div>

      <div class="loading-overlay" v-if="loading">
        <div class="loading-spinner"></div>
      </div>
    </main>
  </div>
</template>

<script>
import { product } from '../api/product.js';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import { ref, onMounted } from 'vue';

export default {
  name: 'TabletPage',
  setup() {
    const router = useRouter()
    const store = useStore()

    const products = ref([])
    const currentPage = ref(1)
    const totalPages = ref(1)
    const loading = ref(false)
    const priceFilter = ref('all')
    const selectedBrand = ref('all')

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

    const priceOptions = [
      { value: 'all', label: 'All' },
      { value: 'under2000', label: 'Under 2000' },
      { value: '2000-4000', label: '2000 - 4000' },
      { value: '4000-6000', label: '4000 - 6000' },
      { value: 'above6000', label: 'Above 6000' }
    ]

    const brands = [
      { value: 'all', label: 'All' },
      { value: 'Apple', label: 'Apple' },
      { value: 'Samsung', label: 'Samsung' },
      { value: 'Huawei', label: 'Huawei' },
      { value: 'Xiaomi', label: 'Xiaomi' },
      { value: 'Lenovo', label: 'Lenovo' },
      { value: 'Microsoft', label: 'Microsoft' }
    ]

    const fetchProducts = async () => {
      try {
        loading.value = true
        const response = await product.getProductsByCategory('tablet', {
          params: {
            page: currentPage.value,
            limit: 12,
            priceRange: JSON.stringify(getPriceRange(priceFilter.value)),
            brand: selectedBrand.value === 'all' ? null : selectedBrand.value
          }
        })
        if (!response || typeof response !== 'object') {
          throw new Error('Failed to fetch data: Invalid server response')
        }
        const { products: productList, totalPages: total } = response
        if (!Array.isArray(productList)) {
          throw new Error('Failed to fetch data: Invalid product data format')
        }
        products.value = productList
        totalPages.value = total || 1
      } catch (error) {
        console.error('Failed to fetch tablet products:', error)
      } finally {
        loading.value = false
      }
    }

    const handlePageChange = (page) => {
      currentPage.value = page
      fetchProducts()
    }

    const handlePriceFilter = (range) => {
      priceFilter.value = range
      currentPage.value = 1
      fetchProducts()
    }

    const getPriceRange = (range) => {
      switch (range) {
        case 'under2000':
          return { min: 0, max: 2000 }
        case '2000-4000':
          return { min: 2000, max: 4000 }
        case '4000-6000':
          return { min: 4000, max: 6000 }
        case 'above6000':
          return { min: 6000, max: 1000000 }
        default:
          return null
      }
    }

    const handleBrandFilter = (brand) => {
      selectedBrand.value = brand
      currentPage.value = 1
      fetchProducts()
    }
    
    onMounted(() => {
      fetchProducts()
    })

    const handleProductClick = (productId) => {
      router.push(`/product/${productId}`)
    }

    return {
      products,
      currentPage,
      totalPages,
      loading,
      priceFilter,
      selectedBrand,      
      handleUserClick,
      handleFavoriteClick,
      handleCartClick,
      handleProductClick,
      handleBrandFilter,
      router,
      priceOptions,
      brands,
      fetchProducts,
      getPriceRange,
      handlePageChange,
      handlePriceFilter
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
.tablet-container {
  min-height: 100vh;
  background: #f5f5f5;
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

.main-content {
  padding-top: 100px;
  max-width: 1920px;
  margin: 0 auto;
  padding-left: 24px;
  padding-right: 24px;
}

.filter-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 24px;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
  padding: 0.5rem 0;
}

.filter-item:last-child {
  margin-bottom: 0;
}

.filter-label {
  color: #333;
  font-size: 1rem;
  min-width: 60px;
  font-weight: 500;
}

.filter-item a {
  color: #333;
  text-decoration: none;
  padding: 8px 16px;
  border-radius: 6px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  cursor: pointer;
  font-size: 0.95rem;
}

.filter-item a:hover {
  background: rgba(0, 0, 0, 0.05);
  transform: translateY(-1px) scale(1.02);
}

.filter-item a.active {
  color: #333;
  background: rgba(0, 0, 0, 0.08);
  font-weight: 500;
  transform: scale(1.05);
}

.filter-item a::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: #333;
  transform: translateX(-50%);
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.filter-item a:hover::after,
.filter-item a.active::after {
  width: 80%;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.product-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  aspect-ratio: 1;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.product-info {
  padding: 20px;
}

.product-name {
  font-size: 1.2rem;
  color: #333;
  margin-bottom: 8px;
}

.product-desc {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 12px;
}

.product-price {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.price {
  font-size: 1.5rem;
  color: #FF416C;
  font-weight: bold;
}

.original-price {
  font-size: 1rem;
  color: #999;
  text-decoration: line-through;
}

.product-tags {
  display: flex;
  gap: 8px;
}

.tag {
  padding: 2px 8px;
  background: rgba(255, 65, 108, 0.1);
  color: #FF416C;
  border-radius: 4px;
  font-size: 0.8rem;
}

@media (max-width: 1600px) {
  .product-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 1200px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .product-grid {
    grid-template-columns: 1fr;
  }
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 3rem 0;
  gap: 1.5rem;
  padding: 1rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.page-btn {
  padding: 0.75rem 1.5rem;
  border: none;
  background: linear-gradient(45deg, #FF4B2B, #FF416C);
  color: white;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  font-weight: 500;
  font-size: 1rem;
  letter-spacing: 0.5px;
  position: relative;
  overflow: hidden;
}

.page-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(45deg, #FF416C, #FF4B2B);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.page-btn:disabled {
  background: #e0e0e0;
  cursor: not-allowed;
  transform: none;
}

.page-btn:not(:disabled):hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 65, 108, 0.2);
}

.page-btn:not(:disabled):hover::before {
  opacity: 1;
}

.page-info {
  font-size: 1.2rem;
  color: #333;
  font-weight: 500;
  padding: 0.5rem 1rem;
  background: rgba(255, 65, 108, 0.05);
  border-radius: 6px;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.9);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
  transition: all 0.3s ease;
}

.loading-spinner {
  width: 60px;
  height: 60px;
  border: 4px solid rgba(255, 65, 108, 0.1);
  border-top: 4px solid #FF416C;
  border-right: 4px solid #FF416C;
  border-radius: 50%;
  animation: spin 1s cubic-bezier(0.68, -0.55, 0.265, 1.55) infinite;
  box-shadow: 0 4px 12px rgba(255, 65, 108, 0.2);
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-overlay::after {
  content: '加载中...';
  position: absolute;
  top: 55%;
  left: 50%;
  transform: translateX(-50%);
  color: #FF416C;
  font-size: 1rem;
  font-weight: 500;
  margin-top: 1rem;
  animation: fadeInUp 0.5s ease forwards;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translate(-50%, 10px);
  }
  to {
    opacity: 1;
    transform: translate(-50%, 0);
  }
}
</style>