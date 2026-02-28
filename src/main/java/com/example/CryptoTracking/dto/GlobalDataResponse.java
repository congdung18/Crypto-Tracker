package com.example.CryptoTracking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/*
Include data from /globals.
Provides every information you need for every markets available
*/

@Data
@Schema(description = "Wrapper object containing global cryptocurrency market data fetched from CoinGecko's /global endpoint")
public class GlobalDataResponse {

    @Schema(description = "The core data object containing all global metrics")
    @JsonProperty("data")
    private DataObj data;

    @Data
    @Schema(description = "Detailed global market metrics including active coins, total volume, and market dominance")
    public static class DataObj {

        @Schema(description = "Total number of active cryptocurrencies currently being tracked", example = "13254")
        @JsonProperty("active_cryptocurrencies")
        private Integer activeCryptocurrencies;

        @Schema(description = "Total number of active cryptocurrency exchanges", example = "982")
        @JsonProperty("markets")
        private Integer markets;

        @Schema(description = "Total market capitalization mapped by currency symbol", example = "{\"usd\": 2500000000000.50, \"btc\": 45000000.0}")
        @JsonProperty("total_market_cap")
        private Map<String, BigDecimal> totalMarketCap;

        @Schema(description = "Total trading volume in the last 24 hours mapped by currency symbol", example = "{\"usd\": 125000000000.00}")
        @JsonProperty("total_volume")
        private Map<String, BigDecimal> totalVolume;

        @Schema(description = "Global market cap change percentage in USD over the last 24 hours", example = "1.52")
        @JsonProperty("market_cap_change_percentage_24h_usd")
        private Double marketCapChangePercentage24hUsd;

        @Schema(description = "Market dominance percentage of top cryptocurrencies", example = "{\"btc\": 51.2, \"eth\": 16.8}")
        @JsonProperty("market_cap_percentage")
        private Map<String, Double> marketCapPercentage;

        @Schema(description = "Unix timestamp of the last data update", example = "1708147200")
        @JsonProperty("updated_at")
        private Long updatedAt;
    }
}