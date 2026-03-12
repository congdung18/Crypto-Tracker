package com.example.CryptoTracking.service;

import com.example.CryptoTracking.client.CoinGeckoClient;
import com.example.CryptoTracking.dto.GlobalDataResponse;
import com.example.CryptoTracking.dto.GlobalMarketSummaryResponse;
import com.example.CryptoTracking.entity.GlobalMarket;
import com.example.CryptoTracking.mapper.GlobalMarketMapper;
import com.example.CryptoTracking.repository.GlobalMarketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalMarketServiceTest {

    @Mock
    private CoinGeckoClient coinGeckoClient;

    @Mock
    private GlobalMarketRepository globalMarketRepository;

    @Mock
    private GlobalMarketMapper globalMarketMapper;

    @InjectMocks
    private GlobalMarketService globalMarketService;

    @Test
    void fetchGlobalMarketsFromAPI_Success() {
        GlobalDataResponse mockResponse = new GlobalDataResponse();
        GlobalMarket mockEntity = new GlobalMarket();

        when(coinGeckoClient.getGlobalMarket()).thenReturn(mockResponse);
        when(globalMarketMapper.mapGlobalMarketToEntity(mockResponse)).thenReturn(mockEntity);

        globalMarketService.fetchGlobalMarketsFromAPI();

        verify(coinGeckoClient, times(1)).getGlobalMarket();
        verify(globalMarketMapper, times(1)).mapGlobalMarketToEntity(mockResponse);
        verify(globalMarketRepository, times(1)).save(mockEntity);
    }

    @Test
    void getGlobalMarket_Success() {
        GlobalMarket mockEntity = new GlobalMarket();
        GlobalMarketSummaryResponse mockSummaryDto = new GlobalMarketSummaryResponse();

        when(globalMarketRepository.findTopByOrderByUpdatedAtDesc()).thenReturn(mockEntity);
        when(globalMarketMapper.mapGlobalMarketToSummaryDto(mockEntity)).thenReturn(mockSummaryDto);

        GlobalMarketSummaryResponse result = globalMarketService.getGlobalMarket();

        assertNotNull(result);
        assertEquals(mockSummaryDto, result);
        verify(globalMarketRepository, times(1)).findTopByOrderByUpdatedAtDesc();
        verify(globalMarketMapper, times(1)).mapGlobalMarketToSummaryDto(mockEntity);
    }
}