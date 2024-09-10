package com.danijel.bank_app_leapwise.model.dto;

import com.danijel.bank_app_leapwise.model.entity.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountDTO {

    private Long id;
    private Long accountNumber;
    private Double balance;
    private Long pastMonthTurnover;

    public static AccountDTO fromEntity(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.id = account.getId();
        dto.accountNumber = account.getAccountNumber();
        dto.balance = account.getBalance();
        dto.pastMonthTurnover = account.getPastMonthTurnover();

        return dto;
    }

    public static List<AccountDTO> fromEntityList(List<Account> dtoList) {
        return dtoList.stream().map(AccountDTO::fromEntity).toList();
    }

}
