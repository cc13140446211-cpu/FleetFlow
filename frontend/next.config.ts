import type { NextConfig } from "next";

const backendApiUrl = (
  process.env.BACKEND_API_URL ?? "http://localhost:8080"
).replace(/\/$/, "");

const nextConfig: NextConfig = {
  async rewrites() {
    return [
      {
        source: "/backend-api/:path*",
        destination: `${backendApiUrl}/api/:path*`,
      },
    ];
  },
};

export default nextConfig;
