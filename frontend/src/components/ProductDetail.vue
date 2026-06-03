<template>
  <div class="product-detail-container">
    <!-- Navigation Bar -->
    <nav class="nav-bar">
      <div class="nav-content">
        <div class="logo" @click="router.push('/')">iMall</div>
        <div class="nav-links">
          <a href="#" @click.prevent="router.push('/')">Home</a>
          <a href="#" @click.prevent="router.push('/laptop')">Laptop</a>
          <a href="#" @click.prevent="router.push('/phone')">Phone</a>
          <a href="#" @click.prevent="router.push('/tablet')">Tablet</a>
          <a href="#" @click.prevent="router.push('/watch')">SmartWatch</a>
          <a href="#" @click.prevent="router.push('/headphone')">Headphone</a>
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

    <!-- Main Content -->
    <main class="main-content">
      <!-- Loading State -->
      <div v-if="loading" class="loading-state">
        <div class="spinner"></div>
        <p>Loading...</p>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="error-state">
        <span class="material-icons">error_outline</span>
        <p>{{ error }}</p>
        <button @click="fetchProductDetail" class="retry-button">Retry</button>
      </div>

      <!-- Products Details -->
      <template v-else>
        <div class="product-detail">
          <!-- Product Image Gallery -->
          <div class="product-gallery">
            <div class="main-image">
              <img :src="products.mainImage" :alt="products.name">
            </div>
          </div>

          <!-- Product Information Section -->
          <div class="product-info">
            <h1 class="product-name">{{ products.name }}</h1>
            <div class="product-price">¥{{ (products?.price || 0).toFixed(2) }}</div>
            <div class="stock-info">Stock: {{ products.stock }}</div>
            <div class="product-description">{{ products.description }}</div>

            <!-- Specification Selection -->
            <div v-if="products?.specs && Object.keys(products.specs).length > 0" class="product-specs">
              <div v-for="(spec, specName) in products.specs"
                   :key="specName" 
                   class="spec-group">
                <h3>{{ specName }}</h3>
                <div class="spec-options">
                  <button v-for="option in spec.options" 
                          :key="option.value"
                          :class="{ active: selectedSpecs[specName] === option.value }"
                          @click="selectSpec(specName, option.value)"
                          class="spec-option">
                    {{ option.label }}
                  </button>
                </div>
              </div>
            </div>

            <!-- Quantity Selection -->
            <div class="quantity-selector">
              <h3>Number</h3>
              <div class="quantity-controls">
                <button @click="decreaseQuantity" :disabled="quantity <= 1">
                  <span class="material-icons">remove</span>
                </button>
                <input type="number" 
                       v-model.number="quantity" 
                       min="1"
                       :max="products.stock"
                       @input="quantity = Math.min(Math.max(quantity, 1), products.stock)">
                <button @click="increaseQuantity" :disabled="quantity >= products.stock">
                  <span class="material-icons">add</span>
                </button>
              </div>
            </div>

            <!-- Action Buttons -->
            <div class="action-buttons">
              <button class="favorite-btn" @click="addToFavorite">
                <span class="material-icons" :class="{ 'is-favorite': isFavorite }">
                  {{ isFavorite ? 'favorite' : 'favorite_border' }}
                </span>
                {{ isFavorite ? 'Saved' : 'Add to Favorites' }}
              </button>
              <button class="add-to-cart-btn" 
                      @click="addToCart"
                      :disabled="products.stock === 0">
                <span class="material-icons">shopping_cart</span>
                {{ products.stock === 0 ? 'Out of Stock' : 'Add to Cart' }}
              </button>
              <button class="buy-now-btn"
                      @click="buyNow"
                      :disabled="products.stock === 0">
                {{ products.stock === 0 ? 'Out of Stock' : 'Buy Now' }}
              </button>
            </div>
          </div>
        </div>

        <!-- Recommendation -->
        <div v-if="recommendedProducts.length > 0" class="recommended-products">
          <h2>Recommendations</h2>
          <div class="product-grid">
            <div v-for="item in recommendedProducts" 
                 :key="item.id" 
                 class="product-card"
                 @click="router.push(`/product/${item.id}`)">
              <div class="product-image">
                <img :src="item.images[0]" :alt="item.name">
              </div>
              <div class="product-info">
                <h3>{{ item.name }}</h3>
                <p>{{ item.description }}</p>
                <span class="price">¥{{ (item?.price || 0).toFixed(2) }}</span>
              </div>
            </div>
          </div>
        </div>
      </template>
    </main>

    <!-- Footer -->
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
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
import {product} from '../api/product.js'
import {cart} from '../api/cart.js'
import {ElMessage} from 'element-plus'
import {favorite} from '../api/favorite.js'
import {search} from '../api/search.js'

export default {
  name: 'ProductDetail',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const store = useStore()

    // State Management
    const loading = ref(true)
    const error = ref(null)
    
    // Product Data
    const products = ref({
      id: null,
      name: '',
      price: 0,
      description: '',
      mainImage: '',
      images: [],
      stock: 0,
      category: null
    })

    // Selected Image Index
    const selectedImageIndex = ref(0)
    // Selected Specifications
    const selectedSpecs = ref({})
    // Product Quantity
    const quantity = ref(1)
    // Favorites Status
    const isFavorite = ref(false)
    // Recommended Products
    const recommendedProducts = ref([])

    // Fetch Product Details
    const fetchProductDetail = async () => {
      if (!route.params.id) {
        error.value = 'Invalid product ID'
        return
      }

      loading.value = true
      error.value = null

      try {
        const productData = await product.getProductById(route.params.id)

        if (!productData) {
          throw new Error('Failed to retrieve product data')
        }

        // Validate Required Product Fields
        const requiredFields = {
          id: 'Product ID',
          name: 'Product Name',
          price: 'Price',
          image_url: 'Product Image',
          category: 'Category',
          created_at: 'Created Time',
          updated_at: 'Updated Time'
        }


        const missingFields = Object.entries(requiredFields)
          .filter(([field]) => !productData[field])
          .map(([, label]) => label)

        if (missingFields.length > 0) {
          throw new Error(`Invalid product data format: Missing required fields ${missingFields.join('、')}`)
        }

        // Validate Data Types
        if (isNaN(Number(productData.price)) || Number(productData.price) < 0) {
          throw new Error('Invalid product price format')
        }
        if (isNaN(Number(productData.stock)) || Number(productData.stock) < 0) {
          throw new Error('Invalid product stock format')
        }

        // Update Product Data and Map Database Fields
        products.value = {
          id: productData.id,
          name: productData.name,
          description: productData.description || '',
          price: Number(productData.price),
          originalPrice: Number(productData.original_price || productData.price),
          mainImage: productData.image_url,
          images: productData.images ? productData.images : [productData.image],
          stock: Number(productData.stock) || 0,
          category: productData.category,
          categoryId: productData.category_id,
          createdAt: productData.created_at,
          updatedAt: productData.updated_at,
          tags: Array.isArray(productData.tags)
            ? productData.tags
            : (typeof productData.tags === 'string' ? JSON.parse(productData.tags) : []),
          specs: productData.specs || {},
        }

        // Initialize Default selected Specifications
        if (products.value?.specs) {
          Object.keys(products.value.specs).forEach(specName => {
            if (products.value.specs[specName]?.options?.length > 0) {
              selectedSpecs.value[specName] = products.value.specs[specName].options[0].value
            }
          })
        }

      } catch (error) {
        console.error('Failed to retrieve product details:', error)
        error.value = error.response?.data?.message || error.message || 'Failed to retrieve product information. Please try again later.'
      } finally {
        loading.value = false
      }
    }

    // Fetch Recommended Products
    const fetchRecommendedProducts = async () => {
      if (!product.value?.categoryId) {
        console.warn('Unable to retrieve recommended products: Missing category ID')
        return
      }

      try {
        const { data } = await axios.get(
          `http://localhost:5000/api/products/recommended?categoryId=${product.value.categoryId}`
        )
        
        if (Array.isArray(data)) {
          recommendedProducts.value = data.map(item => ({
            ...item,
            price: Number(item.price)
          }))
        } else {
          console.warn('Invalid recommended products data format.')
          recommendedProducts.value = []
        }
      } catch (error) {
        console.error('Failed to retrieve recommended products:', error)
        recommendedProducts.value = []
      }
    }

    // Fetch Favorite Status
    const fetchFavoriteStatus = async () => {
      if (store.getters.isAuthenticated) {
        try {
          console.log("userId:", store.getters.userId)
          console.log("productId:", products.value.id)
          const response = await favorite.isFavorite(
              store.getters.userId,
              products.value.id
        )
          console.log(response.data)
          isFavorite.value = response.data
        } catch (error) {
          console.error('Failed to retrieve favorite status:', error)
        }
      }
    }

    // Select Image
    const selectImage = (index) => {
      selectedImageIndex.value = index
      product.value.mainImage = product.value.images[index]
    }

    // Select Specifications
    const selectSpec = (specName, value) => {
      selectedSpecs.value[specName] = value
    }

    // Increase Quantity
    const increaseQuantity = () => {
      if (quantity.value < products.value.stock) {
        quantity.value++
      }
    }

    // Decrease Quantity
    const decreaseQuantity = () => {
      if (quantity.value > 1) {
        quantity.value--
      }
    }

    // Add to Favorites
    const addToFavorite = async () => {
      if (!store.getters.isAuthenticated) {
        router.push('/login')
        return
      }
      //Parameter Validation
      if (isFavorite.value){
        alert('This product is already in your favorites')
        ElMessage.error('This product is already in your favorites list')
      }

      // Axios Response Object
      if (!products.value || !products.value.id) {
        ElMessage.error('Invalid product information')
        return
      }
      console.log(products.value)
      try {
        const response = await favorite.addToFavorite(
            store.getters.userId,
            products.value.id
        );

        // Axios response object
        const responseData = response.data;
        alert('Added to favorites successfully')
        ElMessage.success('Added to favorites');
      } catch (error) {

        alert('This product is already in your favorites list')
      }

      console.log('Added to favorites')
    }


    // Add to Cart
    const addToCart = async () => {
      if (!store.getters.isAuthenticated) {
        router.push('/login')
        return
      }

      // Parameter Validation
      if (!products.value || !products.value.id) {
        ElMessage.error('Invalid product information')
        return
      }

      if (!quantity.value || quantity.value <= 0) {
        ElMessage.error('Please select a valid quantity')
        return
      }
      console.log(products.value,quantity)
      try {
        const response = await cart.addToCart(
            store.getters.userId,
            products.value.id,
            quantity.value
        );

        // Axios response object
        const responseData = response.data;

        alert("Successful!")
      } catch (error) {
        let errorMessage = 'Failed to add to cart. Please try again later';

        if (error.response && error.response.data && error.response.data.message) {
          errorMessage = error.response.data.message;
        }

        console.error('Failed to add to cart:', error);
        ElMessage.error(errorMessage);
      }

        ElMessage.success('Added to cart')
      }

    // Buy Now
    const buyNow = () => {
      if (!store.getters.isAuthenticated) {
        router.push('/login')
        return
      }

      // Navigate to Checkout Page
      router.push({
        path: '/ordercheckout',
        query: {
          source: 'product',
          items: JSON.stringify([{ productId: products.value.id, quantity: quantity.value }])
        }
      })
    }

    // Navigation Actions
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

    // Watch Route Parameter Changes
    watch(
      () => route.params.id,
      (newId) => {
        if (newId) {
          fetchProductDetail()
          fetchFavoriteStatus()
        }
      }
    )

    onMounted(async () => {
      await fetchProductDetail(); // Load Product Information
      await fetchFavoriteStatus(); // Check Favorite Status After Product Loads
      await search.readReadHistory(store.getters.userId,route.params.id);
    });

    return {
      router,
      products,
      loading,
      error,
      recommendedProducts,
      selectedImageIndex,
      selectedSpecs,
      quantity,
      isFavorite,
      selectImage,
      selectSpec,
      increaseQuantity,
      decreaseQuantity,
      addToFavorite,
      addToCart,
      buyNow,
      handleUserClick,
      handleFavoriteClick,
      handleCartClick
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

.product-detail-container {
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
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

.nav-links a:hover {
  color: #FF416C;
}

.nav-actions {
  display: flex;
  gap: 2rem;
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

.main-content {
  padding: 120px 40px 60px;
  max-width: 1600px;
  margin: 0 auto;
  width: 100%;
}

.product-detail {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  margin-bottom: 80px;
}

.product-gallery {
  background: white;
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
}

.main-image {
  width: 100%;
  max-width: 600px;
  height: 400px;
  max-height: 600px;
  margin-bottom: 20px;
  border-radius: 10px;
  overflow: hidden;
}

.main-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.thumbnail-list {
  display: flex;
  gap: 15px;
  overflow-x: auto;
  padding: 10px 0;
}

.thumbnail-list img {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  cursor: pointer;
  object-fit: cover;
  transition: all 0.3s ease;
}

.thumbnail-list img:hover {
  transform: scale(1.05);
}

.thumbnail-list img.active {
  border: 2px solid #FF416C;
}

.product-info {
  background: white;
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
}

.product-name {
  font-size: 2.5rem;
  color: #333;
  margin-bottom: 20px;
}

.product-price {
  font-size: 2.5rem;
  color: #FF416C;
  font-weight: bold;
  margin-bottom: 30px;
}

.stock-info {
  font-size: 1.1rem;
  color: #666;
  margin-bottom: 30px;
}

.product-description {
  font-size: 1.1rem;
  color: #666;
  line-height: 1.6;
  margin-bottom: 30px;
}

.product-specs {
  margin-bottom: 30px;
}

.spec-group {
  margin-bottom: 20px;
}

.spec-group h3 {
  font-size: 1.2rem;
  color: #333;
  margin-bottom: 15px;
}

.spec-options {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.spec-option {
  padding: 10px 20px;
  border: 1px solid #ddd;
  border-radius: 25px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  background: white;
}

.spec-option:hover {
  border-color: #FF416C;
  color: #FF416C;
}

.spec-option.active {
  background: #FF416C;
  color: white;
  border-color: #FF416C;
}

.quantity-selector {
  margin-bottom: 30px;
}

.quantity-selector h3 {
  font-size: 1.2rem;
  color: #333;
  margin-bottom: 15px;
}

.quantity-controls {
  display: flex;
  align-items: center;
  gap: 15px;
}

.quantity-controls button {
  width: 40px;
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 50%;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.quantity-controls button:hover {
  border-color: #FF416C;
  color: #FF416C;
}

.quantity-controls input {
  width: 60px;
  height: 40px;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 20px;
  font-size: 1.1rem;
}

.action-buttons {
  display: flex;
  gap: 20px;
}

.favorite-btn,
.add-to-cart-btn,
.buy-now-btn {
  padding: 15px 30px;
  border-radius: 25px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.favorite-btn {
  background: white;
  border: 1px solid #FF416C;
  color: #FF416C;
}

.favorite-btn:hover {
  background: #FF416C;
  color: white;
}

.add-to-cart-btn {
  background: #FF4B2B;
  border: none;
  color: white;
  flex: 1;
}

.buy-now-btn {
  background: #FF416C;
  border: none;
  color: white;
  flex: 1;
}

.add-to-cart-btn:hover,
.buy-now-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(255, 65, 108, 0.3);
}

.recommended-products {
  margin-top: 80px;
}

.recommended-products h2 {
  font-size: 2rem;
  color: #333;
  text-align: center;
  margin-bottom: 40px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30px;
}

.product-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.product-card .product-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.product-card .product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-card .product-info {
  padding: 20px;
}

.product-card h3 {
  font-size: 1.3rem;
  color: #333;
  margin-bottom: 10px;
}

.product-card p {
  font-size: 1rem;
  color: #666;
  margin-bottom: 15px;
}

.product-card .price {
  font-size: 1.5rem;
  color: #FF416C;
  font-weight: bold;
}

footer {
  background: #2c3e50;
  color: white;
  padding: 60px 0;
  margin-top: auto;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 40px;
  padding: 0 40px;
}

.footer-section h4 {
  font-size: 1.4rem;
  margin-bottom: 20px;
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
  color: #bdc3c7;
  line-height: 1.6;
  margin-bottom: 15px;
}

.footer-section a {
  color: #bdc3c7;
  text-decoration: none;
  display: block;
  margin-bottom: 10px;
  transition: color 0.3s ease;
}

.footer-section a:hover {
  color: #FF416C;
}

.social-links {
  display: flex;
  gap: 15px;
}

.social-links a {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.social-links a:hover {
  background: #FF416C;
  color: white;
}

@media (max-width: 1200px) {
  .product-detail {
    grid-template-columns: 1fr;
    gap: 40px;
  }

  .product-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 900px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .footer-content {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 600px) {
  .nav-links {
    display: none;
  }

  .product-grid {
    grid-template-columns: 1fr;
  }

  .action-buttons {
    flex-direction: column;
  }

  .footer-content {
    grid-template-columns: 1fr;
  }
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  padding: 40px;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #FF416C;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  padding: 40px;
  text-align: center;
  color: #ff4444;
}

.error-state .material-icons {
  font-size: 48px;
  margin-bottom: 16px;
}

.error-state p {
  font-size: 18px;
  margin-bottom: 24px;
}

.retry-button {
  padding: 12px 24px;
  background-color: #FF416C;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.retry-button:hover {
  background-color: #FF4B2B;
  transform: translateY(-2px);
}

.stock-info {
  font-size: 1.1rem;
  color: #666;
  margin-bottom: 20px;
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.quantity-controls input::-webkit-inner-spin-button,
.quantity-controls input::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.quantity-controls input {
  -moz-appearance: textfield;
}
</style>