<template>
  <div class="orders-container">
    <!-- Order Filter -->
    <div class="filter-section">
      <div class="search-box">
        <input type="text" v-model="searchQuery" placeholder="Search ID..." @input="handleSearch">
        <span class="material-icons">search</span>
      </div>
      <div class="status-filter">
        <select v-model="statusFilter" @change="handleStatusFilter">
          <option value="all">Order</option>
          <option value="pending">Pending Payment</option>
          <option value="paid">Paid</option>
          <option value="shipping">In Transit</option>
          <option value="delivered">Delivered</option>
          <option value="completed">Completed</option>
        </select>
      </div>
    </div>

    <!-- Order List -->
    <div class="orders-list" v-if="filteredOrders.length > 0">
      <div class="order-item" v-for="order in filteredOrders" :key="order.id">
        <div class="order-header">
          <div class="order-info">
            <span class="order-id">ID：{{ order.id }}</span>
            <span class="order-date">{{ formatDate(order.createdAt) }}</span>
          </div>
          <div class="order-status" :class="order.status">
            {{ getStatusText(order.status) }}
          </div>
        </div>
        <div class="order-products">
          <div class="product-item" v-for="product in order.products" :key="product.id">
            <img :src="product.image" :alt="product.name">
            <div class="product-info">
              <h4>{{ product.name }}</h4>
              <p class="product-price">¥{{ product.price }}</p>
              <p class="product-quantity">x{{ product.quantity }}</p>
            </div>
          </div>
        </div>
        <div class="order-footer">
          <div class="order-total">
            <span>Total Price：</span>
            <span class="total-price">¥{{ order.totalAmount }}</span>
          </div>
          <div class="order-actions">
            <button class="view-details" @click="viewOrderDetails(order)">
              View Details
            </button>
            <button v-if="order.status === 'shipping'" class="track-order" @click="trackOrder(order)">
              Track Order
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div class="empty-orders" v-else>
      <span class="material-icons">receipt_long</span>
      <p>No orders yet</p>
    </div>

    <!-- Order Details Dialog -->
    <div class="modal" v-if="showOrderDetails">
      <div class="modal-content">
        <div class="modal-header">
          <h3>Order Details</h3>
          <span class="material-icons close" @click="closeOrderDetails">close</span>
        </div>
        <div class="modal-body">
          <div class="order-timeline">
            <div class="timeline-item" v-for="(status, index) in selectedOrder.timeline" :key="index">
              <div class="timeline-icon" :class="{ active: status.completed }">
                <span class="material-icons">{{ status.icon }}</span>
              </div>
              <div class="timeline-content">
                <h4>{{ status.title }}</h4>
                <p>{{ status.time || 'Processing' }}</p>
                <p class="description">{{ status.description }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

// Status and Filters
const orders = ref([])
const searchQuery = ref('')
const statusFilter = ref('all')
const showOrderDetails = ref(false)
const selectedOrder = ref(null)

// Fetch order list
const fetchOrders = async () => {
  try {
    const response = await fetch('/api/orders', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    const data = await response.json()
    if (response.ok) {
      orders.value = data.map(order => ({
        ...order,
        timeline: [
          {
            title: 'Order Placed',
            time: order.createdAt,
            description: `Order Amount: ¥${order.totalAmount}`,
            icon: 'receipt',
            completed: true
          },
          {
            title: 'Payment Completed',
            time: order.paidAt,
            description: 'Waiting for shipment',
            icon: 'payment',
            completed: order.status !== 'pending'
          },
          {
            title: 'Order Shipped',
            time: order.shippedAt,
            description: order.trackingNumber
                ? `Tracking Number: ${order.trackingNumber}`
                : 'Waiting for shipment',
            icon: 'local_shipping',
            completed: ['shipping', 'delivered', 'completed'].includes(order.status)
          },
          {
            title: 'Order Completed',
            time: order.completedAt,
            description: 'The product has been delivered',
            icon: 'check_circle',
            completed: ['delivered', 'completed'].includes(order.status)
          }
        ]
      }))
    } else {
      throw new Error(data.message || '获取订单列表失败')
    }
  } catch (error) {
    ElMessage.error(error.message || '获取订单列表失败')
  }
}

// filter Order
const filteredOrders = computed(() => {
  return orders.value.filter(order => {
    const matchesSearch = order.id.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesStatus = statusFilter.value === 'all' || order.status === statusFilter.value
    return matchesSearch && matchesStatus
  })
})

const handleSearch = () => {
}

const handleStatusFilter = () => {
}

// view Order Details
const viewOrderDetails = (order) => {
  selectedOrder.value = order
  showOrderDetails.value = true
}

// close Order Details
const closeOrderDetails = () => {
  showOrderDetails.value = false
  selectedOrder.value = null
}

// Track Order
const trackOrder = async (order) => {
  try {
    const response = await fetch(`/api/orders/${order.id}/tracking`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    const data = await response.json()
    if (response.ok) {
      ElMessage.success('Tracking information retrieved successfully')
    } else {
      throw new Error(data.message || 'Failed to fetch tracking information')
    }
  } catch (error) {
    ElMessage.error(error.message || 'Failed to fetch tracking information')
  }
}

// format Data
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// get status Text
const getStatusText = (status) => {
  const statusMap = {
    pending: 'Pending',
    paid: 'Paid',
    shipping: 'Shipped',
    delivered: 'Delivered',
    completed: 'Completed'
  }
  return statusMap[status] || status
}

fetchOrders()
</script>

<style scoped>
.orders-container {
  padding: 1rem;
}

.filter-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.search-box {
  position: relative;
  width: 300px;
}

.search-box input {
  width: 100%;
  padding: 0.5rem 2rem 0.5rem 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  outline: none;
}

.search-box .material-icons {
  position: absolute;
  right: 0.5rem;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
}

.status-filter select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  outline: none;
  cursor: pointer;
}

.orders-list {
  display: grid;
  gap: 1rem;
}

.order-item {
  background: #fff;
  border-radius: 8px;
  padding: 1rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #eee;
}

.order-info {
  display: flex;
  gap: 1rem;
}

.order-id {
  font-weight: bold;
}

.order-date {
  color: #666;
}

.order-status {
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.875rem;
}

.order-status.pending { background: #fff1f0; color: #ff4d4f; }
.order-status.paid { background: #e6f7ff; color: #1890ff; }
.order-status.shipping { background: #f6ffed; color: #52c41a; }
.order-status.delivered { background: #f9f0ff; color: #722ed1; }
.order-status.completed { background: #f5f5f5; color: #666; }

.order-products {
  display: grid;
  gap: 1rem;
  margin-bottom: 1rem;
}

.product-item {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.product-item img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.product-info h4 {
  margin: 0 0 0.5rem;
}

.product-price {
  color: #ff4d4f;
  margin: 0;
}

.product-quantity {
  color: #666;
  margin: 0;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;
}

.total-price {
  color: #ff4d4f;
  font-size: 1.25rem;
  font-weight: bold;
}

.order-actions {
  display: flex;
  gap: 1rem;
}

.view-details,
.track-order {
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  border: none;
}

.view-details {
  background: #ff4d4f;
  color: #fff;
}

.track-order {
  background: #fff;
  border: 1px solid #ff4d4f;
  color: #ff4d4f;
}

.empty-orders {
  text-align: center;
  padding: 3rem;
  color: #666;
}

.empty-orders .material-icons {
  font-size: 48px;
  margin-bottom: 1rem;
  color: #ddd;
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
  background: #fff;
  border-radius: 8px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
}

.modal-header .close {
  cursor: pointer;
  color: #666;
}

.modal-body {
  padding: 1.5rem;
}

.order-timeline {
  display: grid;
  gap: 2rem;
}

.timeline-item {
  display: flex;
  gap: 1rem;
}

.timeline-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
}

.timeline-icon.active {
  background: #ff4d4f;
  color: #fff;
}

.empty-orders {
  text-align: center;
  padding: 3rem;
  color: #666;
}

.empty-orders .material-icons {
  font-size: 48px;
  margin-bottom: 1rem;
  color: #ddd;
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
  background: #fff;
  border-radius: 8px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
}

.modal-header .close {
  cursor: pointer;
  color: #666;
}

.modal-body {
  padding: 1.5rem;
}
</style>