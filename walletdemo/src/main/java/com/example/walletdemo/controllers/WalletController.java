package com.example.walletdemo.controllers;

import com.example.walletdemo.dto.TransferRequest;
import com.example.walletdemo.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest transferRequest) {
        // Get logged-in user's ID from JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long senderUserId = Long.parseLong(authentication.getName()); // JWT should store user ID

        walletService.transfer(senderUserId, transferRequest);
        return ResponseEntity.ok("Transfer Successful!");
    }
}
