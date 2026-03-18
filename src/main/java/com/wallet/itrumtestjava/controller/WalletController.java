package com.wallet.itrumtestjava.controller;

import com.wallet.itrumtestjava.dto.WalletBalanceResponse;
import com.wallet.itrumtestjava.dto.WalletUpdateBalanceRequest;
import com.wallet.itrumtestjava.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/wallets/{wallet_uuid}")
    @ResponseStatus(HttpStatus.OK)
    public WalletBalanceResponse getBalance(@PathVariable("wallet_uuid") UUID walletUuid) {
        return walletService.getBalance(walletUuid);
    }

    @PostMapping("/wallet")
    @ResponseStatus(HttpStatus.OK)
    public WalletBalanceResponse updateBalance(@Valid @RequestBody WalletUpdateBalanceRequest request) {
        return walletService.updateBalance(request);
    }
}
