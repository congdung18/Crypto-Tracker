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
                .id("bitcoin")
                .symbol("btc")
                .name("Bitcoin")
                .image("https://assets.coingecko.com/coins/images/1/large/bitcoin.png")
                .price(new BigDecimal("65432.10"))
                .marketCapRank(1)
                .marketCap(1200000000L)
                .totalVolume(new BigDecimal("45000000"))
                .high24h(new BigDecimal("68000.00"))
                .low24h(new BigDecimal("64000.00"))
                .priceChangePercentage24h(2.35)
                .lastUpdated(java.time.Instant.parse("2026-07-23T14:44:15Z"))
                .build();

        JsonContent<CoinSummaryResponse> result = json.write(response);

        assertThat(result).hasJsonPathStringValue("@.id");
        assertThat(result).extractingJsonPathStringValue("@.id").isEqualTo("bitcoin");

        assertThat(result).hasJsonPathStringValue("@.symbol");
        assertThat(result).extractingJsonPathStringValue("@.symbol").isEqualTo("btc");

        assertThat(result).extractingJsonPathStringValue("@.name").isEqualTo("Bitcoin");
        assertThat(result).extractingJsonPathStringValue("@.image").isEqualTo("https://assets.coingecko.com/coins/images/1/large/bitcoin.png");

        assertThat(result).extractingJsonPathNumberValue("@.price").isEqualTo(65432.10);
        assertThat(result).extractingJsonPathNumberValue("@.marketCapRank").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("@.marketCap").isEqualTo(1200000000);
        assertThat(result).extractingJsonPathNumberValue("@.totalVolume").isEqualTo(45000000);
        assertThat(result).extractingJsonPathNumberValue("@.high24h").isEqualTo(68000.0);
        assertThat(result).extractingJsonPathNumberValue("@.low24h").isEqualTo(64000.0);
        assertThat(result).extractingJsonPathNumberValue("@.priceChangePercentage24h").isEqualTo(2.35);
        assertThat(result).extractingJsonPathStringValue("@.lastUpdated").isEqualTo("2026-07-23T14:44:15Z");
    }
}