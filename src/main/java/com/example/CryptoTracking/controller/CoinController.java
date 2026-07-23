package com.example.CryptoTracking.controller;

import com.example.CryptoTracking.dto.*;
import com.example.CryptoTracking.entity.CoinPriceHistory;
import com.example.CryptoTracking.service.CoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coins")
@CrossOrigin(origins = "*")
@Tag(name = "Coins", description = "Endpoints for retrieving and searching cryptocurrency data")
public class CoinController {
    @Autowired
    private CoinService coinService;

    @Operation(
            summary = "Get a paginated list of coins",
            description = "Retrieves a list of cryptocurrencies. Supports filtering by price, rank, name, and pagination."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of coins")
    })
    @GetMapping
    public ResponseEntity<Page<CoinSummaryResponse>> getCoins(
            @ParameterObject CoinPaginationRequest coinPaginationRequest,
            @ParameterObject @PageableDefault(page = 0, size = 50, sort = "marketCapRank", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<CoinSummaryResponse> coinSummaryResponsePage = coinService.getCoins(coinPaginationRequest, pageable);
        return ResponseEntity.ok(coinSummaryResponsePage);
    }

    @Operation(
            summary = "Get coin details by ID",
            description = "Retrieves summarized information for a specific cryptocurrency using its system identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the coin details"),
            @ApiResponse(responseCode = "404", description = "Coin not found with the provided ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CoinSummaryResponse> getCoinById(
            @Parameter(description = "The unique system identifier of the coin", example = "bitcoin")
            @PathVariable String id) {

        CoinSummaryResponse coinSummaryResponse = coinService.getCoinById(id);
        return ResponseEntity.ok(coinSummaryResponse);
    }

    @Operation(
            summary = "Get coin exchange tickers and markets",
            description = "Retrieves active exchange listings and target trading pairs for a specific cryptocurrency."
    )
    @GetMapping("/{id}/tickers")
    public ResponseEntity<CoinTickerResponse> getCoinTickers(
            @PathVariable String id) {
        return ResponseEntity.ok(coinService.getCoinTickers(id));
    }

    @Operation(
            summary = "Get local historical price data",
            description = "Retrieves stored price history snapshots for a specific coin from the local database."
    )
    @GetMapping("/{id}/history")
    public ResponseEntity<List<CoinPriceHistory>> getCoinHistory(
            @PathVariable String id) {
        return ResponseEntity.ok(coinService.getCoinPriceHistory(id));
    }

    @Operation(
            summary = "Get coin historical market chart from CoinGecko",
            description = "Retrieves historical price coordinates from the CoinGecko API."
    )
    @GetMapping("/{id}/chart")
    public ResponseEntity<MarketChartResponse> getCoinMarketChart(
            @PathVariable String id,
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(coinService.getCoinMarketChart(id, days));
    }
}