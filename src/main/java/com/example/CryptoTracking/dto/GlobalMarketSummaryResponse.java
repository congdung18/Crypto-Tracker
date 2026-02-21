package com.example.CryptoTracking.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class GlobalMarketSummaryResponse{
    private Integer activeCryptocurrencies;
    private Integer markets;
    private BigDecimal totalMarketCap;
    private BigDecimal totalVolume;
}
