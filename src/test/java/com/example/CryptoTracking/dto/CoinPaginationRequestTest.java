package com.example.CryptoTracking.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CoinPaginationRequestTest {

    @Autowired
    private JacksonTester<CoinPaginationRequest> json;

    @Test
    @DisplayName("Test Lombok Getters, Setters và Equals")
    void testPojoLombok() {
        CoinPaginationRequest request1 = new CoinPaginationRequest();
        request1.setId("bitcoin");
        request1.setName("Bitcoin");
        request1.setMinPrice(new BigDecimal("50000.00"));
        request1.setMaxPrice(new BigDecimal("75000.00"));
        request1.setMinMarketCapRank(1);
        request1.setMaxMarketCapRank(100);

        CoinPaginationRequest request2 = new CoinPaginationRequest();
        request2.setId("bitcoin");
        request2.setName("Bitcoin");
        request2.setMinPrice(new BigDecimal("50000.00"));
        request2.setMaxPrice(new BigDecimal("75000.00"));
        request2.setMinMarketCapRank(1);
        request2.setMaxMarketCapRank(100);

        assertThat(request1.getId()).isEqualTo("bitcoin");
        assertThat(request1.getMinPrice()).isEqualTo(new BigDecimal("50000.00"));

        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    @DisplayName("Test convert JSON string from Entity to DTO")
    void testJsonDeserialization() throws Exception {
        String jsonContent = """
                {
                  "id": "ethereum",
                  "name": "Ethereum",
                  "minPrice": 2000.50,
                  "maxPrice": 4000.00,
                  "minMarketCapRank": 2,
                  "maxMarketCapRank": 10
                }
                """;

        CoinPaginationRequest request = json.parseObject(jsonContent);

        assertThat(request.getId()).isEqualTo("ethereum");
        assertThat(request.getName()).isEqualTo("Ethereum");
        assertThat(request.getMinPrice()).isEqualTo(new BigDecimal("2000.50"));
        assertThat(request.getMaxPrice()).isEqualTo(new BigDecimal("4000.00"));
        assertThat(request.getMinMarketCapRank()).isEqualTo(2);
        assertThat(request.getMaxMarketCapRank()).isEqualTo(10);
    }

    @Test
    @DisplayName("Test DTO when Client send JSON without values")
    void testJsonDeserializationWithMissingFields() throws Exception {
        String jsonContent = """
                {
                  "id": "bitcoin"
                }
                """;

        CoinPaginationRequest request = json.parseObject(jsonContent);

        assertThat(request.getId()).isEqualTo("bitcoin");
        assertThat(request.getName()).isNull();
        assertThat(request.getMinPrice()).isNull();
    }
}