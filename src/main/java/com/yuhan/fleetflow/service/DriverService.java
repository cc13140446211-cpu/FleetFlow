package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.response.DriverResponse;
import com.yuhan.fleetflow.mapper.EmployeeMapper;
import com.yuhan.fleetflow.model.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final EmployeeMapper employeeMapper;

    public DriverService(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    public List<DriverResponse> getAllDrivers() {

        return employeeMapper.findAllDrivers()
                .stream()
                .map(this::toDriverResponse)
                .toList();
    }

    public DriverResponse getDriverById(Long id) {

        Employee employee =
                employeeMapper.findDriverById(id);

        if (employee == null) {
            throw new IllegalArgumentException(
                    "Driver not found"
            );
        }

        return toDriverResponse(employee);
    }

    private DriverResponse toDriverResponse(
            Employee employee
    ) {

        DriverResponse response =
                new DriverResponse();

        response.setDriverId(
                employee.getEmpId()
        );

        response.setName(
                employee.getEmpName()
        );

        response.setPhone(
                employee.getEmpPhone()
        );

        response.setLicenseNumber(
                employee.getEmpLicenseNumber()
        );

        response.setLicenseExpiryDate(
                employee.getEmpLicenseExpiryDate()
        );

        response.setStatus(
                employee.getEmpStatus()
        );

        return response;
    }
}