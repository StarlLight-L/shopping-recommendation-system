import api from './index';

export const user = {
  // 用户登录
  login(loginData) {
    return api.post('/auth/login', loginData);
  },

  // 用户注册
  register(user) {
    return api.post('/auth/register', user);
  },

  // 更新用户信息
  updateUser(id, user) {
    return api.put(`/users/${id}`, user);
  },

  // 删除用户
  deleteUser(id) {
    return api.delete(`/users/${id}`);
  },

  // 获取用户详情
  getUserById(id) {
    return api.get(`/users/${id}`);
  },

  // 获取所有用户
  getAllUsers() {
    return api.get('/users');
  },

  //上传用户头像
  getUserAvatar(id, user_avatar) {
    return api.put(`/users/${id}/avatar`, user_avatar, {
      headers: {
        'Content-Type': 'multipart/form-data', // 确保内容类型为 multipart/form-data
      },
    });
  },

  //获取地址列表
  getUserAddress(id){
    return api.get(`/users/address/list/${id}`);
  },
  //保存地址列表
  saveUserAddress(user_address){
    return api.post(`/users/address/insert/`,user_address);
  },
  //删除地址
  deleteUserAddress(address_id){
    return api.delete(`/users/address/delete/${address_id}`);
  },
  //设为默认地址
  setDefaultAddress(address_id){
    return api.put(`/users/address/default/${address_id}`)
  },
  //修改地址
  updateUserAddress(address_id){
    return api.post(`/users/address/update/`,address_id);
  },

  googleLogin(user) {
    return api.post('/auth/google-login',user);
  }
};