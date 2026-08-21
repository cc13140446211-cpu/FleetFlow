package com.yuhan.fleetflow.controller;

import com.yuhan.fleetflow.dto.request.CreateQuoteRequest;
import com.yuhan.fleetflow.dto.request.UpdateQuoteStatusRequest;
import com.yuhan.fleetflow.model.Quote;
import com.yuhan.fleetflow.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping
    public List<Quote> getAllQuotes() {
        return quoteService.getAllQuotes();
    }

    @GetMapping("/{id}")
    public Quote getQuoteById(@PathVariable Long id) {
        return quoteService.getQuoteById(id);
    }

    @PostMapping
    public Quote createQuote(
            @Valid @RequestBody CreateQuoteRequest request
    ) {
        return quoteService.createQuote(request);
    }

    @PatchMapping("/{id}/status")
    public Quote updateQuoteStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateQuoteStatusRequest request
    ) {
        return quoteService.updateQuoteStatus(id, request);
    }
}