package com.example.CryptoTracking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketChartResponse {

    @JsonProperty("prices")
    private List<List<BigDecimal>> prices;
}
