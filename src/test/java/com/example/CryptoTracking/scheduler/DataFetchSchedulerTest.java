package com.example.CryptoTracking.scheduler;

import com.example.CryptoTracking.service.CryptoService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DataFetchSchedulerTest {
    @Mock
    private CryptoService cryptoService;

    @InjectMocks
    private DataFetchScheduler dataFetchScheduler;

    @Test
    void whenFetchAndStore_thenCallCryptoService(){
        dataFetchScheduler.fetchAndStore();

        verify(cryptoService, times(1)).fetchCoinsFromAPI();
    }
}
