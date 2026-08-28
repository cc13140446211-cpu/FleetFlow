import { PageHeader } from "@/components/page-header";

type PlaceholderPageProps = {
  title: string;
  plannedContent: string;
};

export function PlaceholderPage({
  title,
  plannedContent,
}: PlaceholderPageProps) {
  return (
    <div className="space-y-8">
      <PageHeader title={title} />
      <section className="border-t border-border pt-8" aria-label={`${title} content`}>
        <div className="min-h-[280px] max-w-2xl border-l border-border pl-6">
          <h2 className="text-base font-semibold">Workspace ready</h2>
          <p className="mt-2 max-w-xl text-secondary">
            {plannedContent} This area is intentionally left as a placeholder for
            the next implementation phase.
          </p>
        </div>
      </section>
    </div>
  );
}
