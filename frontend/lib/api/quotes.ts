import { apiRequest } from "@/lib/api/client";
import type {
  CreateQuoteRequest,
  Quote,
  QuoteStatus,
} from "@/lib/api/types";

export function getQuotes(signal?: AbortSignal) {
  return apiRequest<Quote[]>("/quotes", { signal });
}

export function getQuote(quoteId: number, signal?: AbortSignal) {
  return apiRequest<Quote>(`/quotes/${quoteId}`, { signal });
}

export function createQuote(request: CreateQuoteRequest) {
  return apiRequest<Quote>("/quotes", {
    method: "POST",
    body: JSON.stringify(request),
  });
}

export function updateQuoteStatus(quoteId: number, status: QuoteStatus) {
  return apiRequest<Quote>(`/quotes/${quoteId}/status`, {
    method: "PATCH",
    body: JSON.stringify({ status }),
  });
}

export function recordQuotePayment(quoteId: number) {
  return apiRequest<Quote>(`/quotes/${quoteId}/payment`, {
    method: "PATCH",
    body: JSON.stringify({ status: "PAID" }),
  });
}
