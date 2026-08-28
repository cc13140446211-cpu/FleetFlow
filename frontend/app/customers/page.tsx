import type { Metadata } from "next";
import { CustomerManagement } from "@/components/customers/customer-management";

export const metadata: Metadata = { title: "Customers" };

export default function CustomersPage() {
  return <CustomerManagement />;
}
