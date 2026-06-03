import axios from 'axios'

const instance = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Add request interceptor
instance.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// Add response interceptor
instance.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
        // Clear token and user information from local storage
      localStorage.removeItem('token')
      localStorage.removeItem('user')
        // Redirect to login page
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default instance