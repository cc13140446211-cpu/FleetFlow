import type { ReactNode } from "react";

export type DataTableColumn<T> = {
  key: string;
  header: string;
  className?: string;
  render: (row: T) => ReactNode;
};

type DataTableProps<T> = {
  columns: DataTableColumn<T>[];
  rows: T[];
  getRowKey: (row: T) => string | number;
  onRowClick?: (row: T) => void;
  getRowLabel?: (row: T) => string;
};

export function DataTable<T>({
  columns,
  rows,
  getRowKey,
  onRowClick,
  getRowLabel,
}: DataTableProps<T>) {
  return (
    <div className="overflow-x-auto rounded-[10px] border border-border bg-surface">
      <table className="w-full min-w-[760px] border-collapse text-left">
        <thead>
          <tr className="border-b border-border">
            {columns.map((column) => (
              <th
                key={column.key}
                scope="col"
                className={`h-11 px-4 text-xs font-semibold uppercase tracking-[0.06em] text-secondary ${column.className ?? ""}`}
              >
                {column.header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((row) => (
            <tr
              key={getRowKey(row)}
              className={`h-14 border-b border-border last:border-b-0 ${
                onRowClick
                  ? "cursor-pointer transition-colors hover:bg-active/50 focus-within:bg-active/50"
                  : ""
              }`}
              onClick={onRowClick ? () => onRowClick(row) : undefined}
              aria-label={getRowLabel?.(row)}
            >
              {columns.map((column) => (
                <td key={column.key} className={`px-4 ${column.className ?? ""}`}>
                  {column.render(row)}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export function DataTableSkeleton({ columns = 5, rows = 5 }) {
  return (
    <div
      className="overflow-hidden rounded-[10px] border border-border bg-surface"
      aria-label="Loading customers"
      aria-busy="true"
    >
      <div className="grid h-11 grid-cols-5 gap-6 border-b border-border px-4">
        {Array.from({ length: columns }).map((_, index) => (
          <div
            key={index}
            className="my-auto h-2.5 max-w-20 rounded-full bg-active"
          />
        ))}
      </div>
      {Array.from({ length: rows }).map((_, rowIndex) => (
        <div
          key={rowIndex}
          className="grid h-14 grid-cols-5 items-center gap-6 border-b border-border px-4 last:border-b-0"
        >
          {Array.from({ length: columns }).map((_, columnIndex) => (
            <div
              key={columnIndex}
              className={`h-3 rounded-full bg-active ${
                columnIndex === 0 ? "w-28" : "w-20"
              }`}
            />
          ))}
        </div>
      ))}
    </div>
  );
}
