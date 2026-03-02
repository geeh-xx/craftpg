# Design Guidelines — Flowbite + Tailwind (Light Mode)

## Design goals
- Clean, “productivity app” look
- Minimal clutter
- Mobile-first character sheet
- High readability for long sessions/notes

## Color palette (Tailwind-based)
- Background: white
- Surface: gray-50 / slate-50
- Border: gray-200
- Text: slate-900
- Muted: slate-600
- Primary: blue-600 (hover blue-700)
- Success: green-600
- Warning: amber-500
- Danger: red-600

## Typography
- Font: Inter (or system UI)
- Headings: semibold
- Body: regular
- Sizes:
  - H1: 30–36
  - H2: 22–28
  - H3: 18–20
  - Body: 14–16

## Spacing & layout
- 8px spacing grid
- Page max width: 1200–1280
- Cards padding: 16–24
- Rounded corners: lg/xl
- Shadows: subtle (shadow-sm)

## Component mapping (Flowbite)

### Global layout
- Navbar: Flowbite Navbar
- Sidebar: Flowbite Sidebar (desktop) + Drawer pattern (mobile)
- Breadcrumb (optional): Flowbite Breadcrumb

### Forms
- Inputs: Flowbite Input
- Select: Flowbite Select
- Textarea: Flowbite Textarea
- Checkbox/Radio: Flowbite Checkbox/Radio
- Modal confirmations: Flowbite Modal

### Data display
- Tables: Flowbite Table (sessions list, inventory list)
- Badges: Flowbite Badge (campaign status, roles)
- Tabs: Flowbite Tabs (character sheet sections, session subsections)
- Accordion: Flowbite Accordion (FAQ, collapsible details)
- Dropdowns: Flowbite Dropdown (actions menu)

### Feedback
- Toasts: Flowbite Toast (saved, error)
- Alerts: Flowbite Alert (permissions, warnings)
- Spinner: Flowbite Spinner (loading)
- Progress: Flowbite Progress (campaign progressPercent)

## Character sheet layout (must)
- Top summary bar (sticky on mobile):
  - Name, race, class, level
  - Current HP/MP, Defense
  - Primary actions: Edit / Level Up
- Main content as Tabs:
  1. Attributes
  2. Skills
  3. Powers/Features
  4. Spells (if applicable)
  5. Inventory
  6. Notes
- Desktop: 2-column layout inside tabs where possible
- Mobile: single-column, collapsible sections

## Session page layout
- Header: session title + datetime + actions
- Tabs:
  - Summary/Notes
  - Attendance & XP
  - NPCs
  - Maps
  - Treasures

## Accessibility
- Maintain contrast (AA)
- Always label inputs
- Focus rings visible
- Buttons have icons + text for critical actions

## UX rules
- Hard rule reminders:
  - “Character is locked for this campaign” visible in membership UI
  - Invite acceptance requires character selection (blocking step)
- Confirmation modals for destructive actions
- Autosave is optional; prefer explicit Save/Cancel in MVP