package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.request.CreateCustomerRequest;
import com.yuhan.fleetflow.dto.request.UpdateCustomerRequest;
import com.yuhan.fleetflow.exception.CustomerNotFoundException;
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
        Customer customer = customerMapper.findById(id);

        if (customer == null) {
            throw new CustomerNotFoundException(id);
        }

        return customer;
    }

    public Customer createCustomer(CreateCustomerRequest request) {

        Customer customer = new Customer();

        customer.setCustName(request.getCustName());
        customer.setCustCompanyName(request.getCustCompanyName());
        customer.setCustPhone(request.getCustPhone());
        customer.setCustEmail(request.getCustEmail());
        customer.setCustAddress(request.getCustAddress());

        customerMapper.insert(customer);

        return customer;
    }

    public Customer updateCustomer(Long id, UpdateCustomerRequest request) {
        Customer customer = getCustomerById(id);

        customer.setCustName(request.getCustName());
        customer.setCustCompanyName(request.getCustCompanyName());
        customer.setCustPhone(request.getCustPhone());
        customer.setCustEmail(request.getCustEmail());
        customer.setCustAddress(request.getCustAddress());

        customerMapper.update(customer);

        return getCustomerById(id);
    }
}
