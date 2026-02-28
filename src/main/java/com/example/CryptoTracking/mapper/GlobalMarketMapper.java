package com.example.CryptoTracking.mapper;

import com.example.CryptoTracking.dto.GlobalDataResponse;
import com.example.CryptoTracking.dto.GlobalMarketSummaryResponse;
import com.example.CryptoTracking.entity.GlobalMarket;
import org.springframework.stereotype.Component;

import java.time.*;
import java.math.*;

@Component
public class GlobalMarketMapper {
    public GlobalMarket mapGlobalMarketToEntity(GlobalDataResponse response){
        if (response == null || response.getData() == null){
            return null;
        }

        GlobalDataResponse.DataObj data = response.getData();

        return GlobalMarket.builder()
                .activeCryptocurrencies(data.getActiveCryptocurrencies())
                .markets(data.getMarkets())
                .totalMarketCapUsd(data.getTotalMarketCap().getOrDefault("usd", BigDecimal.ZERO))
                .totalVolumeUsd(data.getTotalVolume().getOrDefault("usd", BigDecimal.ZERO))
                .marketCapChangePercentage24hUsd(data.getMarketCapChangePercentage24hUsd())
                .btcDominance(data.getMarketCapPercentage().getOrDefault("btc", 0.0))
                .updatedAt(Instant.ofEpochSecond(data.getUpdatedAt()))
                .build();
    }

    public GlobalMarketSummaryResponse mapGlobalMarketToSummaryDto(GlobalMarket globalMarket){
        if (globalMarket == null){
            return null;
        }

        return GlobalMarketSummaryResponse.builder()
                .activeCryptocurrencies(globalMarket.getActiveCryptocurrencies())
                .markets(globalMarket.getMarkets())
                .totalMarketCap(globalMarket.getTotalMarketCapUsd())
                .totalVolume(globalMarket.getTotalVolumeUsd())
                .build();
    }
}
