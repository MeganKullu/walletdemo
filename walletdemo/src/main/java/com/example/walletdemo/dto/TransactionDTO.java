package com.example.walletdemo.dto;

import com.example.walletdemo.models.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDTO {
    private Long id;
    private String type;
    private Double amount;
    private String timestamp;
    private String status;
    private String description;
    private String senderName;
    private String receiverName;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType().toString();
        this.amount = transaction.getAmount();
        this.timestamp = transaction.getTimestamp().toString();
        this.status = transaction.getStatus().toString();
        this.description = transaction.getDescription();

        if (transaction.getSender() != null) {
            this.senderName = transaction.getSender().getName();
        }

        if (transaction.getReceiver() != null) {
            this.receiverName = transaction.getReceiver().getName();
        }
    }
}