package com.example.CryptoTracking.scheduler;

import com.example.CryptoTracking.service.GlobalMarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.cache.Cache;

import com.example.CryptoTracking.client.CoinGeckoClient;
import com.example.CryptoTracking.dto.CoinSummaryResponse;
import com.example.CryptoTracking.dto.GlobalDataResponse;
import com.example.CryptoTracking.service.CoinService;

@Slf4j
@Component
public class DataFetchScheduler {
    @Autowired
    private CoinService coinService;

    @Autowired
    private GlobalMarketService globalMarketService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired 
    private CoinGeckoClient coinGeckoClient;

    // Run 10 seconds after startup, then every 5 minutes
    @Scheduled(initialDelay = 10000, fixedRate = 300000)
    public void fetchAndStore() {
        try {
            
            coinService.fetchCoinsFromAPI();
            globalMarketService.fetchGlobalMarketsFromAPI();

            GlobalDataResponse globalDataResponse = coinGeckoClient.getGlobalMarket();
            if(globalDataResponse != null && globalDataResponse.getData() != null){
                Cache cache = cacheManager.getCache("globalData");
                if(cache != null){
                    cache.put("current", globalDataResponse.getData());
                }
            }
        } catch (Exception e) {
            log.error("Scheduler failed: ", e);
        }
    }

}
