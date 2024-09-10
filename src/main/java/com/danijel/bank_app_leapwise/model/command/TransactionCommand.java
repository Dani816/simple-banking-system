package com.danijel.bank_app_leapwise.model.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionCommand {

    private Long senderAccountId;
    private Long receiverAccountId;
    private Double amount;
    private String message;
}
