package com.yuhan.fleetflow.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Job {

    private Long jobId;
    private Long quoteId;
    private Long driverEmpId;
    private Long scheduledByEmpId;
    private Long truckId;
    private LocalDateTime jobPickupDatetime;
    private LocalDateTime jobExpectedDropoffDatetime;
    private BigDecimal jobFinalPrice;
    private String jobStatus;
    private LocalDateTime jobCreatedAt;
    private LocalDateTime jobUpdatedAt;

    public Job() {
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public Long getDriverEmpId() {
        return driverEmpId;
    }

    public void setDriverEmpId(Long driverEmpId) {
        this.driverEmpId = driverEmpId;
    }

    public Long getScheduledByEmpId() {
        return scheduledByEmpId;
    }

    public void setScheduledByEmpId(Long scheduledByEmpId) {
        this.scheduledByEmpId = scheduledByEmpId;
    }

    public Long getTruckId() {
        return truckId;
    }

    public void setTruckId(Long truckId) {
        this.truckId = truckId;
    }

    public LocalDateTime getJobPickupDatetime() {
        return jobPickupDatetime;
    }

    public void setJobPickupDatetime(LocalDateTime jobPickupDatetime) {
        this.jobPickupDatetime = jobPickupDatetime;
    }

    public LocalDateTime getJobExpectedDropoffDatetime() {
        return jobExpectedDropoffDatetime;
    }

    public void setJobExpectedDropoffDatetime(LocalDateTime jobExpectedDropoffDatetime) {
        this.jobExpectedDropoffDatetime = jobExpectedDropoffDatetime;
    }

    public BigDecimal getJobFinalPrice() {
        return jobFinalPrice;
    }

    public void setJobFinalPrice(BigDecimal jobFinalPrice) {
        this.jobFinalPrice = jobFinalPrice;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public LocalDateTime getJobCreatedAt() {
        return jobCreatedAt;
    }

    public void setJobCreatedAt(LocalDateTime jobCreatedAt) {
        this.jobCreatedAt = jobCreatedAt;
    }

    public LocalDateTime getJobUpdatedAt() {
        return jobUpdatedAt;
    }

    public void setJobUpdatedAt(LocalDateTime jobUpdatedAt) {
        this.jobUpdatedAt = jobUpdatedAt;
    }
}