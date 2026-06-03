package com.example.backend.service;

import com.example.backend.model.dto.OrderDTO;
import com.example.backend.model.entity.Order;
import com.example.backend.model.entity.OrderItem;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    List<Order> getOrdersByUserId(int userId);

    Order createOrder(OrderDTO orderDTO);

    Order getOrderById(String orderId);

    Boolean deleteOrderById(String orderId);

    Boolean updateOrder(String orderId);

    List<OrderItem> getOrdersByOrderId(String orderId);

    Boolean ConfirmOrder(String orderId);

    List<Order> getOrders();

    Boolean SendOutGood(String orderId);

    List<OrderItem> getOrdersItems();
}
