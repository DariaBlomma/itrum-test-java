package com.wallet.itrumtestjava.controller;

import com.wallet.itrumtestjava.dto.WalletBalanceResponse;
import com.wallet.itrumtestjava.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/{wallet_uuid}")
    @ResponseStatus(HttpStatus.OK)
    public WalletBalanceResponse getBalance(@PathVariable("wallet_uuid") UUID walletUuid) {
        return walletService.getBalance(walletUuid);
    }
}
