package com.danijel.bank_app_leapwise.service;

import com.danijel.bank_app_leapwise.model.dto.CustomerDTO;
import com.danijel.bank_app_leapwise.model.entity.Customer;
import com.danijel.bank_app_leapwise.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO getCustomerDetails(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(CustomerDTO::fromEntity).orElse(null);
    }

}
