package com.example.CryptoTracking.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.CryptoTracking.entity.Coin;
import com.example.CryptoTracking.entity.GlobalMarketData;
import com.example.CryptoTracking.repository.CoinRepository;
import com.example.CryptoTracking.repository.GlobalMarketDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class CryptoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://api.coingecko.com/api/v3";

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private GlobalMarketDataRepository globalRepo;

    public List<Coin> getCoins(int page, int perPage) {
        String url = BASE_URL + "/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=" + perPage + "&page=" + page + "&sparkline=false&price_change_percentage=24h%2C7d%2C14d%2C30d";
        try {
            Coin[] coins = restTemplate.getForObject(url, Coin[].class);
            List<Coin> coinList = Arrays.asList(coins != null ? coins : new Coin[0]);
            coinRepository.saveAll(coinList);
            return coinList;
        } catch (Exception e) {
            System.err.println("Lỗi CoinGecko getCoins: " + e.getMessage());
            return coinRepository.findAll();
        }
    }

    public GlobalMarketData getGlobalMarketData() {
        String url = BASE_URL + "/global";
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            GlobalMarketData globalData = GlobalMarketData.fromMap(response);
            if (globalData == null) {
                globalData = new GlobalMarketData();
                globalData.setUpdatedAt(System.currentTimeMillis() / 1000L);
            }
            globalRepo.save(globalData);
            return globalData;
        } catch (Exception e) {
            System.err.println("Lỗi CoinGecko getGlobalMarketData: " + e.getMessage());
            throw new RuntimeException("Error fetching global data: " + e.getMessage());
        }
    }

    public Coin getCoinById(String id) {
        return coinRepository.findById(id).orElseGet(() -> fetchCoinFromApi(id));
    }

    private Coin fetchCoinFromApi(String id) {
        String url = BASE_URL + "/coins/" + id + "?localization=false&tickers=false&market_data=true&community_data=false&developer_data=false&sparkline=false";
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            Coin coin = mapToCoinFromDetail(response);
            return coinRepository.save(coin);
        } catch (Exception e) {
            System.err.println("Lỗi CoinGecko getCoinById: " + e.getMessage());
            throw new RuntimeException("Could not find coin with ID: " + id);
        }
    }

    public List<Coin> searchCoins(String query) {
        if (query == null || query.trim().isEmpty()) {
            return coinRepository.findAll();
        }
        return coinRepository.findByNameOrSymbol(query.trim());
    }

    public List<Coin> getCoinsSortedByRank() {
        return coinRepository.findAllByOrderByMarketCapRankAsc();
    }

    public List<Coin> getCoinsSortedByPrice() {
        return coinRepository.findAllByOrderByCurrentPriceDesc();
    }

    public List<Coin> getCoinsSortedByMarketCap() {
        return coinRepository.findAllByOrderByMarketCapDesc();
    }

    @SuppressWarnings("unchecked")
    private Coin mapToCoinFromDetail(Map<String, Object> data) {
        Coin coin = new Coin();
        coin.setId((String) data.get("id"));
        coin.setSymbol((String) data.get("symbol"));
        coin.setName((String) data.get("name"));

        // Extract image from nested image object
        Object imageObj = data.get("image");
        if (imageObj instanceof Map) {
            coin.setImage((String) ((Map) imageObj).get("large"));
        }

        Map<String, Object> marketData = (Map<String, Object>) data.get("market_data");
        if (marketData != null) {
            // current_price: { usd: ... }
            Map<String, Object> cpMap = (Map<String, Object>) marketData.get("current_price");
            if (cpMap != null) coin.setCurrentPrice(asDouble(cpMap.get("usd")));

            // market_cap: { usd: ... }
            Map<String, Object> mcMap = (Map<String, Object>) marketData.get("market_cap");
            if (mcMap != null) coin.setMarketCap(((Number) mcMap.get("usd")).longValue());

            coin.setMarketCapRank(asInt(marketData.get("market_cap_rank")));

            // total_volume: { usd: ... }
            Map<String, Object> tvMap = (Map<String, Object>) marketData.get("total_volume");
            if (tvMap != null) coin.setTotalVolume(asDouble(tvMap.get("usd")));

            // high_24h: { usd: ... }
            Map<String, Object> h24Map = (Map<String, Object>) marketData.get("high_24h");
            if (h24Map != null) coin.setHigh24h(asDouble(h24Map.get("usd")));

            // low_24h: { usd: ... }
            Map<String, Object> l24Map = (Map<String, Object>) marketData.get("low_24h");
            if (l24Map != null) coin.setLow24h(asDouble(l24Map.get("usd")));

            // price changes (already flat)
            coin.setPriceChangePercentage24h(asDouble(marketData.get("price_change_percentage_24h")));
            coin.setPriceChangePercentage7d(asDouble(marketData.get("price_change_percentage_7d_in_currency")));
            coin.setPriceChangePercentage14d(asDouble(marketData.get("price_change_percentage_14d_in_currency")));
            coin.setPriceChangePercentage30d(asDouble(marketData.get("price_change_percentage_30d_in_currency")));
            coin.setPriceChangePercentage1h(asDouble(marketData.get("price_change_percentage_1h_in_currency")));
        }

        coin.setLastUpdated((String) data.get("last_updated"));
        return coin;
    }

    private Double asDouble(Object o) {
        return (o instanceof Number) ? ((Number) o).doubleValue() : 0.0;
    }

    private Integer asInt(Object o) {
        return (o instanceof Number) ? ((Number) o).intValue() : 0;
    }
}