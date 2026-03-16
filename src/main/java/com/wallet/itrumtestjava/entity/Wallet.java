package com.wallet.itrumtestjava.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;
}
