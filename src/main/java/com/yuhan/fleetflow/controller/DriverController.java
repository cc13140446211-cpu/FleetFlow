package com.yuhan.fleetflow.controller;

import com.yuhan.fleetflow.dto.response.DriverResponse;
import com.yuhan.fleetflow.service.DriverService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(
            DriverService driverService
    ) {
        this.driverService = driverService;
    }

    @GetMapping
    public List<DriverResponse> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/{id}")
    public DriverResponse getDriverById(
            @PathVariable Long id
    ) {
        return driverService.getDriverById(id);
    }
}