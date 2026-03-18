package com.wallet.itrumtestjava.dto;

import com.wallet.itrumtestjava.enums.WalletOperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class WalletUpdateBalanceRequest {
    @NotNull(message = "Wallet id is required")
    private UUID walletUuid;

    @NotNull(message = "Operation type is required")
    private WalletOperationType operationType;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Amount has invalid format")
    private BigDecimal amount;
}
