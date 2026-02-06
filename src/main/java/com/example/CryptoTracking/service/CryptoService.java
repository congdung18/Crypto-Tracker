package com.example.CryptoTracking.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import com.example.CryptoTracking.entity.Coin;
import com.example.CryptoTracking.entity.GlobalMarketData;
import com.example.CryptoTracking.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

@Service
public class CryptoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://api.coingecko.com/api/v3";

    @Autowired
    private CoinRepository coinRepository;

    public List<Coin> getCoins(int page, int perPage) {
        String url = BASE_URL + "/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=" + perPage + "&page=" + page + "&sparkline=false&price_change_percentage=24h%2C7d%2C14d%2C30d";
        try {
            Coin[] coins = restTemplate.getForObject(url, Coin[].class);
            List<Coin> coinList = Arrays.asList(coins);
            // Save to database for caching
            coinRepository.saveAll(coinList);
            return coinList;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle API errors
            throw new RuntimeException("Error fetching coins: " + e.getMessage());
        }
    }

    public Coin getCoinById(String id) {
        // First check database
        Coin coin = coinRepository.findById(id).orElse(null);
        if (coin != null) {
            return coin;
        }
        // If not in DB, fetch from API
        String url = BASE_URL + "/coins/" + id + "?localization=false&tickers=false&market_data=true&community_data=false&developer_data=false&sparkline=false";
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            coin = mapToCoin(response);
            coinRepository.save(coin);
            return coin;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Error fetching coin: " + e.getMessage());
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

    private Coin mapToCoin(Map<String, Object> data) {
        Coin coin = new Coin();
        coin.setId((String) data.get("id"));
        coin.setSymbol((String) data.get("symbol"));
        coin.setName((String) data.get("name"));
        coin.setImage((String) ((Map<String, Object>) data.get("image")).get("large"));

        Map<String, Object> marketData = (Map<String, Object>) data.get("market_data");
        if (marketData != null) {
            Map<String, Object> currentPrice = (Map<String, Object>) marketData.get("current_price");
            coin.setCurrentPrice(((Number) currentPrice.get("usd")).doubleValue());

            Map<String, Object> marketCap = (Map<String, Object>) marketData.get("market_cap");
            coin.setMarketCap(((Number) marketCap.get("usd")).longValue());

            coin.setMarketCapRank(((Number) marketData.get("market_cap_rank")).intValue());

            Map<String, Object> totalVolume = (Map<String, Object>) marketData.get("total_volume");
            coin.setTotalVolume(((Number) totalVolume.get("usd")).doubleValue());

            Map<String, Object> high1h = (Map<String, Object>) marketData.get("high_1h");
            coin.setHigh1h(((Number) high1h.get("usd")).doubleValue());

            Map<String, Object> low1h = (Map<String, Object>) marketData.get("low_1h");
            coin.setLow1h(((Number) low1h.get("usd")).doubleValue());

            Map<String, Object> high24h = (Map<String, Object>) marketData.get("high_24h");
            coin.setHigh24h(((Number) high24h.get("usd")).doubleValue());

            Map<String, Object> low24h = (Map<String, Object>) marketData.get("low_24h");
            coin.setLow24h(((Number) low24h.get("usd")).doubleValue());

            Map<String, Object> priceChange24h = (Map<String, Object>) marketData.get("price_change_percentage_24h");
            coin.setPriceChangePercentage24h(((Number) priceChange24h.get("usd")).doubleValue());

            Map<String, Object> priceChange7d = (Map<String, Object>) marketData.get("price_change_percentage_7d");
            coin.setPriceChangePercentage7d(((Number) priceChange7d.get("usd")).doubleValue());

        }

        coin.setLastUpdated((String) data.get("last_updated"));
        return coin;
    }
}