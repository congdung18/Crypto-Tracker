package com.example.CryptoTracking.controller;

import com.example.CryptoTracking.dto.CoinPaginationRequest;
import com.example.CryptoTracking.dto.CoinSummaryResponse;
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
}