import type { Metadata } from "next";
import { QuoteManagement } from "@/components/quotes/quote-management";

export const metadata: Metadata = { title: "Quotes" };

export default function QuotesPage() {
  return <QuoteManagement />;
}
