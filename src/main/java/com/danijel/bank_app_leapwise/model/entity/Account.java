package com.danijel.bank_app_leapwise.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountNumber;

    private Double balance;

    private Long pastMonthTurnover;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Customer customer;
}
