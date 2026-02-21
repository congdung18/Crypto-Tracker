package com.example.CryptoTracking.service;

import com.example.CryptoTracking.client.CoinGeckoClient;
import com.example.CryptoTracking.dto.CoinGeckoResponse;
import com.example.CryptoTracking.dto.CoinPaginationRequest;
import com.example.CryptoTracking.dto.CoinSummaryResponse;
import com.example.CryptoTracking.exception.ApplicationException;
import com.example.CryptoTracking.exception.ErrorCode;
import com.example.CryptoTracking.mapper.CoinMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.CryptoTracking.entity.Coin;
import com.example.CryptoTracking.repository.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final CoinGeckoClient coinGeckoClient;
    private final CoinRepository coinRepository;
    private final CoinMapper coinMapper;

    @Transactional
    public void fetchCoinsFromAPI(){
        List<CoinGeckoResponse> rawData = coinGeckoClient.getCoinsMarket(1, 100);
        List<Coin> data = coinMapper.mapCoinGeckoResponseToEntity(rawData);

        coinRepository.saveAll(data);
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
        return coinMapper.mapCoinToSummaryDto(coinPage);
    }

    public CoinSummaryResponse getCoinById(String id) {
        Coin coin = coinRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.APP_RESOURCE_NOT_FOUND));

        return coinMapper.mapCoinToSummaryDto(coin);
    }
}