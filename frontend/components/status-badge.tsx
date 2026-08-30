const statusStyles: Record<string, string> = {
  PENDING: "bg-warning-background text-warning-foreground",
  ACCEPTED: "bg-info-background text-info-foreground",
  REJECTED: "bg-error-background text-error-foreground",
  CANCELLED: "bg-error-background text-error-foreground",
  CONVERTED: "bg-success-background text-success-foreground",
  PAID: "bg-success-background text-success-foreground",
  UNPAID: "bg-warning-background text-warning-foreground",
  SCHEDULED: "bg-info-background text-info-foreground",
  IN_PROGRESS: "bg-warning-background text-warning-foreground",
  COMPLETED: "bg-success-background text-success-foreground",
};

export function StatusBadge({ status }: { status: string }) {
  return (
    <span
      className={`inline-flex rounded-full border border-current/15 px-2.5 py-1 text-xs font-semibold ${
        statusStyles[status] ?? "bg-active text-secondary"
      }`}
    >
      {status.replaceAll("_", " ")}
    </span>
  );
}
