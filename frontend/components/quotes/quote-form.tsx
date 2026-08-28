"use client";

import Link from "next/link";
import { useState } from "react";
import { ApiError } from "@/lib/api/client";
import { createQuote } from "@/lib/api/quotes";
import type { Customer, Quote } from "@/lib/api/types";

type QuoteFormProps = {
  customers: Customer[];
  onCancel: () => void;
  onCreated: (quote: Quote) => Promise<void>;
};

type FormValues = {
  custId: string;
  preparedByEmpId: string;
  quotePickupLocation: string;
  quoteDropoffLocation: string;
  quotePreferredPickupDate: string;
  quotePrice: string;
};

type FormErrors = Partial<Record<keyof FormValues, string>>;

const initialValues: FormValues = {
  custId: "",
  preparedByEmpId: "",
  quotePickupLocation: "",
  quoteDropoffLocation: "",
  quotePreferredPickupDate: "",
  quotePrice: "",
};

const inputClassName =
  "h-10 w-full rounded-lg border border-border bg-surface px-3 text-sm text-foreground transition-colors placeholder:text-muted hover:border-secondary/50 focus:border-focus focus:outline-none disabled:cursor-not-allowed disabled:bg-active";

function validate(values: FormValues): FormErrors {
  const errors: FormErrors = {};
  const customerId = Number(values.custId);
  const employeeId = Number(values.preparedByEmpId);
  const price = Number(values.quotePrice);
  const pickup = values.quotePickupLocation.trim();
  const dropoff = values.quoteDropoffLocation.trim();

  if (!Number.isInteger(customerId) || customerId < 1) {
    errors.custId = "Select a customer.";
  }
  if (!Number.isInteger(employeeId) || employeeId < 1) {
    errors.preparedByEmpId = "Enter a valid dispatcher employee ID.";
  }
  if (!pickup) errors.quotePickupLocation = "Pickup location is required.";
  else if (pickup.length > 255) {
    errors.quotePickupLocation =
      "Pickup location must not exceed 255 characters.";
  }
  if (!dropoff) errors.quoteDropoffLocation = "Drop-off location is required.";
  else if (dropoff.length > 255) {
    errors.quoteDropoffLocation =
      "Drop-off location must not exceed 255 characters.";
  }
  if (!values.quotePreferredPickupDate) {
    errors.quotePreferredPickupDate = "Preferred pickup date is required.";
  }
  if (!Number.isFinite(price) || price <= 0) {
    errors.quotePrice = "Quote price must be greater than 0.";
  }

  return errors;
}

export function QuoteForm({ customers, onCancel, onCreated }: QuoteFormProps) {
  const [values, setValues] = useState(initialValues);
  const [errors, setErrors] = useState<FormErrors>({});
  const [formError, setFormError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  function setField<Key extends keyof FormValues>(
    key: Key,
    value: FormValues[Key],
  ) {
    setValues((current) => ({ ...current, [key]: value }));
    setErrors((current) => ({ ...current, [key]: undefined }));
  }

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setFormError(null);

    const validationErrors = validate(values);
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    setIsSubmitting(true);

    try {
      const quote = await createQuote({
        custId: Number(values.custId),
        preparedByEmpId: Number(values.preparedByEmpId),
        quotePickupLocation: values.quotePickupLocation.trim(),
        quoteDropoffLocation: values.quoteDropoffLocation.trim(),
        quotePreferredPickupDate: values.quotePreferredPickupDate,
        quotePrice: Number(values.quotePrice),
      });
      await onCreated(quote);
    } catch (error) {
      if (error instanceof ApiError) {
        const fieldErrors: FormErrors = {};
        for (const key of Object.keys(initialValues) as Array<keyof FormValues>) {
          if (error.details[key]) fieldErrors[key] = error.details[key];
        }
        setErrors(fieldErrors);
        setFormError(
          Object.keys(fieldErrors).length > 0
            ? "Review the highlighted fields and try again."
            : error.message,
        );
      } else {
        setFormError("Unable to create the quote. Please try again.");
      }
    } finally {
      setIsSubmitting(false);
    }
  }

  if (customers.length === 0) {
    return (
      <div className="px-6 py-8 sm:px-8">
        <h3 className="font-semibold">A customer is required</h3>
        <p className="mt-2 text-secondary">
          Add a customer before creating a freight quotation.
        </p>
        <Link
          href="/customers"
          className="mt-5 inline-flex h-10 items-center rounded-lg border border-border px-4 font-medium transition-colors hover:bg-active"
        >
          Go to customers
        </Link>
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit} noValidate className="flex min-h-full flex-col">
      <div className="flex-1 space-y-8 px-6 py-6 sm:px-8">
        {formError ? (
          <div
            role="alert"
            className="rounded-lg bg-error-background px-4 py-3 text-sm text-error-foreground"
          >
            {formError}
          </div>
        ) : null}

        <fieldset disabled={isSubmitting} className="space-y-5">
          <legend className="mb-5 text-xs font-semibold uppercase tracking-[0.08em] text-secondary">
            Quote information
          </legend>

          <FormField label="Customer" name="custId" error={errors.custId}>
            <select
              id="custId"
              value={values.custId}
              onChange={(event) => setField("custId", event.target.value)}
              required
              className={inputClassName}
            >
              <option value="">Select customer</option>
              {customers.map((customer) => (
                <option key={customer.custId} value={customer.custId}>
                  {customer.custName}
                  {customer.custCompanyName
                    ? ` · ${customer.custCompanyName}`
                    : ""}
                </option>
              ))}
            </select>
          </FormField>

          <FormField
            label="Prepared by employee ID"
            name="preparedByEmpId"
            error={errors.preparedByEmpId}
            hint="Use the employee ID of an active dispatcher."
          >
            <input
              id="preparedByEmpId"
              type="number"
              min="1"
              step="1"
              value={values.preparedByEmpId}
              onChange={(event) =>
                setField("preparedByEmpId", event.target.value)
              }
              required
              className={inputClassName}
            />
          </FormField>
        </fieldset>

        <fieldset disabled={isSubmitting} className="space-y-5 border-t border-border pt-8">
          <legend className="mb-5 text-xs font-semibold uppercase tracking-[0.08em] text-secondary">
            Route and timing
          </legend>
          <FormField
            label="Pickup location"
            name="quotePickupLocation"
            error={errors.quotePickupLocation}
          >
            <input
              id="quotePickupLocation"
              value={values.quotePickupLocation}
              onChange={(event) =>
                setField("quotePickupLocation", event.target.value)
              }
              maxLength={255}
              required
              className={inputClassName}
            />
          </FormField>
          <FormField
            label="Drop-off location"
            name="quoteDropoffLocation"
            error={errors.quoteDropoffLocation}
          >
            <input
              id="quoteDropoffLocation"
              value={values.quoteDropoffLocation}
              onChange={(event) =>
                setField("quoteDropoffLocation", event.target.value)
              }
              maxLength={255}
              required
              className={inputClassName}
            />
          </FormField>
          <FormField
            label="Preferred pickup date"
            name="quotePreferredPickupDate"
            error={errors.quotePreferredPickupDate}
          >
            <input
              id="quotePreferredPickupDate"
              type="date"
              value={values.quotePreferredPickupDate}
              onChange={(event) =>
                setField("quotePreferredPickupDate", event.target.value)
              }
              required
              className={inputClassName}
            />
          </FormField>
        </fieldset>

        <fieldset disabled={isSubmitting} className="border-t border-border pt-8">
          <legend className="mb-5 text-xs font-semibold uppercase tracking-[0.08em] text-secondary">
            Pricing
          </legend>
          <FormField
            label="Quoted price (MYR)"
            name="quotePrice"
            error={errors.quotePrice}
          >
            <input
              id="quotePrice"
              type="number"
              min="0.01"
              step="0.01"
              value={values.quotePrice}
              onChange={(event) => setField("quotePrice", event.target.value)}
              required
              className={inputClassName}
            />
          </FormField>
        </fieldset>
      </div>

      <footer className="flex justify-end gap-3 border-t border-border px-6 py-4 sm:px-8">
        <button
          type="button"
          onClick={onCancel}
          disabled={isSubmitting}
          className="h-10 rounded-lg border border-border bg-surface px-4 font-medium transition-colors hover:bg-active disabled:cursor-not-allowed disabled:opacity-60"
        >
          Cancel
        </button>
        <button
          type="submit"
          disabled={isSubmitting}
          className="h-10 rounded-lg bg-foreground px-4 font-medium text-white transition-colors hover:bg-foreground/90 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {isSubmitting ? "Creating quote…" : "Create quote"}
        </button>
      </footer>
    </form>
  );
}

function FormField({
  label,
  name,
  error,
  hint,
  children,
}: {
  label: string;
  name: string;
  error?: string;
  hint?: string;
  children: React.ReactNode;
}) {
  return (
    <div>
      <label htmlFor={name} className="mb-2 block text-[13px] font-medium">
        {label}
        <span className="ml-1 text-error-foreground">*</span>
      </label>
      {children}
      {error ? (
        <p className="mt-1.5 text-xs text-error-foreground">{error}</p>
      ) : hint ? (
        <p className="mt-1.5 text-xs text-muted">{hint}</p>
      ) : null}
    </div>
  );
}
