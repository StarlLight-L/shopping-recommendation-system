<template>
  <div class="favorite-container">
    <NavBar />

    <div class="main-content" v-if="isLoggedIn">
      <!-- Navigate Bar -->
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
      <!-- Favorites Content -->
      <div class="favorite-content">
        <h2>My Favorites</h2>
        <div class="favorite-items" v-if="favoriteItems.length > 0">
          <table class="favorite-table">
            <thead>
              <tr>
                <th class="product-info">Product</th>
                <th class="product-price">Price</th>
                <th class="product-action">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in favoriteItems" :key="item.id" class="favorite-item">
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
                <td class="product-action">
                  <button class="add-to-cart" @click="addToCart(item)">
                    <span class="material-icons">shopping_cart</span>
                    Add to Cart
                  </button>
                  <button class="remove-btn" @click="removeFavorite(item.id)">
                    <span class="material-icons">delete</span>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="empty-favorite" v-else>
          <span class="material-icons">favorite_border</span>
          <p>Your favorites list is empty</p>
          <button class="continue-shopping" @click="router.push('/shopping')">Continue Shopping</button>
        </div>
      </div>
    </div>
    <div class="login-prompt" v-else>
      <p>Please sign in first</p>
      <button @click="handleUserClick">Sign In</button>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import {favorite} from '../api/favorite.js'
import {cart} from '../api/cart.js'
import {ElMessage} from 'element-plus'

export default {
  name: 'FavoritePage',
  setup() {
    const router = useRouter()
    const store = useStore()

    const isLoggedIn = computed(() => store.state.isAuthenticated)
    const favoriteItems = ref([])

    const handleUserClick = () => {
      router.push('/profile')
    }

    const addToCart = async (item) => {
      if (!store.getters.isAuthenticated) {
        router.push('/login')
        return
      }
      try {
        const response = await cart.addToCart(
            store.getters.userId,
            item.id,
            1
        );

        const responseData = response.data;
        alert("Added to cart successfully")
        ElMessage.success('Added to cart successfully');
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


    const removeFavorite = async (item) => {
      try {
        await favorite.removeFromFavorite(store.getters.userId, item)

        // Find item id and delete
        const index = favoriteItems.value.findIndex(fav => fav.id === item)
        if (index > -1) {
          favoriteItems.value.splice(index, 1)
        }
      } catch (error) {
        console.error('Failed to remove item:', error)
      }
    }


    const loadFavoriteItems = async () => {
      try {
        if (!store.state.user || !store.state.user.id) {
          console.error('User is not logged in or the user ID does not exist')
          return
        }
        const response = await favorite.getFavoriteItems(store.getters.userId)
        if (!response) {
          console.error('Failed to fetch favorites data: The server returned empty data')
          return
        }
        console.log(response)

        favoriteItems.value = response.map(item => ({
          id: item.id,
          name: item.name,
          description: item.description,
          price: item.price,
          image: item.image_url
        }))
      } catch (error) {
        console.error('Failed to load favorites. Please try again later', error)
        if (error.response && error.response.status === 401) {
          store.dispatch('logout')
          router.push('/login')
        }
      }
    }

    onMounted(() => {
      if (isLoggedIn.value) {
        loadFavoriteItems()
      }
    })

    return {
      router,
      isLoggedIn,
      favoriteItems,
      handleUserClick,
      addToCart,
      removeFavorite
    }
  }
}
</script>

<style scoped>
.favorite-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  display: flex;
  gap: 30px;
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

.side-menu {
  width: 200px;
  background: white;
  border-radius: 15px;
  padding: 20px;
  height: fit-content;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 15px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.menu-item:hover {
  background: #f5f5f5;
}

.menu-item.active {
  background: #FF416C;
  color: white;
}

.menu-item .material-icons {
  font-size: 20px;
}

.favorite-content {
  flex: 1;
  background: white;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.favorite-content h2 {
  font-size: 1.8rem;
  color: #333;
  margin-bottom: 30px;
}

.favorite-table {
  width: 100%;
  border-collapse: collapse;
}

thead th {
  padding: 15px;
  text-align: left;
  border-bottom: 2px solid #eee;
  color: #666;
}

.favorite-item td {
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

.product-price {
  min-width: 30px;
  text-align: center;
  color: #666;
}

.product-action {
  display: flex;
  align-items: center;
  gap: 15px;

}

.add-to-cart {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: #FF416C;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.add-to-cart:hover {
  background: #FF2E5B;
}

.remove-btn {
  background: none;
  border: none;
  color: #FF416C;
  cursor: pointer;
  transition: color 0.3s ease;
}

.remove-btn:hover {
  background: #FF416C;
  color: white;
}

.empty-favorite {
  text-align: center;
  padding: 50px 0;
}

.empty-favorite .material-icons {
  font-size: 64px;
  color: #ccc;
  margin-bottom: 20px;
}

.empty-favorite p {
  color: #666;
  margin-bottom: 20px;
}

.continue-shopping {
  background: #FF416C;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 10px 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.continue-shopping:hover {
  background: #FF2E5B;
}

.add-to-cart {
  background: #FF416C;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 8px 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
  transition: all 0.3s ease;
}

.add-to-cart:hover {
  background: #ff1f4b;
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

  background: #e0e0e0;
}

.empty-favorite {
  text-align: center;
  padding: 50px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.empty-favorite .material-icons {
  font-size: 4rem;
  color: #FF416C;
  margin-bottom: 20px;
}

.empty-favorite p {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 20px;
}

.continue-shopping {
  background: #FF416C;
  color: white;
  border: none;
  padding: 10px 30px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1.1rem;
  transition: background 0.3s ease;
}

.continue-shopping:hover {
  background: #ff1f4b;
}

.login-prompt {
  text-align: center;
  padding: 50px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  margin: 40px auto;
  max-width: 400px;
}

.login-prompt p {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 20px;
}

.login-prompt button {
  background: #FF416C;
  color: white;
  border: none;
  padding: 10px 30px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1.1rem;
  transition: background 0.3s ease;
}

.login-prompt button:hover {
  background: #ff1f4b;
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
}

.nav-links {
  display: flex;
  gap: 2.5rem;
}

.nav-link {
  color: #333;
  text-decoration: none;
  font-size: 1.1rem;
  font-weight: 500;
  transition: color 0.3s ease;
}

.nav-link:hover {
  color: #FF416C;
}

.nav-actions {
  display: flex;
  gap: 2rem;
  align-items: center;
}

.nav-actions a {
  color: #666;
  text-decoration: none;
  transition: color 0.3s ease;
}

.nav-actions a:hover {
  color: #FF416C;
}

.nav-actions .material-icons {
  font-size: 1.5rem;
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

.search-icon {
  position: absolute;
  right: 12px;
  color: #666;
  cursor: pointer;
}
</style>