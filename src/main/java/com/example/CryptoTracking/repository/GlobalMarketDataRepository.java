package com.example.CryptoTracking.repository;

import com.example.CryptoTracking.entity.GlobalMarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalMarketDataRepository extends JpaRepository<GlobalMarketData, Long> {
}
