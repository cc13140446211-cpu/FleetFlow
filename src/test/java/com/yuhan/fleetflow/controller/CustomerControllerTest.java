package com.yuhan.fleetflow.controller;

import com.yuhan.fleetflow.dto.request.CreateCustomerRequest;
import com.yuhan.fleetflow.model.Customer;
import com.yuhan.fleetflow.service.CustomerService;

import com.yuhan.fleetflow.service.QuoteService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private QuoteService quoteService;

    @Test
    void shouldReturnBadRequestWhenRequiredFieldsAreMissing() throws Exception {

        String requestBody = """
                {
                    "custCompanyName": "ABC Logistics"
                }
                """;

        mockMvc.perform(
                        post("/api/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {

        String requestBody = """
                {
                    "custName": "ABC Logistics",
                    "custCompanyName": "ABC Logistics Sdn Bhd",
                    "custPhone": "0123456789",
                    "custEmail": "invalid-email",
                    "custAddress": "Kuala Lumpur"
                }
                """;

        mockMvc.perform(
                        post("/api/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenCustomerNameIsBlank() throws Exception {

        String requestBody = """
                {
                    "custName": "",
                    "custCompanyName": "ABC Logistics Sdn Bhd",
                    "custPhone": "0123456789",
                    "custEmail": "contact@abc.com",
                    "custAddress": "Kuala Lumpur"
                }
                """;

        mockMvc.perform(
                        post("/api/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateCustomerWhenRequestIsValid() throws Exception {

        String requestBody = """
                {
                    "custName": "ABC Logistics",
                    "custCompanyName": "ABC Logistics Sdn Bhd",
                    "custPhone": "0123456789",
                    "custEmail": "contact@abc.com",
                    "custAddress": "Kuala Lumpur"
                }
                """;

        Customer customer = new Customer();

        customer.setCustId(1L);
        customer.setCustName("ABC Logistics");
        customer.setCustCompanyName("ABC Logistics Sdn Bhd");
        customer.setCustPhone("0123456789");
        customer.setCustEmail("contact@abc.com");
        customer.setCustAddress("Kuala Lumpur");

        when(
                customerService.createCustomer(
                        any(CreateCustomerRequest.class)
                )
        ).thenReturn(customer);

        mockMvc.perform(
                        post("/api/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.custId").value(1))
                .andExpect(
                        jsonPath("$.custName")
                                .value("ABC Logistics")
                )
                .andExpect(
                        jsonPath("$.custEmail")
                                .value("contact@abc.com")
                );
    }
}