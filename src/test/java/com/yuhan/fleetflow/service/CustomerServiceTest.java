package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.request.UpdateCustomerRequest;
import com.yuhan.fleetflow.mapper.CustomerMapper;
import com.yuhan.fleetflow.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    @Mock
    private CustomerMapper customerMapper;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(customerMapper);
    }

    @Test
    void shouldUpdateCustomerWithoutReplacingIdentityOrCreatedTime() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 2, 10, 30);
        Customer existing = new Customer();
        existing.setCustId(8L);
        existing.setCustName("Old name");
        existing.setCustCreatedAt(createdAt);

        Customer updated = new Customer();
        updated.setCustId(8L);
        updated.setCustName("New name");
        updated.setCustCreatedAt(createdAt);

        when(customerMapper.findById(8L)).thenReturn(existing, updated);

        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setCustName("New name");
        request.setCustCompanyName("Fleet Co");
        request.setCustPhone("0123456789");
        request.setCustEmail("ops@fleet.test");
        request.setCustAddress("Kuala Lumpur");

        Customer result = customerService.updateCustomer(8L, request);

        verify(customerMapper).update(any(Customer.class));
        assertEquals(8L, result.getCustId());
        assertEquals(createdAt, result.getCustCreatedAt());
        assertEquals("New name", result.getCustName());
    }
}
