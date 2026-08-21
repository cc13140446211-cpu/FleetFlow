package com.yuhan.fleetflow.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Quote {

    private Long quoteId;
    private Long custId;
    private Long preparedByEmpId;
    private String quotePickupLocation;
    private String quoteDropoffLocation;
    private LocalDate quotePreferredPickupDate;
    private BigDecimal quotePrice;
    private String quoteStatus;
    private String quotePaymentStatus;
    private LocalDateTime quoteCreatedAt;
    private LocalDateTime quoteUpdatedAt;

    public Quote() {
    }

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public Long getPreparedByEmpId() {
        return preparedByEmpId;
    }

    public void setPreparedByEmpId(Long preparedByEmpId) {
        this.preparedByEmpId = preparedByEmpId;
    }

    public String getQuotePickupLocation() {
        return quotePickupLocation;
    }

    public void setQuotePickupLocation(String quotePickupLocation) {
        this.quotePickupLocation = quotePickupLocation;
    }

    public String getQuoteDropoffLocation() {
        return quoteDropoffLocation;
    }

    public void setQuoteDropoffLocation(String quoteDropoffLocation) {
        this.quoteDropoffLocation = quoteDropoffLocation;
    }

    public LocalDate getQuotePreferredPickupDate() {
        return quotePreferredPickupDate;
    }

    public void setQuotePreferredPickupDate(LocalDate quotePreferredPickupDate) {
        this.quotePreferredPickupDate = quotePreferredPickupDate;
    }

    public BigDecimal getQuotePrice() {
        return quotePrice;
    }

    public void setQuotePrice(BigDecimal quotePrice) {
        this.quotePrice = quotePrice;
    }

    public String getQuoteStatus() {
        return quoteStatus;
    }

    public void setQuoteStatus(String quoteStatus) {
        this.quoteStatus = quoteStatus;
    }

    public String getQuotePaymentStatus() {
        return quotePaymentStatus;
    }

    public void setQuotePaymentStatus(String quotePaymentStatus) {
        this.quotePaymentStatus = quotePaymentStatus;
    }

    public LocalDateTime getQuoteCreatedAt() {
        return quoteCreatedAt;
    }

    public void setQuoteCreatedAt(LocalDateTime quoteCreatedAt) {
        this.quoteCreatedAt = quoteCreatedAt;
    }

    public LocalDateTime getQuoteUpdatedAt() {
        return quoteUpdatedAt;
    }

    public void setQuoteUpdatedAt(LocalDateTime quoteUpdatedAt) {
        this.quoteUpdatedAt = quoteUpdatedAt;
    }
}