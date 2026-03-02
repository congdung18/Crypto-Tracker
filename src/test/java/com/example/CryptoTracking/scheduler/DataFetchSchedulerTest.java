package com.example.CryptoTracking.scheduler;

import com.example.CryptoTracking.service.CoinService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DataFetchSchedulerTest {
    @Mock
    private CoinService coinService;

    @InjectMocks
    private DataFetchScheduler dataFetchScheduler;

    @Test
    void whenFetchAndStore_thenCallCryptoService(){
        dataFetchScheduler.fetchAndStore();

        verify(coinService, times(1)).fetchCoinsFromAPI();
    }
}
