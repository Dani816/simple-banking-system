package com.danijel.bank_app_leapwise.service;

import com.danijel.bank_app_leapwise.model.entity.Account;
import com.danijel.bank_app_leapwise.model.entity.Transaction;
import com.danijel.bank_app_leapwise.repository.AccountRepository;
import com.danijel.bank_app_leapwise.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Transaction saveTransaction(Transaction transaction) {

        var sender = accountRepository.findById(transaction.getSenderAccountId());
        var receiver = accountRepository.findById(transaction.getReceiverAccountId());

        if (sender.isEmpty() || receiver.isEmpty()) {
            throw new RuntimeException("Sender: " + sender.isEmpty() + "Receiver: " + receiver.isEmpty());
        }

        if (sender.get().getBalance() < transaction.getAmount()) {
            throw new RuntimeException("Sender's account balance is insufficient to complete the transaction.");
        }

        sender.get().setBalance(sender.get().getBalance() - transaction.getAmount());
        receiver.ifPresent(acc -> acc.setBalance(acc.getBalance() + transaction.getAmount()));

        List<Account> accounts = List.of(sender.get(), receiver.get());
        accountRepository.saveAll(accounts);

        return transactionRepository.save(transaction);
    }
}
