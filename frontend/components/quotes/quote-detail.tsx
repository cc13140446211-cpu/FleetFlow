import Link from "next/link";
import { StatusBadge } from "@/components/status-badge";
import type { Customer, Quote, QuoteStatus } from "@/lib/api/types";

type QuoteDetailProps = {
  quote: Quote | null;
  customer: Customer | null;
  isLoading: boolean;
  error: string | null;
  actionError: string | null;
  actionMessage: string | null;
  isUpdating: boolean;
  onRetry: () => void;
  onStatusChange: (status: QuoteStatus) => void;
  onRecordPayment: () => void;
};

const dateFormatter = new Intl.DateTimeFormat("en-MY", {
  day: "numeric",
  month: "long",
  year: "numeric",
});

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

function formatDate(value: string | null, includeTime = false) {
  if (!value) return "—";
  const date = value.length === 10 ? new Date(`${value}T00:00:00`) : new Date(value);
  if (Number.isNaN(date.valueOf())) return value;
  return includeTime ? dateTimeFormatter.format(date) : dateFormatter.format(date);
}

export function QuoteDetail({
  quote,
  customer,
  isLoading,
  error,
  actionError,
  actionMessage,
  isUpdating,
  onRetry,
  onStatusChange,
  onRecordPayment,
}: QuoteDetailProps) {
  if (isLoading) return <QuoteDetailSkeleton />;

  if (error) {
    return (
      <div className="px-6 py-8 sm:px-8" role="alert">
        <h3 className="font-semibold">Unable to load quote details</h3>
        <p className="mt-2 text-secondary">{error}</p>
        <button
          type="button"
          onClick={onRetry}
          className="mt-5 h-10 rounded-lg border border-border bg-surface px-4 font-medium transition-colors hover:bg-active"
        >
          Retry
        </button>
      </div>
    );
  }

  if (!quote) return null;

  const canSchedule =
    quote.quoteStatus === "ACCEPTED" && quote.quotePaymentStatus === "PAID";

  return (
    <div className="space-y-8 px-6 py-6 sm:px-8">
      {actionMessage ? (
        <div
          role="status"
          className="rounded-lg bg-success-background px-4 py-3 text-success-foreground"
        >
          {actionMessage}
        </div>
      ) : null}
      {actionError ? (
        <div
          role="alert"
          className="rounded-lg bg-error-background px-4 py-3 text-error-foreground"
        >
          {actionError}
        </div>
      ) : null}

      <section>
        <div className="flex items-start justify-between gap-4">
          <div>
            <p className="text-lg font-semibold">Quote Q-{quote.quoteId}</p>
            <p className="mt-1 text-secondary">
              {customer?.custName ?? `Customer #${quote.custId}`}
            </p>
            {customer?.custCompanyName ? (
              <p className="mt-0.5 text-xs text-muted">
                {customer.custCompanyName}
              </p>
            ) : null}
          </div>
          <StatusBadge status={quote.quoteStatus} />
        </div>

        <dl className="mt-8 grid gap-5 sm:grid-cols-2">
          <DetailItem label="Pickup location">
            {quote.quotePickupLocation}
          </DetailItem>
          <DetailItem label="Drop-off location">
            {quote.quoteDropoffLocation}
          </DetailItem>
          <DetailItem label="Preferred pickup">
            {formatDate(quote.quotePreferredPickupDate)}
          </DetailItem>
          <DetailItem label="Quoted price">
            <span className="font-medium">
              {currencyFormatter.format(Number(quote.quotePrice))}
            </span>
          </DetailItem>
          <DetailItem label="Prepared by">
            Employee #{quote.preparedByEmpId}
          </DetailItem>
          <DetailItem label="Created">
            {formatDate(quote.quoteCreatedAt, true)}
          </DetailItem>
        </dl>
      </section>

      <section className="border-t border-border pt-8" aria-labelledby="payment-heading">
        <div className="flex items-center justify-between gap-4">
          <h3
            id="payment-heading"
            className="text-xs font-semibold uppercase tracking-[0.08em] text-secondary"
          >
            Payment
          </h3>
          <StatusBadge status={quote.quotePaymentStatus} />
        </div>
        <p className="mt-4 text-secondary">
          {quote.quoteStatus === "PENDING"
            ? "Payment can be recorded after the quote is accepted."
            : quote.quotePaymentStatus === "PAID"
              ? "Payment has been recorded for this quotation."
              : "Payment is still required before this quote can be scheduled."}
        </p>
      </section>

      <QuoteActions
        quote={quote}
        isUpdating={isUpdating}
        canSchedule={canSchedule}
        onStatusChange={onStatusChange}
        onRecordPayment={onRecordPayment}
      />
    </div>
  );
}

function QuoteActions({
  quote,
  isUpdating,
  canSchedule,
  onStatusChange,
  onRecordPayment,
}: {
  quote: Quote;
  isUpdating: boolean;
  canSchedule: boolean;
  onStatusChange: (status: QuoteStatus) => void;
  onRecordPayment: () => void;
}) {
  const secondaryButton =
    "h-10 rounded-lg border border-border bg-surface px-4 font-medium transition-colors hover:bg-active disabled:cursor-not-allowed disabled:opacity-60";
  const primaryButton =
    "h-10 rounded-lg bg-foreground px-4 font-medium text-white transition-colors hover:bg-foreground/90 disabled:cursor-not-allowed disabled:opacity-60";

  if (quote.quoteStatus === "PENDING") {
    return (
      <section className="flex justify-end gap-3 border-t border-border pt-6">
        <button
          type="button"
          disabled={isUpdating}
          onClick={() => onStatusChange("REJECTED")}
          className={secondaryButton}
        >
          Reject quote
        </button>
        <button
          type="button"
          disabled={isUpdating}
          onClick={() => onStatusChange("ACCEPTED")}
          className={primaryButton}
        >
          {isUpdating ? "Updating…" : "Accept quote"}
        </button>
      </section>
    );
  }

  if (quote.quoteStatus === "ACCEPTED") {
    return (
      <section className="flex flex-wrap items-center justify-end gap-3 border-t border-border pt-6">
        <button
          type="button"
          disabled={isUpdating}
          onClick={() => onStatusChange("CANCELLED")}
          className={secondaryButton}
        >
          Cancel quote
        </button>
        {quote.quotePaymentStatus === "UNPAID" ? (
          <button
            type="button"
            disabled={isUpdating}
            onClick={onRecordPayment}
            className={primaryButton}
          >
            {isUpdating ? "Recording…" : "Mark as paid"}
          </button>
        ) : null}
        {canSchedule ? (
          <Link
            href={`/schedule?quoteId=${quote.quoteId}`}
            className="inline-flex h-10 items-center rounded-lg border border-foreground px-4 font-medium transition-colors hover:bg-active"
          >
            Schedule job
            <span aria-hidden="true" className="ml-1.5">
              →
            </span>
          </Link>
        ) : null}
      </section>
    );
  }

  if (quote.quoteStatus === "CONVERTED") {
    return (
      <p className="border-t border-border pt-6 text-sm text-secondary">
        This quote has been converted to a freight job.
      </p>
    );
  }

  return null;
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

function QuoteDetailSkeleton() {
  return (
    <div className="space-y-8 px-6 py-6 sm:px-8" aria-busy="true">
      <div className="flex items-start justify-between">
        <div className="space-y-3">
          <div className="h-5 w-36 rounded-full bg-active" />
          <div className="h-3 w-28 rounded-full bg-active" />
        </div>
        <div className="h-6 w-20 rounded-full bg-active" />
      </div>
      <div className="grid gap-6 sm:grid-cols-2">
        {Array.from({ length: 6 }).map((_, index) => (
          <div key={index} className="space-y-2">
            <div className="h-2.5 w-20 rounded-full bg-active" />
            <div className="h-3 w-32 rounded-full bg-active" />
          </div>
        ))}
      </div>
      <div className="border-t border-border pt-8">
        <div className="h-3 w-24 rounded-full bg-active" />
        <div className="mt-4 h-3 w-64 rounded-full bg-active" />
      </div>
    </div>
  );
}
