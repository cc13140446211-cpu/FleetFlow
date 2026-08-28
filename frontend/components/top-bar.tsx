export function UserIdentity() {
  return (
    <div className="flex shrink-0 items-center gap-2.5">
      <div className="hidden text-right lg:block">
        <p className="text-[13px] font-medium leading-4">Dispatcher</p>
        <p className="text-[11px] leading-4 text-secondary">Operations</p>
      </div>
      <div
        aria-label="Signed in as Dispatcher"
        className="grid size-9 place-items-center rounded-full bg-active text-xs font-semibold"
      >
        YC
      </div>
    </div>
  );
}
