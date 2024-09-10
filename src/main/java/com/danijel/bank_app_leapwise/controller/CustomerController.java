package com.danijel.bank_app_leapwise.controller;

import com.danijel.bank_app_leapwise.model.dto.CustomerDTO;
import com.danijel.bank_app_leapwise.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(path = "/details/{id}")
    public CustomerDTO customer(@PathVariable("id") Long id) {
        return customerService.getCustomerDetails(id);
    }
}
