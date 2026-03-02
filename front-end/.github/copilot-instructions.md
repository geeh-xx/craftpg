# Copilot Instructions for craftpg-fe

## Project Description

**craftpg-fe** is a Next.js frontend application built with the following stack:

- **Framework**: Next.js 16.1.6 with App Router
- **Language**: TypeScript 5
- **UI Framework**: React 19.2.3
- **Styling**: Tailwind CSS v4
- **UI Component Library**: Flowbite / flowbite-react
- **Linting**: ESLint with Next.js configuration
- **Development**: Supports hot reload and auto-updates via `next dev`

The project uses the App Router (located in the `app/` directory), enabling React Server Components, layouts, and nested routing by default.

---

## Project Structure

```
craftpg-fe/
├── app/               # App Router — pages, layouts, route handlers
├── public/            # Static assets
├── components/        # Reusable UI components
├── lib/               # Shared utilities and API clients
├── hooks/             # Custom React hooks
├── types/             # TypeScript type definitions
├── styles/            # Global stylesheets
└── .github/
    ├── copilot-instructions.md   # This file — general project overview
    └── instructions/             # Technology-specific coding guidelines
        ├── typescript.instructions.md
        ├── nextjs.instructions.md
        ├── nextjs-tailwind.instructions.md
        ├── react.instructions.md
        └── flowbite.instructions.md
```

---

## Coding Guidelines

Technology-specific instructions are in `.github/instructions/`. Refer to the relevant file when working in that area:

| File | Applies to |
|------|------------|
| `typescript.instructions.md` | All `.ts` files |
| `nextjs.instructions.md` | Next.js pages, layouts, route handlers |
| `nextjs-tailwind.instructions.md` | Components styled with Tailwind CSS |
| `react.instructions.md` | React components and hooks |
| `flowbite.instructions.md` | Flowbite / flowbite-react component usage |

---

## Useful References

- [Flowbite React Documentation](https://flowbite-react.com/)
- [Next.js App Router Docs](https://nextjs.org/docs/app)
- [Tailwind CSS v4 Docs](https://tailwindcss.com/docs)
- [React 19 Docs](https://react.dev/)
