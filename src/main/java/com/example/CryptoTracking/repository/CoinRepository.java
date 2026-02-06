package com.example.CryptoTracking.repository;

import com.example.CryptoTracking.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CoinRepository extends JpaRepository<Coin, String> {
    List<Coin> findByNameContainingIgnoreCase(String name);

    List<Coin> findBySymbolContainingIgnoreCase(String symbol);

    @Query("SELECT c FROM Coin c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.symbol) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Coin> findByNameOrSymbol(@Param("query") String query);

    List<Coin> findAllByOrderByMarketCapRankAsc();

    List<Coin> findAllByOrderByCurrentPriceDesc();

    List<Coin> findAllByOrderByMarketCapDesc();
}