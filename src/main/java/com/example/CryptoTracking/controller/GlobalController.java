package com.example.CryptoTracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.*;
import com.example.CryptoTracking.entity.GlobalMarketData;
import com.example.CryptoTracking.service.CryptoService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GlobalController {

    @Autowired
    private CryptoService cryptoService;

    @GetMapping("/global")
    public GlobalMarketData getGlobalMarketData() {
        return cryptoService.getGlobalMarketData();
    }
}
