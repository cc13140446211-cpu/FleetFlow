import type { Job } from "@/lib/api/types";
import { apiRequest } from "@/lib/api/client";

export function getJobs(signal?: AbortSignal) {
  return apiRequest<Job[]>("/jobs", { signal });
}

export function getJobByID(jobId: number, signal?: AbortSignal) {
  return apiRequest<Job>(`/jobs/${jobId}`, { signal });
}
