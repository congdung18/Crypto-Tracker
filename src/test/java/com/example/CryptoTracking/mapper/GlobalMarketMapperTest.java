package com.example.CryptoTracking.mapper;

import com.example.CryptoTracking.dto.GlobalDataResponse;
import com.example.CryptoTracking.entity.GlobalMarket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalMarketMapperTest {
    private GlobalMarketMapper mapper = new GlobalMarketMapper();

    @BeforeEach
    public void setup(){
        mapper = new GlobalMarketMapper();
    }

    @Test
    void mapGlobalMarketToEntity_WithValidData_ShouldReturnEntity(){
        GlobalDataResponse.DataObj dataObj = new GlobalDataResponse.DataObj();

        dataObj.setActiveCryptocurrencies(1000);
        dataObj.setMarkets(500);
        dataObj.setTotalMarketCap(Map.of("usd", new BigDecimal("1500000.366718")));
        dataObj.setTotalVolume(Map.of("usd", new BigDecimal("1500000.366718")));
        dataObj.setMarketCapChangePercentage24hUsd(3.6);
        dataObj.setMarketCapPercentage(Map.of("btc", 36.18));
        dataObj.setUpdatedAt(1836670000L);

        GlobalDataResponse globalDataResponse = new GlobalDataResponse();
        globalDataResponse.setData(dataObj);

        GlobalMarket result = mapper.mapGlobalMarketToEntity(globalDataResponse);

        assertNotNull(result);
        assertEquals(1000, result.getActiveCryptocurrencies());
        assertEquals(500, result.getMarkets());
        assertEquals(new BigDecimal("1500000.366718"), result.getTotalMarketCapUsd());
        assertEquals(new BigDecimal("1500000.366718"), result.getTotalVolumeUsd());
        assertEquals(3.6,  result.getMarketCapChangePercentage24hUsd());
        assertEquals(36.18, result.getBtcDominance());
        assertEquals(Instant.ofEpochSecond(1836670000L), result.getUpdatedAt());
    }

    @Test
    void mapGlobalMarketToEntity_WithNullResponse_ShouldReturnNull() {
        assertNull(mapper.mapGlobalMarketToEntity(null));
    }
}
