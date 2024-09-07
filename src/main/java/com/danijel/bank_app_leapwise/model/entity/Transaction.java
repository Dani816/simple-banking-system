package com.danijel.bank_app_leapwise.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name="transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderAccountId;

    private Long receiverAccountId;

    private Double amount;

    private String message;

    private ZonedDateTime timeStamp;
}
