package com.yuhan.fleetflow.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Truck {

    private Long truckId;
    private String truckVin;
    private String truckRegistrationNumber;
    private String truckModel;
    private BigDecimal truckCapacityKg;
    private String truckStatus;
    private LocalDateTime truckCreatedAt;

    public Truck() {
    }

    public Long getTruckId() {
        return truckId;
    }

    public void setTruckId(Long truckId) {
        this.truckId = truckId;
    }

    public String getTruckVin() {
        return truckVin;
    }

    public void setTruckVin(String truckVin) {
        this.truckVin = truckVin;
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

    public BigDecimal getTruckCapacityKg() {
        return truckCapacityKg;
    }

    public void setTruckCapacityKg(BigDecimal truckCapacityKg) {
        this.truckCapacityKg = truckCapacityKg;
    }

    public String getTruckStatus() {
        return truckStatus;
    }

    public void setTruckStatus(String truckStatus) {
        this.truckStatus = truckStatus;
    }

    public LocalDateTime getTruckCreatedAt() {
        return truckCreatedAt;
    }

    public void setTruckCreatedAt(LocalDateTime truckCreatedAt) {
        this.truckCreatedAt = truckCreatedAt;
    }
}