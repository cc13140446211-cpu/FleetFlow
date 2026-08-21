package com.yuhan.fleetflow.controller;

import com.yuhan.fleetflow.dto.request.CreateJobRequest;
import com.yuhan.fleetflow.model.Job;
import com.yuhan.fleetflow.service.JobService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotes")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/{quoteId}/jobs")
    public Job createJobFromQuote(
            @PathVariable Long quoteId,
            @Valid @RequestBody CreateJobRequest request
    ) {
        return jobService.createJobFromQuote(quoteId, request);
    }
}