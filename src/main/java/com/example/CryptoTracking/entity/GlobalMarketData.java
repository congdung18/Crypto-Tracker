package com.example.CryptoTracking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Map;

@Entity
@Table(name = "global_market_data")
public class GlobalMarketData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int activeCryptocurrencies; // total coin
    private int markets;                // total market
    private double totalMarketCapUsd;   // total market cap (USD)
    private double totalVolumeUsd;      // total volume (USD)
    private double marketCapChangePercentage24hUsd; // % change 24h (USD)
    private double btcDominance;        // BTC dominance (%)
    private long updatedAt;             // timestamp

    public GlobalMarketData() {}

    // Convenience mapper from CoinGecko `/global` response or similar
    @SuppressWarnings("unchecked")
    public static GlobalMarketData fromMap(Map<String, Object> root) {
        if (root == null) return null;
        Object dataObj = root.get("data");
        Map<String, Object> data;
        if (dataObj instanceof Map) {
            data = (Map<String, Object>) dataObj;
        } else if (root.containsKey("total_market_cap") || root.containsKey("total_volume")) {
            // sometimes API returns flattened object
            data = root;
        } else {
            return null;
        }

        GlobalMarketData g = new GlobalMarketData();
        Object ac = data.get("active_cryptocurrencies");
        if (ac instanceof Number) g.setActiveCryptocurrencies(((Number) ac).intValue());

        Object mk = data.get("markets");
        if (mk instanceof Number) g.setMarkets(((Number) mk).intValue());

        Object totalMarketCap = data.get("total_market_cap");
        if (totalMarketCap instanceof Map) {
            Object usd = ((Map) totalMarketCap).get("usd");
            if (usd instanceof Number) g.setTotalMarketCapUsd(((Number) usd).doubleValue());
        } else if (totalMarketCap instanceof Number) {
            g.setTotalMarketCapUsd(((Number) totalMarketCap).doubleValue());
        }

        Object totalVolume = data.get("total_volume");
        if (totalVolume instanceof Map) {
            Object usd = ((Map) totalVolume).get("usd");
            if (usd instanceof Number) g.setTotalVolumeUsd(((Number) usd).doubleValue());
        } else if (totalVolume instanceof Number) {
            g.setTotalVolumeUsd(((Number) totalVolume).doubleValue());
        }

        Object mcChange = data.get("market_cap_change_percentage_24h_usd");
        if (mcChange instanceof Number) g.setMarketCapChangePercentage24hUsd(((Number) mcChange).doubleValue());

        Object mcPercent = data.get("market_cap_percentage");
        if (mcPercent instanceof Map) {
            Object btc = ((Map) mcPercent).get("btc");
            if (btc instanceof Number) g.setBtcDominance(((Number) btc).doubleValue());
        }

        Object updated = data.get("updated_at");
        if (updated instanceof Number) g.setUpdatedAt(((Number) updated).longValue());

        return g;
    }

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getActiveCryptocurrencies() { return activeCryptocurrencies; }
    public void setActiveCryptocurrencies(int activeCryptocurrencies) { this.activeCryptocurrencies = activeCryptocurrencies; }

    public int getMarkets() { return markets; }
    public void setMarkets(int markets) { this.markets = markets; }

    public double getTotalMarketCapUsd() { return totalMarketCapUsd; }
    public void setTotalMarketCapUsd(double totalMarketCapUsd) { this.totalMarketCapUsd = totalMarketCapUsd; }

    public double getTotalVolumeUsd() { return totalVolumeUsd; }
    public void setTotalVolumeUsd(double totalVolumeUsd) { this.totalVolumeUsd = totalVolumeUsd; }

    public double getMarketCapChangePercentage24hUsd() { return marketCapChangePercentage24hUsd; }
    public void setMarketCapChangePercentage24hUsd(double marketCapChangePercentage24hUsd) { this.marketCapChangePercentage24hUsd = marketCapChangePercentage24hUsd; }

    public double getBtcDominance() { return btcDominance; }
    public void setBtcDominance(double btcDominance) { this.btcDominance = btcDominance; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}