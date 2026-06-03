import api from './index';

export const order = {
    //获取订单列表
    getOrderItems(user_id){
        return api.get(`/orders/list/${user_id}`);
    },
    //添加订单项
    createOrder(order){
        return api.post(`/orders/create/`,order);
    },
    //删除订单及订单项
    deleteOrder(orderId){
        return api.delete(`/orders/delete/${orderId}`)
    },
    //更改订单状态
    updateOrder(orderId){
        return api.put(`/orders/update?orderId=${orderId}`)
    },
    //根据订单号判断订单项
    getOrderItemsByOrderId(orderId){
        return api.get(`/orders/item_list/${orderId}`);
    },
    //确认收货
    confirmReceive(orderId){
        return api.put(`/orders/confirm?orderId=${orderId}`)
    },
    //管理员
    //获取所有订单
    getOrders(){
        return api.get('/orders/')
    },
    //发货
    sendOrder(orderId){
        return api.put(`/orders/send?orderId=${orderId}`)
    }
};