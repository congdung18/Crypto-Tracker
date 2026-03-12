package com.example.CryptoTracking.service;

import com.example.CryptoTracking.client.CoinGeckoClient;
import com.example.CryptoTracking.dto.CoinGeckoResponse;
import com.example.CryptoTracking.dto.CoinPaginationRequest;
import com.example.CryptoTracking.dto.CoinSummaryResponse;
import com.example.CryptoTracking.entity.Coin;
import com.example.CryptoTracking.exception.ApplicationException;
import com.example.CryptoTracking.exception.ErrorCode;
import com.example.CryptoTracking.mapper.CoinMapper;
import com.example.CryptoTracking.repository.CoinRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinServiceTest {

    @Mock
    private CoinGeckoClient coinGeckoClient;

    @Mock
    private CoinRepository coinRepository;

    @Mock
    private CoinMapper coinMapper;

    @InjectMocks
    private CoinService coinService;

    @Test
    void fetchCoinsFromAPI_Success() {
        List<CoinGeckoResponse> mockRawData = List.of(new CoinGeckoResponse());
        List<Coin> mockEntities = List.of(new Coin());

        when(coinGeckoClient.getCoinsMarket(1, 100)).thenReturn(mockRawData);
        when(coinMapper.mapCoinGeckoResponseToEntity(mockRawData)).thenReturn(mockEntities);

        coinService.fetchCoinsFromAPI();

        verify(coinGeckoClient, times(1)).getCoinsMarket(1, 100);
        verify(coinMapper, times(1)).mapCoinGeckoResponseToEntity(mockRawData);
        verify(coinRepository, times(1)).saveAll(mockEntities);
    }

    @Test
    void getCoinById_Found_ReturnsDto() {
        String coinId = "bitcoin";
        Coin mockCoin = new Coin();
        mockCoin.setId(coinId);

        CoinSummaryResponse mockDto = new CoinSummaryResponse();

        when(coinRepository.findById(coinId)).thenReturn(Optional.of(mockCoin));
        when(coinMapper.mapCoinToSummaryDto(mockCoin)).thenReturn(mockDto);

        CoinSummaryResponse result = coinService.getCoinById(coinId);

        assertNotNull(result);
        assertEquals(mockDto, result);
        verify(coinRepository).findById(coinId);
    }

    @Test
    void getCoinById_NotFound_ThrowsException() {
        String coinId = "invalid_id";
        when(coinRepository.findById(coinId)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            coinService.getCoinById(coinId);
        });

        assertEquals(ErrorCode.APP_RESOURCE_NOT_FOUND, exception.getErrorCode());
        verify(coinRepository).findById(coinId);
        verifyNoInteractions(coinMapper);
    }

    @Test
    void getCoins_Success() {
        CoinPaginationRequest request = new CoinPaginationRequest();
        request.setId("bit");
        Pageable pageable = PageRequest.of(0, 10);

        Page<Coin> mockCoinPage = new PageImpl<>(List.of(new Coin()));
        Page<CoinSummaryResponse> mockDtoPage = new PageImpl<>(List.of(new CoinSummaryResponse()));

        when(coinRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(mockCoinPage);
        when(coinMapper.mapCoinToSummaryDto(mockCoinPage)).thenReturn(mockDtoPage);

        Page<CoinSummaryResponse> result = coinService.getCoins(request, pageable);

        assertNotNull(result);
        assertEquals(mockDtoPage, result);
        verify(coinRepository).findAll(any(Specification.class), eq(pageable));
        verify(coinMapper).mapCoinToSummaryDto(mockCoinPage);
    }

    @Test
    void getCoins_WithAllFilters_Success() {
        CoinPaginationRequest request = new CoinPaginationRequest();
        request.setId("bit");
        request.setName("Bitcoin");
        request.setMinPrice(java.math.BigDecimal.valueOf(1000));
        request.setMaxPrice(java.math.BigDecimal.valueOf(100000));
        request.setMinMarketCapRank(1);
        request.setMaxMarketCapRank(10);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Coin> mockCoinPage = new PageImpl<>(List.of(new Coin()));
        Page<CoinSummaryResponse> mockDtoPage = new PageImpl<>(List.of(new CoinSummaryResponse()));

        when(coinRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(mockCoinPage);
        when(coinMapper.mapCoinToSummaryDto(mockCoinPage)).thenReturn(mockDtoPage);

        Page<CoinSummaryResponse> result = coinService.getCoins(request, pageable);

        assertNotNull(result);
        assertEquals(mockDtoPage, result);
        verify(coinRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void getCoins_WithNullRequest_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Coin> mockCoinPage = new PageImpl<>(List.of(new Coin()));
        Page<CoinSummaryResponse> mockDtoPage = new PageImpl<>(List.of(new CoinSummaryResponse()));

        when(coinRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(mockCoinPage);
        when(coinMapper.mapCoinToSummaryDto(mockCoinPage)).thenReturn(mockDtoPage);

        Page<CoinSummaryResponse> result = coinService.getCoins(null, pageable);

        assertNotNull(result);
        assertEquals(mockDtoPage, result);
        verify(coinRepository).findAll(any(Specification.class), eq(pageable));
    }
}