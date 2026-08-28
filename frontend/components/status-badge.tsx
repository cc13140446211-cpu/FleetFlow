const statusStyles: Record<string, string> = {
  PENDING: "bg-warning-background text-warning-foreground",
  ACCEPTED: "bg-info-background text-info-foreground",
  REJECTED: "bg-error-background text-error-foreground",
  CANCELLED: "bg-error-background text-error-foreground",
  CONVERTED: "bg-success-background text-success-foreground",
  PAID: "bg-success-background text-success-foreground",
  UNPAID: "bg-warning-background text-warning-foreground",
};

export function StatusBadge({ status }: { status: string }) {
  return (
    <span
      className={`inline-flex rounded-full px-2.5 py-1 text-xs font-medium ${
        statusStyles[status] ?? "bg-active text-secondary"
      }`}
    >
      {status.replaceAll("_", " ")}
    </span>
  );
}
