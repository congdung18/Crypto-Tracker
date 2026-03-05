package com.example.CryptoTracking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Summary response object containing key global cryptocurrency market metrics. Designed to expose only necessary aggregated data to the client.")
public class GlobalMarketSummaryResponse {

    @Schema(description = "Total number of active cryptocurrencies currently being tracked", example = "13254")
    private Integer activeCryptocurrencies;

    @Schema(description = "Total number of active cryptocurrency exchanges or markets", example = "982")
    private Integer markets;

    @Schema(description = "Total global market capitalization in fiat currency or in USD", example = "2500000000000.50")
    private BigDecimal totalMarketCap;

    @Schema(description = "Total global trading volume across all markets in the last 24 hours", example = "125000000000.00")
    private BigDecimal totalVolume;
}