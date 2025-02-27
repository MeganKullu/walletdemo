package com.example.walletdemo.controllers;

import com.example.walletdemo.dto.*;
import com.example.walletdemo.models.Transaction;
import com.example.walletdemo.models.User;
import com.example.walletdemo.models.Wallet;
import com.example.walletdemo.services.JwtService;
import com.example.walletdemo.services.TransactionService;
import com.example.walletdemo.services.UserService;
import com.example.walletdemo.services.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    // Update transfer method
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
            @RequestBody TransferWithPinRequest transferRequest,
            HttpServletRequest request
    ) {
        // Extract JWT from request header
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtService.extractEmail(token);

        // Verify PIN before proceeding
        if (!userService.verifyPin(email, transferRequest.getPin())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid PIN");
        }

        // Find user and wallet
        User sender = userService.findByEmail(email);

        // Check if PIN is set
        if (!sender.isPinSet()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("PIN not set. Please set your PIN first.");
        }

        Wallet senderWallet = sender.getWallet();

        // Perform transfer
        walletService.transfer(senderWallet.getUser().getId(), transferRequest);

        return ResponseEntity.ok("Transfer Successful!");
    }

    // Transfer by email method
    @PostMapping("/transfer-by-email")
    public ResponseEntity<String> transferByEmail(
            @RequestBody TransferByEmailRequest transferRequest,
            HttpServletRequest request
    ) {
        // Extract JWT from request header
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String senderEmail = jwtService.extractEmail(token);

        // Verify PIN before proceeding
        if (!userService.verifyPin(senderEmail, transferRequest.getPin())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid PIN");
        }

        // Find sender
        User sender = userService.findByEmail(senderEmail);

        // Check if PIN is set
        if (!sender.isPinSet()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("PIN not set. Please set your PIN first.");
        }

        try {
            // Find receiver by email
            User receiver = userService.findByEmail(transferRequest.getReceiverEmail());

            // Create transfer request
            TransferRequest transferReq = new TransferRequest();
            transferReq.setAmount(transferRequest.getAmount());
            transferReq.setReceiverUserId(receiver.getId());

            // Perform transfer
            walletService.transfer(sender.getId(), transferReq);

            return ResponseEntity.ok("Transfer Successful!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get wallet info method
    @GetMapping("/wallet-info")
    public ResponseEntity<WalletInfoDTO> getWalletInfo(HttpServletRequest request) {
        // Extract JWT from request header
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtService.extractEmail(token);

        // Find user and wallet
        User user = userService.findByEmail(email);
        Wallet wallet = user.getWallet();

        // Get user transactions
        List<Transaction> transactions = transactionService.getTransactionsByUserId(user.getId());

        // Create and return wallet info DTO
        WalletInfoDTO walletInfo = new WalletInfoDTO(wallet, transactions);
        return ResponseEntity.ok(walletInfo);
    }

    // Checking the pin status
    @GetMapping("/check-pin-status")
    public ResponseEntity<Map<String, Boolean>> checkPinStatus(HttpServletRequest request) {
        try {
            // Extract JWT from request header
            String token = request.getHeader("Authorization").replace("Bearer ", "");
            String email = jwtService.extractEmail(token);

            // Find user
            User user = userService.findByEmail(email);

            Map<String, Boolean> response = new HashMap<>();
            response.put("isPinSet", user.isPinSet());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error checking PIN status: " + e.getMessage());

            // Return a generic response
            Map<String, Boolean> errorResponse = new HashMap<>();
            errorResponse.put("isPinSet", false);
            return ResponseEntity.ok(errorResponse);
        }
    }
}