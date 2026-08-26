package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.request.UpdateQuotePaymentRequest;
import com.yuhan.fleetflow.dto.request.UpdateQuoteStatusRequest;
import com.yuhan.fleetflow.exception.InvalidQuoteStateException;
import com.yuhan.fleetflow.mapper.CustomerMapper;
import com.yuhan.fleetflow.mapper.EmployeeMapper;
import com.yuhan.fleetflow.mapper.QuoteMapper;
import com.yuhan.fleetflow.model.Quote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QuoteServiceTest {

    @Mock
    private QuoteMapper quoteMapper;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private EmployeeMapper employeeMapper;

    private QuoteService quoteService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        quoteService = new QuoteService(
                quoteMapper,
                customerMapper,
                employeeMapper
        );
    }

    @Test
    void shouldAcceptPendingQuote() {

        Quote pendingQuote = createQuote(
                "PENDING",
                "UNPAID"
        );

        Quote acceptedQuote = createQuote(
                "ACCEPTED",
                "UNPAID"
        );

        when(quoteMapper.findById(1L))
                .thenReturn(pendingQuote, acceptedQuote);

        UpdateQuoteStatusRequest request =
                new UpdateQuoteStatusRequest();

        request.setStatus("ACCEPTED");

        Quote result =
                quoteService.updateQuoteStatus(1L, request);

        assertEquals("ACCEPTED", result.getQuoteStatus());

        verify(quoteMapper)
                .updateStatus(1L, "ACCEPTED");
    }

    @Test
    void shouldRejectPendingQuote() {

        Quote pendingQuote = createQuote(
                "PENDING",
                "UNPAID"
        );

        Quote rejectedQuote = createQuote(
                "REJECTED",
                "UNPAID"
        );

        when(quoteMapper.findById(1L))
                .thenReturn(pendingQuote, rejectedQuote);

        UpdateQuoteStatusRequest request =
                new UpdateQuoteStatusRequest();

        request.setStatus("REJECTED");

        Quote result =
                quoteService.updateQuoteStatus(1L, request);

        assertEquals("REJECTED", result.getQuoteStatus());

        verify(quoteMapper)
                .updateStatus(1L, "REJECTED");
    }

    @Test
    void shouldCancelAcceptedQuote() {

        Quote acceptedQuote = createQuote(
                "ACCEPTED",
                "UNPAID"
        );

        Quote cancelledQuote = createQuote(
                "CANCELLED",
                "UNPAID"
        );

        when(quoteMapper.findById(1L))
                .thenReturn(acceptedQuote, cancelledQuote);

        UpdateQuoteStatusRequest request =
                new UpdateQuoteStatusRequest();

        request.setStatus("CANCELLED");

        Quote result =
                quoteService.updateQuoteStatus(1L, request);

        assertEquals("CANCELLED", result.getQuoteStatus());

        verify(quoteMapper)
                .updateStatus(1L, "CANCELLED");
    }

    @Test
    void shouldRejectInvalidQuoteStatusTransition() {

        Quote rejectedQuote = createQuote(
                "REJECTED",
                "UNPAID"
        );

        when(quoteMapper.findById(1L))
                .thenReturn(rejectedQuote);

        UpdateQuoteStatusRequest request =
                new UpdateQuoteStatusRequest();

        request.setStatus("ACCEPTED");

        assertThrows(
                InvalidQuoteStateException.class,
                () -> quoteService.updateQuoteStatus(1L, request)
        );

        verify(quoteMapper, never())
                .updateStatus(anyLong(), anyString());
    }

    @Test
    void shouldMarkAcceptedQuoteAsPaid() {

        Quote acceptedUnpaidQuote = createQuote(
                "ACCEPTED",
                "UNPAID"
        );

        Quote acceptedPaidQuote = createQuote(
                "ACCEPTED",
                "PAID"
        );

        when(quoteMapper.findById(1L))
                .thenReturn(
                        acceptedUnpaidQuote,
                        acceptedPaidQuote
                );

        UpdateQuotePaymentRequest request =
                new UpdateQuotePaymentRequest();

        request.setStatus("PAID");

        Quote result =
                quoteService.updateQuotePaymentStatus(
                        1L,
                        request
                );

        assertEquals(
                "PAID",
                result.getQuotePaymentStatus()
        );

        verify(quoteMapper)
                .updatePaymentStatus(1L, "PAID");
    }

    @Test
    void shouldRejectPaymentForNonAcceptedQuote() {

        Quote pendingQuote = createQuote(
                "PENDING",
                "UNPAID"
        );

        when(quoteMapper.findById(1L))
                .thenReturn(pendingQuote);

        UpdateQuotePaymentRequest request =
                new UpdateQuotePaymentRequest();

        request.setStatus("PAID");

        assertThrows(
                InvalidQuoteStateException.class,
                () -> quoteService.updateQuotePaymentStatus(
                        1L,
                        request
                )
        );

        verify(quoteMapper, never())
                .updatePaymentStatus(
                        anyLong(),
                        anyString()
                );
    }

    private Quote createQuote(
            String quoteStatus,
            String paymentStatus
    ) {

        Quote quote = new Quote();

        quote.setQuoteId(1L);
        quote.setQuoteStatus(quoteStatus);
        quote.setQuotePaymentStatus(paymentStatus);

        return quote;
    }
}