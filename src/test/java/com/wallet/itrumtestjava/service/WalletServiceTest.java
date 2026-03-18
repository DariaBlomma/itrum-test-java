package com.wallet.itrumtestjava.service;
import com.wallet.itrumtestjava.dto.WalletBalanceResponse;
import com.wallet.itrumtestjava.dto.WalletUpdateBalanceRequest;
import com.wallet.itrumtestjava.entity.Wallet;
import com.wallet.itrumtestjava.enums.WalletOperationType;
import com.wallet.itrumtestjava.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class WalletServiceTest {
    @MockitoBean
    private WalletRepository walletRepository;

    @Autowired
    private WalletService walletService;

    @Test
    void getBalance_shouldReturnBalance_whenWalletIdExists() {
        Wallet wallet = createWallet(BigDecimal.valueOf(100));
        UUID walletId = wallet.getId();
        mockWalletFound(wallet);

        WalletBalanceResponse response = walletService.getBalance(walletId);

        assertEquals(walletId, response.getId());
        assertEquals(BigDecimal.valueOf(100), response.getBalance());
    }

    // updateBalance, Deposit
    @Test
    void updateBalance_shouldAddAmountOnDeposit_whenAmountIsInteger() {
        Wallet wallet = createWallet(BigDecimal.valueOf(100));
        UUID walletId = wallet.getId();
        mockWalletWithLockFound(wallet);
        WalletUpdateBalanceRequest request = new WalletUpdateBalanceRequest(
                walletId, WalletOperationType.DEPOSIT, BigDecimal.valueOf(10)
        );

        WalletBalanceResponse response = walletService.updateBalance(request);

        assertEquals(walletId, response.getId());
        assertEquals(BigDecimal.valueOf(110), response.getBalance());
    }

    @Test
    void updateBalance_shouldAddAmountOnDeposit_whenAmountIsDecimal() {
        Wallet wallet = createWallet(BigDecimal.valueOf(100));
        UUID walletId = wallet.getId();
        mockWalletWithLockFound(wallet);
        WalletUpdateBalanceRequest request = new WalletUpdateBalanceRequest(
                walletId, WalletOperationType.DEPOSIT, BigDecimal.valueOf(10.02)
        );

        WalletBalanceResponse response = walletService.updateBalance(request);

        assertEquals(walletId, response.getId());
        assertEquals(BigDecimal.valueOf(110.02), response.getBalance());
    }

    // updateBalance, Withdraw
    @Test
    void updateBalance_shouldSubstructAmountOnWithdraw_whenAmountIsInteger() {
        Wallet wallet = createWallet(BigDecimal.valueOf(100));
        UUID walletId = wallet.getId();
        mockWalletWithLockFound(wallet);
        WalletUpdateBalanceRequest request = new WalletUpdateBalanceRequest(
                walletId, WalletOperationType.WITHDRAW, BigDecimal.valueOf(50)
        );

        WalletBalanceResponse response = walletService.updateBalance(request);

        assertEquals(walletId, response.getId());
        assertEquals(BigDecimal.valueOf(50), response.getBalance());
    }

    @Test
    void updateBalance_shouldSubstructAmountOnWithdraw_whenAmountIsDecimal() {
        Wallet wallet = createWallet(BigDecimal.valueOf(100));
        UUID walletId = wallet.getId();
        mockWalletWithLockFound(wallet);
        WalletUpdateBalanceRequest request = new WalletUpdateBalanceRequest(
                walletId, WalletOperationType.WITHDRAW, BigDecimal.valueOf(50.35)
        );

        WalletBalanceResponse response = walletService.updateBalance(request);

        assertEquals(walletId, response.getId());
        assertEquals(BigDecimal.valueOf(49.65), response.getBalance());
    }

    @Test
    void updateBalance_shouldReturnZeroBalanceOnWithdraw_whenAmountEqualsBalance() {
        Wallet wallet = createWallet(BigDecimal.valueOf(100));
        UUID walletId = wallet.getId();
        mockWalletWithLockFound(wallet);
        WalletUpdateBalanceRequest request = new WalletUpdateBalanceRequest(
                walletId, WalletOperationType.WITHDRAW, BigDecimal.valueOf(100)
        );

        WalletBalanceResponse response = walletService.updateBalance(request);

        assertEquals(walletId, response.getId());
        assertEquals(BigDecimal.valueOf(0), response.getBalance());
    }

    private Wallet createWallet(BigDecimal balance) {
        return new Wallet(UUID.randomUUID(), balance);
    }

    private void mockWalletFound(Wallet wallet) {
        when(walletRepository.findById(wallet.getId()))
                .thenReturn(Optional.of(wallet));
    }

    private void mockWalletWithLockFound(Wallet wallet) {
        when(walletRepository.findByIdWithLock(wallet.getId()))
                .thenReturn(Optional.of(wallet));
    }
}
