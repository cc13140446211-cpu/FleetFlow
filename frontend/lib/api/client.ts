import type { ApiErrorPayload } from "@/lib/api/types";

const API_BASE_PATH = "/backend-api";

export class ApiError extends Error {
  readonly status: number;
  readonly details: ApiErrorPayload;

  constructor(status: number, message: string, details: ApiErrorPayload = {}) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.details = details;
  }
}

function isErrorPayload(value: unknown): value is ApiErrorPayload {
  return (
    typeof value === "object" &&
    value !== null &&
    Object.values(value).every((item) => typeof item === "string")
  );
}

export async function apiRequest<T>(
  path: string,
  init?: RequestInit,
): Promise<T> {
  const response = await fetch(`${API_BASE_PATH}${path}`, {
    ...init,
    headers: {
      Accept: "application/json",
      ...(init?.body ? { "Content-Type": "application/json" } : {}),
      ...init?.headers,
    },
  });

  const payload: unknown = await response.json().catch(() => null);

  if (!response.ok) {
    const details = isErrorPayload(payload) ? payload : {};
    const message =
      details.error ??
      Object.values(details)[0] ??
      `Request failed with status ${response.status}.`;

    throw new ApiError(response.status, message, details);
  }

  return payload as T;
}
