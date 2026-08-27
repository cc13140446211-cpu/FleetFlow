package com.yuhan.fleetflow.dto.response;

import java.time.LocalDateTime;

public class TruckAvailabilityResponse {

    private Long truckId;
    private String registrationNumber;
    private String model;
    private String status;

    private boolean available;

    private Long conflictingJobId;
    private LocalDateTime conflictStart;
    private LocalDateTime conflictEnd;

    private String reason;

    public TruckAvailabilityResponse() {
    }

    public Long getTruckId() {
        return truckId;
    }

    public void setTruckId(Long truckId) {
        this.truckId = truckId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Long getConflictingJobId() {
        return conflictingJobId;
    }

    public void setConflictingJobId(Long conflictingJobId) {
        this.conflictingJobId = conflictingJobId;
    }

    public LocalDateTime getConflictStart() {
        return conflictStart;
    }

    public void setConflictStart(LocalDateTime conflictStart) {
        this.conflictStart = conflictStart;
    }

    public LocalDateTime getConflictEnd() {
        return conflictEnd;
    }

    public void setConflictEnd(LocalDateTime conflictEnd) {
        this.conflictEnd = conflictEnd;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}