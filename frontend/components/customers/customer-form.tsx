"use client";

import { useState } from "react";
import { ApiError } from "@/lib/api/client";
import { createCustomer } from "@/lib/api/customers";
import type {
  CreateCustomerRequest,
  Customer,
} from "@/lib/api/types";

type CustomerFormProps = {
  onCancel: () => void;
  onCreated: (customer: Customer) => Promise<void>;
};

type FormErrors = Partial<Record<keyof CreateCustomerRequest, string>>;

const initialValues: CreateCustomerRequest = {
  custName: "",
  custCompanyName: null,
  custPhone: "",
  custEmail: "",
  custAddress: "",
};

const inputClassName =
  "h-10 w-full rounded-lg border border-border bg-surface px-3 text-sm text-foreground transition-colors placeholder:text-muted hover:border-secondary/50 focus:border-focus focus:outline-none disabled:cursor-not-allowed disabled:bg-active";

function validate(values: CreateCustomerRequest): FormErrors {
  const errors: FormErrors = {};
  const name = values.custName.trim();
  const phone = values.custPhone.trim();
  const email = values.custEmail.trim();
  const address = values.custAddress.trim();

  if (!name) errors.custName = "Customer name is required.";
  else if (name.length > 100)
    errors.custName = "Customer name must not exceed 100 characters.";

  if ((values.custCompanyName?.length ?? 0) > 100)
    errors.custCompanyName = "Company name must not exceed 100 characters.";

  if (!phone) errors.custPhone = "Phone number is required.";
  else if (phone.length > 20)
    errors.custPhone = "Phone number must not exceed 20 characters.";

  if (!email) errors.custEmail = "Email is required.";
  else if (!/^\S+@\S+\.\S+$/.test(email))
    errors.custEmail = "Enter a valid email address.";
  else if (email.length > 100)
    errors.custEmail = "Email must not exceed 100 characters.";

  if (!address) errors.custAddress = "Address is required.";
  else if (address.length > 255)
    errors.custAddress = "Address must not exceed 255 characters.";

  return errors;
}

export function CustomerForm({ onCancel, onCreated }: CustomerFormProps) {
  const [values, setValues] = useState(initialValues);
  const [errors, setErrors] = useState<FormErrors>({});
  const [formError, setFormError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  function setField<Key extends keyof CreateCustomerRequest>(
    key: Key,
    value: CreateCustomerRequest[Key],
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
      const customer = await createCustomer({
        custName: values.custName.trim(),
        custCompanyName: values.custCompanyName?.trim() || null,
        custPhone: values.custPhone.trim(),
        custEmail: values.custEmail.trim(),
        custAddress: values.custAddress.trim(),
      });
      await onCreated(customer);
    } catch (error) {
      if (error instanceof ApiError) {
        const fieldErrors: FormErrors = {};
        for (const key of Object.keys(initialValues) as Array<
          keyof CreateCustomerRequest
        >) {
          if (error.details[key]) fieldErrors[key] = error.details[key];
        }
        setErrors(fieldErrors);
        setFormError(
          Object.keys(fieldErrors).length > 0
            ? "Review the highlighted fields and try again."
            : error.message,
        );
      } else {
        setFormError("Unable to create the customer. Please try again.");
      }
    } finally {
      setIsSubmitting(false);
    }
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
            Customer information
          </legend>
          <FormField
            label="Customer name"
            name="custName"
            required
            error={errors.custName}
          >
            <input
              id="custName"
              name="custName"
              value={values.custName}
              onChange={(event) => setField("custName", event.target.value)}
              maxLength={100}
              required
              autoComplete="name"
              className={inputClassName}
            />
          </FormField>

          <FormField
            label="Company name"
            name="custCompanyName"
            error={errors.custCompanyName}
          >
            <input
              id="custCompanyName"
              name="custCompanyName"
              value={values.custCompanyName ?? ""}
              onChange={(event) =>
                setField("custCompanyName", event.target.value)
              }
              maxLength={100}
              autoComplete="organization"
              className={inputClassName}
            />
          </FormField>

          <div className="grid gap-5 sm:grid-cols-2">
            <FormField
              label="Phone"
              name="custPhone"
              required
              error={errors.custPhone}
            >
              <input
                id="custPhone"
                name="custPhone"
                type="tel"
                value={values.custPhone}
                onChange={(event) => setField("custPhone", event.target.value)}
                maxLength={20}
                required
                autoComplete="tel"
                className={inputClassName}
              />
            </FormField>

            <FormField
              label="Email"
              name="custEmail"
              required
              error={errors.custEmail}
            >
              <input
                id="custEmail"
                name="custEmail"
                type="email"
                value={values.custEmail}
                onChange={(event) => setField("custEmail", event.target.value)}
                maxLength={100}
                required
                autoComplete="email"
                className={inputClassName}
              />
            </FormField>
          </div>

          <FormField
            label="Address"
            name="custAddress"
            required
            error={errors.custAddress}
          >
            <textarea
              id="custAddress"
              name="custAddress"
              value={values.custAddress}
              onChange={(event) => setField("custAddress", event.target.value)}
              maxLength={255}
              required
              rows={4}
              autoComplete="street-address"
              className={`${inputClassName} h-auto min-h-24 resize-y py-2.5`}
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
          {isSubmitting ? "Adding customer…" : "Add customer"}
        </button>
      </footer>
    </form>
  );
}

type FormFieldProps = {
  label: string;
  name: string;
  required?: boolean;
  error?: string;
  children: React.ReactNode;
};

function FormField({
  label,
  name,
  required,
  error,
  children,
}: FormFieldProps) {
  return (
    <div>
      <label htmlFor={name} className="mb-2 block text-[13px] font-medium">
        {label}
        {required ? <span className="ml-1 text-error-foreground">*</span> : null}
      </label>
      {children}
      {error ? (
        <p className="mt-1.5 text-xs text-error-foreground">{error}</p>
      ) : null}
    </div>
  );
}
