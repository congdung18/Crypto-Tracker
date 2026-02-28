package com.example.CryptoTracking.service;

import com.example.CryptoTracking.client.CoinGeckoClient;
import com.example.CryptoTracking.dto.GlobalDataResponse;
import com.example.CryptoTracking.dto.GlobalMarketSummaryResponse;
import com.example.CryptoTracking.entity.GlobalMarket;
import com.example.CryptoTracking.mapper.GlobalMarketMapper;
import com.example.CryptoTracking.repository.GlobalMarketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GlobalMarketService {
    private final CoinGeckoClient coinGeckoClient;
    private final GlobalMarketRepository globalMarketRepository;
    private final GlobalMarketMapper globalMarketMapper;

    @Transactional
    public void fetchGlobalMarketsFromAPI(){
        GlobalDataResponse globalDataResponse = coinGeckoClient.getGlobalMarket();
        GlobalMarket globalMarket = globalMarketMapper.mapGlobalMarketToEntity(globalDataResponse);

        globalMarketRepository.save(globalMarket);
    }

    @Transactional
    public GlobalMarketSummaryResponse getGlobalMarket(){
        GlobalMarket globalMarket = globalMarketRepository.findTopByOrderByUpdatedAtDesc();
        return globalMarketMapper.mapGlobalMarketToSummaryDto(globalMarket);
    }
}
