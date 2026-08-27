package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.response.AvailabilityResponse;
import com.yuhan.fleetflow.dto.response.DriverAvailabilityResponse;
import com.yuhan.fleetflow.dto.response.ScheduleJobResponse;
import com.yuhan.fleetflow.dto.response.TruckAvailabilityResponse;
import com.yuhan.fleetflow.mapper.ScheduleMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {

    @Mock
    private ScheduleMapper scheduleMapper;

    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        scheduleService =
                new ScheduleService(scheduleMapper);
    }

    @Test
    void shouldReturnScheduleForDate() {

        LocalDate date =
                LocalDate.of(2026, 8, 30);

        ScheduleJobResponse job =
                new ScheduleJobResponse();

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

        when(scheduleMapper.findJobsByDate(date))
                .thenReturn(List.of(job));

        List<ScheduleJobResponse> result =
                scheduleService.getSchedule(date);

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(
                104L,
                result.get(0).getJobId()
        );

        assertEquals(
                "Ahmad Rahman",
                result.get(0).getDriverName()
        );

        assertEquals(
                "VBC 2314",
                result.get(0)
                        .getTruckRegistrationNumber()
        );

        verify(scheduleMapper)
                .findJobsByDate(date);
    }

    @Test
    void shouldReturnResourceAvailability() {

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

        when(
                scheduleMapper.findDriverAvailability(
                        pickup,
                        dropoff
                )
        ).thenReturn(
                List.of(driver)
        );

        when(
                scheduleMapper.findTruckAvailability(
                        pickup,
                        dropoff
                )
        ).thenReturn(
                List.of(truck)
        );

        AvailabilityResponse result =
                scheduleService.getAvailability(
                        pickup,
                        dropoff
                );

        assertNotNull(result);

        assertEquals(
                pickup,
                result.getPickup()
        );

        assertEquals(
                dropoff,
                result.getDropoff()
        );

        assertEquals(
                1,
                result.getDrivers().size()
        );

        assertEquals(
                1,
                result.getTrucks().size()
        );

        assertFalse(
                result.getDrivers()
                        .get(0)
                        .isAvailable()
        );

        assertEquals(
                104L,
                result.getDrivers()
                        .get(0)
                        .getConflictingJobId()
        );

        assertTrue(
                result.getTrucks()
                        .get(0)
                        .isAvailable()
        );

        verify(scheduleMapper)
                .findDriverAvailability(
                        pickup,
                        dropoff
                );

        verify(scheduleMapper)
                .findTruckAvailability(
                        pickup,
                        dropoff
                );
    }

    @Test
    void shouldRejectAvailabilityWhenDropoffIsBeforePickup() {

        LocalDateTime pickup =
                LocalDateTime.of(
                        2026,
                        8,
                        30,
                        16,
                        0
                );

        LocalDateTime dropoff =
                LocalDateTime.of(
                        2026,
                        8,
                        30,
                        9,
                        0
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                scheduleService.getAvailability(
                                        pickup,
                                        dropoff
                                )
                );

        assertEquals(
                "Drop-off time must be later than pickup time",
                exception.getMessage()
        );

        verify(
                scheduleMapper,
                never()
        ).findDriverAvailability(
                any(),
                any()
        );

        verify(
                scheduleMapper,
                never()
        ).findTruckAvailability(
                any(),
                any()
        );
    }

    @Test
    void shouldRejectAvailabilityWhenPickupEqualsDropoff() {

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
                        9,
                        0
                );

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        scheduleService.getAvailability(
                                pickup,
                                dropoff
                        )
        );

        verify(
                scheduleMapper,
                never()
        ).findDriverAvailability(
                any(),
                any()
        );

        verify(
                scheduleMapper,
                never()
        ).findTruckAvailability(
                any(),
                any()
        );
    }
}