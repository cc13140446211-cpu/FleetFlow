package com.yuhan.fleetflow.dto.request;

import jakarta.validation.constraints.*;

public class CreateCustomerRequest {

    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name must not exceed 100 characters")
    private String custName;

    @Size(max = 100, message = "Company name must not exceed 100 characters")
    private String custCompanyName;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String custPhone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String custEmail;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String custAddress;

    //getter and setter
    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustCompanyName() {
        return custCompanyName;
    }

    public void setCustCompanyName(String custCompanyName) {
        this.custCompanyName = custCompanyName;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }
}