package com.example.CryptoTracking.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@JsonTest
class CoinGeckoResponseTest {

    @Autowired
    private JacksonTester<CoinGeckoResponse> json;

    @Test
    void testDeserialize_HappyPath_WithExtremeCryptoValues(){
        String jsonContent = """
                {
                    "id": "shiba-inu",
                    "symbol": "shib",
                    "name": "Shiba Inu",
                    "last_updated": "2023-10-25T14:30:00.000Z",
                    "current_price": 0.00000789,
                    "market_cap": 850000000000,
                    "price_change_percentage_7d_in_currency": -15.5
                }
                """;

        try{
            CoinGeckoResponse response = json.parse(jsonContent).getObject();


            assertThat(response.getId()).isEqualTo("shiba-inu");

            assertThat(response.getLastUpdated()).isEqualTo(Instant.parse("2023-10-25T14:30:00.000Z"));

            assertThat(response.getCurrentPrice()).isEqualTo(new BigDecimal("0.00000789"));

            assertThat(response.getMarketCap()).isEqualTo(850000000000L);

            assertThat(response.getPriceChangePercentage7d()).isEqualTo(-15.5);

            assertThat(response.getTotalVolume()).isNull();
        }catch(IOException e){
            log.error("Error while parsing CoinGeckoResponse", e);
        }
    }
}