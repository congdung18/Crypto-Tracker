package com.example.CryptoTracking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO containing detailed coin market data fetched from CoinGecko API")
public class CoinGeckoResponse {

    @Schema(description = "System identifier of the coin", example = "bitcoin")
    @JsonProperty("id")
    private String id;

    @Schema(description = "Symbol of the coin", example = "btc")
    @JsonProperty("symbol")
    private String symbol;

    @Schema(description = "Display name of the coin", example = "Bitcoin")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Timestamp of the last data update", example = "2024-05-12T10:00:00Z")
    @JsonProperty("last_updated")
    private Instant lastUpdated;

    @Schema(description = "URL of the coin's logo image in large size", example = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png")
    @JsonProperty("image")
    private String image;

    @Schema(description = "Current price of the coin in the configured fiat currency or USD", example = "65432.10")
    @JsonProperty("current_price")
    private BigDecimal currentPrice;

    @Schema(description = "Total market capitalization", example = "1285000000000")
    @JsonProperty("market_cap")
    private Long marketCap;

    @Schema(description = "Market capitalization rank on CoinGecko", example = "1")
    @JsonProperty("market_cap_rank")
    private Integer marketCapRank;

    @Schema(description = "Total trading volume in the last 24 hours", example = "35000000000.50")
    @JsonProperty("total_volume")
    private BigDecimal totalVolume;

    @Schema(description = "Highest price reached in the last 24 hours", example = "66000.00")
    @JsonProperty("high_24h")
    private BigDecimal high24h;

    @Schema(description = "Lowest price reached in the last 24 hours", example = "64200.50")
    @JsonProperty("low_24h")
    private BigDecimal low24h;

    @Schema(description = "Price change percentage in the last 24 hours", example = "2.35")
    @JsonProperty("price_change_percentage_24h")
    private Double priceChangePercentage24h;

    @Schema(description = "Price change percentage in the last 7 days", example = "-1.42")
    @JsonProperty("price_change_percentage_7d_in_currency")
    private Double priceChangePercentage7d;

    @Schema(description = "Price change percentage in the last 1 hour", example = "0.15")
    @JsonProperty("price_change_percentage_1h_in_currency")
    private Double priceChangePercentage1h;
}