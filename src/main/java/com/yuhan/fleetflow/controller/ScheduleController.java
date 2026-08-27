package com.yuhan.fleetflow.controller;

import com.yuhan.fleetflow.dto.response.AvailabilityResponse;
import com.yuhan.fleetflow.dto.response.ScheduleJobResponse;
import com.yuhan.fleetflow.service.ScheduleService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(
            ScheduleService scheduleService
    ) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/schedule")
    public List<ScheduleJobResponse> getSchedule(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return scheduleService.getSchedule(date);
    }

    @GetMapping("/availability")
    public AvailabilityResponse getAvailability(

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime pickup,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime dropoff
    ) {

        return scheduleService.getAvailability(
                pickup,
                dropoff
        );
    }
}