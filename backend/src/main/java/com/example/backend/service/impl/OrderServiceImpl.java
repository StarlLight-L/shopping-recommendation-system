package com.example.backend.service.impl;

import com.example.backend.mapper.OrderMapper;
import com.example.backend.mapper.OrderItemMapper;
import com.example.backend.model.dto.OrderDTO;
import com.example.backend.model.entity.Order;
import com.example.backend.model.entity.OrderItem;
import com.example.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderItemMapper OrderItemMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        return orderMapper.selectOrdersByUserId(userId);
    }

    public static class OrderUtil {
        public static String generateOrderNo() {
            return "ORD" + System.currentTimeMillis() + (int)(Math.random() * 1000);
        }
    }
    @Override
    @Transactional
    public  Order createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrder_no(OrderUtil.generateOrderNo());
        order.setUser_id(orderDTO.getUser_id());
        order.setTotal_amount(orderDTO.getTotal_amount());
        order.setCreate_time(LocalDateTime.now());
        order.setUpdate_time(LocalDateTime.now());
        order.setPay_status(0);
        order.setOrder_status(0);
        order.setShipping_address(orderDTO.getShipping_address());
        order.setReceiver_name(orderDTO.getReceiver_name());
        order.setReceiver_phone(orderDTO.getReceiver_phone());
        System.out.print(order);
        orderMapper.insert(order);

        List<OrderItem> items = orderDTO.getItems().stream().map(dto -> {
            OrderItem item = new OrderItem();
            item.setOrder_id(order.getOrder_no());
            item.setProduct_id(dto.getProductId());
            item.setProduct_name(dto.getProductName());
            item.setProduct_image(dto.getProductImage());
            item.setProduct_price(dto.getProductPrice());
            item.setQuantity(dto.getQuantity());
            item.setTotal_price(dto.getTotalPrice());
            item.setCreate_time(LocalDateTime.now());
            return item;
        }).collect(Collectors.toList());

        for (OrderItem item : items) {
            OrderItemMapper.insert(item);
        }
        return order;
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderMapper.getOrderById(orderId);
    }


    @Override
    public Boolean deleteOrderById(String orderId) {
        orderMapper.deleteOrderById(orderId);
        OrderItemMapper.deleteOrderById(orderId);
        return true;
    }

    @Override
    public Boolean updateOrder(String orderId) {
        orderMapper.updateStatus(orderId);
        return true;
    }

    @Override
    public List<OrderItem> getOrdersByOrderId(String orderId) {
        return orderItemMapper.selectByOrderId(orderId);
    }

    @Override
    public Boolean ConfirmOrder(String orderId) {
        orderMapper.ConfirmReceive(orderId);
        return true;
    }

    @Override
    public List<Order> getOrders() {
        return orderMapper.selectOrders();
    }

    @Override
    public Boolean SendOutGood(String orderId){
        orderMapper.SendOutGood(orderId);
        return true;
    }

    @Override
    public List<OrderItem> getOrdersItems() {
        return orderItemMapper.selectAllOrdersItems();
    }

}
