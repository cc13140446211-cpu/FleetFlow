import { DataTable, type DataTableColumn } from "@/components/data-table";
import { StatusBadge } from "@/components/status-badge";
import type { Customer, Quote } from "@/lib/api/types";

type CustomerDetailProps = {
  customer: Customer | null;
  quotes: Quote[];
  isLoading: boolean;
  error: string | null;
  onRetry: () => void;
};

const dateFormatter = new Intl.DateTimeFormat("en-MY", {
  day: "numeric",
  month: "short",
  year: "numeric",
});

const currencyFormatter = new Intl.NumberFormat("en-MY", {
  style: "currency",
  currency: "MYR",
  maximumFractionDigits: 2,
});

function formatDate(value: string | null) {
  if (!value) return "—";
  const date = value.length === 10 ? new Date(`${value}T00:00:00`) : new Date(value);
  return Number.isNaN(date.valueOf()) ? value : dateFormatter.format(date);
}

const quoteColumns: DataTableColumn<Quote>[] = [
  {
    key: "quote",
    header: "Quote",
    render: (quote) => (
      <span className="font-medium">Q-{quote.quoteId}</span>
    ),
  },
  {
    key: "route",
    header: "Route",
    render: (quote) => (
      <span>
        {quote.quotePickupLocation}
        <span className="mx-1.5 text-muted">→</span>
        {quote.quoteDropoffLocation}
      </span>
    ),
  },
  {
    key: "pickup",
    header: "Pickup",
    render: (quote) => formatDate(quote.quotePreferredPickupDate),
  },
  {
    key: "price",
    header: "Price",
    render: (quote) => currencyFormatter.format(Number(quote.quotePrice)),
  },
  {
    key: "status",
    header: "Status",
    render: (quote) => <StatusBadge status={quote.quoteStatus} />,
  },
];

export function CustomerDetail({
  customer,
  quotes,
  isLoading,
  error,
  onRetry,
}: CustomerDetailProps) {
  if (isLoading) return <CustomerDetailSkeleton />;

  if (error) {
    return (
      <div className="px-6 py-8 sm:px-8" role="alert">
        <h3 className="font-semibold">Unable to load customer details</h3>
        <p className="mt-2 text-secondary">{error}</p>
        <button
          type="button"
          onClick={onRetry}
          className="button-secondary mt-5"
        >
          Retry
        </button>
      </div>
    );
  }

  if (!customer) return null;

  return (
    <div className="space-y-8 px-6 py-6 sm:px-8">
      <section aria-labelledby="contact-heading">
        <div className="flex items-start justify-between gap-4">
          <div>
            <p className="text-lg font-semibold">{customer.custName}</p>
            <p className="mt-0.5 text-secondary">
              {customer.custCompanyName || "Independent customer"}
            </p>
          </div>
          <p className="text-xs text-muted">#{customer.custId}</p>
        </div>

        <h3
          id="contact-heading"
          className="mt-8 text-xs font-semibold uppercase tracking-[0.08em] text-secondary"
        >
          Contact information
        </h3>
        <dl className="mt-4 grid gap-x-6 gap-y-5 sm:grid-cols-2">
          <DetailItem label="Phone">
            <a className="hover:underline" href={`tel:${customer.custPhone}`}>
              {customer.custPhone}
            </a>
          </DetailItem>
          <DetailItem label="Email">
            {customer.custEmail ? (
              <a className="break-all hover:underline" href={`mailto:${customer.custEmail}`}>
                {customer.custEmail}
              </a>
            ) : (
              "—"
            )}
          </DetailItem>
          <DetailItem label="Address" className="sm:col-span-2">
            {customer.custAddress || "—"}
          </DetailItem>
          <DetailItem label="Customer since" className="sm:col-span-2">
            {formatDate(customer.custCreatedAt)}
          </DetailItem>
        </dl>
      </section>

      <section className="border-t border-border pt-8" aria-labelledby="quotes-heading">
        <div className="mb-4 flex items-center justify-between gap-4">
          <h3
            id="quotes-heading"
            className="text-xs font-semibold uppercase tracking-[0.08em] text-secondary"
          >
            Quotation history
          </h3>
          <span className="text-xs text-muted">
            {quotes.length} {quotes.length === 1 ? "quote" : "quotes"}
          </span>
        </div>

        {quotes.length > 0 ? (
          <DataTable
            columns={quoteColumns}
            rows={quotes}
            getRowKey={(quote) => quote.quoteId}
          />
        ) : (
          <div className="panel rounded-[10px] px-5 py-8 text-center">
            <p className="font-medium">No quotations yet</p>
            <p className="mt-1 text-sm text-secondary">
              This customer does not have any quotation history.
            </p>
          </div>
        )}
      </section>
    </div>
  );
}

function DetailItem({
  label,
  className,
  children,
}: {
  label: string;
  className?: string;
  children: React.ReactNode;
}) {
  return (
    <div className={className}>
      <dt className="text-xs text-muted">{label}</dt>
      <dd className="mt-1 whitespace-pre-line">{children}</dd>
    </div>
  );
}

function CustomerDetailSkeleton() {
  return (
    <div className="space-y-8 px-6 py-6 sm:px-8" aria-busy="true">
      <div className="space-y-3">
        <div className="h-5 w-40 rounded-full bg-active" />
        <div className="h-3 w-28 rounded-full bg-active" />
      </div>
      <div className="grid gap-6 sm:grid-cols-2">
        {Array.from({ length: 4 }).map((_, index) => (
          <div key={index} className="space-y-2">
            <div className="h-2.5 w-14 rounded-full bg-active" />
            <div className="h-3 w-32 rounded-full bg-active" />
          </div>
        ))}
      </div>
      <div className="border-t border-border pt-8">
        <div className="h-3 w-32 rounded-full bg-active" />
        <div className="mt-5 h-40 rounded-[10px] border border-border bg-active/40" />
      </div>
    </div>
  );
}
