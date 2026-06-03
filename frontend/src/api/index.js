import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8087/api',
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json'
    }
});

// 请求拦截器
api.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 响应拦截器
api.interceptors.response.use(
    response => {
        return response.data;
    },
    error => {
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    // 未授权，清除token并跳转到登录页面
                    localStorage.removeItem('token');
                    window.location.href = '/login';
                    break;
                case 403:
                    // 权限不足
                    console.error('没有权限访问该资源');
                    break;
                case 404:
                    // 资源不存在
                    console.error('请求的资源不存在');
                    break;
                default:
                    console.error('服务器错误:', error.response.data);
            }
        } else {
            console.error('网络错误:', error.message);
        }
        return Promise.reject(error);
    }
);

export default api;