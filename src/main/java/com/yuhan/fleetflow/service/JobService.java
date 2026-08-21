package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.request.CreateJobRequest;
import com.yuhan.fleetflow.exception.*;
import com.yuhan.fleetflow.mapper.EmployeeMapper;
import com.yuhan.fleetflow.mapper.JobMapper;
import com.yuhan.fleetflow.mapper.QuoteMapper;
import com.yuhan.fleetflow.mapper.TruckMapper;
import com.yuhan.fleetflow.model.Employee;
import com.yuhan.fleetflow.model.Job;
import com.yuhan.fleetflow.model.Quote;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class JobService {

    private final JobMapper jobMapper;
    private final QuoteMapper quoteMapper;
    private final EmployeeMapper employeeMapper;
    private final TruckMapper truckMapper;

    public JobService(
            JobMapper jobMapper,
            QuoteMapper quoteMapper,
            EmployeeMapper employeeMapper,
            TruckMapper truckMapper
    ) {
        this.jobMapper = jobMapper;
        this.quoteMapper = quoteMapper;
        this.employeeMapper = employeeMapper;
        this.truckMapper = truckMapper;
    }

    @Transactional
    public Job createJobFromQuote(
            Long quoteId,
            CreateJobRequest request
    ) {

        Quote quote = quoteMapper.findById(quoteId);

        if (quote == null) {
            throw new QuoteNotFoundException(quoteId);
        }

        if (!"ACCEPTED".equals(quote.getQuoteStatus())) {
            throw new InvalidQuoteStateException(
                    "Only ACCEPTED quotes can be converted to jobs"
            );
        }

        if (jobMapper.countByQuoteId(quoteId) > 0) {
            throw new InvalidQuoteStateException(
                    "A job has already been created for quote " + quoteId
            );
        }

        Employee driver =
                employeeMapper.findById(request.getDriverEmpId());

        if (driver == null) {
            throw new EmployeeNotFoundException(
                    request.getDriverEmpId()
            );
        }

        if (!"DRIVER".equals(driver.getEmpRole())) {
            throw new InvalidEmployeeRoleException(
                    driver.getEmpId(),
                    "DRIVER"
            );
        }

        Employee scheduler =
                employeeMapper.findById(request.getScheduledByEmpId());

        if (scheduler == null) {
            throw new EmployeeNotFoundException(
                    request.getScheduledByEmpId()
            );
        }

        if (!"DISPATCHER".equals(scheduler.getEmpRole())) {
            throw new InvalidEmployeeRoleException(
                    scheduler.getEmpId(),
                    "DISPATCHER"
            );
        }

        if (truckMapper.existsById(request.getTruckId()) == 0) {
            throw new ResourceNotFoundException(
                    "Truck not found with id: " + request.getTruckId()
            );
        }

        if (!request.getJobExpectedDropoffDatetime()
                .isAfter(request.getJobPickupDatetime())) {

            throw new InvalidJobStateException(
                    "Expected drop-off time must be after pickup time"
            );
        }

        LocalDateTime pickupTime =
                request.getJobPickupDatetime();

        LocalDateTime dropoffTime =
                request.getJobExpectedDropoffDatetime();

        int driverConflicts =
                jobMapper.countDriverConflicts(
                        request.getDriverEmpId(),
                        pickupTime,
                        dropoffTime
                );

        if (driverConflicts > 0) {
            throw new ResourceUnavailableException(
                    "Driver " + request.getDriverEmpId()
                            + " is unavailable during the requested time"
            );
        }

        int truckConflicts =
                jobMapper.countTruckConflicts(
                        request.getTruckId(),
                        pickupTime,
                        dropoffTime
                );

        if (truckConflicts > 0) {
            throw new ResourceUnavailableException(
                    "Truck " + request.getTruckId()
                            + " is unavailable during the requested time"
            );
        }

        Job job = new Job();

        job.setQuoteId(quoteId);
        job.setDriverEmpId(request.getDriverEmpId());
        job.setScheduledByEmpId(request.getScheduledByEmpId());
        job.setTruckId(request.getTruckId());
        job.setJobPickupDatetime(request.getJobPickupDatetime());
        job.setJobExpectedDropoffDatetime(
                request.getJobExpectedDropoffDatetime()
        );
        job.setJobFinalPrice(request.getJobFinalPrice());

        jobMapper.insert(job);

        quoteMapper.updateStatus(quoteId, "CONVERTED");

        return jobMapper.findById(job.getJobId());
    }
}