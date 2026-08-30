export type Customer = {
  custId: number;
  custName: string;
  custCompanyName: string | null;
  custPhone: string;
  custEmail: string | null;
  custAddress: string | null;
  custCreatedAt: string | null;
};

export type CreateCustomerRequest = {
  custName: string;
  custCompanyName: string | null;
  custPhone: string;
  custEmail: string;
  custAddress: string;
};

export type QuoteStatus =
  | "PENDING"
  | "ACCEPTED"
  | "REJECTED"
  | "CANCELLED"
  | "CONVERTED";

export type PaymentStatus = "UNPAID" | "PAID";

export type Quote = {
  quoteId: number;
  custId: number;
  preparedByEmpId: number;
  quotePickupLocation: string;
  quoteDropoffLocation: string;
  quotePreferredPickupDate: string;
  quotePrice: number;
  quoteStatus: QuoteStatus;
  quotePaymentStatus: PaymentStatus;
  quoteCreatedAt: string;
  quoteUpdatedAt: string | null;
};

export type CreateQuoteRequest = {
  custId: number;
  preparedByEmpId: number;
  quotePickupLocation: string;
  quoteDropoffLocation: string;
  quotePreferredPickupDate: string;
  quotePrice: number;
};

export type UpdateQuoteStatusRequest = {
  status: QuoteStatus;
};

export type UpdateQuotePaymentRequest = {
  status: "PAID";
};

export type JobStatus =
  | "SCHEDULED"
  | "IN_PROGRESS"
  | "COMPLETED"
  | "CANCELLED";

export type Job = {
  jobId: number;
  quoteId: number;
  driverEmpId: number;
  scheduledByEmpId: number;
  truckId: number;
  jobPickupDatetime: string;
  jobExpectedDropoffDatetime: string;
  jobFinalPrice: number;
  jobStatus: JobStatus;
  jobCreatedAt: string;
  jobUpdatedAt: string;
};

export type ApiErrorPayload = Record<string, string>;
