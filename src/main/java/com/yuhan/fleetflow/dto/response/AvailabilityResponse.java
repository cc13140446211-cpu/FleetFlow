package com.yuhan.fleetflow.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class AvailabilityResponse {

    private LocalDateTime pickup;
    private LocalDateTime dropoff;

    private List<DriverAvailabilityResponse> drivers;
    private List<TruckAvailabilityResponse> trucks;

    public AvailabilityResponse() {
    }

    public LocalDateTime getPickup() {
        return pickup;
    }

    public void setPickup(LocalDateTime pickup) {
        this.pickup = pickup;
    }

    public LocalDateTime getDropoff() {
        return dropoff;
    }

    public void setDropoff(LocalDateTime dropoff) {
        this.dropoff = dropoff;
    }

    public List<DriverAvailabilityResponse> getDrivers() {
        return drivers;
    }

    public void setDrivers(
            List<DriverAvailabilityResponse> drivers
    ) {
        this.drivers = drivers;
    }

    public List<TruckAvailabilityResponse> getTrucks() {
        return trucks;
    }

    public void setTrucks(
            List<TruckAvailabilityResponse> trucks
    ) {
        this.trucks = trucks;
    }
}