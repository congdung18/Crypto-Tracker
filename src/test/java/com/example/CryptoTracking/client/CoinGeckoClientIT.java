package com.example.CryptoTracking.client;

import com.example.CryptoTracking.config.RestTemplateConfiguration;
import com.example.CryptoTracking.dto.GlobalDataResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(
        value = CoinGeckoClient.class,
        properties = {
                "coingecko.api.connect.timeout=5000",
                "coingecko.api.read.timeout=5000",
                "coingecko.api.key=test-api-key",
                "coingecko.api.url=https://api.coingecko.com/api/v3"
        }
)
@Import(RestTemplateConfiguration.class)
public class CoinGeckoClientIT {
    @Autowired
    private CoinGeckoClient coinGeckoClient;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Test
    void getGlobalMarket_shouldReturnParsedResponse(){
        String coinGeckoUrl = "https://api.coingecko.com/api/v3/global";
        String mockData = """
                {
                "data":{
                    "active_cryptocurrencies": 100,
                    "markets": 100,
                    "total_market_cap":{
                        "btc": 36.18,
                        "eth": 18.36
                    },
                    "total_volume":{
                        "btc":36.18,
                        "eth":18.36
                    },
                    "market_cap_percentage":{
                        "btc": 36,
                        "eth": 18.36
                    },
                    "market_cap_change_percentage_24h_usd": 0.36,
                    "updated_at": 1836183618
                    }
                }
                """;

        mockRestServiceServer.expect(requestTo(coinGeckoUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mockData, MediaType.APPLICATION_JSON));

        GlobalDataResponse response = coinGeckoClient.getGlobalMarket();

        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getActiveCryptocurrencies()).isEqualTo(100);
        assertThat(response.getData().getMarkets()).isEqualTo(100);
        assertThat(response.getData().getTotalMarketCap()).isNotNull();
        assertThat(response.getData().getTotalVolume()).isNotNull();
        assertThat(response.getData().getMarketCapPercentage()).isNotNull();
        assertThat(response.getData().getMarketCapChangePercentage24hUsd()).isEqualTo(0.36);
        assertThat(response.getData().getUpdatedAt()).isEqualTo(1836183618);

        mockRestServiceServer.verify();
    }
}
