<template>
  <div class="profile-container">
    <nav class="nav-bar">
      <div class="nav-content">
        <div class="logo" @click="router.push('/')">iMall</div>
        <div class="nav-links">
          <a href="#" @click.prevent="router.push('/')">Home</a>
          <a href="#" @click.prevent="router.push('/laptop')">Laptop</a>
          <a href="#" @click.prevent="router.push('/phone')">Phone</a>
          <a href="#" @click.prevent="router.push('/tablet')">Tablet</a>
          <a href="#" @click.prevent="router.push('/watch')">SmartWatch</a>
          <a href="#" @click.prevent="router.push('/headphone')">HeadPhone</a>
        </div>
        <div class="nav-actions">
          <div class="search-box">
            <input type="text" placeholder="Search..." class="search-input">
            <span class="material-icons search-icon">search</span>
          </div>
          <a href="#" @click.prevent="router.push('/favorite')">
            <span class="material-icons">favorite</span>
          </a>
          <a href="#" @click.prevent="router.push('/cart')">
            <span class="material-icons">shopping_cart</span>
          </a>
          <a href="#" class="active">
            <span class="material-icons">person</span>
          </a>
        </div>
      </div>
    </nav>

    <main class="main-content">
      <div class="profile-grid">
        <div class="sidebar">
          <div class="menu-item" 
               v-for="(item, index) in menuItems" 
               :key="index"
               :class="{ active: currentTab === item.id }"
               @click="currentTab = item.id">
            <span class="material-icons">{{ item.icon }}</span>
            {{ item.name }}
        </div>
      </div>

      <div class="content-area">
          <div v-if="currentTab === 'profile'" class="profile-section">
            <h2>Personal Information</h2>
            <div class="avatar-section">
              <div class="avatar-container">
                <img :src="userInfo.user_avatar || '/default-avatar.png'" alt="User Avatar"  v-if="userInfo.user_avatar">
                <div class="avatar-overlay" @click="triggerFileInput">
                  <span class="material-icons">photo_camera</span>
                  <span>Change avatar</span>
              </div>
            </div>
              <input 
                type="file" 
                ref="fileInput" 
                style="display: none" 
                accept="image/*"
                @change="handleAvatarUpload"
              >
            </div>
            <form class="profile-form" @submit.prevent="updateProfile">
              <div class="form-group" >
                <label>Username</label>
                <input type="text" v-model="userInfo.user_name">
              </div>
              <div class="form-group"  >
                <label>Email</label>
                <input type="email" v-model="userInfo.user_email" disabled >
              </div>
              <div class="form-group">
                <label>Phone number</label>
                <input type="tel" v-model="userInfo.user_phone">
              </div>
              <div class="form-group">
                <label>Gender</label>
                <select v-model="userInfo.user_gender">
                  <option value="male">male</option>
                  <option value="female">female</option>
                  <option value="other">other</option>
                </select>
            </div>
              <div class="form-group">
                <label>birthday</label>
                <input type="date" v-model="userInfo.user_birthday">
          </div>
              <button type="submit" class="save-btn">Save</button>
            </form>
        </div>

          <!-- Order -->
          <div v-if="currentTab === 'orders'" class="orders-section">
            <h2>My Order</h2>
            <div class="order-tabs">
              <span 
                v-for="(tab, index) in orderTabs" 
                :key="index"
                :class="{ active: currentOrderTab === tab.id }"
                @click="currentOrderTab = tab.id"
              >
                {{ tab.name }}
              </span>
          </div>
            <div class="order-list">
              <div v-for="order in filteredOrders" :key="order.id" class="order-card">
                <div class="order-header">
                  <span class="order-id">OrderNo：{{ order.order_no }}</span>
                  <span class="order-date">{{ order.create_time }}</span>
                  <span class="order-status">
                    {{
                      order.pay_status === 0 ? 'Pending Payment' :
                          order.order_status === 0 ? 'Processing' :
                              order.order_status === 1 ? 'Shipped' :
                                  order.order_status === 2 ? 'Completed' :
                                      'Unknown Status'
                    }}
                  </span>
                </div>
                <div class="order-items">
                  <div v-for="item in orderItemsMap[order.order_no]" :key="item.id" class="order-item">
                    <img :src="item.product_image" :alt="item.product_name" class="item-image">
                    <div class="item-info">
                      <h4>{{ item.product_name }}</h4>
                      <p>{{ item.specs }}</p>
                      <span class="item-price">¥{{ item.product_price }}</span>
                      <span class="item-quantity">x{{ item.quantity }}</span>
                    </div>
                  </div>
                </div>
                <div class="order-footer">
                  <span class="order-total">TotalAmount：¥{{ order.total_amount }}</span>
                  <div class="order-actions">
                    <button v-if="order.pay_status === 0" @click="payOrder(order.order_no)">
                      PayNow
                    </button>
                    <button
                        v-else-if="order.order_status === 1"
                        @click="confirmReceive(order.order_no)"
                        class="confirm-receive-btn"
                    >
                      Confirm Receipt
                    </button>

                    <button v-else-if="order.order_status === 2" @click="reviewOrder(order.order_no)">
                      Write a Review
                    </button>
              </div>
            </div>
          </div>
          </div>
        </div>

          <div v-if="currentTab === 'address'" class="address-section">
            <h2>Shipping Address</h2>
            <div class="address-list">
              <div v-for="address in addresses" :key="address.id" class="address-card">
              <div class="address-info">
                <div class="address-header">
                    <span class="receiver">{{ address.consignee }}</span>
                    <span class="phone">{{ address.phone }}</span>
                    <span v-if="address.is_default" class="default-tag">Default</span>
                </div>
                  <div class="address-detail">
                    {{ address.province }} {{ address.city }} {{ address.district }}
                    {{ address.detail }}
                  </div>
              </div>
              <div class="address-actions">
                <button @click="editAddress(address)">Edit</button>
                <button @click="deleteAddress(address.id)">Delete</button>
                <button v-if="!address.isDefault" @click="setDefaultAddress(address.id)">
                  Set as Default
                </button>
              </div>
            </div>
              <div class="add-address" @click="showAddressForm = true">
                <span class="material-icons">add</span>
                <span>Add Shipping Address</span>
          </div>
          </div>

            <div v-if="showAddressForm" class="modal">
              <div class="modal-content">
                <h3>{{ isEditMode ? 'Edit Shipping Address' : 'Add Shipping Address' }}</h3>
                <form @submit.prevent="saveAddress" class="address-form">
                  <div class="form-group">
                    <label>Recipient Name</label>
                    <input type="text" v-model="addressForm.consignee" placeholder="Enter recipient name">
                  </div>
                  <div class="form-group">
                    <label>Phone Number</label>
                    <input type="tel" v-model="addressForm.phone" placeholder="Enter phone number">
                  </div>
                  <div class="form-group">
                    <label>Region</label>
                    <div class="area-select">
                      <textarea v-model="addressForm.province" placeholder="Enter state/province"></textarea>
                      <textarea v-model="addressForm.city" placeholder="Enter city"></textarea>
                      <textarea v-model="addressForm.district" placeholder="Enter district"></textarea>
                    </div>
                  </div>
                  <div class="form-group">
                    <label>Detailed Address</label>
                    <textarea v-model="addressForm.detail" placeholder="Enter detailed address information, such as street, building number, apartment, unit, etc"></textarea>
                  </div>
                  <div class="form-group checkbox">
                    <input type="checkbox" v-model="addressForm.is_default" id="defaultAddress">
                    <label for="defaultAddress">Set as default shipping address</label>
                  </div>
                  <div class="modal-actions">
                    <button type="button" @click="cancelEdit">Cancel</button>
                    <button type="submit">{{ isEditMode ? 'Save Changes' : 'Save' }}</button>
                  </div>
                </form>
              </div>
            </div>

          </div>

          <!-- Account Security -->
          <div v-if="currentTab === 'security'" class="security-section">
            <h2>Account Security</h2>
            <div class="security-items">
              <div class="security-item">
                <div class="security-info">
                  <h3>Login Password</h3>
                  <p>It is recommended to change your password regularly to keep your account secure</p>
                </div>
                <button @click="showChangePassword = true">Change</button>
              </div>
              <div class="security-item">
                <div class="security-info">
                  <h3>Phone Number</h3>
                  <p>Bound: {{ userInfo.phone }}</p>
                </div>
                <button @click="showChangePhone = true">Change</button>
              </div>
              <div class="security-item">
                <div class="security-info">
                  <h3>Email Address</h3>
                  <p>Bound: {{ userInfo.email }}</p>
                </div>
                <button @click="showChangeEmail = true">Change</button>
              </div>
            </div>
      </div>

          <!-- My Devices -->
          <div v-if="currentTab === 'devices'" class="devices-section">
            <h2>My Devices</h2>
            <div class="devices-list">
              <div v-for="device in devices" :key="device.id" class="device-card">
                <div class="device-info">
                  <h3>{{ device.device_name }}</h3>
                  <p>{{ device.device_type }}</p>
                  <p>{{ device.device_model }}</p>
                  <p>Added on:{{ device.device_add_time }}</p>
                </div>
                <div class="device-actions">
                  <button @click="editDevice(device)">Edit</button>
                  <button @click="deleteDevice(device.id)">Delete</button>
                </div>
              </div>
              <div class="add-device" @click="showDeviceForm = true">
                <span class="material-icons">add</span>
                <span>Add New Device</span>
              </div>
            </div>

            <!-- Add/Edit Device Form -->
            <div v-if="showDeviceForm" class="modal">
              <div class="modal-content">
                <h3>{{ editingDevice ? 'Edit Device' : 'Add New Device' }}</h3>
                <form @submit.prevent="saveDevice" class="device-form">
          <div class="form-group">
            <label>Device Name</label>
                    <input type="text" v-model="deviceForm.device_name" placeholder="Enter device name">
          </div>
          <div class="form-group">
            <label>Device Type</label>
                    <select v-model="deviceForm.device_type">
                      <option value="">Select device type</option>
                      <option value="phone">Phone</option>
              <option value="tablet">Tablet</option>
                      <option value="laptop">Laptop</option>
              <option value="watch">Smart Watch</option>
                      <option value="headphone">Headphones</option>
            </select>
          </div>
                  <div class="form-group">
                    <label>Device Model</label>
                    <input type="text" v-model="deviceForm.device_model" placeholder="Enter device model">
                  </div>
                  <div class="modal-actions">
                    <button type="button" @click="showDeviceForm = false">Cancel</button>
                    <button type="submit">Save</button>
                  </div>
                </form>
          </div>
          </div>
          </div>
        </div>
      </div>
    </main>

    <!-- Change Password Dialog -->
    <div v-if="showChangePassword" class="modal">
      <div class="modal-content">
        <h3>Change Password</h3>
        <form @submit.prevent="changePassword">
          <div class="form-group">
            <label>Current Password</label>
            <input type="password" v-model="passwordForm.oldPassword">
          </div>
          <div class="form-group">
            <label>New Password</label>
            <input type="password" v-model="passwordForm.newPassword">
          </div>
          <div class="form-group">
            <label>Confirm New Password</label>
            <input type="password" v-model="passwordForm.confirmPassword">
          </div>
          <div class="modal-actions">
            <button type="button" @click="showChangePassword = false">Cancel</button>
            <button type="submit">Confirm</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

import {user} from '../api/user.js'
import {device} from '../api/devices.js'
import {order} from '../api/order.js'

export default {
  name: 'UserProfile',
  setup() {

    const router = useRouter()
    const store = useStore()
    const fileInput = ref(null)
    const isEditMode = ref(false)
    const items = []

    const menuItems = [
      {id: 'profile', name: 'Profile', icon: 'person'},
      {id: 'orders', name: 'My Orders', icon: 'shopping_bag'},
      {id: 'address', name: 'Shipping Address', icon: 'location_on'},
      {id: 'security', name: 'Account Security', icon: 'security'},
      {id: 'devices', name: 'My Devices', icon: 'devices'}
    ]

// OrderTabs
    const orderTabs = [
      {id: 'all', name: 'All'},
      {id: 'unpaid', name: 'Pending Payment'},
      {id: 'unshipped', name: 'Pending Shipment'},
      {id: 'shipped', name: 'In Transit'},
      {id: 'completed', name: 'Completed'}
    ]

    // State Management
    const currentTab = ref('profile')
    const currentOrderTab = ref('all')
    const showChangePassword = ref(false)
    const showAddressForm = ref(false)
    const showDeviceForm = ref(false)
    const editingDevice = ref(null)

    const editAddress = (address) => {
      addressForm.value = { ...address }
      isEditMode.value = true
      showAddressForm.value = true
    }

    const cancelEdit = () => {
      resetForm()
      showAddressForm.value = false
    }

    const resetForm = () => {
      addressForm.value = {
        id: null,
        consignee: '',
        phone: '',
        province: '',
        city: '',
        district: '',
        detail: '',
        is_default: false
      }
      isEditMode.value = false
    }

    const devices = ref([])
    const deviceForm = ref({
      user_id: store.getters.userId,
      device_name: '',
      device_type: '',
      device_model: ''
    })

    const fetchDevices = async () => {
      try {
        const response = await device.getDeviceItems(store.getters.userId)
        if (response) {
          devices.value = response
          console.log(device.value)
        }
      } catch (error) {
        console.error('Failed to fetch device list:', error)
      }
    }

    const saveDevice = async () => {
      if (!deviceForm.value.device_name || !deviceForm.value.device_type || !deviceForm.value.device_model) {
        alert('Please fill in all device information')
        return
      }

      const user_device = {
        id: editingDevice.value?.id,
        device_name: deviceForm.value.device_name,
        device_type: deviceForm.value.device_type,
        device_model: deviceForm.value.device_model
      }

      try {
        let response
        if (editingDevice.value) {
          response = await device.updateUserDevice(user_device)
        } else {
          response = await device.addUserDevice(deviceForm.value)
        }

        if (response) {
          alert(editingDevice.value ? 'Device updated successfully' : 'Device added successfully')
          showDeviceForm.value = false
          resetDeviceForm()
          await fetchDevices()
        }
      } catch (error) {
        console.error('Failed to save device:', error)
        alert('Failed to save device. Please try again later.')
      }
    }

    const editDevice = (device) => {
      editingDevice.value = device
      deviceForm.value = {
        device_name: device.device_name,
        device_type: device.device_type,
        device_model: device.device_model
      }
      showDeviceForm.value = true
    }

    const resetDeviceForm = () => {
      deviceForm.value = {
        device_name: '',
        device_type: '',
        device_model: ''
      }
      editingDevice.value = null
    }

    const deleteDevice = async (deviceId) => {
      if (!confirm('Are you sure you want to delete this device?')) return

      try {
        const response = await device.removeUserDevice(deviceId)

        if (response) {
          alert('Device deleted successfully')
          await fetchDevices()
        }
      } catch (error) {
        console.error('Failed to delete device:', error)
        alert('Failed to delete device. Please try again later.')
      }
    }

    const userInfo = ref({
      user_name: '',
      user_email: '',
      user_phone: '',
      user_gender: '',
      user_birthday:'',
      user_avatar:''
    })

    const passwordForm = ref({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    })

    const orders = ref([])
    const addresses = ref([])
    const orderItemsMap = ref({})

    const fetchOrders = async () => {
      try {
        const response = await order.getOrderItems(store.getters.userId)
        orders.value = response

        for (const ord of response) {
          await fetchOrderItems(ord.order_no)
        }
      } catch (error) {
        console.error('Failed to retrieve orders', error)
      }
    }

    // 根据订单号获取对应的订单项
    const fetchOrderItems = async (orderNo) => {
      try {
        const res = await order.getOrderItemsByOrderId(orderNo)
        orderItemsMap.value[orderNo] = res
      } catch (error) {
        console.error(`Failed to fetch products for order ${orderNo}`, error)
      }
    }

    const payOrder = async (orderId) => {
      try {
        const response = await order.pay(orderId);

        if (response.success) {
          console.log('Payment successful', response);
          fetchOrders();
        } else {
          console.error('Payment failed', response.message);
        }
      } catch (error) {
        console.error('Payment failed', error);
      }
    }

    const confirmReceive = async (orderId) => {
      try {
        console.log(`Confirming receipt for order ID: ${orderId}`);
        const response = await order.confirmReceive(orderId);
        if (response) {
          alert("Order received successfully")
          console.log("Order received successfully", response);

          fetchOrders(); // Refresh the order list
        } else {
          console.error("Failed to confirm receipt", response.message);
        }
      } catch (error) {
        console.error("Failed to confirm receipt", error);
      }
    };

    const filteredOrders = computed(() => {
      if (currentOrderTab.value === "all") {
        return orders.value;
      }else if (currentOrderTab.value === 'unpaid') {
        return orders.value.filter(order => order.pay_status === 0)
      }else if (currentOrderTab.value === 'unshipped') {
        return orders.value.filter(order => order.pay_status === 1 && order.order_status === 0)
      }else if (currentOrderTab.value === 'shipped') {
        return orders.value.filter(order => order.pay_status === 1 && order.order_status === 1)
      }else if (currentOrderTab.value === 'completed') {
        return orders.value.filter(order => order.pay_status === 1 && order.order_status === 2)
      }
    });

    const addressForm = ref({
      consignee: '',
      phone: '',
      province: '',
      city: '',
      district: '',
      detail: '',
      is_default: false
    })
    const validateAddress = () => {
      if (!addressForm.value.consignee) {
        alert('Please enter the recipient name')
        return false
      }

      if (!/^1[3-9]\d{9}$/.test(addressForm.value.phone)) {
        alert('Please enter a valid phone number')
        return false
      }

      if (
          !addressForm.value.province ||
          !addressForm.value.city ||
          !addressForm.value.district
      ) {
        alert('Please complete the state, city, and district information')
        return false
      }

      if (!addressForm.value.detail) {
        alert('Please enter the detailed address')
        return false
      }

      return true
    }

    const fetchAddresses = async () => {
      try {
        const response = await user.getUserAddress(store.getters.userId)
        addresses.value = response
      } catch (error) {
        console.error('Failed to fetch address list:', error)
      }
    }

    const saveAddress = async () => {
      if (!validateAddress()) return

      const hasDefault = addresses.value.some(
          addr => addr.is_default && addr.id !== addressForm.value.id
      )

      if (hasDefault && addressForm.value.is_default) {
        alert('A default address already exists. Multiple default addresses are not allowed')
        return
      }

      const user_address = {
        user_id: store.getters.userId,
        consignee: addressForm.value.consignee,
        phone: addressForm.value.phone,
        province: addressForm.value.province,
        city: addressForm.value.city,
        district: addressForm.value.district,
        detail: addressForm.value.detail,
        is_default: addressForm.value.is_default,
      }

      try {
        let response
        if (isEditMode.value) {
          response = await user.updateUserAddress(addressForm.value)
        } else {
          response = await user.saveUserAddress(user_address)
        }

        if (response) {
          alert(isEditMode.value ? 'Address updated successfully' : 'Address saved successfully')
          showAddressForm.value = false
          resetForm()
          await fetchAddresses()
        }
      } catch (error) {
        console.error('Failed to save address:', error)
        alert('Failed to save address. Please try again later.')
      }
    }

    const setDefaultAddress = async (addressId) => {
      try {
        const currentDefault = addresses.value.find(a => a.is_default)

        if (currentDefault) {
          alert('A default address already exists')
          return
        }

        const response = await user.setDefaultAddress(addressId)

        if (!response) {
          alert('Default address set successfully')
          await fetchAddresses()
        } else {
          alert('Failed to set default address')
        }
      } catch (error) {
        console.error('Failed to set default address:', error)
        alert('Failed to set default address. Please try again later.')
      }
    }

    const deleteAddress = async (addressId) => {
      if (!confirm('Are you sure you want to delete this address?')) return

      try {
        const response = await user.deleteUserAddress(addressId)
        if (!response) {
          await fetchAddresses()
        }
      } catch (error) {
        console.error('Failed to delete address:', error)
        alert('Failed to delete address. Please try again later.')
      }
    }

    const fetchUserInfo = async () => {
      try {
        const data = await user.getUserById(store.getters.userId)
        userInfo.value = {
          user_name: data.user_name,
          user_email: data.user_email,
          user_phone: data.user_phone,
          user_birthday:data.user_birthday,
          user_gender: data.user_gender,
          user_avatar: "http://localhost:8087"+data.user_avatar
        }
      } catch (error) {
        console.error('Failed to fetch user information:', error)
      }
    }

    const updateProfile = async () => {
      try {
        console.log(userInfo.user_email)
        const response = await user.updateUser(store.getters.userId,
            {
              user_name: userInfo.value.user_name,
              user_email:userInfo.value.user_email,
              user_phone:userInfo.value.user_phone,
              user_birthday:userInfo.value.user_birthday,
              user_gender: userInfo.value.user_gender
            }
        )
        if (response.ok) {
          alert('Profile updated successfully')
        }
      } catch (error) {
        console.error('Failed to update user information:', error)
      }
    }

    const triggerFileInput = () => {
      fileInput.value.click()
    }

    const handleAvatarUpload = async (event) => {
      const file = event.target.files[0]
      if (!file) return

      const formData = new FormData()
      formData.append('user_avatar', file)

      try {
        const response = await user.getUserAvatar(
            store.getters.userId,
            formData
        )
        console.log(response);
        console.log(response.user_avatar);
        userInfo.value.user_avatar = 'http://localhost:8087' + response.user_avatar
        console.log(userInfo.value.user_avatar);
        alert('Avatar uploaded successfully')
      } catch (error) {
        console.error('Failed to upload avatar:', error)
        alert('Upload failed. Please try again later.')
      }
    }


    const changePassword = async () => {
      if (passwordForm.value.newPassword.length < 6) {
        alert('The new password must be at least 6 characters long')
        return
      }

      if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(passwordForm.value.newPassword)) {
        alert('The new password must contain uppercase and lowercase letters, and numbers')
        return
      }

      if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
        alert('The passwords do not match')
        return
      }

      if (!passwordForm.value.oldPassword) {
        alert('Please enter your current password')
        return
      }
      try {
        const response = await fetch('http://localhost:5000/api/user/password', {
          method: 'PUT',
          headers: {
            'Authorization': `Bearer ${store.state.token}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            oldPassword: passwordForm.value.oldPassword,
            newPassword: passwordForm.value.newPassword
          })
        })
      if (response.ok) {
          alert('Password changed successfully')
          showChangePassword.value = false
          passwordForm.value = {
            oldPassword: '',
            newPassword: '',
            confirmPassword: ''
          }
      }
    } catch (error) {
        console.error('Failed to change password:', error)
      }
    }

    onMounted(() => {
      fetchUserInfo()
      fetchAddresses()
      fetchDevices()
      fetchOrders()
    })

    return {
      router,
      menuItems,
      orderTabs,
      currentTab,
      currentOrderTab,
      showChangePassword,
      showAddressForm,
      userInfo,
      passwordForm,
      orders,
      addresses,
      fileInput,
      addressForm,
      deleteDevice,
      devices,
      deviceForm,
      editDevice,
      fetchAddresses,
      showDeviceForm,
      fetchDevices,
      saveDevice,
      deleteAddress,
      validateAddress,
      triggerFileInput,
      handleAvatarUpload,
      updateProfile,
      changePassword,
      saveAddress,
      setDefaultAddress,
      editAddress,
      cancelEdit,
      fetchOrders,
      filteredOrders,
      fetchOrderItems,
      orderItemsMap,
      confirmReceive
    }
  }
}
</script>

<style scoped>
.devices-section {
  padding: 20px;
}

.devices-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.device-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}

.device-card:hover {
  transform: translateY(-2px);
}

.device-info h3 {
  margin: 0 0 10px;
  color: #333;
  font-size: 18px;
}

.device-info p {
  margin: 5px 0;
  color: #666;
  font-size: 14px;
}

.device-actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

.device-actions button {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.device-actions button:first-child {
  background-color: #4CAF50;
  color: white;
}

.device-actions button:last-child {
  background-color: #f44336;
  color: white;
}

.add-device {
  background: #f5f5f5;
  border: 2px dashed #ddd;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.add-device:hover {
  border-color: #4CAF50;
  color: #4CAF50;
}

.add-device .material-icons {
  font-size: 36px;
  margin-bottom: 8px;
}

.device-form {
  width: 100%;
  max-width: 500px;
}

.device-form .form-group {
  margin-bottom: 15px;
}

.device-form label {
  display: block;
  margin-bottom: 5px;
  color: #333;
}

.device-form input,
.device-form select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.device-form input:focus,
.device-form select:focus {
  border-color: #4CAF50;
  outline: none;
}

.profile-container {
  min-height: 100vh;
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
  padding: 100px 40px 60px;
  max-width: 1600px;
  margin: 0 auto;
}

.profile-grid {
  display: grid;
  grid-template-columns: 250px 1fr;
  gap: 30px;
}

.sidebar {
  background: white;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  height: fit-content;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 15px;
  cursor: pointer;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.menu-item:hover {
  background: #f8f9fa;
  color: #FF416C;
}

.menu-item.active {
  background: #FF416C;
  color: white;
}

.content-area {
  background: white;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.avatar-section {
  display: flex;
  justify-content: center;
  margin: 30px 0;
}

.avatar-container {
  position: relative;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
}

.avatar-container img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  opacity: 0;
  transition: opacity 0.3s ease;
  cursor: pointer;
}

.avatar-overlay:hover {
  opacity: 1;
}

.profile-form {
  max-width: 600px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #666;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #FF416C;
  box-shadow: 0 0 5px rgba(255, 65, 108, 0.3);
}

.save-btn {
  background: #FF416C;
  color: white;
  border: none;
  padding: 12px 30px;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  width: 100%;
}

.save-btn:hover {
  background: #FF4B2B;
  transform: translateY(-2px);
}

.order-tabs {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  border-bottom: 1px solid #eee;
  padding-bottom: 15px;
}

.order-tabs span {
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.order-tabs span.active {
  background: #FF416C;
  color: white;
}

.order-card {
  border: 1px solid #eee;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
  color: black;
}

.order-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.order-item {
  display: flex;
  gap: 20px;
  padding: 15px 0;
  color: black;
}

.order-item img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.address-card {
  border: 1px solid #eee;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.address-header {
  margin-bottom: 10px;
  color: #5e5e5e;
}
.address-info{
  color: #5e5e5e;
}

.default-tag {
  background: #FF416C;
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  margin-left: 10px;
}

.address-actions {
  display: flex;
  gap: 10px;
}

.add-address {
  border: 2px dashed #ddd;
  border-radius: 10px;
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.add-address:hover {
  border-color: #FF416C;
  color: #FF416C;
}

.address-section{
  color: black;
}
.address-form {
  max-width: 100%;
  margin: 0 auto;
}

.area-select {
  display: flex;
  gap: 10px;
}

.area-select select {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
}

.area-select select:focus {
  outline: none;
  border-color: #FF416C;
  box-shadow: 0 0 5px rgba(255, 65, 108, 0.3);
}

.form-group textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  min-height: 100px;
  resize: vertical;
}

.form-group textarea:focus {
  outline: none;
  border-color: #FF416C;
  box-shadow: 0 0 5px rgba(255, 65, 108, 0.3);
}
.devices-section{
  color:black;
}
.form-group.checkbox {
  display: flex;
  align-items: center;
  gap: 10px;
}

.form-group.checkbox input[type="checkbox"] {
  width: auto;
}

.form-group.checkbox label {
  margin: 0;
  cursor: pointer;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 20px;
}

.modal-actions button {
  padding: 10px 25px;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.modal-actions button[type="button"] {
  background: #f5f5f5;
  border: 1px solid #ddd;
  color: #666;
}

.modal-actions button[type="submit"] {
  background: #FF416C;
  border: none;
  color: white;
}

.profile-section{
  color: black;
}

.modal-actions button:hover {
  transform: translateY(-2px);
}

.modal-actions button[type="button"]:hover {
  background: #e5e5e5;
}

.modal-actions button[type="submit"]:hover {
  background: #FF4B2B;
}
.address-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(580px, 1fr));
  gap: 20px;
  padding: 20px;
}

.address-card {
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  padding: 16px;
  transition: all 0.3s ease;
}

.address-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.address-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.receiver {
  font-weight: 600;
  font-size: 16px;
  color: #333;
}

.phone {
  margin-left: 12px;
  color: #666;
  font-size: 14px;
}

.default-tag {
  background-color: #10b981;
  color: white;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.address-detail {
  color: #444;
  font-size: 14px;
  margin-top: 4px;
  line-height: 1.5;
}

.address-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 12px;
}

.address-actions button {
  padding: 6px 12px;
  border-radius: 8px;
  border: none;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.edit-btn {
  background-color: #3b82f6;
  color: white;
}

.edit-btn:hover {
  background-color: #2563eb;
}

.delete-btn {
  background-color: #ef4444;
  color: white;
}

.delete-btn:hover {
  background-color: #dc2626;
}

.default-btn {
  background-color: #6b7280;
  color: white;
}

.default-btn:hover {
  background-color: #4b5563;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.security-info h3 {
  margin-bottom: 5px;
}

.security-info p {
  color: #666;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 15px;
  width: 90%;
  max-width: 500px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 20px;
}

.orders-section{
  color: black;
}

.confirm-receive-btn {
  color: black;
  padding: 12px 20px;
  font-size: 16px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.confirm-receive-btn:hover {
  background-color: white;
}

.confirm-receive-btn:focus {
  outline: none;
}
@media (max-width: 1200px) {
  .profile-grid {
    grid-template-columns: 200px 1fr;
  }
}

@media (max-width: 900px) {
  .profile-grid {
    grid-template-columns: 1fr;
  }
  
  .sidebar {
  display: flex;
    overflow-x: auto;
    padding: 10px;
  }
  
  .menu-item {
    white-space: nowrap;
  }
}

img {
  max-width: 100%;
  height: auto;
  display: block;
}

@media (max-width: 600px) {
  .main-content {
    padding: 80px 20px 40px;
  }
  
  .order-tabs {
    overflow-x: auto;
    padding-bottom: 10px;
  }
  
  .order-item {
    flex-direction: column;
  }
  
  .address-card {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .address-actions {
    margin-top: 15px;
  }
}
</style>