package com.example.CryptoTracking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.*;
import java.time.Instant;

@Entity
@Table(name = "coins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coin {
    @Id //mark ID is primary key
    @Column(length = 255)
    private String id;

    @Column(nullable = false, length = 20)
    private String symbol;

    @Column(nullable = false)
    private String name;

    @Column(length = 500, columnDefinition = "TEXT")
    private String image;

    @Column(precision = 19, scale = 8)
    private BigDecimal currentPrice;

    private Long marketCap;

    private Integer marketCapRank;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalVolume;

    @Column(precision = 19, scale = 8)
    private BigDecimal high24h;

    @Column(precision = 19, scale = 8)
    private BigDecimal low24h;

    private Double priceChangePercentage24h;
    private Double priceChangePercentage7d;
    private Double priceChangePercentage1h;

    private Instant lastUpdated;
}