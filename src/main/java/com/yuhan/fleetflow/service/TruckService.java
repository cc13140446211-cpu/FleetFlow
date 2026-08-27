package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.response.ScheduleJobResponse;
import com.yuhan.fleetflow.dto.response.TruckResponse;
import com.yuhan.fleetflow.exception.TruckNotFoundException;
import com.yuhan.fleetflow.mapper.ScheduleMapper;
import com.yuhan.fleetflow.mapper.TruckMapper;
import com.yuhan.fleetflow.model.Truck;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TruckService {

    private final TruckMapper truckMapper;
    private final ScheduleMapper scheduleMapper;

    public TruckService(TruckMapper truckMapper, ScheduleMapper scheduleMapper) {
        this.truckMapper = truckMapper;
        this.scheduleMapper = scheduleMapper;
    }

    public List<TruckResponse> getAllTrucks() {
        return truckMapper.findAll()
                .stream()
                .map(this::toTruckResponse)
                .toList();
    }

    public TruckResponse getTruckById(Long id) {

        Truck truck = truckMapper.findById(id);

        if (truck == null) {
            throw new TruckNotFoundException(id);
        }

        return toTruckResponse(truck);
    }

    private TruckResponse toTruckResponse(Truck truck) {

        TruckResponse response = new TruckResponse();

        response.setTruckId(truck.getTruckId());
        response.setVin(truck.getTruckVin());
        response.setRegistrationNumber(
                truck.getTruckRegistrationNumber()
        );
        response.setModel(truck.getTruckModel());
        response.setCapacityKg(
                truck.getTruckCapacityKg()
        );
        response.setStatus(
                truck.getTruckStatus()
        );

        return response;
    }

    public List<ScheduleJobResponse> getTruckSchedule(
            Long truckId,
            LocalDate date
    ) {

        Truck truck =
                truckMapper.findById(truckId);

        if (truck == null) {
            throw new TruckNotFoundException(truckId);
        }

        return scheduleMapper.findTruckScheduleByDate(
                truckId,
                date
        );
    }
}