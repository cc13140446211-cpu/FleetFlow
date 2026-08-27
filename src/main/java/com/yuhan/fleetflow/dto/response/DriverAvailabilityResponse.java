package com.yuhan.fleetflow.dto.response;

import java.time.LocalDateTime;

public class DriverAvailabilityResponse {

    private Long driverId;
    private String driverName;

    private boolean available;

    private Long conflictingJobId;
    private LocalDateTime conflictStart;
    private LocalDateTime conflictEnd;

    private String reason;

    public DriverAvailabilityResponse() {
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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