package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.request.UpdateQuotePaymentRequest;
import com.yuhan.fleetflow.dto.request.UpdateQuoteRequest;
import com.yuhan.fleetflow.dto.request.UpdateQuoteStatusRequest;
import com.yuhan.fleetflow.exception.InvalidQuoteStateException;
import com.yuhan.fleetflow.mapper.CustomerMapper;
import com.yuhan.fleetflow.mapper.EmployeeMapper;
import com.yuhan.fleetflow.mapper.QuoteMapper;
import com.yuhan.fleetflow.model.Quote;
import com.yuhan.fleetflow.model.Customer;
import com.yuhan.fleetflow.model.Employee;

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

    @Test
    void shouldUpdatePendingQuote() {
        Quote pending = createQuote("PENDING", "UNPAID");
        Quote updated = createQuote("PENDING", "UNPAID");
        updated.setQuotePickupLocation("Johor Bahru");

        Customer customer = new Customer();
        customer.setCustId(2L);
        Employee dispatcher = new Employee();
        dispatcher.setEmpId(3L);
        dispatcher.setEmpRole("DISPATCHER");

        when(quoteMapper.findById(1L)).thenReturn(pending, updated);
        when(customerMapper.findById(2L)).thenReturn(customer);
        when(employeeMapper.findById(3L)).thenReturn(dispatcher);

        UpdateQuoteRequest request = createUpdateQuoteRequest();
        Quote result = quoteService.updateQuote(1L, request);

        assertEquals("Johor Bahru", result.getQuotePickupLocation());
        verify(quoteMapper).update(any(Quote.class));
    }

    @Test
    void shouldRejectEditingNonPendingQuote() {
        when(quoteMapper.findById(1L)).thenReturn(createQuote("ACCEPTED", "UNPAID"));

        assertThrows(
                InvalidQuoteStateException.class,
                () -> quoteService.updateQuote(1L, createUpdateQuoteRequest())
        );

        verify(quoteMapper, never()).update(any(Quote.class));
    }

    private UpdateQuoteRequest createUpdateQuoteRequest() {
        UpdateQuoteRequest request = new UpdateQuoteRequest();
        request.setCustId(2L);
        request.setPreparedByEmpId(3L);
        request.setQuotePickupLocation("Johor Bahru");
        request.setQuoteDropoffLocation("Kuala Lumpur");
        request.setQuotePreferredPickupDate(java.time.LocalDate.of(2026, 9, 2));
        request.setQuotePrice(new java.math.BigDecimal("1500.00"));
        return request;
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
