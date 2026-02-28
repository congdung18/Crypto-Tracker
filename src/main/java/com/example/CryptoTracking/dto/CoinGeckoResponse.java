package com.example.CryptoTracking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/*
Include data from coins/markets.
Provides every information you need for every coins available
Can use JsonTest for testing
*/

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinGeckoResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("name")
    private String name;

    @JsonProperty("last_updated")
    private Instant lastUpdated;

    @JsonProperty("image")
    private String image;

    @JsonProperty("current_price")
    private BigDecimal currentPrice;

    @JsonProperty("market_cap")
    private Long marketCap;

    @JsonProperty("market_cap_rank")
    private Integer marketCapRank;

    @JsonProperty("total_volume")
    private BigDecimal totalVolume;

    @JsonProperty("high_24h")
    private BigDecimal high24h;

    @JsonProperty("low_24h")
    private BigDecimal low24h;

    @JsonProperty("price_change_percentage_24h")
    private Double priceChangePercentage24h;

    @JsonProperty("price_change_percentage_7d_in_currency")
    private Double priceChangePercentage7d;

    @JsonProperty("price_change_percentage_1h_in_currency")
    private Double priceChangePercentage1h;
}
