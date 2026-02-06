package com.example.CryptoTracking.repository;

import com.example.CryptoTracking.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CoinRepository extends JpaRepository<Coin, String> {
    // Tìm kiếm theo tên (không phân biệt hoa thường)
    List<Coin> findByNameContainingIgnoreCase(String name);

    // Tìm kiếm theo symbol (không phân biệt hoa thường)
    List<Coin> findBySymbolContainingIgnoreCase(String symbol);

    // Tìm kiếm theo tên hoặc symbol
    @Query("SELECT c FROM Coin c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.symbol) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Coin> findByNameOrSymbol(@Param("query") String query);

    // Sắp xếp theo market cap rank
    List<Coin> findAllByOrderByMarketCapRankAsc();

    // Sắp xếp theo current price
    List<Coin> findAllByOrderByCurrentPriceDesc();

    // Sắp xếp theo market cap
    List<Coin> findAllByOrderByMarketCapDesc();

    // Có thể thêm custom queries nếu cần, ví dụ:
    // List<Coin> findByMarketCapRankLessThanEqual(int rank);
}