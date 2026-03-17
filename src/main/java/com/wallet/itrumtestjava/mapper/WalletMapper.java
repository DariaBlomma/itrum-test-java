package com.wallet.itrumtestjava.mapper;

import com.wallet.itrumtestjava.dto.WalletBalanceResponse;
import com.wallet.itrumtestjava.entity.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletBalanceResponse toBalanceResponse(Wallet wallet);
}
