package com.codetatva.poc_resilience4j.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/inventory")
public class MockInventoryController {

    @GetMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> checkInventory(@PathVariable String productId) throws InterruptedException {
        Random rand = new Random();
        if (rand.nextDouble() < 0.3) {
            throw new RuntimeException("Inventory service failed");
        }
        if (rand.nextDouble() < 0.2) {
            Thread.sleep(5000); // Simulate slow response
        }
        Map<String, Object> response = new HashMap<>();
        response.put("productId", productId);
        response.put("stock", rand.nextInt(20));
        return ResponseEntity.ok(response);
    }
}
