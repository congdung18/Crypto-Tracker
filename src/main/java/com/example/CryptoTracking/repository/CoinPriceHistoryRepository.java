package com.example.CryptoTracking.repository;

import com.example.CryptoTracking.entity.CoinPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public interface CoinPriceHistoryRepository extends JpaRepository<CoinPriceHistory, Long> {

    List<CoinPriceHistory> findAllByCoinIdOrderByTimestampAsc(String coinId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CoinPriceHistory c WHERE c.timestamp < :threshold")
    void deleteOldHistory(@Param("threshold") Instant threshold);
}
