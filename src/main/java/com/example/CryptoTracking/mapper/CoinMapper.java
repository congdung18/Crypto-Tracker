package com.example.CryptoTracking.mapper;

import com.example.CryptoTracking.dto.CoinGeckoResponse;
import com.example.CryptoTracking.dto.CoinSummaryResponse;
import com.example.CryptoTracking.entity.Coin;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CoinMapper {
    public Coin mapCoinGeckoResponseToEntity(CoinGeckoResponse response){
        if (response == null){
            return null;
        }

        return Coin.builder()
                .id(response.getId())
                .symbol(response.getSymbol())
                .name(response.getName())
                .image(response.getImage())
                .currentPrice(response.getCurrentPrice())
                .marketCap(response.getMarketCap())
                .marketCapRank(response.getMarketCapRank())
                .totalVolume(response.getTotalVolume())
                .high24h(response.getHigh24h())
                .low24h(response.getLow24h())
                .priceChangePercentage24h(response.getPriceChangePercentage24h())
                .priceChangePercentage1h(response.getPriceChangePercentage1h())
                .priceChangePercentage7d(response.getPriceChangePercentage7d())
                .lastUpdated(response.getLastUpdated())
                .build();
    }

    public List<Coin> mapCoinGeckoResponseToEntity(List<CoinGeckoResponse> response){
        if (response == null || response.isEmpty()){
            return Collections.emptyList();
        }

        return response.stream()
                .map(this::mapCoinGeckoResponseToEntity)
                .toList();
    }

    public Page<CoinSummaryResponse> mapCoinToSummaryDto(Page<Coin> coinPage){
        if (coinPage == null){
            return Page.empty();
        }
        return coinPage.map(this::mapCoinToSummaryDto);
    }

    public CoinSummaryResponse mapCoinToSummaryDto(Coin response){
        if (response == null){
            return null;
        }

        return CoinSummaryResponse.builder()
                .symbol(response.getSymbol())
                .name(response.getName())
                .image(response.getImage())
                .price(response.getCurrentPrice())
                .marketCapRank(response.getMarketCapRank())
                .priceChangePercentage24h(response.getPriceChangePercentage24h())
                .build();
    }
}
