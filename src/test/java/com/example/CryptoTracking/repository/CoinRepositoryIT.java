package com.example.CryptoTracking.repository;

import com.example.CryptoTracking.entity.Coin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.sql.init.mode=always",
        "spring.jpa.hibernate.ddl-auto=validate"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class CoinRepositoryIT {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18-alpine");

    @Autowired
    private CoinRepository coinRepository;

    @BeforeEach
    void setUp() {
        coinRepository.deleteAll();
        coinRepository.saveAll(List.of(
                Coin.builder().id("bitcoin").symbol("btc").name("Bitcoin").currentPrice(new BigDecimal("65000")).marketCapRank(1).build(),
                Coin.builder().id("ethereum").symbol("eth").name("Ethereum").currentPrice(new BigDecimal("3400")).marketCapRank(2).build()
        ));
    }

    @Test
    @DisplayName("Should find coins when keyword partially matches name using real PostgreSQL")
    void searchByKeyword_NamePartialMatch() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Coin> result = coinRepository.searchByKeyword("bIt", pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .extracting(Coin::getName)
                .containsExactly("Bitcoin");
    }

    @Test
    @DisplayName("Should find coins matching price and rank criteria using Specification against PostgreSQL")
    void findAll_WithSpecification() {
        Specification<Coin> spec = (root, query, cb) -> {
            var p1 = cb.greaterThanOrEqualTo(root.get("currentPrice"), new BigDecimal("5000"));
            var p2 = cb.lessThanOrEqualTo(root.get("marketCapRank"), 5);
            return cb.and(p1, p2);
        };

        Pageable pageable = PageRequest.of(0, 10);
        Page<Coin> result = coinRepository.findAll(spec, pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .extracting(Coin::getName)
                .containsExactly("Bitcoin");
    }
}