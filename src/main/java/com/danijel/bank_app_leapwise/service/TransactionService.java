package com.danijel.bank_app_leapwise.service;

import com.danijel.bank_app_leapwise.model.command.TransactionCommand;
import com.danijel.bank_app_leapwise.model.entity.Account;
import com.danijel.bank_app_leapwise.model.entity.Transaction;
import com.danijel.bank_app_leapwise.repository.AccountRepository;
import com.danijel.bank_app_leapwise.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    public Long saveTransaction(TransactionCommand command) {

        Transaction transaction = new Transaction();

        modelMapper.map(command, transaction);
        transaction.setTimeStamp(ZonedDateTime.now());

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

        Transaction savedTransaction = transactionRepository.save(transaction);

        return savedTransaction.getId();
    }
}
