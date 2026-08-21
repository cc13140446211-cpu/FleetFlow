package com.yuhan.fleetflow.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateJobStatusRequest {

    @NotBlank(message = "Job status is required")
    private String status;

    public UpdateJobStatusRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}