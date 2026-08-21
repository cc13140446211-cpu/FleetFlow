package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.request.CreateQuoteRequest;
import com.yuhan.fleetflow.exception.CustomerNotFoundException;
import com.yuhan.fleetflow.exception.EmployeeNotFoundException;
import com.yuhan.fleetflow.exception.InvalidEmployeeRoleException;
import com.yuhan.fleetflow.exception.QuoteNotFoundException;
import com.yuhan.fleetflow.mapper.CustomerMapper;
import com.yuhan.fleetflow.mapper.EmployeeMapper;
import com.yuhan.fleetflow.mapper.QuoteMapper;
import com.yuhan.fleetflow.model.Customer;
import com.yuhan.fleetflow.model.Employee;
import com.yuhan.fleetflow.model.Quote;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteService {

    private final QuoteMapper quoteMapper;
    private final CustomerMapper customerMapper;
    private final EmployeeMapper employeeMapper;

    public QuoteService(
            QuoteMapper quoteMapper,
            CustomerMapper customerMapper,
            EmployeeMapper employeeMapper
    ) {
        this.quoteMapper = quoteMapper;
        this.customerMapper = customerMapper;
        this.employeeMapper = employeeMapper;
    }

    public List<Quote> getAllQuotes() {
        return quoteMapper.findAll();
    }

    public Quote getQuoteById(Long id) {

        Quote quote = quoteMapper.findById(id);

        if (quote == null) {
            throw new QuoteNotFoundException(id);
        }

        return quote;
    }

    public Quote createQuote(CreateQuoteRequest request) {

        Customer customer = customerMapper.findById(request.getCustId());

        if (customer == null) {
            throw new CustomerNotFoundException(request.getCustId());
        }

        Employee employee =
                employeeMapper.findById(
                        request.getPreparedByEmpId()
                );

        if (employee == null) {
            throw new EmployeeNotFoundException(
                    request.getPreparedByEmpId()
            );
        }

        if (!"DISPATCHER".equals(employee.getEmpRole())) {
            throw new InvalidEmployeeRoleException(
                    employee.getEmpId(),
                    "DISPATCHER"
            );
        }

        Quote quote = new Quote();

        quote.setCustId(request.getCustId());
        quote.setPreparedByEmpId(request.getPreparedByEmpId());
        quote.setQuotePickupLocation(request.getQuotePickupLocation());
        quote.setQuoteDropoffLocation(request.getQuoteDropoffLocation());
        quote.setQuotePreferredPickupDate(
                request.getQuotePreferredPickupDate()
        );
        quote.setQuotePrice(request.getQuotePrice());

        quoteMapper.insert(quote);

        return quoteMapper.findById(quote.getQuoteId());
    }
}