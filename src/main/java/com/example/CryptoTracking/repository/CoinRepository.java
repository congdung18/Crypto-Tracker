package com.example.CryptoTracking.repository;

import com.example.CryptoTracking.entity.Coin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoinRepository extends JpaRepository<Coin, String>, JpaSpecificationExecutor<Coin> {
    @Query("SELECT c FROM Coin c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.symbol) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Coin> searchByKeyword(@Param("query") String query, Pageable pageable);
}