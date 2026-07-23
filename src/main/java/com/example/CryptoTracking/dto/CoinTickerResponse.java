package com.example.CryptoTracking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinTickerResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("tickers")
    private List<Ticker> tickers;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ticker {
        @JsonProperty("base")
        private String base;

        @JsonProperty("target")
        private String target;

        @JsonProperty("market")
        private Market market;

        @JsonProperty("last")
        private BigDecimal last;

        @JsonProperty("volume")
        private BigDecimal volume;

        @JsonProperty("bid_ask_spread_percentage")
        private BigDecimal spread;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Market {
        @JsonProperty("name")
        private String name;

        @JsonProperty("identifier")
        private String identifier;
    }
}
