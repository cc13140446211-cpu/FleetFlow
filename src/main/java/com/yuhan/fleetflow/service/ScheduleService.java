package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.response.AvailabilityResponse;
import com.yuhan.fleetflow.dto.response.DriverAvailabilityResponse;
import com.yuhan.fleetflow.dto.response.ScheduleJobResponse;
import com.yuhan.fleetflow.dto.response.TruckAvailabilityResponse;

import com.yuhan.fleetflow.exception.InvalidJobStateException;
import com.yuhan.fleetflow.mapper.ScheduleMapper;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleMapper scheduleMapper;

    public ScheduleService(
            ScheduleMapper scheduleMapper
    ) {
        this.scheduleMapper = scheduleMapper;
    }

    public List<ScheduleJobResponse> getSchedule(
            LocalDate date
    ) {
        return scheduleMapper.findJobsByDate(date);
    }

    public AvailabilityResponse getAvailability(
            LocalDateTime pickup,
            LocalDateTime dropoff
    ) {

        if (!dropoff.isAfter(pickup)) {
            throw new IllegalArgumentException(
                    "Drop-off time must be later than pickup time"
            );
        }

        List<DriverAvailabilityResponse> drivers =
                scheduleMapper.findDriverAvailability(
                        pickup,
                        dropoff
                );

        List<TruckAvailabilityResponse> trucks =
                scheduleMapper.findTruckAvailability(
                        pickup,
                        dropoff
                );

        AvailabilityResponse response =
                new AvailabilityResponse();

        response.setPickup(pickup);
        response.setDropoff(dropoff);

        response.setDrivers(drivers);
        response.setTrucks(trucks);

        return response;
    }
}