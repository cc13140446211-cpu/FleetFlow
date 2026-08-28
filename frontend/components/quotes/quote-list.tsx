"use client";

import { useMemo } from "react";
import {
  DataTable,
  DataTableSkeleton,
  type DataTableColumn,
} from "@/components/data-table";
import { StatusBadge } from "@/components/status-badge";
import type { Customer, Quote, QuoteStatus } from "@/lib/api/types";

export type QuoteStatusFilter = "ALL" | QuoteStatus;

type QuoteControlsProps = {
  search: string;
  onSearchChange: (value: string) => void;
  status: QuoteStatusFilter;
  onStatusChange: (value: QuoteStatusFilter) => void;
  pickupDate: string;
  onPickupDateChange: (value: string) => void;
  visibleCount: number;
  totalCount: number;
  showCount: boolean;
  onAddQuote: () => void;
};

export function QuoteControls({
  search,
  onSearchChange,
  status,
  onStatusChange,
  pickupDate,
  onPickupDateChange,
  visibleCount,
  totalCount,
  showCount,
  onAddQuote,
}: QuoteControlsProps) {
  const controlClassName = "control-field";

  return (
    <div className="flex w-full flex-wrap items-center gap-3">
      <label className="block min-w-[190px] flex-1 sm:max-w-sm">
        <span className="sr-only">Search quotes</span>
        <input
          type="search"
          value={search}
          onChange={(event) => onSearchChange(event.target.value)}
          placeholder="Search quotes, customers, or routes…"
          className={`${controlClassName} w-full placeholder:text-muted`}
        />
      </label>

      <label className="min-w-36">
        <span className="sr-only">Filter by quote status</span>
        <select
          value={status}
          onChange={(event) =>
            onStatusChange(event.target.value as QuoteStatusFilter)
          }
          className={controlClassName}
        >
          <option value="ALL">All statuses</option>
          <option value="PENDING">Pending</option>
          <option value="ACCEPTED">Accepted</option>
          <option value="REJECTED">Rejected</option>
          <option value="CANCELLED">Cancelled</option>
          <option value="CONVERTED">Converted</option>
        </select>
      </label>

      <label className="min-w-40">
        <span className="sr-only">Filter by preferred pickup date</span>
        <input
          type="date"
          value={pickupDate}
          onChange={(event) => onPickupDateChange(event.target.value)}
          className={controlClassName}
        />
      </label>

      {showCount ? (
        <p className="hidden text-xs text-muted xl:block">
          {visibleCount} of {totalCount} quotes
        </p>
      ) : null}

      <button
        type="button"
        onClick={onAddQuote}
        className="button-primary ml-auto shrink-0"
      >
        <span aria-hidden="true" className="mr-1.5">
          +
        </span>
        Add quote
      </button>
    </div>
  );
}

type QuoteListProps = {
  quotes: Quote[];
  totalCount: number;
  customersById: Map<number, Customer>;
  isLoading: boolean;
  error: string | null;
  onRetry: () => void;
  onOpenQuote: (quote: Quote) => void;
  onAddQuote: () => void;
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

export function QuoteList({
  quotes,
  totalCount,
  customersById,
  isLoading,
  error,
  onRetry,
  onOpenQuote,
  onAddQuote,
}: QuoteListProps) {
  const columns = useMemo<DataTableColumn<Quote>[]>(
    () => [
      {
        key: "quote",
        header: "Quote",
        render: (quote) => (
          <div>
            <p className="font-medium">Q-{quote.quoteId}</p>
            <p className="mt-0.5 text-xs text-muted">
              {formatDate(quote.quoteCreatedAt)}
            </p>
          </div>
        ),
      },
      {
        key: "customer",
        header: "Customer",
        render: (quote) => {
          const customer = customersById.get(quote.custId);
          return (
            <div>
              <p>{customer?.custName ?? `Customer #${quote.custId}`}</p>
              {customer?.custCompanyName ? (
                <p className="mt-0.5 text-xs text-muted">
                  {customer.custCompanyName}
                </p>
              ) : null}
            </div>
          );
        },
      },
      {
        key: "route",
        header: "Route",
        render: (quote) => (
          <div className="min-w-44">
            <p>{quote.quotePickupLocation}</p>
            <p className="mt-0.5 text-xs text-secondary">
              <span className="mr-1.5 text-muted">→</span>
              {quote.quoteDropoffLocation}
            </p>
          </div>
        ),
      },
      {
        key: "pickup",
        header: "Preferred pickup",
        render: (quote) => (
          <span className="whitespace-nowrap">
            {formatDate(quote.quotePreferredPickupDate)}
          </span>
        ),
      },
      {
        key: "price",
        header: "Quoted price",
        render: (quote) => (
          <span className="whitespace-nowrap font-medium">
            {currencyFormatter.format(Number(quote.quotePrice))}
          </span>
        ),
      },
      {
        key: "payment",
        header: "Payment",
        render: (quote) => <StatusBadge status={quote.quotePaymentStatus} />,
      },
      {
        key: "status",
        header: "Status",
        render: (quote) => <StatusBadge status={quote.quoteStatus} />,
      },
      {
        key: "action",
        header: "",
        className: "w-16 text-right",
        render: (quote) => (
          <button
            type="button"
            onClick={(event) => {
              event.stopPropagation();
              onOpenQuote(quote);
            }}
            className="rounded-md px-2 py-1 font-medium text-secondary transition-colors hover:bg-accent-soft hover:text-accent-foreground"
          >
            View
          </button>
        ),
      },
    ],
    [customersById, onOpenQuote],
  );

  if (isLoading) {
    return <DataTableSkeleton columns={5} label="Loading quotes" />;
  }

  if (error) {
    return (
      <div
        role="alert"
        className="panel rounded-[10px] px-6 py-12 text-center"
      >
        <h2 className="text-base font-semibold">Unable to load quotes</h2>
        <p className="mx-auto mt-2 max-w-md text-secondary">{error}</p>
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

  if (quotes.length > 0) {
    return (
      <DataTable
        columns={columns}
        rows={quotes}
        getRowKey={(quote) => quote.quoteId}
        getRowLabel={(quote) => `View quote Q-${quote.quoteId}`}
        onRowClick={onOpenQuote}
      />
    );
  }

  const hasQuotes = totalCount > 0;

  return (
    <div className="panel rounded-[10px] px-6 py-12 text-center">
      <h2 className="text-base font-semibold">
        {hasQuotes ? "No quotes found" : "No quotations yet"}
      </h2>
      <p className="mx-auto mt-2 max-w-md text-secondary">
        {hasQuotes
          ? "Adjust the search, status, or pickup date filters."
          : "Create the first quotation to begin managing a freight request."}
      </p>
      {!hasQuotes ? (
        <button
          type="button"
          onClick={onAddQuote}
          className="button-primary mt-5"
        >
          Add quote
        </button>
      ) : null}
    </div>
  );
}
