"use client";

import { useOpenNavigation } from "@/components/app-shell";
import { UserIdentity } from "@/components/top-bar";

type PageHeaderProps = {
  title: string;
  action?: React.ReactNode;
};

export function PageHeader({ title, action }: PageHeaderProps) {
  const openNavigation = useOpenNavigation();

  return (
    <header className="flex min-h-10 items-center gap-2">
      <button
        type="button"
        onClick={() => openNavigation?.()}
        aria-label="Open navigation"
        className="grid size-9 shrink-0 place-items-center rounded-lg text-secondary transition-colors hover:bg-active hover:text-foreground lg:hidden"
      >
        <span aria-hidden="true" className="flex w-4.5 flex-col gap-1.5">
          <span className="h-px w-full bg-current" />
          <span className="h-px w-full bg-current" />
          <span className="h-px w-full bg-current" />
        </span>
      </button>

      <h1 className="min-w-0 flex-1 truncate text-2xl font-semibold leading-[1.2] tracking-[-0.025em] sm:text-[30px]">
        {title}
      </h1>

      <div className="ml-auto flex shrink-0 items-center gap-2">
        {action}
        {action ? <span className="mx-1 hidden h-6 w-px bg-border sm:block" /> : null}
        <UserIdentity />
      </div>
    </header>
  );
}
