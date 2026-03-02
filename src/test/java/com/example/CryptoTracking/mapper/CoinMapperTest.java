package com.example.CryptoTracking.mapper;

import com.example.CryptoTracking.dto.CoinGeckoResponse;
import com.example.CryptoTracking.dto.CoinSummaryResponse;
import com.example.CryptoTracking.entity.Coin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoinMapperTest {

    private CoinMapper coinMapper;

    @BeforeEach
    void setUp() {
        coinMapper = new CoinMapper();
    }

    // Test Coin mapCoinGeckoResponseToEntity(CoinGeckoResponse)
    @Test
    void mapCoinGeckoResponseToEntity_WithCoinGeckoResponse_ShouldReturnEntity() {
        CoinGeckoResponse mockResponse = new CoinGeckoResponse();
        mockResponse.setId("bitcoin");
        mockResponse.setSymbol("btc");
        mockResponse.setName("Bitcoin");
        mockResponse.setMarketCapRank(1);

        Coin result = coinMapper.mapCoinGeckoResponseToEntity(mockResponse);

        assertNotNull(result);
        assertEquals("bitcoin", result.getId());
        assertEquals("btc", result.getSymbol());
        assertEquals("Bitcoin", result.getName());
        assertEquals(1, result.getMarketCapRank());
    }

    @Test
    void mapCoinGeckoResponseToEntity_WithNullResponse_ShouldReturnNull() {
        CoinGeckoResponse mockResponse = null;

        Coin result = coinMapper.mapCoinGeckoResponseToEntity(mockResponse);

        assertNull(result);
    }

    // Test List<Coin> mapCoinGeckoResponseToEntity(List<CoinGeckoResponse>)
    @Test
    void mapCoinGeckoResponseToEntityList_WithCoinGeckoResponse_ShouldReturnEntity() {
        CoinGeckoResponse btc = new CoinGeckoResponse();
        btc.setId("bitcoin");
        btc.setSymbol("btc");

        CoinGeckoResponse eth = new CoinGeckoResponse();
        eth.setId("ethereum");
        eth.setSymbol("eth");

        List<CoinGeckoResponse> mockList = List.of(btc, eth);

        List<Coin> result = coinMapper.mapCoinGeckoResponseToEntity(mockList);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("bitcoin", result.get(0).getId());
        assertEquals("btc", result.get(0).getSymbol());
        assertEquals("ethereum", result.get(1).getId());
        assertEquals("eth", result.get(1).getSymbol());
    }

    @Test
    void mapCoinGeckoResponseToEntityList_WithNullResponse_ShouldReturnNull() {
        List<Coin> result = coinMapper.mapCoinGeckoResponseToEntity((List<CoinGeckoResponse>) null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void mapCoinGeckoResponseToEntityList_WithCoinGeckoResponse_ShouldReturnEntityList() {
        List<Coin> result = coinMapper.mapCoinGeckoResponseToEntity(new ArrayList<>());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test CoinSummaryResponse mapCoinToSummaryDto(Coin)
    @Test
    void mapCoinToSummaryDto_WithEntity_ShouldReturnCoinSummaryDto() {
        Coin mockCoin = Coin.builder()
                .symbol("bnb")
                .name("Binance Coin")
                .image("https://example.com/bnb.png")
                .currentPrice(new BigDecimal("600.50"))
                .marketCapRank(3)
                .priceChangePercentage24h(5.5)
                .build();

        CoinSummaryResponse result = coinMapper.mapCoinToSummaryDto(mockCoin);

        assertNotNull(result);
        assertEquals("bnb", result.getSymbol());
        assertEquals("Binance Coin", result.getName());
        assertEquals("https://example.com/bnb.png", result.getImage());
        assertEquals(new BigDecimal("600.50"), result.getPrice());
        assertEquals(3, result.getMarketCapRank());
        assertEquals(5.5, result.getPriceChangePercentage24h());
    }

    @Test
    void mapCoinToSummaryDto_WithNullResponse_ShouldReturnNull() {
        CoinSummaryResponse result = coinMapper.mapCoinToSummaryDto((Coin) null);

        assertNull(result);
    }

    // Test Page<CoinSummaryResponse> mapCoinToSummaryDto(Page<Coin>)
    @Test
    void mapCoinToSummaryDtoPage_WithEntity_ShouldReturnCoinSummaryDtoPage() {
        Coin mockCoin = Coin.builder()
                .symbol("sol")
                .name("Solana")
                .currentPrice(new BigDecimal("150.0"))
                .build();

        Page<Coin> mockPage = new PageImpl<>(List.of(mockCoin));

        Page<CoinSummaryResponse> result = coinMapper.mapCoinToSummaryDto(mockPage);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        CoinSummaryResponse mappedDto = result.getContent().get(0);
        assertEquals("sol", mappedDto.getSymbol());
        assertEquals("Solana", mappedDto.getName());
        assertEquals(new BigDecimal("150.0"), mappedDto.getPrice());
    }

    @Test
    void mapCoinToSummaryDtoPage_WithNullResponse_ShouldReturnNull() {
        Page<CoinSummaryResponse> result = coinMapper.mapCoinToSummaryDto((Page<Coin>) null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}