import { apiRequest } from "@/lib/api/client";
import type {
  CreateCustomerRequest,
  Customer,
  Quote,
} from "@/lib/api/types";

export function getCustomers(signal?: AbortSignal) {
  return apiRequest<Customer[]>("/customers", { signal });
}

export function getCustomer(customerId: number, signal?: AbortSignal) {
  return apiRequest<Customer>(`/customers/${customerId}`, { signal });
}

export function getCustomerQuotes(customerId: number, signal?: AbortSignal) {
  return apiRequest<Quote[]>(`/customers/${customerId}/quotes`, { signal });
}

export function createCustomer(request: CreateCustomerRequest) {
  return apiRequest<Customer>("/customers", {
    method: "POST",
    body: JSON.stringify(request),
  });
}
