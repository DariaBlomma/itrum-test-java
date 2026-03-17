package com.wallet.itrumtestjava.service;

import com.wallet.itrumtestjava.dto.WalletBalanceResponse;
import com.wallet.itrumtestjava.dto.WalletUpdateBalanceRequest;
import com.wallet.itrumtestjava.entity.Wallet;
import com.wallet.itrumtestjava.exceptions.InsufficientFundsException;
import com.wallet.itrumtestjava.exceptions.ResourceNotFoundException;
import com.wallet.itrumtestjava.mapper.WalletMapper;
import com.wallet.itrumtestjava.repository.WalletRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    public WalletBalanceResponse getBalance(UUID id) {
        Wallet wallet = walletRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with such id" + id));
        return new WalletBalanceResponse(wallet.getId(), wallet.getBalance());
    }

    @Transactional(timeout = 5)
    public WalletBalanceResponse updateBalance(WalletUpdateBalanceRequest request) {
        UUID id = request.getWalletUuid();
        Wallet wallet = walletRepository
                .findByIdWithLock(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with such id" + id));
        BigDecimal currentBalance = wallet.getBalance();
        switch (request.getOperationType()) {
            case DEPOSIT -> wallet.setBalance(currentBalance.add(request.getAmount()));
            case WITHDRAW -> {
                if (currentBalance.compareTo(request.getAmount()) < 0) {
                    throw new InsufficientFundsException("Insufficient funds. Available: " + currentBalance +
                            ", Requested: " + request.getAmount());
                }
                wallet.setBalance(currentBalance.subtract(request.getAmount()));
            }
        }

        return walletMapper.toBalanceResponse(wallet);
    }
}
