package com.example.CryptoTracking.repository;

import com.example.CryptoTracking.entity.GlobalMarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalMarketRepository extends JpaRepository<GlobalMarket, Long> {
    GlobalMarket findTopByOrderByUpdatedAtDesc();
}
