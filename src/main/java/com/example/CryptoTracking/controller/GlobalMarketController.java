package com.example.CryptoTracking.controller;

import com.example.CryptoTracking.dto.GlobalMarketSummaryResponse;
import com.example.CryptoTracking.exception.ErrorResponse;
import com.example.CryptoTracking.service.GlobalMarketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/global")
@CrossOrigin(origins = "*")
@Tag(name = "Global Market", description = "Endpoints for retrieving overall global cryptocurrency market data")
public class GlobalMarketController {

    @Autowired
    private GlobalMarketService globalMarketService;

    @Operation(
            summary = "Get global market summary",
            description = "Fetches a summarized view of the global cryptocurrency market including total market cap, 24h volume, and active coin count."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the global market data"),

            @ApiResponse(responseCode = "500", description = "Internal server error while fetching data from third-party API",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<GlobalMarketSummaryResponse> getGlobalMarketData() {
        GlobalMarketSummaryResponse data = globalMarketService.getGlobalMarket();
        return ResponseEntity.ok(data);
    }
}