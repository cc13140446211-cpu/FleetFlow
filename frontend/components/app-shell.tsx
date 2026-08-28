"use client";

import { createContext, useContext, useState } from "react";
import { Sidebar } from "@/components/sidebar";

const OpenNavigationContext = createContext<(() => void) | null>(null);

export function useOpenNavigation() {
  return useContext(OpenNavigationContext);
}

export function AppShell({ children }: { children: React.ReactNode }) {
  const [isNavigationOpen, setIsNavigationOpen] = useState(false);

  return (
    <div className="min-h-screen bg-background text-foreground lg:grid lg:grid-cols-[232px_minmax(0,1fr)]">
      <Sidebar
        isOpen={isNavigationOpen}
        onClose={() => setIsNavigationOpen(false)}
      />
      <OpenNavigationContext.Provider value={() => setIsNavigationOpen(true)}>
        <div className="min-w-0">
          <main className="mx-auto w-full max-w-[1440px] px-5 py-8 sm:px-8">
            {children}
          </main>
        </div>
      </OpenNavigationContext.Provider>
    </div>
  );
}
