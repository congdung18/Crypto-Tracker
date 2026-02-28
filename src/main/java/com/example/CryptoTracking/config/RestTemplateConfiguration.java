package com.example.CryptoTracking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {
    @Value("${coingecko.api.connect.timeout}")
    private int connectTimeout;

    @Value("${coingecko.api.read.timeout}")
    private int readTimeout;

    @Value("${coingecko.api.key:xx-cg-demo-api-key}")
    private String apiKey;

    @Bean
    // HttpHeader has a Map type
    public RestTemplate restTemplate(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);

        RestTemplate restTemplate = new RestTemplate(factory);

        restTemplate.getInterceptors().add(((request, body, execution) -> {
            request.getHeaders().add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

            if (apiKey != null && !apiKey.isBlank()){
                request.getHeaders().add("xx-cg-demo-api-key", apiKey);
            }

            return execution.execute(request, body);
        }));

        return restTemplate;
    }
}
