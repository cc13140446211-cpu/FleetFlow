package com.yuhan.fleetflow.controller;

import com.yuhan.fleetflow.dto.request.CreateJobRequest;
import com.yuhan.fleetflow.dto.request.UpdateJobStatusRequest;
import com.yuhan.fleetflow.model.Job;
import com.yuhan.fleetflow.service.JobService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/api/quotes/{quoteId}/jobs")
    public Job createJobFromQuote(
            @PathVariable Long quoteId,
            @Valid @RequestBody CreateJobRequest request
    ) {
        return jobService.createJobFromQuote(quoteId, request);
    }

    @GetMapping("/api/jobs/{id}")
    public Job getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }

    @PatchMapping("/api/jobs/{id}/status")
    public Job updateJobStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateJobStatusRequest request
    ) {
        return jobService.updateJobStatus(id, request);
    }

    @GetMapping("/api/jobs")
    public List<Job> getJobs(

            @RequestParam(required = false)
            String status,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate date
    ) {
        return jobService.getJobs(
                status,
                date
        );
    }
}