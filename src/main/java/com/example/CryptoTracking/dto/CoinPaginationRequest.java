package com.example.CryptoTracking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/*
Contain every parameter clients request.
However, this approach is only used because there are not many criteria in Coins
 */

@Data
@Schema(description = "Request object for filtering and paginating cryptocurrency data")
public class CoinPaginationRequest {

    @Schema(description = "Filter by specific coin system ID", example = "bitcoin")
    private String id;

    @Schema(description = "Filter by coin display name", example = "Bitcoin")
    private String name;

    @Schema(description = "Minimum current price threshold in USD", example = "50000.00")
    private BigDecimal minPrice;

    @Schema(description = "Maximum current price threshold in USD", example = "75000.00")
    private BigDecimal maxPrice;

    @Schema(description = "Highest market cap rank to filter", example = "1")
    private Integer minMarketCapRank;

    @Schema(description = "Lowest market cap rank to filter", example = "100")
    private Integer maxMarketCapRank;
}