package com.example.CryptoTracking.controller;

import com.example.CryptoTracking.dto.CoinPaginationRequest;
import com.example.CryptoTracking.dto.CoinSummaryResponse;
import com.example.CryptoTracking.service.CoinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoinController.class)
class CoinControllerIT{
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CoinService coinService;

    @Test
    void getCoins_ShouldReturnPaginatedListOfCoins() throws Exception {
        CoinSummaryResponse mockCoin = new CoinSummaryResponse();

        Page<CoinSummaryResponse> mockPage = new PageImpl<>(
                List.of(mockCoin),
                PageRequest.of(0, 50),
                1
        );

        when(coinService.getCoins(any(CoinPaginationRequest.class), any(Pageable.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/v1/coins")
                        .param("page", "0")
                        .param("size", "50")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void getCoinById_ShouldReturnCoinDetails() throws Exception {
        String coinId = "bitcoin";
        CoinSummaryResponse mockCoin = new CoinSummaryResponse();

        when(coinService.getCoinById(eq(coinId))).thenReturn(mockCoin);

        mockMvc.perform(get("/api/v1/coins/{id}", coinId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}