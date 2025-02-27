package com.example.walletdemo.dto;

import lombok.Data;

@Data
public class TransferWithPinRequest extends TransferRequest {
    private String pin;
}