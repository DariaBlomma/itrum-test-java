package com.wallet.itrumtestjava.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.itrumtestjava.dto.WalletUpdateBalanceRequest;
import com.wallet.itrumtestjava.entity.Wallet;
import com.wallet.itrumtestjava.enums.WalletOperationType;
import com.wallet.itrumtestjava.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.TransactionTimedOutException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WalletRepository walletRepository;

    private UUID walletId;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        walletId = UUID.randomUUID();
        wallet = new Wallet(walletId, BigDecimal.valueOf(1000));
    }

    // getBalance
    @Test
    void getBalance_shouldReturn200_whenWalletExists() throws Exception {
        mockWalletFound(wallet);

        performGetWallet(walletId).andExpect(status().isOk());
    }

    @Test
    void getBalance_shouldReturn404_whenWalletDoesNotExist() throws Exception {
        mockWalletNotFound(walletId);

        performGetWallet(walletId).andExpect(status().isNotFound());
    }

    // updateBalance
    @Test
    void updateBalance_shouldReturn200_whenOperationIsValid() throws Exception {
        mockWalletWithLockFound(wallet);
        WalletUpdateBalanceRequest request = createUpdateBalanceRequest();

        performPostWallet(request).andExpect(status().isOk());
    }

    @Test
    void updateBalance_shouldReturn404_whenWalletDoesNotExist() throws Exception {
        mockWalletWithLockNotFound(walletId);
        WalletUpdateBalanceRequest request = createUpdateBalanceRequest();

        performPostWallet(request).andExpect(status().isNotFound());
    }

    @Test
    void updateBalance_shouldReturn400_whenNotEnoughFundsForWithdrawal() throws Exception {
        mockWalletWithLockFound(wallet);
        WalletUpdateBalanceRequest request = new WalletUpdateBalanceRequest(
                walletId, WalletOperationType.WITHDRAW, BigDecimal.valueOf(1001)
        );

        performPostWallet(request).andExpect(status().isBadRequest());
    }

    @Test
    void updateBalance_shouldReturn503_whenTransactionTimedOut() throws Exception {
        when(walletRepository.findByIdWithLock(walletId))
                .thenThrow(new TransactionTimedOutException("Transaction timed out"));
        WalletUpdateBalanceRequest request = createUpdateBalanceRequest();

        performPostWallet(request)
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void updateBalance_shouldReturn503_whenConnectionPoolTimedOut() throws Exception {
        when(walletRepository.findByIdWithLock(walletId))
                .thenThrow(new DataAccessResourceFailureException("Connection pool timeout"));
        WalletUpdateBalanceRequest request = createUpdateBalanceRequest();

        performPostWallet(request)
                .andExpect(status().isServiceUnavailable());
    }

    private void mockWalletFound(Wallet wallet) {
        when(walletRepository.findById(wallet.getId()))
                .thenReturn(Optional.of(wallet));
    }

    private void mockWalletNotFound(UUID walletId) {
        when(walletRepository.findById(walletId))
                .thenReturn(Optional.empty());
    }

    private void mockWalletWithLockFound(Wallet wallet) {
        when(walletRepository.findByIdWithLock(wallet.getId()))
                .thenReturn(Optional.of(wallet));
    }

    private void mockWalletWithLockNotFound(UUID walletId) {
        when(walletRepository.findByIdWithLock(walletId))
                .thenReturn(Optional.empty());
    }


    private ResultActions performGetWallet(UUID walletId) throws Exception {
        return mockMvc.perform(get("/wallets/{id}", walletId));
    }

    private ResultActions performPostWallet(WalletUpdateBalanceRequest request) throws Exception {
        return mockMvc.perform(post("/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    private WalletUpdateBalanceRequest createUpdateBalanceRequest() {
        return new WalletUpdateBalanceRequest(
                walletId, WalletOperationType.DEPOSIT, BigDecimal.valueOf(500)
        );
    }
}
