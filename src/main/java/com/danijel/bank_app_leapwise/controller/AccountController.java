package com.danijel.bank_app_leapwise.controller;

import com.danijel.bank_app_leapwise.model.entity.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @GetMapping("/details")
    private Account tempo() {
        Account account = new Account();

        account.setBalance(100d);
        account.setAccountNumber(214244124L);

        return account;
    }
}
