package com.example.CryptoTracking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Summary response object containing essential cryptocurrency details, designed to minimize payload size and hide internal data")
public class CoinSummaryResponse {

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

    @Schema(description = "Percentage change in price over the last 24 hours", example = "2.35")
    private Double priceChangePercentage24h;
}