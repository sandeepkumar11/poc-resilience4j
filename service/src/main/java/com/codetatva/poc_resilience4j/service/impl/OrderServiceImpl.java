package com.codetatva.poc_resilience4j.service.impl;

import com.codetatva.poc_resilience4j.client.InventoryClient;
import com.codetatva.poc_resilience4j.service.OrderService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final InventoryClient inventoryClient;
    private final ExecutorService executorService;

    public OrderServiceImpl(InventoryClient inventoryClient, ExecutorService executorService) {
        this.inventoryClient = inventoryClient;
        this.executorService = executorService;
    }

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "fallbackAsync")
    @Retry(name = "inventoryService")
    @RateLimiter(name = "inventoryService")
    @Bulkhead(name = "inventoryService")
    @TimeLimiter(name = "inventoryService")
    @Override
    public CompletableFuture<Map<String, Object>> checkInventoryWithAllPatterns(String productId) {
        return CompletableFuture.supplyAsync(() -> inventoryClient.checkInventory(productId), executorService);
    }

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "fallback")
    @Override
    public Map<String, Object> checkInventoryWithCircuitBreaker(String productId) {
        return inventoryClient.checkInventory(productId);
    }

    @Retry(name = "inventoryService", fallbackMethod = "fallback")
    @Override
    public Map<String, Object> checkInventoryWithRetry(String productId) {
        return inventoryClient.checkInventory(productId);
    }

    @Bulkhead(name = "inventoryService", fallbackMethod = "fallback")
    @Override
    public Map<String, Object> checkInventoryWithBulkhead(String productId) {
        return inventoryClient.checkInventory(productId);
    }

    @RateLimiter(name = "inventoryService", fallbackMethod = "fallback")
    @Override
    public Map<String, Object> checkInventoryWithRateLimiter(String productId) {
        return inventoryClient.checkInventory(productId);
    }

    @TimeLimiter(name = "inventoryService", fallbackMethod = "fallbackAsync")
    @Override
    public CompletableFuture<Map<String, Object>> checkInventoryWithTimeLimiter(String productId) {
        return CompletableFuture.supplyAsync(() -> inventoryClient.checkInventory(productId), executorService);
    }

    public Map<String, Object> fallback(String productId, Exception e) {
        logger.error("Check failed for productId {}: {}", productId, e.getMessage());
        return Map.of("status", "Inventory service is currently unavailable. Please try again later.");
    }

    public CompletableFuture<Map<String, Object>> fallbackAsync(String productId, Exception e) {
        logger.error("Async check failed for productId {}: {}", productId, e.getMessage());
        return CompletableFuture.completedFuture(
                Map.of("status", "Inventory service is currently unavailable. Please try again later.")
        );
    }
}