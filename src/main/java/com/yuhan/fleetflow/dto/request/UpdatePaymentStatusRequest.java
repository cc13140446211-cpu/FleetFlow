package com.yuhan.fleetflow.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdatePaymentStatusRequest {

    @NotBlank(message = "Payment status is required")
    private String status;

    public UpdatePaymentStatusRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}