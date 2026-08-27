package com.yuhan.fleetflow.controller;

import com.yuhan.fleetflow.dto.response.AvailabilityResponse;
import com.yuhan.fleetflow.dto.response.DriverAvailabilityResponse;
import com.yuhan.fleetflow.dto.response.ScheduleJobResponse;
import com.yuhan.fleetflow.dto.response.TruckAvailabilityResponse;
import com.yuhan.fleetflow.service.ScheduleService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScheduleService scheduleService;

    @Test
    void shouldGetScheduleForDate() throws Exception {

        LocalDate date = LocalDate.of(2026, 8, 30);

        ScheduleJobResponse job = new ScheduleJobResponse();

        job.setJobId(104L);
        job.setPickupLocation("Kuala Lumpur");
        job.setDropoffLocation("Penang");

        job.setPickupDatetime(
                LocalDateTime.of(
                        2026,
                        8,
                        30,
                        9,
                        0
                )
        );

        job.setExpectedDropoffDatetime(
                LocalDateTime.of(
                        2026,
                        8,
                        30,
                        16,
                        0
                )
        );

        job.setJobStatus("SCHEDULED");

        job.setDriverId(2L);
        job.setDriverName("Ahmad Rahman");

        job.setTruckId(1L);
        job.setTruckRegistrationNumber("VBC 2314");
        job.setTruckModel("Volvo FH");

        when(scheduleService.getSchedule(date))
                .thenReturn(List.of(job));

        mockMvc.perform(
                        get("/api/schedule")
                                .param("date", "2026-08-30")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].jobId").value(104))
                .andExpect(
                        jsonPath("$[0].pickupLocation")
                                .value("Kuala Lumpur")
                )
                .andExpect(
                        jsonPath("$[0].dropoffLocation")
                                .value("Penang")
                )
                .andExpect(
                        jsonPath("$[0].driverName")
                                .value("Ahmad Rahman")
                )
                .andExpect(
                        jsonPath("$[0].truckRegistrationNumber")
                                .value("VBC 2314")
                );
    }

    @Test
    void shouldGetAvailabilityForRequestedPeriod() throws Exception {

        LocalDateTime pickup =
                LocalDateTime.of(
                        2026,
                        8,
                        30,
                        9,
                        0
                );

        LocalDateTime dropoff =
                LocalDateTime.of(
                        2026,
                        8,
                        30,
                        16,
                        0
                );

        DriverAvailabilityResponse driver =
                new DriverAvailabilityResponse();

        driver.setDriverId(2L);
        driver.setDriverName("Ahmad Rahman");
        driver.setAvailable(false);
        driver.setConflictingJobId(104L);

        driver.setConflictStart(
                LocalDateTime.of(
                        2026,
                        8,
                        30,
                        10,
                        0
                )
        );

        driver.setConflictEnd(
                LocalDateTime.of(
                        2026,
                        8,
                        30,
                        14,
                        0
                )
        );

        driver.setReason("SCHEDULE_CONFLICT");

        TruckAvailabilityResponse truck =
                new TruckAvailabilityResponse();

        truck.setTruckId(1L);
        truck.setRegistrationNumber("VBC 2314");
        truck.setModel("Volvo FH");
        truck.setStatus("ACTIVE");
        truck.setAvailable(true);

        AvailabilityResponse response =
                new AvailabilityResponse();

        response.setPickup(pickup);
        response.setDropoff(dropoff);
        response.setDrivers(List.of(driver));
        response.setTrucks(List.of(truck));

        when(
                scheduleService.getAvailability(
                        pickup,
                        dropoff
                )
        ).thenReturn(response);

        mockMvc.perform(
                        get("/api/availability")
                                .param(
                                        "pickup",
                                        "2026-08-30T09:00:00"
                                )
                                .param(
                                        "dropoff",
                                        "2026-08-30T16:00:00"
                                )
                )
                .andExpect(status().isOk())

                .andExpect(
                        jsonPath("$.pickup")
                                .value("2026-08-30T09:00:00")
                )

                .andExpect(
                        jsonPath("$.dropoff")
                                .value("2026-08-30T16:00:00")
                )

                .andExpect(
                        jsonPath("$.drivers[0].driverId")
                                .value(2)
                )

                .andExpect(
                        jsonPath("$.drivers[0].driverName")
                                .value("Ahmad Rahman")
                )

                .andExpect(
                        jsonPath("$.drivers[0].available")
                                .value(false)
                )

                .andExpect(
                        jsonPath("$.drivers[0].conflictingJobId")
                                .value(104)
                )

                .andExpect(
                        jsonPath("$.drivers[0].reason")
                                .value("SCHEDULE_CONFLICT")
                )

                .andExpect(
                        jsonPath("$.trucks[0].truckId")
                                .value(1)
                )

                .andExpect(
                        jsonPath("$.trucks[0].available")
                                .value(true)
                );
    }

    @Test
    void shouldReturnBadRequestWhenScheduleDateIsMissing()
            throws Exception {

        mockMvc.perform(
                        get("/api/schedule")
                )
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void shouldReturnBadRequestWhenAvailabilityParameterIsMissing()
            throws Exception {

        mockMvc.perform(
                        get("/api/availability")
                                .param(
                                        "pickup",
                                        "2026-08-30T09:00:00"
                                )
                )
                .andExpect(
                        status().isBadRequest()
                );
    }
}