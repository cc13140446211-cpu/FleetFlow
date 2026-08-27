package com.yuhan.fleetflow.dto.response;

import java.time.LocalDateTime;

public class ScheduleJobResponse {

    private Long jobId;

    private String pickupLocation;
    private String dropoffLocation;

    private LocalDateTime pickupDatetime;
    private LocalDateTime expectedDropoffDatetime;

    private String jobStatus;

    private Long driverId;
    private String driverName;

    private Long truckId;
    private String truckRegistrationNumber;
    private String truckModel;

    public ScheduleJobResponse() {
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public LocalDateTime getPickupDatetime() {
        return pickupDatetime;
    }

    public void setPickupDatetime(LocalDateTime pickupDatetime) {
        this.pickupDatetime = pickupDatetime;
    }

    public LocalDateTime getExpectedDropoffDatetime() {
        return expectedDropoffDatetime;
    }

    public void setExpectedDropoffDatetime(
            LocalDateTime expectedDropoffDatetime
    ) {
        this.expectedDropoffDatetime = expectedDropoffDatetime;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
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

    public Long getTruckId() {
        return truckId;
    }

    public void setTruckId(Long truckId) {
        this.truckId = truckId;
    }

    public String getTruckRegistrationNumber() {
        return truckRegistrationNumber;
    }

    public void setTruckRegistrationNumber(
            String truckRegistrationNumber
    ) {
        this.truckRegistrationNumber = truckRegistrationNumber;
    }

    public String getTruckModel() {
        return truckModel;
    }

    public void setTruckModel(String truckModel) {
        this.truckModel = truckModel;
    }
}