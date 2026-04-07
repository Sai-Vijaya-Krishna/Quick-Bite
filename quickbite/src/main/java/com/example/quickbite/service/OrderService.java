package com.example.quickbite.service;

import com.example.quickbite.dto.OrderRequest;
import com.example.quickbite.entity.*;
import com.example.quickbite.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private MenuItemRepository menuItemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    public Order placeOrder(String email, OrderRequest req) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(req.getDeliveryAddress());
        order.setPaymentMethod(req.getPaymentMethod());
        order.setStatus("PLACED");
        order.setOrderTime(LocalDateTime.now());
        order.setEstimatedDelivery(LocalDateTime.now().plusMinutes(40));

        double total = 0;
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRequest.CartItem cartItem : req.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(cartItem.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));
            OrderItem oi = new OrderItem();
            oi.setOrder(savedOrder);
            oi.setMenuItem(menuItem);
            oi.setQuantity(cartItem.getQuantity());
            oi.setPrice(menuItem.getPrice() * cartItem.getQuantity());
            total += oi.getPrice();
            orderItems.add(oi);
        }
        orderItemRepository.saveAll(orderItems);
        savedOrder.setItems(orderItems);
        savedOrder.setTotalAmount(total);
        orderRepository.save(savedOrder);

        emailService.sendOrderConfirmationEmail(user.getEmail(), user.getName(), savedOrder.getId(), total);
        return savedOrder;
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getUserOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserOrderByOrderTimeDesc(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);

        // Simulate location movement for OUT_FOR_DELIVERY
        if ("OUT_FOR_DELIVERY".equals(status)) {
            order.setCurrentLat(17.7200 + Math.random() * 0.01);
            order.setCurrentLng(83.3000 + Math.random() * 0.01);
            orderRepository.save(order);
        }

        if ("DELIVERED".equals(status)) {
            emailService.sendOrderDeliveredEmail(
                order.getUser().getEmail(),
                order.getUser().getName(),
                order.getId()
            );
        }

        // Push real-time update via WebSocket
        Map<String, Object> update = new HashMap<>();
        update.put("orderId", orderId);
        update.put("status", status);
        update.put("currentLat", order.getCurrentLat());
        update.put("currentLng", order.getCurrentLng());
        messagingTemplate.convertAndSend("/topic/order/" + orderId, update);

        return order;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", orderRepository.count());
        stats.put("totalUsers", userRepository.count());
        stats.put("placedOrders", orderRepository.countByStatus("PLACED"));
        stats.put("preparingOrders", orderRepository.countByStatus("PREPARING"));
        stats.put("outForDelivery", orderRepository.countByStatus("OUT_FOR_DELIVERY"));
        stats.put("deliveredOrders", orderRepository.countByStatus("DELIVERED"));
        stats.put("recentOrders", orderRepository.findAll().stream().limit(5).toList());
        return stats;
    }
}
