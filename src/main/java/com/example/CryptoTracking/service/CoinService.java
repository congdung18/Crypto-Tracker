package com.example.CryptoTracking.service;

import com.example.CryptoTracking.client.CoinGeckoClient;
import com.example.CryptoTracking.dto.CoinGeckoResponse;
import com.example.CryptoTracking.dto.CoinPaginationRequest;
import com.example.CryptoTracking.dto.CoinSummaryResponse;
import com.example.CryptoTracking.entity.Coin;
import com.example.CryptoTracking.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.CryptoTracking.exception.ApplicationException;
import com.example.CryptoTracking.exception.ErrorCode;
import com.example.CryptoTracking.mapper.CoinMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class CoinService {
    private final CoinGeckoClient coinGeckoClient;
    private final CoinRepository coinRepository;
    private final CoinMapper coinMapper;
    private final RedisTemplate<String, String> template;
    private final ObjectMapper mapper;

    @Transactional
    public void fetchCoinsFromAPI(){
        List<CoinGeckoResponse> rawData = coinGeckoClient.getCoinsMarket(1, 100);
        List<Coin> data = coinMapper.mapCoinGeckoResponseToEntity(rawData);

        coinRepository.saveAll(data);

        data.forEach(coin ->{
            try{
                CoinSummaryResponse dto = coinMapper.mapCoinToSummaryDto(coin);
                String json = mapper.writeValueAsString(dto);

                template.opsForValue().set("coinDetail::" + coin.getId(), json, Duration.ofMinutes(1));
            }catch(JsonProcessingException e){
                log.error("Serialize coin error {}: {}", coin.getId(), e.getMessage());
            }

        });
        log.info("Cache Completed");
    }

    @Transactional
    public Page<CoinSummaryResponse> getCoins(CoinPaginationRequest coinPaginationRequest, Pageable pageable) {
        Specification<Coin> specification = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (coinPaginationRequest != null) {
            if (coinPaginationRequest.getId() != null &&  !coinPaginationRequest.getId().isEmpty()) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("id")), "%" + coinPaginationRequest.getId().toLowerCase() + "%"));
            }

            if (coinPaginationRequest.getName() != null &&  !coinPaginationRequest.getName().isEmpty()) {
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), coinPaginationRequest.getName().toLowerCase() + "%"));
            }

            if (coinPaginationRequest.getMinPrice() != null){
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get("min_price"), coinPaginationRequest.getMinPrice().toPlainString()));
            }

            if (coinPaginationRequest.getMaxPrice() != null){
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get("max_price"), coinPaginationRequest.getMaxPrice().toPlainString()));
            }

            if (coinPaginationRequest.getMinMarketCapRank() != null){
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get("min_market_cap_rank"), coinPaginationRequest.getMinMarketCapRank().toString()));
            }

            if (coinPaginationRequest.getMaxMarketCapRank() != null){
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get("max_market_cap_rank"), coinPaginationRequest.getMaxMarketCapRank().toString()));
            }
        }

        Page<Coin> coinPage = coinRepository.findAll(specification, pageable);
        Page<CoinSummaryResponse> dtoPage = coinMapper.mapCoinToSummaryDto(coinPage);

        if (dtoPage.hasContent()) {
                dtoPage.getContent().forEach(coinDto -> {
                    try {
                        String json = mapper.writeValueAsString(coinDto);
                        String redisKey = "coinDetail::" + coinDto.getId().toLowerCase();
                        template.opsForValue().set(redisKey, json, Duration.ofMinutes(60));
                        
                    } catch (JsonProcessingException e) {
                        log.error("Coin Cache Error {}: {}", coinDto.getId(), e.getMessage());
                    }
                });
                log.info("Cache Completed", dtoPage.getNumberOfElements());
        }

    return dtoPage;
    }

    public CoinSummaryResponse getCoinById(String id) {
        String key = "coinDetail::" + id.toLowerCase();

        String json = template.opsForValue().get(key);
        if(json != null){
            try{
                return mapper.readValue(json, CoinSummaryResponse.class);
            }catch(JsonProcessingException e){
                log.warn("Caching Corruption!");
            }
        }
        Coin coin = coinRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.APP_RESOURCE_NOT_FOUND));
        
        CoinSummaryResponse dto = coinMapper.mapCoinToSummaryDto(coin);
        try {
            template.opsForValue().set(key, mapper.writeValueAsString(dto), Duration.ofHours(1));
        } catch (JsonProcessingException ignored) {}

        return dto;
    }
}