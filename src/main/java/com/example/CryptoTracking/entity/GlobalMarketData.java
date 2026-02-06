package com.example.CryptoTracking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class GlobalMarketData {
    private int activeCryptocurrencies; // tổng số coin
    private int markets;                // tổng số sàn
    private double totalMarketCapUsd;   // tổng vốn hóa (USD)
    private double totalVolumeUsd;      // tổng volume (USD)
    private double marketCapChangePercentage24hUsd; // % change 24h (USD)
    private double btcDominance;        // BTC dominance (%)
    private long updatedAt;             // timestamp

    public GlobalMarketData() {}

    // Convenience mapper from CoinGecko `/global` response
    @SuppressWarnings("unchecked")
    public static GlobalMarketData fromMap(Map<String, Object> root) {
        if (root == null) return null;
        Object dataObj = root.get("data");
        if (!(dataObj instanceof Map)) return null;
        Map<String, Object> data = (Map<String, Object>) dataObj;

        GlobalMarketData g = new GlobalMarketData();
        Object ac = data.get("active_cryptocurrencies");
        if (ac instanceof Number) g.setActiveCryptocurrencies(((Number) ac).intValue());

        Object mk = data.get("markets");
        if (mk instanceof Number) g.setMarkets(((Number) mk).intValue());

        Object totalMarketCap = data.get("total_market_cap");
        if (totalMarketCap instanceof Map) {
            Object usd = ((Map) totalMarketCap).get("usd");
            if (usd instanceof Number) g.setTotalMarketCapUsd(((Number) usd).doubleValue());
        }

        Object totalVolume = data.get("total_volume");
        if (totalVolume instanceof Map) {
            Object usd = ((Map) totalVolume).get("usd");
            if (usd instanceof Number) g.setTotalVolumeUsd(((Number) usd).doubleValue());
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