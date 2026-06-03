import api from './index';

export const cart = {
  // 获取购物车列表
  getCartItems(userId) {
    return api.get(`/user/cart/list/${userId}`);
  },

  // 添加商品到购物车
  addToCart(userId, productId, quantity) {
    return api.post('/user/cart/add', { userId, productId, quantity });
  },

  // 更新购物车商品数量
  updateCartItem(userId, cartItemId, quantity) {
    return api.put(`/user/cart/update?userId=${userId}&cartItemId=${cartItemId}&quantity=${quantity}`);
  },


  // 删除购物车商品
  removeFromCart(userId,cartItemId) {
    return api.delete(`/user/cart/remove?userId=${userId}&cartItemId=${cartItemId}`);
  },

  // 清空购物车
  clearCart() {
    return api.delete('/user/cart/clear');
  },

  // 获取购物车商品总数
  getCartItemCount() {
    return api.get('/user/cart/count');
  }
};