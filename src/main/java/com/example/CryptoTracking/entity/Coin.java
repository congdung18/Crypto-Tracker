package com.example.CryptoTracking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "coins")
public class Coin {
    @Id //mark ID is primary key
    private String id;
    private String symbol;
    private String name;
    private String image;
    @JsonProperty("current_price")
    private double currentPrice;
    @JsonProperty("market_cap")
    private long marketCap;
    @JsonProperty("market_cap_rank")
    private int marketCapRank;
    @JsonProperty("total_volume")
    private double totalVolume;
    @JsonProperty("low_1h")
    private double low1h;
    @JsonProperty("high_1h")
    private double high1h;
    @JsonProperty("high_24h")
    private double high24h;
    @JsonProperty("low_24h")
    private double low24h;
    @JsonProperty("price_change_percentage_24h")
    private double priceChangePercentage24h;
    @JsonProperty("price_change_percentage_7d_in_currency")
    private double priceChangePercentage7d;
    @JsonProperty("price_change_percentage_14d_in_currency")
    private double priceChangePercentage14d;
    @JsonProperty("price_change_percentage_30d_in_currency")
    private double priceChangePercentage30d;
    @JsonProperty("price_change_percentage_1h_in_currency")
    private double priceChangePercentage1h;
    @JsonProperty("last_updated")
    private String lastUpdated;

    // Constructors, getters, setters
    public Coin() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }

    public long getMarketCap() { return marketCap; }
    public void setMarketCap(long marketCap) { this.marketCap = marketCap; }

    public int getMarketCapRank() { return marketCapRank; }
    public void setMarketCapRank(int marketCapRank) { this.marketCapRank = marketCapRank; }

    public double getTotalVolume() { return totalVolume; }
    public void setTotalVolume(double totalVolume) { this.totalVolume = totalVolume; }

    public double getLow1h(){ return low1h; }
    public void setLow1h(double low1h){ this.low1h = low1h; }

    public double getHigh1h(){ return high1h; }
    public void setHigh1h(double high1h){ this.high1h = high1h; }

    public double getHigh24h() { return high24h; }
    public void setHigh24h(double high24h) { this.high24h = high24h; }

    public double getLow24h() { return low24h; }
    public void setLow24h(double low24h) { this.low24h = low24h; }

    public double getPriceChangePercentage24h() { return priceChangePercentage24h; }
    public void setPriceChangePercentage24h(double priceChangePercentage24h) { this.priceChangePercentage24h = priceChangePercentage24h; }

    public double getPriceChangePercentage7d() { return priceChangePercentage7d; }
    public void setPriceChangePercentage7d(double priceChangePercentage7d) { this.priceChangePercentage7d = priceChangePercentage7d; }

    public double getPriceChangePercentage14d() { return priceChangePercentage14d; }
    public void setPriceChangePercentage14d(double priceChangePercentage14d) { this.priceChangePercentage14d = priceChangePercentage14d; }

    public double getPriceChangePercentage30d() { return priceChangePercentage30d; }
    public void setPriceChangePercentage30d(double priceChangePercentage30d) { this.priceChangePercentage30d = priceChangePercentage30d; }

    public double getPriceChangePercentage1h() { return priceChangePercentage1h; }
    public void setPriceChangePercentage1h(double priceChangePercentage1h) { this.priceChangePercentage1h = priceChangePercentage1h; }

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
}