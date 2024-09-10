package com.danijel.bank_app_leapwise.controller;

import com.danijel.bank_app_leapwise.model.command.TransactionCommand;
import com.danijel.bank_app_leapwise.model.dto.TransactionDTO;
import com.danijel.bank_app_leapwise.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/history/{customerId}")
    public List<TransactionDTO> getTransactionHistory(
            @PathVariable Long customerId,
            @RequestParam(required = false) String filter_name,
            @RequestParam(required = false) String filter_value) {
        return transactionService.getTransactionHistory(customerId, filter_name, filter_value);
    }
}
