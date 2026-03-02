package com.example.CryptoTracking.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GlobalDataResponseTest {

    @Autowired
    private JacksonTester<GlobalDataResponse> json;

    @Test
    @DisplayName("Test parse JSON from CoinGecko /global API to GlobalDataResponse DTO")
    void testDeserializeGlobalDataJson() throws Exception {
        String jsonContent = """
                {
                  "data": {
                    "active_cryptocurrencies": 13254,
                    "markets": 982,
                    "total_market_cap": {
                      "usd": 2500000000000.50,
                      "btc": 45000000.0
                    },
                    "total_volume": {
                      "usd": 125000000000.00
                    },
                    "market_cap_change_percentage_24h_usd": 1.52,
                    "market_cap_percentage": {
                      "btc": 51.2,
                      "eth": 16.8
                    },
                    "updated_at": 1708147200
                  }
                }
                """;

        GlobalDataResponse response = json.parseObject(jsonContent);

        assertThat(response).isNotNull();
        GlobalDataResponse.DataObj dataObj = response.getData();
        assertThat(dataObj).isNotNull();

        assertThat(dataObj.getActiveCryptocurrencies()).isEqualTo(13254);
        assertThat(dataObj.getMarkets()).isEqualTo(982);
        assertThat(dataObj.getMarketCapChangePercentage24hUsd()).isEqualTo(1.52);
        assertThat(dataObj.getUpdatedAt()).isEqualTo(1708147200L);

        Map<String, BigDecimal> totalMarketCap = dataObj.getTotalMarketCap();
        assertThat(totalMarketCap).isNotNull()
                .containsEntry("usd", new BigDecimal("2500000000000.50"))
                .containsEntry("btc", new BigDecimal("45000000.0"));

        Map<String, BigDecimal> totalVolume = dataObj.getTotalVolume();
        assertThat(totalVolume).isNotNull()
                .containsEntry("usd", new BigDecimal("125000000000.00"));

        Map<String, Double> marketCapPercentage = dataObj.getMarketCapPercentage();
        assertThat(marketCapPercentage).isNotNull()
                .containsEntry("btc", 51.2)
                .containsEntry("eth", 16.8);
    }
}