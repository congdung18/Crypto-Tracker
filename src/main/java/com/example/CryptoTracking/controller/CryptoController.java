    package com.example.CryptoTracking.controller;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;
    import com.example.CryptoTracking.entity.Coin;
    import com.example.CryptoTracking.service.CryptoService;
    import com.example.CryptoTracking.entity.GlobalMarketData;
    import java.util.List;

    //http://localhost:server.port/api/coins/... (link)
    @RestController
    @RequestMapping("/api/coins")
    @CrossOrigin(origins = "*") // For frontend integration
    public class CryptoController {

        @Autowired
        private CryptoService cryptoService;

        @GetMapping
        public List<Coin> getCoins(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "50") int perPage) {
            return cryptoService.getCoins(page, perPage);
        }

        @GetMapping("/{id}")
        public Coin getCoinById(@PathVariable String id) {
            return cryptoService.getCoinById(id);
        }

    @GetMapping("/sort/rank") 
    //http://localhost:server.port/api/coins/sort/rank (link)
    public List<Coin> getCoinsSortedByRank() {
        return cryptoService.getCoinsSortedByRank();
    }

    @GetMapping("/sort/price")
    //http://localhost:server.port/api/coins/sort/price
    public List<Coin> getCoinsSortedByPrice() {
        return cryptoService.getCoinsSortedByPrice();
    }

    @GetMapping("/sort/market_cap")
    //http://localhost:server.port/api/coins/sort/market_cap
    public List<Coin> getCoinsSortedByMarketCap() {
        return cryptoService.getCoinsSortedByMarketCap();
    }

    @GetMapping("/search")
    //http://localhost:server.port/api/coins/search?query=(id of coin) | ex: btc, eth,...
    public List<Coin> searchCoins(@RequestParam String query) {
        return cryptoService.searchCoins(query);
    }
    }