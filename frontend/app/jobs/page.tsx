import type { Metadata } from "next";
import { PlaceholderPage } from "@/components/placeholder-page";

export const metadata: Metadata = { title: "Jobs" };

export default function JobsPage() {
  return (
    <PlaceholderPage
      title="Jobs"
      description="Monitor scheduled and active freight operations."
      plannedContent="Job search, operational statuses, and assignment records will appear here."
    />
  );
}
