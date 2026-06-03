<template>
  <div class="search-page">
    <nav class="nav-bar">
      <div class="nav-content">
        <div class="logo" @click="router.push('/')">iMall</div>
        <div class="nav-links">
          <a href="#" class="active" @click.prevent="router.push('/')">Home</a>
          <a href="#" @click.prevent="router.push('/laptop')">Laptop</a>
          <a href="#" @click.prevent="router.push('/phone')">Phone</a>
          <a href="#" @click.prevent="router.push('/tablet')">Tablet</a>
          <a href="#" @click.prevent="router.push('/watch')">SmartPhone</a>
          <a href="#" @click.prevent="router.push('/headphone')">Headphone</a>
        </div>
        <div class="nav-actions">
          <a href="#" @click.prevent="handleFavoriteClick"><span class="material-icons">favorite</span></a>
          <a href="#" @click.prevent="handleCartClick"><span class="material-icons">shopping_cart</span></a>
          <a href="#" @click.prevent="handleUserClick"><span class="material-icons">person</span></a>
        </div>
      </div>
    </nav>
    <div class="search-header">
      <h1>Search Results</h1>
      <div class="search-box">
        <input type="text" v-model="searchQuery" @keyup.enter="handleSearch" placeholder="Search..." class="search-input">
        <span class="material-icons search-icon" @click="handleSearch">search</span>
      </div>

      <p v-if="searchQuery" class="search-info">
        Found {{ products.length }} products related to "{{ searchQuery }}"
      </p>
    </div>

    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>

    <div v-else-if="products.length === 0" class="empty-result">
      <span class="material-icons">search_off</span>
      <p>No products found</p>
      <el-button type="primary" @click="$router.push('/')">Back to Home</el-button>
    </div>

    <div v-else class="product-grid">
      <div v-for="product in products" :key="product.id" class="product-card" @click="goToProductDetail(product.id)">
        <div class="product-image">
          <img :src="product.image_url" :alt="product.name">
        </div>
        <div class="product-info">
          <h3 class="product-name">{{ product.name }}</h3>
          <p class="product-description">{{ product.description }}</p>
          <div class="product-price">
            <span class="price-symbol">¥</span>
            <span class="price-amount">{{ product.price }}</span>
          </div>
        </div>
      </div>
    </div>

    <el-pagination
        v-if="products.length > 0"
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[12, 24, 36, 48]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
    />
  </div>
</template>


<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { product } from '../api/product.js'

const route = useRoute()
const router = useRouter()

const searchQuery = ref('')
const products = ref([])
const loading = ref(true)
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

// listen
watch(
  () => route.query.q,
  (newQuery) => {
    if (newQuery) {
      searchQuery.value = newQuery
      fetchProducts()
    }
  }
)

const fetchProducts = async () => {
  loading.value = true
  try {
    const response = await product.searchProducts(searchQuery.value)
    products.value = response.content
    total.value = response.total
  } catch (error) {
    ElMessage.error('Failed to fetch search results')
    console.error('Search failed:', error)
  } finally {
    loading.value = false
  }
}

const goToProductDetail = (productId) => {
  router.push(`/product/${productId}`)
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchProducts()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchProducts()
}

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    // Add user search history
    search.getSearchContent(store.getters.userId,searchQuery.value.trim())
    router.push(`/search?q=${encodeURIComponent(searchQuery.value.trim())}`)
  }
}


onMounted(() => {
  const query = route.query.q
  if (query) {
    searchQuery.value = query
    fetchProducts()
  }
})

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


.search-box {
  display: flex;
  align-items: center;
  width: 100%;
  max-width: 960px;
  height: 56px;
  background-color: #f5f5f5;
  border-radius: 28px;
  padding: 0 20px;
  margin: 0 auto 48px;
  box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.08);
}

.search-box .search-icon {
  font-size: 28px;
  color: #888;
  margin-right: 16px;
}

.search-box input {
  border: none;
  outline: none;
  background: transparent;
  flex: 1;
  font-size: 18px;
  color: #333;
}
.search-icon {
  position: absolute;
  right: 300px;
  color: #666;
  cursor: pointer;
  transition: color 0.3s ease;
}
.search-icon:hover {
  color: #FF416C;
}

.search-page {
  background-color: #fff;
  min-width: 1536px;
  margin: 0 auto;
  padding-top: 100px;
}

.search-header {
  text-align: center;
  margin-bottom: 30px;
}

.search-header h1 {
  font-size: 2.5rem;
  color: #333;
  font-weight: bold;
}

.search-info {
  font-size: 1.2rem;
  color: #777;
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}

.empty-result {
  text-align: center;
  padding: 40px;
  color: #666;
}

.empty-result .material-icons {
  font-size: 4rem;
  color: #FF416C;
}

.empty-result p {
  font-size: 1.2rem;
  margin-top: 20px;
}

.empty-result .el-button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #FF416C;
  border-color: #FF416C;
  color: white;
  transition: background-color 0.3s ease;
}

.empty-result .el-button:hover {
  background-color: #FF4B2B;
  border-color: #FF4B2B;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30px;
  margin-top: 20px;
}

.product-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
  overflow: hidden;
  max-width: 100%;
}

.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  height: 250px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .product-image img {
  transform: scale(1.1);
}

.product-info {
  padding: 25px;
}

.product-name {
  font-size: 1.6rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 12px;
}

.product-description {
  font-size: 1.1rem;
  color: #666;
  margin-bottom: 12px;
}

.product-price {
  font-size: 1.4rem;
  color: #FF416C;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 5px;
}

.price-symbol {
  font-size: 1.6rem;
}

.price-amount {
  font-size: 1.8rem;
}

.pagination {
  margin-top: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
}

.pagination .el-pagination {
  border-radius: 6px;
}

.pagination .el-pagination__button {
  border-radius: 6px;
}

.pagination .el-pagination__button:hover {
  background-color: #FF416C;
  color: white;
}

.pagination .el-pagination__sizes {
  font-size: 1rem;
}

.pagination .el-pagination__total {
  font-size: 1rem;
  color: #333;
}

.pagination .el-pagination__jumper {
  font-size: 1rem;
}

@media (max-width: 1024px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .product-grid {
    grid-template-columns: 1fr;
  }
}



</style>