import type { Metadata } from "next";
import { PlaceholderPage } from "@/components/placeholder-page";

export const metadata: Metadata = { title: "Trucks" };

export default function TrucksPage() {
  return (
    <PlaceholderPage
      title="Trucks"
      description="View fleet availability and operational status."
      plannedContent="Read-only truck status, capacity, and assignment details will appear here."
    />
  );
}
