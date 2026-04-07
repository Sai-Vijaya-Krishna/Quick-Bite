package com.example.quickbite.controller;

import com.example.quickbite.entity.MenuItem;
import com.example.quickbite.entity.Order;
import com.example.quickbite.service.MenuService;
import com.example.quickbite.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired private OrderService orderService;
    @Autowired private MenuService menuService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        return ResponseEntity.ok(orderService.getDashboardStats());
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> allOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            Order order = orderService.updateStatus(id, body.get("status"));
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/menu")
    public ResponseEntity<List<MenuItem>> allMenuItems() {
        return ResponseEntity.ok(menuService.getAll());
    }

    @PostMapping("/menu")
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem item) {
        return ResponseEntity.ok(menuService.addItem(item));
    }

    @PutMapping("/menu/{id}")
    public ResponseEntity<?> updateMenuItem(@PathVariable Long id, @RequestBody MenuItem item) {
        try {
            return ResponseEntity.ok(menuService.updateItem(id, item));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/menu/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long id) {
        menuService.deleteItem(id);
        return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
    }
}
