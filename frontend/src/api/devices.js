import api from './index';

export const device = {
  //获取设备列表
  getDeviceItems(userId) {
    return api.get(`/user/device/list/${userId}`);
  },

  //保存设备
  addUserDevice(deviceItem) {
    return api.post('/user/device/add/', deviceItem);
  },

  //删除设备
  removeUserDevice(deviceId) {
    return api.delete(`/user/device/delete/${deviceId}`);
  },

  //编辑设备
  updateUserDevice(deviceItem) {
     return api.post(`/user/device/update/`,deviceItem);
  }
};