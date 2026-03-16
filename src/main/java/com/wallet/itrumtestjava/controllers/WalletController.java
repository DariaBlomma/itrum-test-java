package com.wallet.itrumtestjava.controllers;

import com.wallet.itrumtestjava.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public int getBalance() {
        throw new InvalidRequestException("Invalid request error message");
        //return 2;
    }
}
