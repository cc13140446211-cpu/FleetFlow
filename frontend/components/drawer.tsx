"use client";

import { useEffect, useRef } from "react";

type DrawerProps = {
  open: boolean;
  title: string;
  description?: string;
  onClose: () => void;
  children: React.ReactNode;
};

export function Drawer({
  open,
  title,
  description,
  onClose,
  children,
}: DrawerProps) {
  const dialogRef = useRef<HTMLDialogElement>(null);

  useEffect(() => {
    const dialog = dialogRef.current;
    if (!dialog) return;

    if (open && !dialog.open) dialog.showModal();
    if (!open && dialog.open) dialog.close();
  }, [open]);

  return (
    <dialog
      ref={dialogRef}
      onCancel={(event) => {
        event.preventDefault();
        onClose();
      }}
      onClose={onClose}
      aria-labelledby="drawer-title"
      aria-describedby={description ? "drawer-description" : undefined}
      className="fixed inset-y-0 right-0 m-0 ml-auto h-dvh max-h-none w-full max-w-[560px] overflow-hidden rounded-none border-0 border-l border-border bg-surface p-0 text-foreground shadow-[0_16px_48px_rgba(41,41,41,0.12)] backdrop:bg-black/20 sm:rounded-l-xl"
    >
      <div className="flex h-full flex-col">
        <header className="flex items-start justify-between gap-6 border-b border-border-strong bg-surface-subtle px-6 py-5 sm:px-8">
          <div>
            <h2 id="drawer-title" className="text-xl font-semibold tracking-[-0.02em]">
              {title}
            </h2>
            {description ? (
              <p id="drawer-description" className="mt-1 text-sm text-secondary">
                {description}
              </p>
            ) : null}
          </div>
          <button
            type="button"
            onClick={onClose}
            aria-label="Close drawer"
            className="grid size-9 shrink-0 place-items-center rounded-lg text-xl leading-none text-secondary transition-colors hover:bg-accent-soft hover:text-accent-foreground"
          >
            ×
          </button>
        </header>
        <div className="min-h-0 flex-1 overflow-y-auto">{children}</div>
      </div>
    </dialog>
  );
}
