package com.example.CryptoTracking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "coin_price_history", indexes = {
    @Index(name = "idx_price_history_coin_id", columnList = "coinId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoinPriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String coinId;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal price;

    @Column(nullable = false)
    private Instant timestamp;
}
