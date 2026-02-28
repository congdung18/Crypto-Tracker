package com.example.CryptoTracking.client;

import com.example.CryptoTracking.dto.CoinGeckoResponse;
import com.example.CryptoTracking.dto.GlobalDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoinGeckoClient {

    private final RestTemplate restTemplate;

    @Value("${coingecko.api.url:https://api.coingecko.com/api/v3}")
    private String baseUrl;

    public List<CoinGeckoResponse> getCoinsMarket(int page, int perPage){
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/coins/markets")
                .queryParam("vs_currency", "usd")
                .queryParam("order", "market_cap_desc")
                .queryParam("per_page", perPage)
                .queryParam("page", page)
                .queryParam("sparkline", false)
                .queryParam("price_change_percentage", "1h%24h%7d")
                .queryParam("locale", "en")
                .queryParam("precision", 10)
                .queryParam("include_rehypothecated", false)
                .toUriString();

        var response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CoinGeckoResponse>>() {
                }
        );

        return response.getBody();
    }

    public GlobalDataResponse getGlobalMarket(){
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/global")
                .toUriString();

        var response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<GlobalDataResponse>() {}
        );

        return response.getBody();
    }
}