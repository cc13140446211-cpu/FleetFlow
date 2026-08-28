type TopBarProps = {
  onOpenNavigation: () => void;
};

export function TopBar({ onOpenNavigation }: TopBarProps) {
  return (
    <header className="sticky top-0 z-30 flex h-[72px] items-center justify-between border-b border-border bg-surface/95 px-5 backdrop-blur-sm sm:px-8">
      <button
        type="button"
        onClick={onOpenNavigation}
        aria-label="Open navigation"
        className="grid size-10 place-items-center rounded-lg text-secondary hover:bg-active hover:text-foreground lg:hidden"
      >
        <span aria-hidden="true" className="flex w-5 flex-col gap-1.5">
          <span className="h-px w-full bg-current" />
          <span className="h-px w-full bg-current" />
          <span className="h-px w-full bg-current" />
        </span>
      </button>
      <div className="ml-auto flex items-center gap-3">
        <div className="hidden text-right sm:block">
          <p className="font-medium">Dispatcher</p>
          <p className="text-xs text-secondary">Operations</p>
        </div>
        <div
          aria-hidden="true"
          className="grid size-9 place-items-center rounded-full bg-active text-xs font-semibold"
        >
          YC
        </div>
      </div>
    </header>
  );
}
