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
class CoinSummaryResponseTest {

    @Autowired
    private JacksonTester<CoinSummaryResponse> json;

    @Test
    @DisplayName("Test convert from DTO Object to JSON string")
    void testJsonSerialization() throws Exception {
        CoinSummaryResponse response = CoinSummaryResponse.builder()
                .symbol("btc")
                .name("Bitcoin")
                .image("https://assets.coingecko.com/coins/images/1/large/bitcoin.png")
                .price(new BigDecimal("65432.10"))
                .marketCapRank(1)
                .priceChangePercentage24h(2.35)
                .build();

        JsonContent<CoinSummaryResponse> result = json.write(response);

        assertThat(result).hasJsonPathStringValue("@.symbol");
        assertThat(result).extractingJsonPathStringValue("@.symbol").isEqualTo("btc");

        assertThat(result).extractingJsonPathStringValue("@.name").isEqualTo("Bitcoin");
        assertThat(result).extractingJsonPathStringValue("@.image").isEqualTo("https://assets.coingecko.com/coins/images/1/large/bitcoin.png");

        assertThat(result).extractingJsonPathNumberValue("@.price").isEqualTo(65432.10);
        assertThat(result).extractingJsonPathNumberValue("@.marketCapRank").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("@.priceChangePercentage24h").isEqualTo(2.35);
    }
}