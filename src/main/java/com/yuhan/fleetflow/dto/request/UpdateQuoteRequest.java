package com.yuhan.fleetflow.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateQuoteRequest {

    @NotNull(message = "Customer ID is required")
    private Long custId;

    @NotNull(message = "Preparing employee ID is required")
    private Long preparedByEmpId;

    @NotBlank(message = "Pickup location is required")
    @Size(max = 255, message = "Pickup location must not exceed 255 characters")
    private String quotePickupLocation;

    @NotBlank(message = "Drop-off location is required")
    @Size(max = 255, message = "Drop-off location must not exceed 255 characters")
    private String quoteDropoffLocation;

    @NotNull(message = "Preferred pickup date is required")
    private LocalDate quotePreferredPickupDate;

    @NotNull(message = "Quote price is required")
    @DecimalMin(value = "0.01", message = "Quote price must be greater than 0")
    private BigDecimal quotePrice;

    public Long getCustId() { return custId; }
    public void setCustId(Long custId) { this.custId = custId; }
    public Long getPreparedByEmpId() { return preparedByEmpId; }
    public void setPreparedByEmpId(Long preparedByEmpId) { this.preparedByEmpId = preparedByEmpId; }
    public String getQuotePickupLocation() { return quotePickupLocation; }
    public void setQuotePickupLocation(String quotePickupLocation) { this.quotePickupLocation = quotePickupLocation; }
    public String getQuoteDropoffLocation() { return quoteDropoffLocation; }
    public void setQuoteDropoffLocation(String quoteDropoffLocation) { this.quoteDropoffLocation = quoteDropoffLocation; }
    public LocalDate getQuotePreferredPickupDate() { return quotePreferredPickupDate; }
    public void setQuotePreferredPickupDate(LocalDate quotePreferredPickupDate) { this.quotePreferredPickupDate = quotePreferredPickupDate; }
    public BigDecimal getQuotePrice() { return quotePrice; }
    public void setQuotePrice(BigDecimal quotePrice) { this.quotePrice = quotePrice; }
}
