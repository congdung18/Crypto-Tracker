package com.example.CryptoTracking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Summary response object containing essential cryptocurrency details, designed to minimize payload size and hide internal data")
public class CoinSummaryResponse {

    @Schema(description = "Unique system identifier of the coin", example = "bitcoin")
    private String id;

    @Schema(description = "Cryptocurrency symbol", example = "btc")
    private String symbol;

    @Schema(description = "Display name of the cryptocurrency", example = "Bitcoin")
    private String name;

    @Schema(description = "URL to the cryptocurrency's logo image", example = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png")
    private String image;

    @Schema(description = "Current market price in fiat currency or in USD", example = "65432.10")
    private BigDecimal price;

    @Schema(description = "Current rank based on market capitalization", example = "1")
    private Integer marketCapRank;

    @Schema(description = "Total market capitalization in USD", example = "1200000000")
    private Long marketCap;

    @Schema(description = "Total trading volume in the last 24 hours", example = "45000000")
    private BigDecimal totalVolume;

    @Schema(description = "Highest price in the last 24 hours", example = "68000.00")
    private BigDecimal high24h;

    @Schema(description = "Lowest price in the last 24 hours", example = "64000.00")
    private BigDecimal low24h;

    @Schema(description = "Percentage change in price over the last 24 hours", example = "2.35")
    private Double priceChangePercentage24h;

    @Schema(description = "Circulating supply of the coin", example = "19600000.00")
    private BigDecimal circulatingSupply;

    @Schema(description = "Total supply of the coin", example = "21000000.00")
    private BigDecimal totalSupply;

    @Schema(description = "Maximum supply of the coin", example = "21000000.00")
    private BigDecimal maxSupply;

    @Schema(description = "Fully diluted valuation in USD", example = "1350000000.00")
    private BigDecimal fullyDilutedValuation;

    @Schema(description = "Timestamp of the last update", example = "2026-07-23T14:44:15Z")
    private java.time.Instant lastUpdated;
}