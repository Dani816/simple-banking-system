package com.danijel.bank_app_leapwise.model.dto;

import com.danijel.bank_app_leapwise.model.entity.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class TransactionDTO {

    private Long id;
    private Long senderAccountId;
    private Long receiverAccountId;
    private Double amount;
    private String message;
    private ZonedDateTime timeStamp;

    public static TransactionDTO fromEntity(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.id = transaction.getId();
        dto.senderAccountId = transaction.getSenderAccountId();
        dto.receiverAccountId = transaction.getReceiverAccountId();
        dto.amount = transaction.getAmount();
        dto.message = transaction.getMessage();
        dto.timeStamp = transaction.getTimeStamp();

        return dto;
    }

    public static List<TransactionDTO> fromEntityList(List<Transaction> dtoList) {
        return dtoList.stream().map(TransactionDTO::fromEntity).toList();
    }

}
