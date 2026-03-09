package com.example.CryptoTracking.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Crypto Tracker API",
                version = "1.0.0",
                description = "API provides information about crypto currencies, fetching crypto data from CoinGecko every 5 minutes"
        ),
        servers = {
                @Server(url = "http://localhost:8088/api/v1", description = "Local development server")
        }
)
public class RestTemplateConfiguration {
    @Value("${coingecko.api.connect.timeout}")
    private Duration connectTimeout;

    @Value("${coingecko.api.read.timeout}")
    private Duration readTimeout;

    @Value("${coingecko.api.key:xx-cg-demo-api-key}")
    private String apiKey;

    @Bean
    // HttpHeader has a Map type
    public RestTemplate coinGeckoRestTemplate(RestTemplateBuilder builder){
        return builder
                .connectTimeout(connectTimeout)
                .readTimeout(readTimeout)
                .interceptors(((request, body, execution) -> {
                    request.getHeaders().add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

                    if (apiKey != null && !apiKey.isEmpty()) {
                        request.getHeaders().add("xx-cg-demo-api-key",apiKey);
                    }

                    return execution.execute(request, body);
                }))
                .build();
    }
}
