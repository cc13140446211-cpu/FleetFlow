package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.request.CreateJobRequest;
import com.yuhan.fleetflow.exception.InvalidQuoteStateException;
import com.yuhan.fleetflow.exception.QuoteNotFoundException;
import com.yuhan.fleetflow.exception.ResourceUnavailableException;
import com.yuhan.fleetflow.mapper.EmployeeMapper;
import com.yuhan.fleetflow.mapper.JobMapper;
import com.yuhan.fleetflow.mapper.QuoteMapper;
import com.yuhan.fleetflow.mapper.TruckMapper;
import com.yuhan.fleetflow.model.Employee;
import com.yuhan.fleetflow.model.Job;
import com.yuhan.fleetflow.model.Quote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JobServiceTest {

    @Mock
    private JobMapper jobMapper;

    @Mock
    private QuoteMapper quoteMapper;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private TruckMapper truckMapper;

    private JobService jobService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        jobService = new JobService(
                jobMapper,
                quoteMapper,
                employeeMapper,
                truckMapper
        );
    }

    @Test
    void shouldRejectJobCreationWhenQuoteDoesNotExist() {

        when(quoteMapper.findById(999L))
                .thenReturn(null);

        CreateJobRequest request = createValidJobRequest();

        assertThrows(
                QuoteNotFoundException.class,
                () -> jobService.createJobFromQuote(999L, request)
        );

        verify(jobMapper, never()).insert(any(Job.class));
    }

    @Test
    void shouldRejectJobCreationWhenQuoteIsUnpaid() {

        Quote quote = createAcceptedPaidQuote();
        quote.setQuotePaymentStatus("UNPAID");

        when(quoteMapper.findById(1L))
                .thenReturn(quote);

        CreateJobRequest request = createValidJobRequest();

        assertThrows(
                InvalidQuoteStateException.class,
                () -> jobService.createJobFromQuote(1L, request)
        );

        verify(jobMapper, never()).insert(any(Job.class));
    }

    @Test
    void shouldRejectJobCreationWhenDriverHasScheduleConflict() {

        Quote quote = createAcceptedPaidQuote();

        Employee driver = createDriver();
        Employee dispatcher = createDispatcher();

        CreateJobRequest request = createValidJobRequest();

        when(quoteMapper.findById(1L))
                .thenReturn(quote);

        when(jobMapper.countByQuoteId(1L))
                .thenReturn(0);

        when(employeeMapper.findById(2L))
                .thenReturn(driver);

        when(employeeMapper.findById(1L))
                .thenReturn(dispatcher);

        when(truckMapper.existsById(1L))
                .thenReturn(1);

        when(jobMapper.countDriverConflicts(
                2L,
                request.getJobPickupDatetime(),
                request.getJobExpectedDropoffDatetime()
        )).thenReturn(1);

        assertThrows(
                ResourceUnavailableException.class,
                () -> jobService.createJobFromQuote(1L, request)
        );

        verify(jobMapper, never()).insert(any(Job.class));
        verify(quoteMapper, never())
                .updateStatus(1L, "CONVERTED");
    }

    @Test
    void shouldRejectJobCreationWhenTruckHasScheduleConflict() {

        Quote quote = createAcceptedPaidQuote();

        Employee driver = createDriver();
        Employee dispatcher = createDispatcher();

        CreateJobRequest request = createValidJobRequest();

        when(quoteMapper.findById(1L))
                .thenReturn(quote);

        when(jobMapper.countByQuoteId(1L))
                .thenReturn(0);

        when(employeeMapper.findById(2L))
                .thenReturn(driver);

        when(employeeMapper.findById(1L))
                .thenReturn(dispatcher);

        when(truckMapper.existsById(1L))
                .thenReturn(1);

        when(jobMapper.countDriverConflicts(
                2L,
                request.getJobPickupDatetime(),
                request.getJobExpectedDropoffDatetime()
        )).thenReturn(0);

        when(jobMapper.countTruckConflicts(
                1L,
                request.getJobPickupDatetime(),
                request.getJobExpectedDropoffDatetime()
        )).thenReturn(1);

        assertThrows(
                ResourceUnavailableException.class,
                () -> jobService.createJobFromQuote(1L, request)
        );

        verify(jobMapper, never()).insert(any(Job.class));
        verify(quoteMapper, never())
                .updateStatus(1L, "CONVERTED");
    }

    @Test
    void shouldCreateJobSuccessfully() {

        Quote quote = createAcceptedPaidQuote();

        Employee driver = createDriver();
        Employee dispatcher = createDispatcher();

        CreateJobRequest request = createValidJobRequest();

        when(quoteMapper.findById(1L))
                .thenReturn(quote);

        when(jobMapper.countByQuoteId(1L))
                .thenReturn(0);

        when(employeeMapper.findById(2L))
                .thenReturn(driver);

        when(employeeMapper.findById(1L))
                .thenReturn(dispatcher);

        when(truckMapper.existsById(1L))
                .thenReturn(1);

        when(jobMapper.countDriverConflicts(
                2L,
                request.getJobPickupDatetime(),
                request.getJobExpectedDropoffDatetime()
        )).thenReturn(0);

        when(jobMapper.countTruckConflicts(
                1L,
                request.getJobPickupDatetime(),
                request.getJobExpectedDropoffDatetime()
        )).thenReturn(0);

        Job createdJob = new Job();
        createdJob.setJobId(100L);
        createdJob.setQuoteId(1L);
        createdJob.setDriverEmpId(2L);
        createdJob.setScheduledByEmpId(1L);
        createdJob.setTruckId(1L);
        createdJob.setJobPickupDatetime(
                request.getJobPickupDatetime()
        );
        createdJob.setJobExpectedDropoffDatetime(
                request.getJobExpectedDropoffDatetime()
        );
        createdJob.setJobFinalPrice(
                request.getJobFinalPrice()
        );
        createdJob.setJobStatus("SCHEDULED");

        doAnswer(invocation -> {

            Job jobToInsert = invocation.getArgument(0);

            jobToInsert.setJobId(100L);

            return 1;

        }).when(jobMapper).insert(any(Job.class));

        when(jobMapper.findById(100L))
                .thenReturn(createdJob);

        Job result =
                jobService.createJobFromQuote(1L, request);

        assertNotNull(result);
        assertEquals(100L, result.getJobId());

        verify(jobMapper).insert(any(Job.class));

        verify(quoteMapper)
                .updateStatus(1L, "CONVERTED");
    }

    private Quote createAcceptedPaidQuote() {

        Quote quote = new Quote();

        quote.setQuoteId(1L);
        quote.setQuoteStatus("ACCEPTED");
        quote.setQuotePaymentStatus("PAID");

        return quote;
    }

    private Employee createDriver() {

        Employee driver = new Employee();

        driver.setEmpId(2L);
        driver.setEmpRole("DRIVER");
        driver.setEmpStatus("ACTIVE");

        return driver;
    }

    private Employee createDispatcher() {

        Employee dispatcher = new Employee();

        dispatcher.setEmpId(1L);
        dispatcher.setEmpRole("DISPATCHER");
        dispatcher.setEmpStatus("ACTIVE");

        return dispatcher;
    }

    private CreateJobRequest createValidJobRequest() {

        CreateJobRequest request = new CreateJobRequest();

        request.setDriverEmpId(2L);
        request.setScheduledByEmpId(1L);
        request.setTruckId(1L);

        request.setJobPickupDatetime(
                LocalDateTime.of(
                        2026,
                        8,
                        30,
                        9,
                        0
                )
        );

        request.setJobExpectedDropoffDatetime(
                LocalDateTime.of(
                        2026,
                        8,
                        30,
                        16,
                        0
                )
        );

        request.setJobFinalPrice(
                new BigDecimal("2200.00")
        );

        return request;
    }
}