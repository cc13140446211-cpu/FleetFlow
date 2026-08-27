package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.response.DriverResponse;
import com.yuhan.fleetflow.dto.response.ScheduleJobResponse;
import com.yuhan.fleetflow.exception.DriverNotFoundException;
import com.yuhan.fleetflow.mapper.EmployeeMapper;
import com.yuhan.fleetflow.mapper.ScheduleMapper;
import com.yuhan.fleetflow.model.Employee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DriverService {

    private final EmployeeMapper employeeMapper;
    private final ScheduleMapper scheduleMapper;

    public DriverService(EmployeeMapper employeeMapper, ScheduleMapper scheduleMapper) {
        this.employeeMapper = employeeMapper;
        this.scheduleMapper = scheduleMapper;
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
            throw new DriverNotFoundException(id);
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

    public List<ScheduleJobResponse> getDriverSchedule(
            Long driverId,
            LocalDate date
    ) {

        Employee driver =
                employeeMapper.findDriverById(driverId);

        if (driver == null) {
            throw new DriverNotFoundException(driverId);
        }

        return scheduleMapper.findDriverScheduleByDate(
                driverId,
                date
        );
    }
}