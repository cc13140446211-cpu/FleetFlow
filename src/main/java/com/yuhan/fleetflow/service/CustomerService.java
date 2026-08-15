package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.mapper.CustomerMapper;
import com.yuhan.fleetflow.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerMapper customerMapper;

    public CustomerService(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    public List<Customer> getAllCustomers() {
        return customerMapper.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerMapper.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        customerMapper.insert(customer);
        return customer;
    }
}