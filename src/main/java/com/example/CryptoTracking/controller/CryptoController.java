package com.example.CryptoTracking.controller;

import com.example.CryptoTracking.dto.CoinPaginationRequest;
import com.example.CryptoTracking.dto.CoinSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.CryptoTracking.service.CoinService;

// Later will use MockMvc to test
@RestController
@RequestMapping("/api/v1/coins")
@CrossOrigin(origins = "*")
public class CryptoController {
    @Autowired
    private CoinService coinService;

    @GetMapping
    public ResponseEntity<Page<CoinSummaryResponse>> getCoins(
            CoinPaginationRequest coinPaginationRequest,
            @PageableDefault(page = 0, size = 50, sort = "marketCapRank", direction = Sort.Direction.ASC)
            Pageable pageable) {
        Page<CoinSummaryResponse> coinSummaryResponsePage = coinService.getCoins(coinPaginationRequest, pageable);

        return ResponseEntity.ok(coinSummaryResponsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoinSummaryResponse> getCoinById(@PathVariable String id) {
        CoinSummaryResponse coinSummaryResponse = coinService.getCoinById(id);

        return ResponseEntity.ok(coinSummaryResponse);
    }
}