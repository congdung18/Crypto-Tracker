package com.example.CryptoTracking.service;

import com.example.CryptoTracking.client.CoinGeckoClient;
import com.example.CryptoTracking.dto.*;
import com.example.CryptoTracking.entity.*;
import com.example.CryptoTracking.exception.ApplicationException;
import com.example.CryptoTracking.exception.ErrorCode;
import com.example.CryptoTracking.mapper.CoinMapper;
import com.example.CryptoTracking.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final CoinGeckoClient coinGeckoClient;
    private final CoinRepository coinRepository;
    private final CoinMapper coinMapper;
    private final CoinPriceHistoryRepository coinPriceHistoryRepository;

    @Transactional
    public void fetchCoinsFromAPI(){
        List<CoinGeckoResponse> rawData = coinGeckoClient.getCoinsMarket(1, 100);
        List<Coin> data = coinMapper.mapCoinGeckoResponseToEntity(rawData);

        coinRepository.saveAll(data);

        // Record a price snapshot for each fetched coin in the local price history
        Instant now = Instant.now();
        List<CoinPriceHistory> snapshots = data.stream()
                .filter(coin -> coin.getId() != null && coin.getCurrentPrice() != null)
                .map(coin -> CoinPriceHistory.builder()
                        .coinId(coin.getId())
                        .price(coin.getCurrentPrice())
                        .timestamp(now)
                        .build())
                .toList();
        coinPriceHistoryRepository.saveAll(snapshots);
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
                        criteriaBuilder.greaterThanOrEqualTo(root.get("currentPrice"), coinPaginationRequest.getMinPrice()));
            }

            if (coinPaginationRequest.getMaxPrice() != null){
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get("currentPrice"), coinPaginationRequest.getMaxPrice()));
            }

            if (coinPaginationRequest.getMinMarketCapRank() != null){
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get("marketCapRank"), coinPaginationRequest.getMinMarketCapRank()));
            }

            if (coinPaginationRequest.getMaxMarketCapRank() != null){
                specification = specification.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get("marketCapRank"), coinPaginationRequest.getMaxMarketCapRank()));
            }
        }

        Page<Coin> coinPage = coinRepository.findAll(specification, pageable);
        return coinMapper.mapCoinToSummaryDto(coinPage);
    }

    public CoinSummaryResponse getCoinById(String id) {
        Coin coin = coinRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.APP_RESOURCE_NOT_FOUND));

        return coinMapper.mapCoinToSummaryDto(coin);
    }

    public CoinTickerResponse getCoinTickers(String coinId) {
        return coinGeckoClient.getCoinTickers(coinId);
    }

    public List<CoinPriceHistory> getCoinPriceHistory(String coinId) {
        return coinPriceHistoryRepository.findAllByCoinIdOrderByTimestampAsc(coinId);
    }

    @Transactional
    public void purgeOldHistory() {
        Instant threshold = Instant.now().minus(Duration.ofDays(7));
        coinPriceHistoryRepository.deleteOldHistory(threshold);
    }

    public MarketChartResponse getCoinMarketChart(String coinId, int days) {
        return coinGeckoClient.getCoinMarketChart(coinId, days);
    }
}