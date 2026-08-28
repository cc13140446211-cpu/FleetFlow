"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

const navigation = [
  {
    label: "Overview",
    items: [
      { label: "Overview", href: "/overview" },
      { label: "Schedule", href: "/schedule" },
    ],
  },
  {
    label: "Operations",
    items: [
      { label: "Quotes", href: "/quotes" },
      { label: "Jobs", href: "/jobs" },
    ],
  },
  {
    label: "Relationships",
    items: [{ label: "Customers", href: "/customers" }],
  },
  {
    label: "Resources",
    items: [
      { label: "Drivers", href: "/drivers" },
      { label: "Trucks", href: "/trucks" },
    ],
  },
] as const;

type SidebarProps = {
  isOpen: boolean;
  onClose: () => void;
};

export function Sidebar({ isOpen, onClose }: SidebarProps) {
  const pathname = usePathname();

  return (
    <>
      <button
        type="button"
        aria-label="Close navigation"
        className={`fixed inset-0 z-40 bg-black/20 transition-opacity lg:hidden ${
          isOpen ? "opacity-100" : "pointer-events-none opacity-0"
        }`}
        onClick={onClose}
      />
      <aside
        aria-label="Primary navigation"
        className={`fixed inset-y-0 left-0 z-50 flex w-[232px] flex-col border-r border-border bg-surface transition-transform duration-200 ease-out lg:sticky lg:top-0 lg:h-screen lg:translate-x-0 ${
          isOpen ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <div className="flex h-[88px] items-center justify-between px-6">
          <Link href="/overview" onClick={onClose} className="block rounded-sm">
            <span className="block text-[15px] font-semibold tracking-[0.12em]">
              FLEETFLOW
            </span>
            <span className="mt-0.5 block text-xs text-secondary">Freight Ops</span>
          </Link>
          <button
            type="button"
            onClick={onClose}
            aria-label="Close navigation"
            className="grid size-9 place-items-center rounded-lg text-secondary hover:bg-active hover:text-foreground lg:hidden"
          >
            <span aria-hidden="true" className="text-xl leading-none">
              ×
            </span>
          </button>
        </div>

        <nav className="flex-1 overflow-y-auto px-3 pb-6 pt-3">
          <div className="space-y-6">
            {navigation.map((section) => (
              <div key={section.label}>
                <p className="mb-2 px-3 text-[11px] font-semibold uppercase tracking-[0.12em] text-muted">
                  {section.label}
                </p>
                <ul className="space-y-1">
                  {section.items.map((item) => {
                    const isActive = pathname === item.href;

                    return (
                      <li key={item.href}>
                        <Link
                          href={item.href}
                          aria-current={isActive ? "page" : undefined}
                          onClick={onClose}
                          className={`flex h-10 items-center gap-3 rounded-lg px-3 font-medium transition-colors ${
                            isActive
                              ? "bg-active text-foreground"
                              : "text-secondary hover:bg-active/60 hover:text-foreground"
                          }`}
                        >
                          <span
                            aria-hidden="true"
                            className={`size-1.5 rounded-full ${
                              isActive ? "bg-foreground" : "bg-border"
                            }`}
                          />
                          {item.label}
                        </Link>
                      </li>
                    );
                  })}
                </ul>
              </div>
            ))}
          </div>
        </nav>
      </aside>
    </>
  );
}
