# FleetFlow frontend

Dispatcher-facing frontend for FleetFlow, built with Next.js, TypeScript, and Tailwind CSS.

## Local development

```bash
npm install
npm run dev
```

Open [http://localhost:3000](http://localhost:3000).

The frontend proxies `/backend-api/*` requests to the Spring Boot API. The
default backend URL is `http://localhost:8080`; override it in `.env.local` when
needed:

```bash
BACKEND_API_URL=http://localhost:8080
```

Useful checks:

```bash
npm run lint
npm run build
```
