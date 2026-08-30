package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.request.CreateQuoteRequest;
import com.yuhan.fleetflow.dto.request.UpdateQuoteRequest;
import com.yuhan.fleetflow.dto.request.UpdateQuotePaymentRequest;
import com.yuhan.fleetflow.exception.*;
import com.yuhan.fleetflow.mapper.CustomerMapper;
import com.yuhan.fleetflow.mapper.EmployeeMapper;
import com.yuhan.fleetflow.mapper.QuoteMapper;
import com.yuhan.fleetflow.model.Customer;
import com.yuhan.fleetflow.model.Employee;
import com.yuhan.fleetflow.model.Quote;
import org.springframework.stereotype.Service;
import com.yuhan.fleetflow.dto.request.UpdateQuoteStatusRequest;

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

    public Quote updateQuote(Long id, UpdateQuoteRequest request) {
        Quote quote = getQuoteById(id);

        if (!"PENDING".equals(quote.getQuoteStatus())) {
            throw new InvalidQuoteStateException(
                    "Only PENDING quotes can be edited"
            );
        }

        validateCustomerAndDispatcher(
                request.getCustId(),
                request.getPreparedByEmpId()
        );

        quote.setCustId(request.getCustId());
        quote.setPreparedByEmpId(request.getPreparedByEmpId());
        quote.setQuotePickupLocation(request.getQuotePickupLocation());
        quote.setQuoteDropoffLocation(request.getQuoteDropoffLocation());
        quote.setQuotePreferredPickupDate(request.getQuotePreferredPickupDate());
        quote.setQuotePrice(request.getQuotePrice());

        quoteMapper.update(quote);

        return getQuoteById(id);
    }

    private void validateCustomerAndDispatcher(Long customerId, Long employeeId) {
        Customer customer = customerMapper.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(customerId);
        }

        Employee employee = employeeMapper.findById(employeeId);

        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId);
        }

        if (!"DISPATCHER".equals(employee.getEmpRole())) {
            throw new InvalidEmployeeRoleException(employee.getEmpId(), "DISPATCHER");
        }
    }

    public Quote updateQuoteStatus(
            Long id,
            UpdateQuoteStatusRequest request
    ) {

        Quote quote = quoteMapper.findById(id);

        if (quote == null) {
            throw new QuoteNotFoundException(id);
        }

        String currentStatus = quote.getQuoteStatus();
        String newStatus = request.getStatus().toUpperCase();

        if (!isValidQuoteStatus(newStatus)) {
            throw new InvalidQuoteStatusException(
                    "Invalid quote status: " + newStatus
            );
        }

        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new InvalidQuoteStateException(
                    "Cannot change quote status from "
                            + currentStatus
                            + " to "
                            + newStatus
            );
        }

        quoteMapper.updateStatus(id, newStatus);

        return quoteMapper.findById(id);
    }

    private boolean isValidQuoteStatus(String status) {
        return status.equals("PENDING")
                || status.equals("ACCEPTED")
                || status.equals("REJECTED")
                || status.equals("CANCELLED")
                || status.equals("CONVERTED");
    }

    private boolean isValidStatusTransition(
            String currentStatus,
            String newStatus
    ) {

        if (currentStatus.equals("PENDING")) {
            return newStatus.equals("ACCEPTED")
                    || newStatus.equals("REJECTED");
        }

        if (currentStatus.equals("ACCEPTED")) {
            return newStatus.equals("CANCELLED");
        }

        return false;
    }

    public Quote updateQuotePaymentStatus(
            Long id,
            UpdateQuotePaymentRequest request
    ) {

        Quote quote = quoteMapper.findById(id);

        if (quote == null) {
            throw new QuoteNotFoundException(id);
        }

        if (!"ACCEPTED".equals(quote.getQuoteStatus())) {
            throw new InvalidQuoteStateException(
                    "Only ACCEPTED quotes can be paid"
            );
        }

        String newStatus = request.getStatus().toUpperCase();

        if (!"PAID".equals(newStatus)) {
            throw new InvalidQuoteStateException(
                    "Payment status can only be changed to PAID"
            );
        }

        if ("PAID".equals(quote.getQuotePaymentStatus())) {
            throw new InvalidQuoteStateException(
                    "Quote has already been paid"
            );
        }

        quoteMapper.updatePaymentStatus(id, "PAID");

        return quoteMapper.findById(id);
    }

    public List<Quote> getQuotesByCustomerId(Long customerId) {

        Customer customer =
                customerMapper.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException(customerId);
        }

        return quoteMapper.findByCustomerId(customerId);
    }
}
