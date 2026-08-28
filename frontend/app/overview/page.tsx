import type { Metadata } from "next";
import { PlaceholderPage } from "@/components/placeholder-page";

export const metadata: Metadata = { title: "Overview" };

export default function OverviewPage() {
  return (
    <PlaceholderPage
      title="Overview"
      plannedContent="Operational metrics, today’s schedule, attention items, and resource status will appear here."
    />
  );
}
