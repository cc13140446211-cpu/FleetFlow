"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { useState } from "react";
import {
  LayoutDashboard,
  BriefcaseBusiness,
  Users,
  Truck,
  ChevronDown,
} from "lucide-react";

const navigation = [
  {
    label: "Overview",
    icon: LayoutDashboard,
    items: [
      { label: "Overview", href: "/overview" },
      { label: "Schedule", href: "/schedule" },
    ],
  },
  {
    label: "Operations",
    icon: BriefcaseBusiness,
    items: [
      { label: "Quotes", href: "/quotes" },
      { label: "Jobs", href: "/jobs" },
    ],
  },
  {
    label: "Relationships",
    icon: Users,
    items: [{ label: "Customers", href: "/customers" }],
  },
  {
    label: "Resources",
    icon: Truck,
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
  const [openSections, setOpenSections] = useState({
    Overview: true,
    Operations: false,
    Relationships: false,
    Resources: false,
  });

  const toggleSection = (label: keyof typeof openSections) => {
    setOpenSections((previous) => ({
      ...previous,
      [label]: !previous[label],
    }));
  };

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
        className={`fixed inset-y-0 left-0 z-50 flex w-[232px] flex-col border-r border-sidebar-border bg-sidebar transition-transform duration-200 ease-out lg:sticky lg:top-0 lg:h-screen lg:translate-x-0 ${
          isOpen ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <div className="flex h-[88px] items-center justify-between px-6">
          <Link href="/overview" 
                onClick={onClose} 
                className="block rounded-sm"
          >
            <span className="block text-[15px] font-semibold tracking-[0.12em] text-sidebar-foreground">
              FLEETFLOW
            </span>
            <span className="mt-0.5 block text-xs text-sidebar-muted">
              Freight Ops
            </span>
          </Link>

          <button
            type="button"
            onClick={onClose}
            aria-label="Close navigation"
            className="grid size-9 place-items-center rounded-lg text-sidebar-secondary hover:bg-sidebar-active hover:text-sidebar-foreground lg:hidden">
            <span aria-hidden="true" className="text-xl leading-none">
              ×
            </span>
          </button>
        </div>

        <nav className="flex-1 overflow-y-auto px-3 pb-6 pt-3">
          <div className="space-y-6">
            {navigation.map((section) => {

              const SectionIcon = section.icon;
              return(
                <div key={section.label}>
                <button
                    type="button"
                    onClick={() => toggleSection(section.label)}
                    className="mb-2 flex w-full items-center gap-2 rounded-lg px-3 py-2 text-sm font-semibold text-sidebar-secondary transition-colors hover:bg-sidebar-hover hover:text-sidebar-foreground"
                  >
                    <SectionIcon className="size-4" />

                    <span>{section.label}</span>

                    <ChevronDown
                      className={`ml-auto size-4 transition-transform duration-200 ${
                        openSections[section.label] ? "rotate-0" : "-rotate-90"
                      }`}
                    />
                  </button>

                  {openSections[section.label] && (
                    <ul className="space-y-1">
                      {section.items.map((item) => {
                        const isActive = pathname === item.href;

                        return (
                          <li key={item.href}>
                            <Link
                              href={item.href}
                              aria-current={isActive ? "page" : undefined}
                              onClick={onClose}
                              className={`flex h-10 items-center rounded-lg pl-10 pr-3 font-normal transition-colors ${
                                isActive
                                  ? "bg-sidebar-active text-sidebar-foreground"
                                  : "text-sidebar-secondary hover:bg-sidebar-hover hover:text-sidebar-foreground"
                              }`}
                            >
                              {item.label}
                            </Link>
                          </li>
                        );
                      })}
                    </ul>
                  )}
                </div>
              );
            })}
          </div>
        </nav>
      </aside>
    </>
  );
}