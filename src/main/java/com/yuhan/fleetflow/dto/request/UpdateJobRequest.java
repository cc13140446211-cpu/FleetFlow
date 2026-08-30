package com.yuhan.fleetflow.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UpdateJobRequest {

    @NotNull(message = "Driver employee ID is required")
    private Long driverEmpId;

    @NotNull(message = "Truck ID is required")
    private Long truckId;

    @NotNull(message = "Pickup date and time is required")
    private LocalDateTime jobPickupDatetime;

    @NotNull(message = "Expected drop-off date and time is required")
    private LocalDateTime jobExpectedDropoffDatetime;

    @NotNull(message = "Final price is required")
    @DecimalMin(value = "0.01", message = "Final price must be greater than 0")
    private BigDecimal jobFinalPrice;

    public Long getDriverEmpId() { return driverEmpId; }
    public void setDriverEmpId(Long driverEmpId) { this.driverEmpId = driverEmpId; }
    public Long getTruckId() { return truckId; }
    public void setTruckId(Long truckId) { this.truckId = truckId; }
    public LocalDateTime getJobPickupDatetime() { return jobPickupDatetime; }
    public void setJobPickupDatetime(LocalDateTime jobPickupDatetime) { this.jobPickupDatetime = jobPickupDatetime; }
    public LocalDateTime getJobExpectedDropoffDatetime() { return jobExpectedDropoffDatetime; }
    public void setJobExpectedDropoffDatetime(LocalDateTime jobExpectedDropoffDatetime) { this.jobExpectedDropoffDatetime = jobExpectedDropoffDatetime; }
    public BigDecimal getJobFinalPrice() { return jobFinalPrice; }
    public void setJobFinalPrice(BigDecimal jobFinalPrice) { this.jobFinalPrice = jobFinalPrice; }
}
