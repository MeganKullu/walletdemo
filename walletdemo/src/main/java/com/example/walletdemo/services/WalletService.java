package com.example.walletdemo.services;

import com.example.walletdemo.dto.TransferRequest;
import com.example.walletdemo.models.Wallet;
import com.example.walletdemo.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public void transfer(Long senderUserId, TransferRequest request) {
        // Fetch sender's wallet
        Wallet senderWallet = walletRepository.findByUserId(senderUserId)
                .orElseThrow(() -> new RuntimeException("Sender wallet not found"));

        // Fetch receiver's wallet
        Wallet receiverWallet = walletRepository.findByUserId(request.getReceiverUserId())
                .orElseThrow(() -> new RuntimeException("Receiver wallet not found"));

        // Check balance
        if (senderWallet.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient funds");
        }

        // Perform transfer
        senderWallet.setBalance(senderWallet.getBalance() - request.getAmount());
        receiverWallet.setBalance(receiverWallet.getBalance() + request.getAmount());

        // Save changes
        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);
    }
}
