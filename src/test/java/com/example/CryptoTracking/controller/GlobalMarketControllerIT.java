package com.example.CryptoTracking.controller;

import com.example.CryptoTracking.dto.GlobalMarketSummaryResponse;
import com.example.CryptoTracking.service.GlobalMarketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GlobalMarketController.class)
class GlobalMarketControllerIT{

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GlobalMarketService globalMarketService;

    @Test
    void getGlobalMarketData_ShouldReturn200AndMarketData() throws Exception {
        GlobalMarketSummaryResponse mockResponse = new GlobalMarketSummaryResponse();

        when(globalMarketService.getGlobalMarket()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/global")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getGlobalMarketData_ShouldReturn500_WhenServiceFails() throws Exception {
        when(globalMarketService.getGlobalMarket())
                .thenThrow(new RuntimeException("Simulated third-party API failure"));

        mockMvc.perform(get("/api/v1/global")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}