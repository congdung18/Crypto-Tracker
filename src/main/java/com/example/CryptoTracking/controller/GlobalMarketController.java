package com.example.CryptoTracking.controller;

import com.example.CryptoTracking.dto.GlobalMarketSummaryResponse;
import com.example.CryptoTracking.service.GlobalMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/global")
@CrossOrigin(origins = "*")
public class GlobalMarketController {
    @Autowired
    private GlobalMarketService globalMarketService;

    @GetMapping
    public ResponseEntity<GlobalMarketSummaryResponse> getGlobalMarketData() {
        GlobalMarketSummaryResponse data = globalMarketService.getGlobalMarket();
        return ResponseEntity.ok(data);
    }
}