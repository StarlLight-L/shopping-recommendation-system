package com.example.backend.controller;
import com.example.backend.model.entity.Order;
import com.example.backend.service.OrderService;
import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.example.backend.config.alipay.AlipayConfig;
import com.example.backend.model.dto.OrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// xjlugv6874@sandbox.com
// 9428521.24 - 30 = 9428491.24 + 30 = 9428521.24
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alipay")
public class AlipayController {
    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private OrderService orderService;

    @CrossOrigin(origins = "*", allowCredentials = "false")
    @GetMapping("/pay")
    public void pay(@RequestParam String orderId, HttpServletResponse response) throws Exception {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        String totalAmount = order.getTotal_amount().toString();
        String returnUrlWithOrderId = AlipayConfig.RETURN_URL + "?orderId=" + orderId;// Pass the orderId to the payment success page

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(returnUrlWithOrderId); // Redirect URL after successful payment
        request.setNotifyUrl(AlipayConfig.NOTIFY_URL); // Alipay server asynchronous notification URL

        Map<String, Object> bizContent = new HashMap<>();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        bizContent.put("total_amount", totalAmount);
        bizContent.put("subject", "Payment");

        request.setBizContent(new ObjectMapper().writeValueAsString(bizContent));

        String form = alipayClient.pageExecute(request).getBody();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(form);
        System.out.println("Alipay: " + form);
        response.getWriter().flush();
        response.getWriter().close();

    }

    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        // 异步通知处理逻辑：验签 + 订单处理
        // 推荐使用 AlipaySignature.rsaCheckV1 验签
        return "success"; // 返回 success 告诉支付宝通知已处理
    }

    @GetMapping("/return")
    public String returnUrl() {
        return "支付成功，页面跳转成功！";
    }
}
