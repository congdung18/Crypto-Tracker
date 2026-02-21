package com.example.CryptoTracking.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CoinSummaryResponse {
    private String symbol;
    private String name;
    private String image;
    private BigDecimal price;
    private Integer marketCapRank;
    private Double priceChangePercentage24h;
}
