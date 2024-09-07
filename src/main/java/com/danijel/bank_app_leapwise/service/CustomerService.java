package com.danijel.bank_app_leapwise.service;

import com.danijel.bank_app_leapwise.model.entity.Customer;
import com.danijel.bank_app_leapwise.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void createCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
