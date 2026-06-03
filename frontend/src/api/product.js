import api from './index';

export const product = {
  // 获取商品列表
  getProducts() {
    return api.get('/products');
  },

  // 获取商品详情
  getProductById(id) {
    return api.get(`/products/${id}`);
  },

  // 获取商品分类列表
  getCategories() {
    return api.get('/products/categories');
  },

  // 获取分类下的商品
  getProductsByCategory(category, params) {
    return api.get(`/products/category/${category}`, params);
  },

  // 获取推荐商品
  getRecommendedProducts(categoryId) {
    return api.get(`/products/recommended`, {
      params: { categoryId }
    });
  },

  // 搜索商品
  searchProducts(keyword) {
    return api.get('/products/search', {
      params: { keyword }
    });
  }
};