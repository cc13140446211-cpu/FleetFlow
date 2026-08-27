package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.response.TruckResponse;
import com.yuhan.fleetflow.exception.TruckNotFoundException;
import com.yuhan.fleetflow.mapper.TruckMapper;
import com.yuhan.fleetflow.model.Truck;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruckService {

    private final TruckMapper truckMapper;

    public TruckService(TruckMapper truckMapper) {
        this.truckMapper = truckMapper;
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
}