type PageHeaderProps = {
  title: string;
  description: string;
};

export function PageHeader({ title, description }: PageHeaderProps) {
  return (
    <header className="max-w-3xl">
      <p className="mb-2 text-xs font-semibold uppercase tracking-[0.12em] text-muted">
        Fleet operations
      </p>
      <h1 className="text-[30px] font-semibold leading-[1.2] tracking-[-0.025em]">
        {title}
      </h1>
      <p className="mt-2 text-secondary">{description}</p>
    </header>
  );
}
