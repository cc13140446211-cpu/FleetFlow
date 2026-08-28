import type { Metadata } from "next";
import { PlaceholderPage } from "@/components/placeholder-page";

export const metadata: Metadata = { title: "Schedule" };

export default function SchedulePage() {
  return (
    <PlaceholderPage
      title="Schedule"
      description="Plan and review freight resource assignments."
      plannedContent="The driver and truck day timelines will appear here."
    />
  );
}
