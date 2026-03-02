---
description: 'Guidelines for using Flowbite and flowbite-react in this Next.js + Tailwind CSS v4 project'
applyTo: '**/*.tsx, **/*.ts, **/*.jsx, **/*.js, **/*.css'
---

# Flowbite Integration

[Flowbite](https://flowbite.com/) is an open-source component library built on top of Tailwind CSS. Use **flowbite-react** for first-class React/Next.js component support.

## 1. Installation

Install both `flowbite` and `flowbite-react`:

```bash
npm install flowbite flowbite-react
```

## 2. Tailwind CSS Configuration

Since this project uses **Tailwind CSS v4**, configure via the global CSS file (e.g. `app/globals.css`):

```css
@import "tailwindcss";

/* Make Tailwind scan Flowbite React component sources for class names */
@source "../node_modules/flowbite-react/dist";
```

> **Note:** If the project has been migrated back to a `tailwind.config.ts/js`, add the Flowbite plugin there instead:
>
> ```ts
> import flowbite from "flowbite/plugin";
>
> export default {
>   content: [
>     "./app/**/*.{js,ts,jsx,tsx}",
>     "./components/**/*.{js,ts,jsx,tsx}",
>     "node_modules/flowbite-react/dist/esm/**/*.js",
>   ],
>   plugins: [flowbite],
> };
> ```

## 3. Usage in Components

All Flowbite React components are **Client Components** — they rely on browser APIs and interactivity. Mark any file that imports Flowbite components with the `'use client'` directive.

### Basic Button

```tsx
"use client";

import { Button } from "flowbite-react";

export default function MyButton() {
  return <Button color="blue">Click me</Button>;
}
```

### Modal

```tsx
"use client";

import { useState } from "react";
import { Button, Modal } from "flowbite-react";

export default function MyModal() {
  const [open, setOpen] = useState(false);

  return (
    <>
      <Button onClick={() => setOpen(true)}>Open modal</Button>
      <Modal show={open} onClose={() => setOpen(false)}>
        <Modal.Header>Terms of Service</Modal.Header>
        <Modal.Body>
          <p className="text-base text-gray-500">
            Please read and accept the terms before continuing.
          </p>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={() => setOpen(false)}>Accept</Button>
          <Button color="gray" onClick={() => setOpen(false)}>
            Decline
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}
```

### Form Inputs

```tsx
"use client";

import { Label, TextInput, Textarea, Button } from "flowbite-react";

export default function ContactForm() {
  return (
    <form className="flex flex-col gap-4">
      <div>
        <Label htmlFor="email">Email</Label>
        <TextInput id="email" type="email" placeholder="you@example.com" required />
      </div>
      <div>
        <Label htmlFor="message">Message</Label>
        <Textarea id="message" rows={4} placeholder="Your message…" />
      </div>
      <Button type="submit">Send</Button>
    </form>
  );
}
```

### Navbar

```tsx
"use client";

import { Navbar } from "flowbite-react";

export default function AppNavbar() {
  return (
    <Navbar fluid rounded>
      <Navbar.Brand href="/">
        <span className="text-xl font-semibold">craftpg</span>
      </Navbar.Brand>
      <Navbar.Toggle />
      <Navbar.Collapse>
        <Navbar.Link href="/" active>Home</Navbar.Link>
        <Navbar.Link href="/about">About</Navbar.Link>
      </Navbar.Collapse>
    </Navbar>
  );
}
```

## 4. Customizing Flowbite Components with Tailwind

Override default styles using the `className` prop or the `theme` prop:

```tsx
"use client";

import { Button } from "flowbite-react";

// Via className (simple overrides)
<Button className="bg-purple-600 hover:bg-purple-700">Custom color</Button>

// Via theme (structural overrides)
import type { CustomFlowbiteTheme } from "flowbite-react";

const customTheme: CustomFlowbiteTheme["button"] = {
  color: {
    primary: "bg-brand-500 hover:bg-brand-600 text-white",
  },
};

<Button theme={customTheme} color="primary">Brand button</Button>
```

## 5. Server vs. Client Component Considerations

| Situation | Recommendation |
|-----------|----------------|
| Page-level data fetching | Keep the page as a **Server Component**; import Flowbite components from a child **Client Component** |
| Interactive UI (modals, dropdowns, tabs) | Always add `"use client"` at the top of the file |
| Static layout/content only | Prefer plain Tailwind classes in Server Components to minimise the client bundle |
| Layouts (`app/layout.tsx`) | Do **not** add `"use client"` to root layout; wrap only the interactive parts |

## 6. TypeScript Notes

- `flowbite-react` ships its own TypeScript definitions — no `@types/flowbite-react` package is needed.
- Use `CustomFlowbiteTheme` from `flowbite-react` to type custom theme objects.
- Prop types for each component are exported (e.g. `ButtonProps`, `ModalProps`) and can be imported for extension:

```ts
import type { ButtonProps } from "flowbite-react";

interface MyButtonProps extends ButtonProps {
  label: string;
}
```

## 7. Next.js 16 Specific Notes

- Next.js 16 uses React 19.2 by default. `flowbite-react` is compatible with React 19.
- When using Server Actions inside a Flowbite `<form>`, add the `action` prop directly to the `<form>` element rather than to the Flowbite wrapper.
- Avoid importing Flowbite in `app/layout.tsx` as a Server Component — wrap it in a client shell component if needed.

## Useful References

- [Flowbite React Documentation](https://flowbite-react.com/)
- [Flowbite Components](https://flowbite.com/docs/getting-started/introduction/)
