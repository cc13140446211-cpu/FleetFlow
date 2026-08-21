package com.yuhan.fleetflow.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateQuoteStatusRequest {

    @NotBlank(message = "Quote status is required")
    private String status;

    public UpdateQuoteStatusRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}