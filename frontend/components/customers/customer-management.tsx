"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import { DataTable, DataTableSkeleton, type DataTableColumn } from "@/components/data-table";
import { Drawer } from "@/components/drawer";
import { PageHeader } from "@/components/page-header";
import { CustomerDetail } from "@/components/customers/customer-detail";
import { CustomerForm } from "@/components/customers/customer-form";
import { ApiError } from "@/lib/api/client";
import {
  getCustomer,
  getCustomerQuotes,
  getCustomers,
} from "@/lib/api/customers";
import type { Customer, Quote } from "@/lib/api/types";

type DrawerMode = "details" | "create" | null;

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

function getErrorMessage(error: unknown, fallback: string) {
  if (error instanceof ApiError) return error.message;
  return fallback;
}

export function CustomerManagement() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);
  const [search, setSearch] = useState("");
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [drawerMode, setDrawerMode] = useState<DrawerMode>(null);
  const [selectedCustomerId, setSelectedCustomerId] = useState<number | null>(null);
  const [detailCustomer, setDetailCustomer] = useState<Customer | null>(null);
  const [customerQuotes, setCustomerQuotes] = useState<Quote[]>([]);
  const [isDetailLoading, setIsDetailLoading] = useState(false);
  const [detailError, setDetailError] = useState<string | null>(null);

  const loadCustomers = useCallback(async (signal?: AbortSignal) => {
    setIsLoading(true);
    setLoadError(null);

    try {
      setCustomers(await getCustomers(signal));
    } catch (error) {
      if (error instanceof DOMException && error.name === "AbortError") return;
      setLoadError(
        getErrorMessage(
          error,
          "The customer data could not be retrieved. Please try again.",
        ),
      );
    } finally {
      if (!signal?.aborted) setIsLoading(false);
    }
  }, []);

  const loadCustomerDetail = useCallback(
    async (customerId: number, signal?: AbortSignal) => {
      setIsDetailLoading(true);
      setDetailError(null);
      setDetailCustomer(null);
      setCustomerQuotes([]);

      try {
        const [customer, quotes] = await Promise.all([
          getCustomer(customerId, signal),
          getCustomerQuotes(customerId, signal),
        ]);
        setDetailCustomer(customer);
        setCustomerQuotes(quotes);
      } catch (error) {
        if (error instanceof DOMException && error.name === "AbortError") return;
        setDetailError(
          getErrorMessage(
            error,
            "The customer details could not be retrieved. Please try again.",
          ),
        );
      } finally {
        if (!signal?.aborted) setIsDetailLoading(false);
      }
    },
    [],
  );

  useEffect(() => {
    const controller = new AbortController();
    void getCustomers(controller.signal)
      .then((data) => {
        setCustomers(data);
        setLoadError(null);
      })
      .catch((error: unknown) => {
        if (error instanceof DOMException && error.name === "AbortError") return;
        setLoadError(
          getErrorMessage(
            error,
            "The customer data could not be retrieved. Please try again.",
          ),
        );
      })
      .finally(() => {
        if (!controller.signal.aborted) setIsLoading(false);
      });
    return () => controller.abort();
  }, []);

  useEffect(() => {
    if (drawerMode !== "details" || selectedCustomerId === null) return;
    const controller = new AbortController();
    void Promise.all([
      getCustomer(selectedCustomerId, controller.signal),
      getCustomerQuotes(selectedCustomerId, controller.signal),
    ])
      .then(([customer, quotes]) => {
        setDetailCustomer(customer);
        setCustomerQuotes(quotes);
      })
      .catch((error: unknown) => {
        if (error instanceof DOMException && error.name === "AbortError") return;
        setDetailError(
          getErrorMessage(
            error,
            "The customer details could not be retrieved. Please try again.",
          ),
        );
      })
      .finally(() => {
        if (!controller.signal.aborted) setIsDetailLoading(false);
      });
    return () => controller.abort();
  }, [drawerMode, selectedCustomerId]);

  const filteredCustomers = useMemo(() => {
    const query = search.trim().toLocaleLowerCase();
    if (!query) return customers;

    return customers.filter((customer) =>
      [
        customer.custName,
        customer.custCompanyName,
        customer.custPhone,
        customer.custEmail,
      ].some((value) => value?.toLocaleLowerCase().includes(query)),
    );
  }, [customers, search]);

  function openCustomer(customer: Customer) {
    setSuccessMessage(null);
    setIsDetailLoading(true);
    setDetailError(null);
    setDetailCustomer(null);
    setCustomerQuotes([]);
    setSelectedCustomerId(customer.custId);
    setDrawerMode("details");
  }

  function closeDrawer() {
    setDrawerMode(null);
    setSelectedCustomerId(null);
  }

  async function handleCustomerCreated(customer: Customer) {
    closeDrawer();
    setSuccessMessage(`${customer.custName} was added successfully.`);
    await loadCustomers();
  }

  const columns: DataTableColumn<Customer>[] = [
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
            openCustomer(customer);
          }}
          className="rounded-md px-2 py-1 text-sm font-medium text-secondary hover:bg-active hover:text-foreground"
        >
          View
        </button>
      ),
    },
  ];

  return (
    <div className="space-y-8">
      <div className="flex flex-col items-start justify-between gap-5 sm:flex-row sm:items-end">
        <PageHeader
          title="Customers"
          description="Manage customer records and quotation history."
        />
        <button
          type="button"
          onClick={() => {
            setSuccessMessage(null);
            setDrawerMode("create");
          }}
          className="h-10 shrink-0 rounded-lg bg-foreground px-4 font-medium text-white transition-colors hover:bg-foreground/90"
        >
          <span aria-hidden="true" className="mr-1.5">
            +
          </span>
          Add customer
        </button>
      </div>

      {successMessage ? (
        <div
          role="status"
          className="flex items-center justify-between gap-4 rounded-lg bg-success-background px-4 py-3 text-success-foreground"
        >
          <p>{successMessage}</p>
          <button
            type="button"
            onClick={() => setSuccessMessage(null)}
            aria-label="Dismiss success message"
            className="text-lg leading-none"
          >
            ×
          </button>
        </div>
      ) : null}

      <section className="space-y-4 border-t border-border pt-8" aria-label="Customer list">
        <div className="flex flex-col justify-between gap-3 sm:flex-row sm:items-center">
          <label className="relative block w-full max-w-sm">
            <span className="sr-only">Search customers</span>
            <input
              type="search"
              value={search}
              onChange={(event) => setSearch(event.target.value)}
              placeholder="Search customers…"
              className="h-10 w-full rounded-lg border border-border bg-surface px-3 text-sm placeholder:text-muted hover:border-secondary/50 focus:border-focus focus:outline-none"
            />
          </label>
          {!isLoading && !loadError ? (
            <p className="text-xs text-muted">
              {filteredCustomers.length} of {customers.length} customers
            </p>
          ) : null}
        </div>

        {isLoading ? <DataTableSkeleton columns={5} /> : null}

        {!isLoading && loadError ? (
          <div className="rounded-[10px] border border-border bg-surface px-6 py-12 text-center" role="alert">
            <h2 className="text-base font-semibold">Unable to load customers</h2>
            <p className="mx-auto mt-2 max-w-md text-secondary">{loadError}</p>
            <button
              type="button"
              onClick={() => void loadCustomers()}
              className="mt-5 h-10 rounded-lg border border-border bg-surface px-4 font-medium transition-colors hover:bg-active"
            >
              Retry
            </button>
          </div>
        ) : null}

        {!isLoading && !loadError && filteredCustomers.length > 0 ? (
          <DataTable
            columns={columns}
            rows={filteredCustomers}
            getRowKey={(customer) => customer.custId}
            getRowLabel={(customer) => `View ${customer.custName}`}
            onRowClick={openCustomer}
          />
        ) : null}

        {!isLoading && !loadError && filteredCustomers.length === 0 ? (
          <div className="rounded-[10px] border border-border bg-surface px-6 py-12 text-center">
            <h2 className="text-base font-semibold">
              {customers.length === 0 ? "No customers yet" : "No customers found"}
            </h2>
            <p className="mx-auto mt-2 max-w-md text-secondary">
              {customers.length === 0
                ? "Add the first customer to begin managing freight quotations."
                : "Try a different name, company, phone number, or email."}
            </p>
            {customers.length === 0 ? (
              <button
                type="button"
                onClick={() => setDrawerMode("create")}
                className="mt-5 h-10 rounded-lg bg-foreground px-4 font-medium text-white transition-colors hover:bg-foreground/90"
              >
                Add customer
              </button>
            ) : null}
          </div>
        ) : null}
      </section>

      <Drawer
        open={drawerMode !== null}
        title={drawerMode === "create" ? "Add customer" : "Customer details"}
        description={
          drawerMode === "create"
            ? "Create a customer record for freight enquiries and quotations."
            : "Contact information and quotation history."
        }
        onClose={closeDrawer}
      >
        {drawerMode === "create" ? (
          <CustomerForm onCancel={closeDrawer} onCreated={handleCustomerCreated} />
        ) : (
          <CustomerDetail
            customer={detailCustomer}
            quotes={customerQuotes}
            isLoading={isDetailLoading}
            error={detailError}
            onRetry={() => {
              if (selectedCustomerId !== null)
                void loadCustomerDetail(selectedCustomerId);
            }}
          />
        )}
      </Drawer>
    </div>
  );
}
