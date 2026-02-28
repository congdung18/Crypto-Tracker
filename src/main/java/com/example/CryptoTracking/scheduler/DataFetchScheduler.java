package com.example.CryptoTracking.scheduler;

import com.example.CryptoTracking.service.GlobalMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.example.CryptoTracking.service.CoinService;

@Component
public class DataFetchScheduler {
    @Autowired
    private CoinService coinService;

    @Autowired
    private GlobalMarketService globalMarketService;

    // Run 10 seconds after startup, then every 5 minutes
    @Scheduled(initialDelay = 10000, fixedRate = 300000)
    public void fetchAndStore() {
        try {
            coinService.fetchCoinsFromAPI();
            globalMarketService.fetchGlobalMarketsFromAPI();
        } catch (Exception e) {
            System.err.println("Scheduled fetch failed: " + e.getMessage());
        }
    }
}
