package com.danijel.bank_app_leapwise.controller;

import com.danijel.bank_app_leapwise.model.command.TransactionCommand;
import com.danijel.bank_app_leapwise.service.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(path = "/save")
    public Long saveTransaction(@RequestBody TransactionCommand transaction) {
        return transactionService.saveTransaction(transaction);
    }
}
