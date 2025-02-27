package com.example.walletdemo.controllers;

import com.example.walletdemo.dto.UserDTO;
import com.example.walletdemo.models.Transaction;
import com.example.walletdemo.models.User;
import com.example.walletdemo.services.TransactionService;
import com.example.walletdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @PutMapping("/approve/{userId}")
    public ResponseEntity<String> approveUser(@PathVariable Long userId){
        userService.approveUser(userId);
        return ResponseEntity.ok("User approved successfully");
    }

    //get pending users
    @GetMapping("/pending-users")
    public ResponseEntity<List<UserDTO>> getPendingUsers() {
        List<UserDTO> pendingUsers = userService.getPendingUsersDTO();
        return ResponseEntity.ok(pendingUsers);
    }

    // Get all transactions
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    // Get transactions by user ID
    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }

}
