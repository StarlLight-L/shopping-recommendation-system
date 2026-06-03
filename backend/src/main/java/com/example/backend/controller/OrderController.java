package com.example.backend.controller;

import com.example.backend.model.dto.OrderDTO;
import com.example.backend.model.entity.Order;
import com.example.backend.model.entity.OrderItem;
import com.example.backend.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Api(tags = "Order Management")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    @ApiOperation("Get all orders")
    public ResponseEntity<List<Order>> findAll() {
        List<Order> orders =  orderService.getOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/list/{userId}")
    @ApiOperation("Get the user's orders")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable int userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        Order Order = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(Order);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteOrder(@RequestBody String orderId) {
        Boolean success = orderService.deleteOrderById(orderId);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateOrder(@RequestParam String orderId) {
        Boolean success = orderService.updateOrder(orderId);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/item_list/{orderId}")
    @ApiOperation("Get the user's order list")
    public ResponseEntity<List<OrderItem>> getOrdersByOrderId(@PathVariable String orderId) {
        List<OrderItem> orderItems = orderService.getOrdersByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }

    @PutMapping("/confirm")
    @ApiOperation("Confirm receipt")
    public ResponseEntity<Boolean> confirmOrder(@RequestParam String orderId) {
        Boolean success = orderService.ConfirmOrder(orderId);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/send")
    public ResponseEntity<Boolean> sendOutGood(@RequestParam String orderId) {
        Boolean success = orderService.SendOutGood(orderId);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/all_item_list/")
    public ResponseEntity<List<OrderItem>> getAllOrderItems() {
        List<OrderItem> orderItems = orderService.getOrdersItems();
        return ResponseEntity.ok(orderItems);
    }

}