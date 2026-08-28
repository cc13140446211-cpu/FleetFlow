"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import { Drawer } from "@/components/drawer";
import { PageHeader } from "@/components/page-header";
import { QuoteDetail } from "@/components/quotes/quote-detail";
import { QuoteForm } from "@/components/quotes/quote-form";
import {
  QuoteControls,
  QuoteList,
  type QuoteStatusFilter,
} from "@/components/quotes/quote-list";
import { ApiError } from "@/lib/api/client";
import { getCustomers } from "@/lib/api/customers";
import {
  getQuote,
  getQuotes,
  recordQuotePayment,
  updateQuoteStatus,
} from "@/lib/api/quotes";
import type { Customer, Quote, QuoteStatus } from "@/lib/api/types";

type DrawerMode = "details" | "create" | null;

const QUOTE_LIST_ERROR =
  "The quotation data could not be retrieved. Please try again.";
const QUOTE_DETAIL_ERROR =
  "The quote details could not be retrieved. Please try again.";

function getErrorMessage(error: unknown, fallback: string) {
  return error instanceof ApiError ? error.message : fallback;
}

function isAbortError(error: unknown) {
  return error instanceof DOMException && error.name === "AbortError";
}

export function QuoteManagement() {
  const [quotes, setQuotes] = useState<Quote[]>([]);
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] =
    useState<QuoteStatusFilter>("ALL");
  const [pickupDate, setPickupDate] = useState("");
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const [drawerMode, setDrawerMode] = useState<DrawerMode>(null);
  const [selectedQuoteId, setSelectedQuoteId] = useState<number | null>(null);
  const [detailQuote, setDetailQuote] = useState<Quote | null>(null);
  const [isDetailLoading, setIsDetailLoading] = useState(false);
  const [detailError, setDetailError] = useState<string | null>(null);
  const [isUpdating, setIsUpdating] = useState(false);
  const [actionError, setActionError] = useState<string | null>(null);
  const [actionMessage, setActionMessage] = useState<string | null>(null);

  const loadQuoteData = useCallback(async (signal?: AbortSignal) => {
    setIsLoading(true);
    setLoadError(null);

    try {
      const [quoteData, customerData] = await Promise.all([
        getQuotes(signal),
        getCustomers(signal),
      ]);
      setQuotes(quoteData);
      setCustomers(customerData);
    } catch (error) {
      if (!isAbortError(error)) {
        setLoadError(getErrorMessage(error, QUOTE_LIST_ERROR));
      }
    } finally {
      if (!signal?.aborted) setIsLoading(false);
    }
  }, []);

  const loadQuoteDetail = useCallback(
    async (quoteId: number, signal?: AbortSignal) => {
      setIsDetailLoading(true);
      setDetailError(null);
      setDetailQuote(null);

      try {
        setDetailQuote(await getQuote(quoteId, signal));
      } catch (error) {
        if (!isAbortError(error)) {
          setDetailError(getErrorMessage(error, QUOTE_DETAIL_ERROR));
        }
      } finally {
        if (!signal?.aborted) setIsDetailLoading(false);
      }
    },
    [],
  );

  useEffect(() => {
    const controller = new AbortController();

    void Promise.all([
      getQuotes(controller.signal),
      getCustomers(controller.signal),
    ])
      .then(([quoteData, customerData]) => {
        setQuotes(quoteData);
        setCustomers(customerData);
        setLoadError(null);
      })
      .catch((error: unknown) => {
        if (!isAbortError(error)) {
          setLoadError(getErrorMessage(error, QUOTE_LIST_ERROR));
        }
      })
      .finally(() => {
        if (!controller.signal.aborted) setIsLoading(false);
      });

    return () => controller.abort();
  }, []);

  useEffect(() => {
    if (drawerMode !== "details" || selectedQuoteId === null) return;

    const controller = new AbortController();

    void getQuote(selectedQuoteId, controller.signal)
      .then(setDetailQuote)
      .catch((error: unknown) => {
        if (!isAbortError(error)) {
          setDetailError(getErrorMessage(error, QUOTE_DETAIL_ERROR));
        }
      })
      .finally(() => {
        if (!controller.signal.aborted) setIsDetailLoading(false);
      });

    return () => controller.abort();
  }, [drawerMode, selectedQuoteId]);

  const customersById = useMemo(
    () => new Map(customers.map((customer) => [customer.custId, customer])),
    [customers],
  );

  const filteredQuotes = useMemo(() => {
    const query = search.trim().toLocaleLowerCase();

    return quotes.filter((quote) => {
      const customer = customersById.get(quote.custId);
      const matchesSearch =
        !query ||
        [
          String(quote.quoteId),
          `q-${quote.quoteId}`,
          customer?.custName,
          customer?.custCompanyName,
          quote.quotePickupLocation,
          quote.quoteDropoffLocation,
        ].some((value) => value?.toLocaleLowerCase().includes(query));
      const matchesStatus =
        statusFilter === "ALL" || quote.quoteStatus === statusFilter;
      const matchesDate =
        !pickupDate || quote.quotePreferredPickupDate === pickupDate;

      return matchesSearch && matchesStatus && matchesDate;
    });
  }, [customersById, pickupDate, quotes, search, statusFilter]);

  const openQuote = useCallback((quote: Quote) => {
    setSuccessMessage(null);
    setSelectedQuoteId(quote.quoteId);
    setDetailQuote(null);
    setDetailError(null);
    setActionError(null);
    setActionMessage(null);
    setIsDetailLoading(true);
    setDrawerMode("details");
  }, []);

  const openCreateDrawer = useCallback(() => {
    setSuccessMessage(null);
    setDrawerMode("create");
  }, []);

  const closeDrawer = useCallback(() => {
    setDrawerMode(null);
    setSelectedQuoteId(null);
    setActionError(null);
    setActionMessage(null);
  }, []);

  async function handleQuoteCreated(quote: Quote) {
    closeDrawer();
    setSuccessMessage(`Quote Q-${quote.quoteId} was created successfully.`);
    await loadQuoteData();
  }

  async function runQuoteAction(
    action: () => Promise<Quote>,
    success: string,
  ) {
    setIsUpdating(true);
    setActionError(null);
    setActionMessage(null);

    try {
      const updatedQuote = await action();
      setDetailQuote(updatedQuote);
      setQuotes((current) =>
        current.map((quote) =>
          quote.quoteId === updatedQuote.quoteId ? updatedQuote : quote,
        ),
      );
      setActionMessage(success);
    } catch (error) {
      setActionError(
        getErrorMessage(error, "Unable to update the quote. Please try again."),
      );
    } finally {
      setIsUpdating(false);
    }
  }

  function handleStatusChange(status: QuoteStatus) {
    if (selectedQuoteId === null) return;
    const messages: Partial<Record<QuoteStatus, string>> = {
      ACCEPTED: "Quote accepted successfully.",
      REJECTED: "Quote rejected.",
      CANCELLED: "Quote cancelled.",
    };
    void runQuoteAction(
      () => updateQuoteStatus(selectedQuoteId, status),
      messages[status] ?? "Quote updated successfully.",
    );
  }

  function handleRecordPayment() {
    if (selectedQuoteId === null) return;
    void runQuoteAction(
      () => recordQuotePayment(selectedQuoteId),
      "Payment recorded successfully.",
    );
  }

  return (
    <div className="space-y-8">
      <PageHeader title="Quotes" />

      {successMessage ? (
        <SuccessNotice
          message={successMessage}
          onDismiss={() => setSuccessMessage(null)}
        />
      ) : null}

      <section
        className="space-y-4 border-t border-border pt-8"
        aria-label="Quote list"
      >
        <QuoteControls
          search={search}
          onSearchChange={setSearch}
          status={statusFilter}
          onStatusChange={setStatusFilter}
          pickupDate={pickupDate}
          onPickupDateChange={setPickupDate}
          visibleCount={filteredQuotes.length}
          totalCount={quotes.length}
          showCount={!isLoading && !loadError}
          onAddQuote={openCreateDrawer}
        />

        <QuoteList
          quotes={filteredQuotes}
          totalCount={quotes.length}
          customersById={customersById}
          isLoading={isLoading}
          error={loadError}
          onRetry={() => void loadQuoteData()}
          onOpenQuote={openQuote}
          onAddQuote={openCreateDrawer}
        />
      </section>

      <Drawer
        open={drawerMode !== null}
        title={drawerMode === "create" ? "Add quote" : "Quote details"}
        description={
          drawerMode === "create"
            ? "Create a freight quotation for an existing customer."
            : "Review the commercial and operational state of this quote."
        }
        onClose={closeDrawer}
      >
        {drawerMode === "create" ? (
          <QuoteForm
            customers={customers}
            onCancel={closeDrawer}
            onCreated={handleQuoteCreated}
          />
        ) : (
          <QuoteDetail
            quote={detailQuote}
            customer={
              detailQuote ? (customersById.get(detailQuote.custId) ?? null) : null
            }
            isLoading={isDetailLoading}
            error={detailError}
            actionError={actionError}
            actionMessage={actionMessage}
            isUpdating={isUpdating}
            onRetry={() => {
              if (selectedQuoteId !== null) {
                void loadQuoteDetail(selectedQuoteId);
              }
            }}
            onStatusChange={handleStatusChange}
            onRecordPayment={handleRecordPayment}
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
      className="flex items-center justify-between gap-4 rounded-lg border border-current/15 bg-success-background px-4 py-3 text-success-foreground"
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
