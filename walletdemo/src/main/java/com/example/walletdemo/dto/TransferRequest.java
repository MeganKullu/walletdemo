package com.example.walletdemo.dto;

import lombok.Data;

@Data
public class TransferRequest {
    private Long receiverUserId;
    private double amount;
}
