import type { Metadata } from "next";
import { PlaceholderPage } from "@/components/placeholder-page";

export const metadata: Metadata = { title: "Drivers" };

export default function DriversPage() {
  return (
    <PlaceholderPage
      title="Drivers"
      description="View driver availability and licence information."
      plannedContent="Read-only driver availability, licence, and assignment details will appear here."
    />
  );
}
