<template>
  <div class="product-list">
    <div class="product-grid">
      <div v-for="product in products" :key="product.id" class="product-card" @click="viewProductDetail(product.id)">
        <div class="product-image">
          <img :src="product.image_url ? `/pictures/${product.image_url}` : '/default-product.png'" :alt="product.name" @error="handleImageError">
        </div>
        <div class="product-info">
          <h3>{{ product.name }}</h3>
          <p>{{ product.description }}</p>
          <span class="price">¥{{ product.price }}</span>
          <button class="buy-btn" @click.stop="addToCart(product)">加入购物车</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  name: 'ProductList',
  setup() {
    const router = useRouter();
    const products = ref([]);

    const fetchProducts = async () => {
      try {
        const response = await axios.get('http://localhost:5000/api/products');
        products.value = response.data.data;
      } catch (error) {
        console.error('Failed to fetch product list:', error);
      }
    };

    const viewProductDetail = (productId) => {
      router.push(`/product/${productId}`);
    };

    const addToCart = async (product) => {
      try {
        // 这里可以添加购物车逻辑
        console.log('Add to cart:', product);
      } catch (error) {
        console.error('Failed to add to cart:', error);
      }
    };

    onMounted(() => {
      fetchProducts();
    });

    return {
      products,
      viewProductDetail,
      addToCart
    };
  }
};
</script>

<style scoped>
.product-list {
  padding: 20px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.product-card {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s ease;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 15px;
}

.product-info h3 {
  margin: 0 0 10px;
  font-size: 1.1em;
}

.product-info p {
  margin: 0 0 10px;
  color: #666;
  font-size: 0.9em;
}

.price {
  display: block;
  font-size: 1.2em;
  color: #e53935;
  margin-bottom: 10px;
}

.buy-btn {
  width: 100%;
  padding: 8px;
  background-color: #1976d2;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.buy-btn:hover {
  background-color: #1565c0;
}
</style>