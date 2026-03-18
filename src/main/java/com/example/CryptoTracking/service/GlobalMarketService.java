package com.example.CryptoTracking.service;

import com.example.CryptoTracking.client.CoinGeckoClient;
import com.example.CryptoTracking.dto.GlobalDataResponse;
import com.example.CryptoTracking.dto.GlobalMarketSummaryResponse;
import com.example.CryptoTracking.entity.GlobalMarket;
import com.example.CryptoTracking.mapper.GlobalMarketMapper;
import com.example.CryptoTracking.repository.GlobalMarketRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GlobalMarketService {
    private final CoinGeckoClient coinGeckoClient;
    private final GlobalMarketRepository globalMarketRepository;
    private final GlobalMarketMapper globalMarketMapper;

    @Autowired
    private RedisTemplate<String, String> template;

    @Autowired
    private ObjectMapper mapper;

    @Transactional
    public void fetchGlobalMarketsFromAPI(){
        GlobalDataResponse globalDataResponse = coinGeckoClient.getGlobalMarket();
        GlobalMarket globalMarket = globalMarketMapper.mapGlobalMarketToEntity(globalDataResponse);

        globalMarketRepository.save(globalMarket);
    }

    public void refreshGobalDataCache(GlobalDataResponse data){
        try{
            String json = mapper.writeValueAsString(data.getData());
            template.opsForValue().set("globalData::current", json, Duration.ofMinutes(10));

            log.info("Redis Updated!");
        }catch(JsonProcessingException e){
            log.error("Cache Corruption!");
        }
    }

    @Transactional
    public GlobalMarketSummaryResponse getGlobalMarket(){
        String json = template.opsForValue().get("globalData::current");

        if(json != null){
            try{
                return mapper.readValue(json, GlobalMarketSummaryResponse.class);
            }catch(JsonProcessingException e){
                log.error("Cache Corruption!");
            }
        }

        GlobalMarket globalMarket = globalMarketRepository.findTopByOrderByUpdatedAtDesc();
        return globalMarketMapper.mapGlobalMarketToSummaryDto(globalMarket);
    }
}
