package com.example.CryptoTracking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/*
Include data from /globals.
Provides every information you need for every markets available
*/

@Data
public class GlobalDataResponse {
    @JsonProperty("data")
    private DataObj data;

    @Data
    public static class DataObj{
        @JsonProperty("active_cryptocurrencies")
        private Integer activeCryptocurrencies;

        @JsonProperty("markets")
        private Integer markets;

        @JsonProperty("total_market_cap")
        private Map<String, BigDecimal> totalMarketCap;

        @JsonProperty("total_volume")
        private Map<String, BigDecimal> totalVolume;

        @JsonProperty("market_cap_change_percentage_24h_usd")
        private Double marketCapChangePercentage24hUsd;

        @JsonProperty("market_cap_percentage")
        private Map<String, Double> marketCapPercentage;

        @JsonProperty("updated_at")
        private Long updatedAt;
    }
}
