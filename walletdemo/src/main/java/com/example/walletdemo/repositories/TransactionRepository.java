package com.example.walletdemo.repositories;

import com.example.walletdemo.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Find all transactions related to a user (either sent or received)
    List<Transaction> findByUserId(Long userId);

    // Fetch transactions for a user (either as sender or receiver)
    List<Transaction> findBySenderIdOrReceiverId(Long senderId, Long receiverId);

}
