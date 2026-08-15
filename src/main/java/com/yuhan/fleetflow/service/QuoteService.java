package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.request.CreateQuoteRequest;
import com.yuhan.fleetflow.exception.CustomerNotFoundException;
import com.yuhan.fleetflow.exception.QuoteNotFoundException;
import com.yuhan.fleetflow.mapper.CustomerMapper;
import com.yuhan.fleetflow.mapper.QuoteMapper;
import com.yuhan.fleetflow.model.Customer;
import com.yuhan.fleetflow.model.Quote;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteService {

    private final QuoteMapper quoteMapper;
    private final CustomerMapper customerMapper;

    public QuoteService(
            QuoteMapper quoteMapper,
            CustomerMapper customerMapper
    ) {
        this.quoteMapper = quoteMapper;
        this.customerMapper = customerMapper;
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