package com.example.CryptoTracking.scheduler;

import com.example.CryptoTracking.service.GlobalMarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.example.CryptoTracking.service.CoinService;

@Slf4j
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
            //when fetch api, if cache is not null, cache will be deleted automatically
        } catch (Exception e) {
            log.error("Scheduler failed: ", e);
        }
    }
}
