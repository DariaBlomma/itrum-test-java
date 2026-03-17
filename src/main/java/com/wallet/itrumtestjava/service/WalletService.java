package com.wallet.itrumtestjava.service;

import com.wallet.itrumtestjava.dto.WalletBalanceResponse;
import com.wallet.itrumtestjava.entity.Wallet;
import com.wallet.itrumtestjava.exception.ResourceNotFoundException;
import com.wallet.itrumtestjava.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletBalanceResponse getBalance(UUID id) {
        Wallet wallet = walletRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with such id" + id));
        return new WalletBalanceResponse(wallet.getId(), wallet.getBalance());
    }
}
