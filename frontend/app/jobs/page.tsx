import type { Metadata } from "next";
import { JobManagement } from "@/components/jobs/job-management";

export const metadata: Metadata = { title: "Jobs" };

export default function JobsPage() {
  return <JobManagement />;
}
