package com.example.walletdemo.controllers;

import com.example.walletdemo.dto.TransferRequest;
import com.example.walletdemo.dto.TransferWithPinRequest;
import com.example.walletdemo.models.User;
import com.example.walletdemo.models.Wallet;
import com.example.walletdemo.services.JwtService;
import com.example.walletdemo.services.UserService;
import com.example.walletdemo.services.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

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
        Wallet senderWallet = sender.getWallet();

        // Perform transfer
        walletService.transfer(senderWallet.getUser().getId(), transferRequest);

        return ResponseEntity.ok("Transfer Successful!");
    }
}
