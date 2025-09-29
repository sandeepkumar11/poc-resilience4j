package com.codetatva.poc_resilience4j.controller;

import com.codetatva.poc_resilience4j.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/inventory/{productId}/all-patterns")
    public ResponseEntity<Map<String, Object>> checkInventoryWithAllPatterns(@PathVariable String productId) throws InterruptedException, ExecutionException {
        return ResponseEntity.ok(orderService.checkInventoryWithAllPatterns(productId).get());
    }

    @GetMapping("/inventory/{productId}/circuit-breaker")
    public ResponseEntity<Map<String, Object>> checkInventoryWithCircuitBreaker(@PathVariable String productId) {
        return ResponseEntity.ok(orderService.checkInventoryWithCircuitBreaker(productId));
    }

    @GetMapping("/inventory/{productId}/retry")
    public ResponseEntity<Map<String, Object>> checkInventoryWithRetry(@PathVariable String productId) {
        return ResponseEntity.ok(orderService.checkInventoryWithRetry(productId));
    }

    @GetMapping("/inventory/{productId}/bulkhead")
    public ResponseEntity<Map<String, Object>> checkInventoryWithBulkhead(@PathVariable String productId) {
        return ResponseEntity.ok(orderService.checkInventoryWithBulkhead(productId));
    }

    @GetMapping("/inventory/{productId}/rate-limiter")
    public ResponseEntity<Map<String, Object>> checkInventoryWithRateLimiter(@PathVariable String productId) {
        return ResponseEntity.ok(orderService.checkInventoryWithRateLimiter(productId));
    }

    @GetMapping("/inventory/{productId}/time-limiter")
    public ResponseEntity<Map<String, Object>> checkInventoryWithTimeLimiter(@PathVariable String productId) throws InterruptedException, ExecutionException {
        return ResponseEntity.ok(orderService.checkInventoryWithTimeLimiter(productId).get());
    }
}
