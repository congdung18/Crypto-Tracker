package com.example.CryptoTracking.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.example.CryptoTracking.service.CryptoService;

@Component
public class DataFetchScheduler {

    @Autowired
    private CryptoService cryptoService;

    // Run 10 seconds after startup, then every 5 minutes
    @Scheduled(initialDelay = 10000, fixedRate = 300000)
    public void fetchAndStore() {
        try {
            // Fetch top 100 coins and save
            cryptoService.getCoins(1, 100);
            // Fetch global market data and save
            cryptoService.getGlobalMarketData();
        } catch (Exception e) {
            System.err.println("Scheduled fetch failed: " + e.getMessage());
        }
    }
}
