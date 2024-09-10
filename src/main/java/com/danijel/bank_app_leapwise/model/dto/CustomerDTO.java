package com.danijel.bank_app_leapwise.model.dto;

import com.danijel.bank_app_leapwise.model.entity.Customer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerDTO {

    private Long id;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private List<AccountDTO> accounts;

    public static CustomerDTO fromEntity(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.id = customer.getId();
        dto.name = customer.getName();
        dto.address = customer.getAddress();
        dto.email = customer.getEmail();
        dto.phoneNumber = customer.getPhoneNumber();
        if (!customer.getAccounts().isEmpty()) {
            dto.accounts = AccountDTO.fromEntityList(customer.getAccounts());
        }

        return dto;
    }
}
