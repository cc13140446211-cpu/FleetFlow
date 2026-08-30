package com.yuhan.fleetflow.service;

import com.yuhan.fleetflow.dto.request.CreateJobRequest;
import com.yuhan.fleetflow.dto.request.UpdateJobRequest;
import com.yuhan.fleetflow.dto.request.UpdateJobStatusRequest;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

        if (!"PAID".equals(quote.getQuotePaymentStatus())) {
            throw new InvalidQuoteStateException(
                    "Quote must be paid before a job can be scheduled"
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

    public List<Job> getAllJobs() {
        return jobMapper.findAll();
    }

    public Job getJobById(Long id) {

        Job job = jobMapper.findById(id);

        if (job == null) {
            throw new JobNotFoundException(id);
        }

        return job;
    }

    public Job updateJobStatus(
            Long id,
            UpdateJobStatusRequest request
    ) {

        Job job = jobMapper.findById(id);

        if (job == null) {
            throw new JobNotFoundException(id);
        }

        String currentStatus = job.getJobStatus();
        String newStatus = request.getStatus().toUpperCase();

        if (!isValidJobStatus(newStatus)) {
            throw new InvalidJobStateException(
                    "Invalid job status: " + newStatus
            );
        }

        if (!isValidJobStatusTransition(currentStatus, newStatus)) {
            throw new InvalidJobStateException(
                    "Cannot change job status from "
                            + currentStatus
                            + " to "
                            + newStatus
            );
        }

        jobMapper.updateStatus(id, newStatus);

        return jobMapper.findById(id);
    }

    @Transactional
    public Job updateJob(Long id, UpdateJobRequest request) {
        Job job = getJobById(id);

        if (!"SCHEDULED".equals(job.getJobStatus())) {
            throw new InvalidJobStateException(
                    "Only SCHEDULED jobs can be edited"
            );
        }

        Employee driver = employeeMapper.findById(request.getDriverEmpId());

        if (driver == null) {
            throw new EmployeeNotFoundException(request.getDriverEmpId());
        }

        if (!"DRIVER".equals(driver.getEmpRole())) {
            throw new InvalidEmployeeRoleException(driver.getEmpId(), "DRIVER");
        }

        if (truckMapper.existsById(request.getTruckId()) == 0) {
            throw new ResourceNotFoundException(
                    "Truck not found with id: " + request.getTruckId()
            );
        }

        LocalDateTime pickupTime = request.getJobPickupDatetime();
        LocalDateTime dropoffTime = request.getJobExpectedDropoffDatetime();

        if (!dropoffTime.isAfter(pickupTime)) {
            throw new InvalidJobStateException(
                    "Expected drop-off time must be after pickup time"
            );
        }

        if (jobMapper.countDriverConflictsExcludingJob(
                request.getDriverEmpId(), id, pickupTime, dropoffTime) > 0) {
            throw new ResourceUnavailableException(
                    "Driver " + request.getDriverEmpId()
                            + " is unavailable during the requested time"
            );
        }

        if (jobMapper.countTruckConflictsExcludingJob(
                request.getTruckId(), id, pickupTime, dropoffTime) > 0) {
            throw new ResourceUnavailableException(
                    "Truck " + request.getTruckId()
                            + " is unavailable during the requested time"
            );
        }

        job.setDriverEmpId(request.getDriverEmpId());
        job.setTruckId(request.getTruckId());
        job.setJobPickupDatetime(pickupTime);
        job.setJobExpectedDropoffDatetime(dropoffTime);
        job.setJobFinalPrice(request.getJobFinalPrice());

        jobMapper.update(job);

        return getJobById(id);
    }

    private boolean isValidJobStatus(String status) {
        return status.equals("SCHEDULED")
                || status.equals("IN_PROGRESS")
                || status.equals("COMPLETED")
                || status.equals("CANCELLED");
    }

    private boolean isValidJobStatusTransition(
            String currentStatus,
            String newStatus
    ) {

        if (currentStatus.equals("SCHEDULED")) {
            return newStatus.equals("IN_PROGRESS")
                    || newStatus.equals("CANCELLED");
        }

        if (currentStatus.equals("IN_PROGRESS")) {
            return newStatus.equals("COMPLETED");
        }

        return false;

    }

    public List<Job> getJobs(
            String status,
            LocalDate date
    ) {

        if (status != null) {
            String normalizedStatus =
                    status.toUpperCase();

            if (!List.of(
                    "SCHEDULED",
                    "IN_PROGRESS",
                    "COMPLETED",
                    "CANCELLED"
            ).contains(normalizedStatus)) {
                throw new InvalidJobStatusException(
                        "Invalid job status"
                );
            }

            status = normalizedStatus;
        }

        return jobMapper.findJobs(
                status,
                date
        );
    }
}
