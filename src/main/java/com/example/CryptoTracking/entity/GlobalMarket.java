package com.example.CryptoTracking.entity;

import jakarta.persistence.*;

import java.time.Instant;

import lombok.*;
import java.math.*;

@Entity
@Table(name = "global_market_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalMarket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active_cryptocurrencies")
    private Integer activeCryptocurrencies; // total coin

    @Column(name = "markets")
    private Integer markets;                // total market

    @Column(name = "total_market_cap_usd", precision = 30, scale = 2)
    private BigDecimal totalMarketCapUsd;   // total market cap (USD)

    @Column(name = "total_volume_usd", precision = 30, scale = 2)
    private BigDecimal totalVolumeUsd;      // total volume (USD)

    @Column(name = "market_cap_change_percentage_24h_usd")
    private Double marketCapChangePercentage24hUsd; // % change 24h (USD)

    @Column(name = "btc_dominance")
    private Double btcDominance;        // BTC dominance (%)

    @Column(name = "updated_at")
    private Instant updatedAt;             // timestamp
}