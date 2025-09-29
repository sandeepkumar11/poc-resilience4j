package com.codetatva.poc_resilience4j.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<Map<String, Object>> checkInventoryWithAllPatterns(String productId);

    Map<String, Object> checkInventoryWithCircuitBreaker(String productId);

    Map<String, Object> checkInventoryWithRetry(String productId);

    Map<String, Object> checkInventoryWithBulkhead(String productId);

    Map<String, Object> checkInventoryWithRateLimiter(String productId);

    CompletableFuture<Map<String, Object>> checkInventoryWithTimeLimiter(String productId);
}
