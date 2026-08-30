import { StatusBadge } from "@/components/status-badge";
import type { Job } from "@/lib/api/types";

type JobDetailProps = {
  job: Job | null;
  isLoading: boolean;
  error: string | null;
  onRetry: () => void;
};

const dateTimeFormatter = new Intl.DateTimeFormat("en-MY", {
  day: "numeric",
  month: "short",
  year: "numeric",
  hour: "2-digit",
  minute: "2-digit",
});

const currencyFormatter = new Intl.NumberFormat("en-MY", {
  style: "currency",
  currency: "MYR",
  maximumFractionDigits: 2,
});

function formatDateTime(value: string) {
  const date = new Date(value);
  return Number.isNaN(date.valueOf()) ? value : dateTimeFormatter.format(date);
}

export function JobDetail({
  job,
  isLoading,
  error,
  onRetry,
}: JobDetailProps) {
  if (isLoading) return <JobDetailSkeleton />;

  if (error) {
    return (
      <div className="px-6 py-8 sm:px-8" role="alert">
        <h3 className="font-semibold">Unable to load job details</h3>
        <p className="mt-2 text-secondary">{error}</p>
        <button type="button" onClick={onRetry} className="button-secondary mt-5">
          Retry
        </button>
      </div>
    );
  }

  if (!job) return null;

  return (
    <div className="space-y-8 px-6 py-6 sm:px-8">
      <section>
        <div className="flex items-start justify-between gap-4">
          <div>
            <p className="text-lg font-semibold">Job J-{job.jobId}</p>
            <p className="mt-1 text-secondary">From quote Q-{job.quoteId}</p>
          </div>
          <StatusBadge status={job.jobStatus} />
        </div>

        <dl className="mt-8 grid gap-5 sm:grid-cols-2">
          <DetailItem label="Customer">
            Available via quote Q-{job.quoteId}
          </DetailItem>
          <DetailItem label="Final price">
            <span className="font-medium">
              {currencyFormatter.format(Number(job.jobFinalPrice))}
            </span>
          </DetailItem>
        </dl>
      </section>

      <section className="border-t border-border pt-8" aria-labelledby="route-heading">
        <h3
          id="route-heading"
          className="text-xs font-semibold uppercase tracking-[0.08em] text-secondary"
        >
          Route & schedule
        </h3>
        <dl className="mt-5 grid gap-5 sm:grid-cols-2">
          <DetailItem label="Pickup location">
            Available via quote Q-{job.quoteId}
          </DetailItem>
          <DetailItem label="Drop-off location">
            Available via quote Q-{job.quoteId}
          </DetailItem>
          <DetailItem label="Scheduled pickup">
            {formatDateTime(job.jobPickupDatetime)}
          </DetailItem>
          <DetailItem label="Expected drop-off">
            {formatDateTime(job.jobExpectedDropoffDatetime)}
          </DetailItem>
        </dl>
      </section>

      <section className="border-t border-border pt-8" aria-labelledby="resources-heading">
        <h3
          id="resources-heading"
          className="text-xs font-semibold uppercase tracking-[0.08em] text-secondary"
        >
          Assigned resources
        </h3>
        <dl className="mt-5 grid gap-5 sm:grid-cols-2">
          <DetailItem label="Driver">Employee #{job.driverEmpId}</DetailItem>
          <DetailItem label="Truck">Truck #{job.truckId}</DetailItem>
          <DetailItem label="Scheduled by">
            Employee #{job.scheduledByEmpId}
          </DetailItem>
        </dl>
      </section>

      <section className="border-t border-border pt-8" aria-labelledby="record-heading">
        <h3
          id="record-heading"
          className="text-xs font-semibold uppercase tracking-[0.08em] text-secondary"
        >
          Record
        </h3>
        <dl className="mt-5 grid gap-5 sm:grid-cols-2">
          <DetailItem label="Created">{formatDateTime(job.jobCreatedAt)}</DetailItem>
          <DetailItem label="Last updated">
            {formatDateTime(job.jobUpdatedAt)}
          </DetailItem>
        </dl>
      </section>
    </div>
  );
}

function JobDetailSkeleton() {
  return (
    <div className="space-y-8 px-6 py-6 sm:px-8" aria-busy="true">
      <div className="flex items-start justify-between">
        <div className="space-y-3">
          <div className="h-5 w-32 rounded-full bg-active" />
          <div className="h-3 w-24 rounded-full bg-active" />
        </div>
        <div className="h-6 w-24 rounded-full bg-active" />
      </div>
      {Array.from({ length: 3 }).map((_, sectionIndex) => (
        <div key={sectionIndex} className="border-t border-border pt-8">
          <div className="h-3 w-28 rounded-full bg-active" />
          <div className="mt-5 grid gap-6 sm:grid-cols-2">
            {Array.from({ length: 4 }).map((_, itemIndex) => (
              <div key={itemIndex} className="space-y-2">
                <div className="h-2.5 w-20 rounded-full bg-active" />
                <div className="h-3 w-32 rounded-full bg-active" />
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}

function DetailItem({
  label,
  children,
}: {
  label: string;
  children: React.ReactNode;
}) {
  return (
    <div>
      <dt className="text-xs text-muted">{label}</dt>
      <dd className="mt-1 whitespace-pre-line">{children}</dd>
    </div>
  );
}
