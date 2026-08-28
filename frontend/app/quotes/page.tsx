import type { Metadata } from "next";
import { PlaceholderPage } from "@/components/placeholder-page";

export const metadata: Metadata = { title: "Quotes" };

export default function QuotesPage() {
  return (
    <PlaceholderPage
      title="Quotes"
      description="Manage customer quotations from creation to job conversion."
      plannedContent="Quotation search, filters, statuses, and records will appear here."
    />
  );
}
