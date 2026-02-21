package com.example.CryptoTracking.dto;

import lombok.Data;

import java.math.BigDecimal;

/*
Contain every parameter clients request.
However, this approach is only used because there are not many criteria in Coins
 */

@Data
public class CoinPaginationRequest {
    private String id;
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minMarketCapRank;
    private Integer maxMarketCapRank;
}
