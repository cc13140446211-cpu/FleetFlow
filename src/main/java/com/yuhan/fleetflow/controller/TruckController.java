package com.yuhan.fleetflow.controller;

import com.yuhan.fleetflow.dto.response.ScheduleJobResponse;
import com.yuhan.fleetflow.dto.response.TruckResponse;
import com.yuhan.fleetflow.service.TruckService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trucks")
public class TruckController {

    private final TruckService truckService;

    public TruckController(TruckService truckService) {
        this.truckService = truckService;
    }

    @GetMapping
    public List<TruckResponse> getAllTrucks() {
        return truckService.getAllTrucks();
    }

    @GetMapping("/{id}")
    public TruckResponse getTruckById(
            @PathVariable Long id
    ) {
        return truckService.getTruckById(id);
    }

    @GetMapping("/{id}/schedule")
    public List<ScheduleJobResponse> getTruckSchedule(
            @PathVariable Long id,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate date
    ) {
        return truckService.getTruckSchedule(
                id,
                date
        );
    }
}