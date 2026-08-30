package com.yuhan.fleetflow.controller;

import com.yuhan.fleetflow.dto.request.CreateCustomerRequest;
import com.yuhan.fleetflow.dto.request.UpdateCustomerRequest;
import com.yuhan.fleetflow.model.Customer;
import com.yuhan.fleetflow.model.Quote;
import com.yuhan.fleetflow.service.CustomerService;
import com.yuhan.fleetflow.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final QuoteService quoteService;

    public CustomerController(CustomerService customerService, QuoteService quoteService) {
        this.customerService = customerService;
        this.quoteService = quoteService;
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

    @PutMapping("/{id}")
    public Customer updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request) {
        return customerService.updateCustomer(id, request);
    }

    @GetMapping("/{id}/quotes")
    public List<Quote> getCustomerQuotes(
            @PathVariable Long id
    ) {
        return quoteService.getQuotesByCustomerId(id);
    }
}
