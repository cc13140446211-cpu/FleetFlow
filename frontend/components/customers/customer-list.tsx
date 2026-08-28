"use client";

import { useMemo } from "react";
import {
  DataTable,
  DataTableSkeleton,
  type DataTableColumn,
} from "@/components/data-table";
import type { Customer } from "@/lib/api/types";

const dateFormatter = new Intl.DateTimeFormat("en-MY", {
  day: "numeric",
  month: "short",
  year: "numeric",
});

function formatDate(value: string | null) {
  if (!value) return "—";
  const date = new Date(value);
  return Number.isNaN(date.valueOf()) ? value : dateFormatter.format(date);
}

type CustomerToolbarProps = {
  search: string;
  onSearchChange: (value: string) => void;
  visibleCount: number;
  totalCount: number;
  showCount: boolean;
  onAddCustomer: () => void;
};

export function CustomerToolbar({
  search,
  onSearchChange,
  visibleCount,
  totalCount,
  showCount,
  onAddCustomer,
}: CustomerToolbarProps) {
  return (
    <div className="flex w-full items-center gap-3">
      <label className="relative block min-w-0 flex-1 sm:max-w-sm">
        <span className="sr-only">Search customers</span>
        <input
          type="search"
          value={search}
          onChange={(event) => onSearchChange(event.target.value)}
          placeholder="Search customers…"
          className="control-field"
        />
      </label>

      {showCount ? (
        <p className="hidden text-xs text-muted md:block">
          {visibleCount} of {totalCount} customers
        </p>
      ) : null}

      <button
        type="button"
        onClick={onAddCustomer}
        className="button-primary ml-auto shrink-0 px-3 sm:px-4"
      >
        <span aria-hidden="true" className="mr-1.5">
          +
        </span>
        Add customer
      </button>
    </div>
  );
}

type CustomerListProps = {
  customers: Customer[];
  totalCount: number;
  isLoading: boolean;
  error: string | null;
  onRetry: () => void;
  onOpenCustomer: (customer: Customer) => void;
  onAddCustomer: () => void;
};

export function CustomerList({
  customers,
  totalCount,
  isLoading,
  error,
  onRetry,
  onOpenCustomer,
  onAddCustomer,
}: CustomerListProps) {
  const columns = useMemo<DataTableColumn<Customer>[]>(
    () => [
      {
        key: "customer",
        header: "Customer",
        render: (customer) => (
          <div>
            <p className="font-medium">{customer.custName}</p>
            <p className="mt-0.5 text-xs text-muted">#{customer.custId}</p>
          </div>
        ),
      },
      {
        key: "company",
        header: "Company",
        render: (customer) => customer.custCompanyName || "—",
      },
      {
        key: "phone",
        header: "Phone",
        render: (customer) => customer.custPhone,
      },
      {
        key: "email",
        header: "Email",
        render: (customer) => (
          <span className="text-secondary">{customer.custEmail || "—"}</span>
        ),
      },
      {
        key: "created",
        header: "Added",
        render: (customer) => (
          <span className="whitespace-nowrap text-secondary">
            {formatDate(customer.custCreatedAt)}
          </span>
        ),
      },
      {
        key: "action",
        header: "",
        className: "w-16 text-right",
        render: (customer) => (
          <button
            type="button"
            onClick={(event) => {
              event.stopPropagation();
              onOpenCustomer(customer);
            }}
            className="rounded-md px-2 py-1 text-sm font-medium text-secondary transition-colors hover:bg-accent-soft hover:text-accent-foreground"
          >
            View
          </button>
        ),
      },
    ],
    [onOpenCustomer],
  );

  if (isLoading) return <DataTableSkeleton columns={5} />;

  if (error) {
    return (
      <div
        className="panel rounded-[10px] px-6 py-12 text-center"
        role="alert"
      >
        <h2 className="text-base font-semibold">Unable to load customers</h2>
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

  if (customers.length > 0) {
    return (
      <DataTable
        columns={columns}
        rows={customers}
        getRowKey={(customer) => customer.custId}
        getRowLabel={(customer) => `View ${customer.custName}`}
        onRowClick={onOpenCustomer}
      />
    );
  }

  const hasCustomers = totalCount > 0;

  return (
    <div className="panel rounded-[10px] px-6 py-12 text-center">
      <h2 className="text-base font-semibold">
        {hasCustomers ? "No customers found" : "No customers yet"}
      </h2>
      <p className="mx-auto mt-2 max-w-md text-secondary">
        {hasCustomers
          ? "Try a different name, company, phone number, or email."
          : "Add the first customer to begin managing freight quotations."}
      </p>
      {!hasCustomers ? (
        <button
          type="button"
          onClick={onAddCustomer}
          className="button-primary mt-5"
        >
          Add customer
        </button>
      ) : null}
    </div>
  );
}
