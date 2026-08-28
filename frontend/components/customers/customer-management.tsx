"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import { CustomerDetail } from "@/components/customers/customer-detail";
import { CustomerForm } from "@/components/customers/customer-form";
import {
  CustomerList,
  CustomerToolbar,
} from "@/components/customers/customer-list";
import { Drawer } from "@/components/drawer";
import { PageHeader } from "@/components/page-header";
import { ApiError } from "@/lib/api/client";
import {
  getCustomer,
  getCustomerQuotes,
  getCustomers,
} from "@/lib/api/customers";
import type { Customer, Quote } from "@/lib/api/types";

type DrawerMode = "details" | "create" | null;

const CUSTOMER_LIST_ERROR =
  "The customer data could not be retrieved. Please try again.";
const CUSTOMER_DETAIL_ERROR =
  "The customer details could not be retrieved. Please try again.";

function getErrorMessage(error: unknown, fallback: string) {
  return error instanceof ApiError ? error.message : fallback;
}

function isAbortError(error: unknown) {
  return error instanceof DOMException && error.name === "AbortError";
}

async function getCustomerDetail(customerId: number, signal?: AbortSignal) {
  const [customer, quotes] = await Promise.all([
    getCustomer(customerId, signal),
    getCustomerQuotes(customerId, signal),
  ]);

  return { customer, quotes };
}

export function CustomerManagement() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);
  const [search, setSearch] = useState("");
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const [drawerMode, setDrawerMode] = useState<DrawerMode>(null);
  const [selectedCustomerId, setSelectedCustomerId] = useState<number | null>(
    null,
  );
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
      if (!isAbortError(error)) {
        setLoadError(getErrorMessage(error, CUSTOMER_LIST_ERROR));
      }
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
        const { customer, quotes } = await getCustomerDetail(customerId, signal);
        setDetailCustomer(customer);
        setCustomerQuotes(quotes);
      } catch (error) {
        if (!isAbortError(error)) {
          setDetailError(getErrorMessage(error, CUSTOMER_DETAIL_ERROR));
        }
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
        if (!isAbortError(error)) {
          setLoadError(getErrorMessage(error, CUSTOMER_LIST_ERROR));
        }
      })
      .finally(() => {
        if (!controller.signal.aborted) setIsLoading(false);
      });

    return () => controller.abort();
  }, []);

  useEffect(() => {
    if (drawerMode !== "details" || selectedCustomerId === null) return;

    const controller = new AbortController();

    void getCustomerDetail(selectedCustomerId, controller.signal)
      .then(({ customer, quotes }) => {
        setDetailCustomer(customer);
        setCustomerQuotes(quotes);
      })
      .catch((error: unknown) => {
        if (!isAbortError(error)) {
          setDetailError(getErrorMessage(error, CUSTOMER_DETAIL_ERROR));
        }
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

  const openCustomer = useCallback((customer: Customer) => {
    setSuccessMessage(null);
    setIsDetailLoading(true);
    setDetailError(null);
    setDetailCustomer(null);
    setCustomerQuotes([]);
    setSelectedCustomerId(customer.custId);
    setDrawerMode("details");
  }, []);

  const openCreateDrawer = useCallback(() => {
    setSuccessMessage(null);
    setDrawerMode("create");
  }, []);

  const closeDrawer = useCallback(() => {
    setDrawerMode(null);
    setSelectedCustomerId(null);
  }, []);

  async function handleCustomerCreated(customer: Customer) {
    closeDrawer();
    setSuccessMessage(`${customer.custName} was added successfully.`);
    await loadCustomers();
  }

  return (
    <div className="space-y-8">
      <PageHeader title="Customers" />

      {successMessage ? (
        <SuccessNotice
          message={successMessage}
          onDismiss={() => setSuccessMessage(null)}
        />
      ) : null}

      <section
        className="space-y-4 border-t border-border pt-8"
        aria-label="Customer list"
      >
        <CustomerToolbar
          search={search}
          onSearchChange={setSearch}
          visibleCount={filteredCustomers.length}
          totalCount={customers.length}
          showCount={!isLoading && !loadError}
          onAddCustomer={openCreateDrawer}
        />

        <CustomerList
          customers={filteredCustomers}
          totalCount={customers.length}
          isLoading={isLoading}
          error={loadError}
          onRetry={() => void loadCustomers()}
          onOpenCustomer={openCustomer}
          onAddCustomer={openCreateDrawer}
        />
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
              if (selectedCustomerId !== null) {
                void loadCustomerDetail(selectedCustomerId);
              }
            }}
          />
        )}
      </Drawer>
    </div>
  );
}

function SuccessNotice({
  message,
  onDismiss,
}: {
  message: string;
  onDismiss: () => void;
}) {
  return (
    <div
      role="status"
      className="flex items-center justify-between gap-4 rounded-lg bg-success-background px-4 py-3 text-success-foreground"
    >
      <p>{message}</p>
      <button
        type="button"
        onClick={onDismiss}
        aria-label="Dismiss success message"
        className="text-lg leading-none"
      >
        ×
      </button>
    </div>
  );
}
