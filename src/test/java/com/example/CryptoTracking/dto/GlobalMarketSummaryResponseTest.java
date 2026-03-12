package com.example.CryptoTracking.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GlobalMarketSummaryResponseTest {

    @Autowired
    private JacksonTester<GlobalMarketSummaryResponse> json;

    @Test
    @DisplayName("Test convert GlobalMarketSummaryResponse Object to JSON string")
    void testJsonSerialization() throws Exception {
        GlobalMarketSummaryResponse response = GlobalMarketSummaryResponse.builder()
                .activeCryptocurrencies(13254)
                .markets(982)
                .totalMarketCap(new BigDecimal("2500000000000.50"))
                .totalVolume(new BigDecimal("125000000000.00"))
                .build();

        JsonContent<GlobalMarketSummaryResponse> result = json.write(response);

        assertThat(result).extractingJsonPathNumberValue("@.activeCryptocurrencies").isEqualTo(13254);
        assertThat(result).extractingJsonPathNumberValue("@.markets").isEqualTo(982);

        assertThat(result).extractingJsonPathNumberValue("@.totalMarketCap").isEqualTo(2500000000000.50);
        assertThat(result).extractingJsonPathNumberValue("@.totalVolume").isEqualTo(125000000000.00);
    }
}