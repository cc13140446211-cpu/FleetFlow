"use client";

import { useMemo } from "react";
import {
  DataTable,
  DataTableSkeleton,
  type DataTableColumn,
} from "@/components/data-table";
import { StatusBadge } from "@/components/status-badge";
import type { Job, JobStatus } from "@/lib/api/types";

export type JobStatusFilter = "ALL" | JobStatus;

type JobControlsProps = {
  search: string;
  onSearchChange: (value: string) => void;
  status: JobStatusFilter;
  onStatusChange: (value: JobStatusFilter) => void;
  pickupDate: string;
  onPickupDateChange: (value: string) => void;
  visibleCount: number;
  totalCount: number;
  showCount: boolean;
  onClearFilters: () => void;
};

export function JobControls({
  search,
  onSearchChange,
  status,
  onStatusChange,
  pickupDate,
  onPickupDateChange,
  visibleCount,
  totalCount,
  showCount,
  onClearFilters,
}: JobControlsProps) {
  const hasFilters = search !== "" || status !== "ALL" || pickupDate !== "";

  return (
    <div className="flex w-full flex-wrap items-center gap-3">
      <label className="block min-w-[210px] flex-1 sm:max-w-sm">
        <span className="sr-only">Search jobs</span>
        <input
          type="search"
          value={search}
          onChange={(event) => onSearchChange(event.target.value)}
          placeholder="Search jobs, quotes, drivers, or trucks…"
          className="control-field placeholder:text-muted"
        />
      </label>

      <label className="min-w-40">
        <span className="sr-only">Filter by job status</span>
        <select
          value={status}
          onChange={(event) =>
            onStatusChange(event.target.value as JobStatusFilter)
          }
          className="control-field"
        >
          <option value="ALL">All statuses</option>
          <option value="SCHEDULED">Scheduled</option>
          <option value="IN_PROGRESS">In progress</option>
          <option value="COMPLETED">Completed</option>
          <option value="CANCELLED">Cancelled</option>
        </select>
      </label>

      <label className="min-w-40">
        <span className="sr-only">Filter by pickup date</span>
        <input
          type="date"
          value={pickupDate}
          onChange={(event) => onPickupDateChange(event.target.value)}
          className="control-field"
        />
      </label>

      {hasFilters ? (
        <button
          type="button"
          onClick={onClearFilters}
          className="button-secondary px-3"
        >
          Clear
        </button>
      ) : null}

      {showCount ? (
        <p className="ml-auto text-xs text-muted">
          {visibleCount} of {totalCount} jobs
        </p>
      ) : null}
    </div>
  );
}

type JobListProps = {
  jobs: Job[];
  totalCount: number;
  isLoading: boolean;
  error: string | null;
  onRetry: () => void;
  onOpenJob: (job: Job) => void;
};

const dateFormatter = new Intl.DateTimeFormat("en-MY", {
  day: "numeric",
  month: "short",
  year: "numeric",
});

const timeFormatter = new Intl.DateTimeFormat("en-MY", {
  hour: "2-digit",
  minute: "2-digit",
});

const currencyFormatter = new Intl.NumberFormat("en-MY", {
  style: "currency",
  currency: "MYR",
  maximumFractionDigits: 2,
});

function formatDate(value: string) {
  const date = new Date(value);
  return Number.isNaN(date.valueOf()) ? value : dateFormatter.format(date);
}

function formatTime(value: string) {
  const date = new Date(value);
  return Number.isNaN(date.valueOf()) ? value : timeFormatter.format(date);
}

export function JobList({
  jobs,
  totalCount,
  isLoading,
  error,
  onRetry,
  onOpenJob,
}: JobListProps) {
  const columns = useMemo<DataTableColumn<Job>[]>(
    () => [
      {
        key: "job",
        header: "Job / quote",
        render: (job) => (
          <div className="whitespace-nowrap">
            <p className="font-semibold">J-{job.jobId}</p>
            <p className="mt-0.5 text-xs text-muted">Q-{job.quoteId}</p>
          </div>
        ),
      },
      {
        key: "customer",
        header: "Customer",
        render: (job) => (
          <div className="min-w-32">
            <p className="text-secondary">Linked via quote</p>
            <p className="mt-0.5 text-xs text-muted">Q-{job.quoteId}</p>
          </div>
        ),
      },
      {
        key: "route",
        header: "Route",
        render: () => <span className="text-muted">Available via quote</span>,
      },
      {
        key: "schedule",
        header: "Schedule",
        render: (job) => (
          <div className="min-w-36 whitespace-nowrap">
            <p>{formatDate(job.jobPickupDatetime)}</p>
            <p className="mt-0.5 text-xs text-secondary">
              {formatTime(job.jobPickupDatetime)}–
              {formatTime(job.jobExpectedDropoffDatetime)}
            </p>
          </div>
        ),
      },
      {
        key: "resources",
        header: "Assigned",
        render: (job) => (
          <div className="min-w-24 whitespace-nowrap">
            <p>Driver #{job.driverEmpId}</p>
            <p className="mt-0.5 text-xs text-muted">Truck #{job.truckId}</p>
          </div>
        ),
      },
      {
        key: "cost",
        header: "Final price",
        className: "whitespace-nowrap",
        render: (job) => (
          <span className="font-medium">
            {currencyFormatter.format(Number(job.jobFinalPrice))}
          </span>
        ),
      },
      {
        key: "status",
        header: "Status",
        render: (job) => <StatusBadge status={job.jobStatus} />,
      },
      {
        key: "action",
        header: "",
        className: "w-16 text-right",
        render: (job) => (
          <button
            type="button"
            onClick={(event) => {
              event.stopPropagation();
              onOpenJob(job);
            }}
            className="rounded-md px-2 py-1 font-medium text-secondary transition-colors hover:bg-accent-soft hover:text-accent-foreground"
          >
            View
          </button>
        ),
      },
    ],
    [onOpenJob],
  );

  if (isLoading) {
    return <DataTableSkeleton columns={8} rows={6} label="Loading jobs" />;
  }

  if (error) {
    return (
      <div role="alert" className="panel rounded-[10px] px-6 py-12 text-center">
        <h2 className="text-base font-semibold">Unable to load jobs</h2>
        <p className="mx-auto mt-2 max-w-md text-secondary">{error}</p>
        <button type="button" onClick={onRetry} className="button-secondary mt-5">
          Retry
        </button>
      </div>
    );
  }

  if (jobs.length > 0) {
    return (
      <DataTable
        columns={columns}
        rows={jobs}
        getRowKey={(job) => job.jobId}
        getRowLabel={(job) => `View job J-${job.jobId}`}
        onRowClick={onOpenJob}
      />
    );
  }

  const hasJobs = totalCount > 0;

  return (
    <div className="panel rounded-[10px] px-6 py-12 text-center">
      <h2 className="text-base font-semibold">
        {hasJobs ? "No jobs found" : "No jobs scheduled"}
      </h2>
      <p className="mx-auto mt-2 max-w-md text-secondary">
        {hasJobs
          ? "Adjust the search, status, or pickup date filters."
          : "Jobs will appear here after an accepted, paid quote is scheduled."}
      </p>
    </div>
  );
}
