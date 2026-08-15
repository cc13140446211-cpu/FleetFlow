package com.yuhan.fleetflow.controller;

import com.yuhan.fleetflow.dto.request.CreateCustomerRequest;
import com.yuhan.fleetflow.model.Customer;
import com.yuhan.fleetflow.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public Customer createCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request);
    }
}