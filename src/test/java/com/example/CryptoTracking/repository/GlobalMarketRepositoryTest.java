package com.example.CryptoTracking.repository;

import com.example.CryptoTracking.entity.GlobalMarket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GlobalMarketRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GlobalMarketRepository repository;

    @Test
    void shouldReturnNewestGlobalMarket() {
        Instant now = Instant.now();

        GlobalMarket olderMarket = GlobalMarket.builder()
                .activeCryptocurrencies(10000)
                .markets(500)
                .totalMarketCapUsd(new BigDecimal("1000000000.00"))
                .updatedAt(now.minus(2, ChronoUnit.DAYS))
                .build();
        entityManager.persist(olderMarket);

        GlobalMarket newestMarket = GlobalMarket.builder()
                .activeCryptocurrencies(10050)
                .markets(510)
                .totalMarketCapUsd(new BigDecimal("1500000000.00"))
                .updatedAt(now.minus(1, ChronoUnit.HOURS))
                .build();
        entityManager.persist(newestMarket);

        GlobalMarket oldestMarket = GlobalMarket.builder()
                .activeCryptocurrencies(9000)
                .markets(450)
                .totalMarketCapUsd(new BigDecimal("900000000.00"))
                .updatedAt(now.minus(5, ChronoUnit.DAYS))
                .build();
        entityManager.persist(oldestMarket);

        entityManager.flush();

        GlobalMarket result = repository.findTopByOrderByUpdatedAtDesc();

        assertThat(result).isNotNull();
        assertThat(result.getUpdatedAt()).isEqualTo(newestMarket.getUpdatedAt());
        assertThat(result.getTotalMarketCapUsd()).isEqualByComparingTo(new BigDecimal("1500000000.00"));
    }

    @Test
    void shouldReturnNullWhenDatabaseIsEmpty() {
        GlobalMarket result = repository.findTopByOrderByUpdatedAtDesc();
        assertThat(result).isNull();
    }
}