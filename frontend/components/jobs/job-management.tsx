"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import { Drawer } from "@/components/drawer";
import { JobDetail } from "@/components/jobs/job-detail";
import {
  JobControls,
  JobList,
  type JobStatusFilter,
} from "@/components/jobs/job-list";
import { PageHeader } from "@/components/page-header";
import { ApiError } from "@/lib/api/client";
import { getJobByID, getJobs } from "@/lib/api/jobs";
import type { Job } from "@/lib/api/types";

const JOB_LIST_ERROR = "The job data could not be retrieved. Please try again.";
const JOB_DETAIL_ERROR =
  "The job details could not be retrieved. Please try again.";

function getErrorMessage(error: unknown, fallback: string) {
  return error instanceof ApiError ? error.message : fallback;
}

function isAbortError(error: unknown) {
  return error instanceof DOMException && error.name === "AbortError";
}

export function JobManagement() {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState<JobStatusFilter>("ALL");
  const [pickupDate, setPickupDate] = useState("");
  const [selectedJobId, setSelectedJobId] = useState<number | null>(null);
  const [detailJob, setDetailJob] = useState<Job | null>(null);
  const [isDetailLoading, setIsDetailLoading] = useState(false);
  const [detailError, setDetailError] = useState<string | null>(null);

  const loadJobs = useCallback(async (signal?: AbortSignal) => {
    setIsLoading(true);
    setLoadError(null);

    try {
      setJobs(await getJobs(signal));
    } catch (error) {
      if (!isAbortError(error)) {
        setLoadError(getErrorMessage(error, JOB_LIST_ERROR));
      }
    } finally {
      if (!signal?.aborted) setIsLoading(false);
    }
  }, []);

  const loadJobDetail = useCallback(
    async (jobId: number, signal?: AbortSignal) => {
      setIsDetailLoading(true);
      setDetailError(null);
      setDetailJob(null);

      try {
        setDetailJob(await getJobByID(jobId, signal));
      } catch (error) {
        if (!isAbortError(error)) {
          setDetailError(getErrorMessage(error, JOB_DETAIL_ERROR));
        }
      } finally {
        if (!signal?.aborted) setIsDetailLoading(false);
      }
    },
    [],
  );

  useEffect(() => {
    const controller = new AbortController();

    void getJobs(controller.signal)
      .then((data) => {
        setJobs(data);
        setLoadError(null);
      })
      .catch((error: unknown) => {
        if (!isAbortError(error)) {
          setLoadError(getErrorMessage(error, JOB_LIST_ERROR));
        }
      })
      .finally(() => {
        if (!controller.signal.aborted) setIsLoading(false);
      });

    return () => controller.abort();
  }, []);

  useEffect(() => {
    if (selectedJobId === null) return;

    const controller = new AbortController();

    void getJobByID(selectedJobId, controller.signal)
      .then((job) => {
        setDetailJob(job);
        setDetailError(null);
      })
      .catch((error: unknown) => {
        if (!isAbortError(error)) {
          setDetailError(getErrorMessage(error, JOB_DETAIL_ERROR));
        }
      })
      .finally(() => {
        if (!controller.signal.aborted) setIsDetailLoading(false);
      });

    return () => controller.abort();
  }, [selectedJobId]);

  const filteredJobs = useMemo(() => {
    const query = search.trim().toLocaleLowerCase();

    return jobs.filter((job) => {
      const matchesSearch =
        !query ||
        [
          String(job.jobId),
          `j-${job.jobId}`,
          String(job.quoteId),
          `q-${job.quoteId}`,
          String(job.driverEmpId),
          String(job.truckId),
        ].some((value) => value?.toLocaleLowerCase().includes(query));
      const matchesStatus =
        statusFilter === "ALL" || job.jobStatus === statusFilter;
      const matchesDate =
        !pickupDate || job.jobPickupDatetime.slice(0, 10) === pickupDate;

      return matchesSearch && matchesStatus && matchesDate;
    });
  }, [jobs, pickupDate, search, statusFilter]);

  const openJob = useCallback((job: Job) => {
    setSelectedJobId(job.jobId);
    setDetailJob(null);
    setDetailError(null);
    setIsDetailLoading(true);
  }, []);

  const closeDrawer = useCallback(() => {
    setSelectedJobId(null);
    setDetailJob(null);
    setDetailError(null);
  }, []);

  const clearFilters = useCallback(() => {
    setSearch("");
    setStatusFilter("ALL");
    setPickupDate("");
  }, []);

  return (
    <div className="space-y-8">
      <PageHeader title="Jobs" />

      <section
        className="space-y-4 border-t border-border pt-8"
        aria-label="Job list"
      >
        <JobControls
          search={search}
          onSearchChange={setSearch}
          status={statusFilter}
          onStatusChange={setStatusFilter}
          pickupDate={pickupDate}
          onPickupDateChange={setPickupDate}
          visibleCount={filteredJobs.length}
          totalCount={jobs.length}
          showCount={!isLoading && !loadError}
          onClearFilters={clearFilters}
        />

        <JobList
          jobs={filteredJobs}
          totalCount={jobs.length}
          isLoading={isLoading}
          error={loadError}
          onRetry={() => void loadJobs()}
          onOpenJob={openJob}
        />
      </section>

      <Drawer
        open={selectedJobId !== null}
        title="Job details"
        description="Operational schedule, assignment, and linked quote information."
        onClose={closeDrawer}
      >
        <JobDetail
          job={detailJob}
          isLoading={isDetailLoading}
          error={detailError}
          onRetry={() => {
            if (selectedJobId !== null) {
              void loadJobDetail(selectedJobId);
            }
          }}
        />
      </Drawer>
    </div>
  );
}
