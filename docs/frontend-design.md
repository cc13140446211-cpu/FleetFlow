# FleetFlow Frontend Design System

## 1. Design Direction

Style: Operational Minimalism

Reference:
[Tabela — Awwwards](https://www.awwwards.com/sites/tabela)

Design goals:
- Clean and calm management interface
- Low visual noise
- Strong information hierarchy
- Efficient for dispatcher workflows
- Typography and spacing over decoration
- Consistent visual language across all pages

Avoid:
- Excessive cards
- Heavy shadows
- Decorative gradients
- Glassmorphism
- Unnecessary animations
- Oversized UI elements
- Colourful dashboard charts without operational value


---

# 2. Font

## Family

Primary:
Geist

Fallbacks:
- -apple-system
- BlinkMacSystemFont
- "Segoe UI"
- sans-serif

Applied globally to all UI elements.

## Weights Used

400 — Regular
500 — Medium
600 — Semi-bold

Avoid 700+ unless exceptionally required.

## Base Font Size

14px

## Typography Sizes

| Element | Size | Weight | Usage |
|---|---:|---:|---|
| Page Title | 30px | 600 | Main page heading |
| H2 | 20px | 600 | Major sections |
| H3 | 16px | 600 | Component sections |
| Body | 14px | 400 | General content |
| Label | 13px | 500 | Form labels |
| Button | 14px | 500 | Buttons |
| Input | 14px | 400 | Form values |
| Table Header | 12px | 600 | Column headings |
| Metadata | 12px | 400 | Secondary information |
| KPI | 32px | 500 | Dashboard metrics |

Default line-height:
1.5


---

# 3. Colour

## Base Palette

| Token | Value | Usage |
|---|---|---|
| Background | #F5F4F1 | Main application background |
| Surface | #FFFFFF | Tables, overlays, inputs |
| Primary Text | #292929 | Main text |
| Secondary Text | #737373 | Supporting information |
| Muted Text | #A3A3A3 | Low-priority metadata |
| Border | #E5E3DE | Dividers and component borders |

## Semantic Colours

Success:
Muted green

Warning:
Muted amber

Error:
Muted red

Information:
Muted blue

Semantic colours should mainly be used for:
- Status badges
- Alerts
- Validation
- Operational states

Avoid using semantic colours as decoration.


---

# 4. Spacing

Base spacing unit:
4px

| Token | Size | Typical Usage |
|---|---:|---|
| xs | 4px | Very small internal spacing |
| sm | 8px | Icon/text gap |
| md | 16px | Component spacing |
| lg | 24px | Content groups |
| xl | 32px | Sections |
| 2xl | 48px | Major page separation |

Page horizontal padding:
32px

Page vertical padding:
32px

Section gap:
32px


---

# 5. Border Radius

| Element | Radius |
|---|---:|
| Input | 8px |
| Button | 8px |
| Dropdown | 8px |
| Table container | 10px |
| Modal | 12px |
| Drawer | 12px |
| Badge | 999px |

Avoid excessive rounded cards.


---

# 6. Borders & Shadows

Default border:
1px solid #E5E3DE

Use borders and whitespace before shadows.

Shadows should only appear on floating elements:
- Modal
- Drawer
- Dropdown
- Popover

Normal dashboard sections and tables should not use heavy shadows.


---

# 7. Layout

## Application Shell

Sidebar:
220–240px

Main content:
Flexible width

Recommended maximum content width:
1440px

Page padding:
32px

Structure:

Sidebar
│
└── Main
├── Top Bar
├── Page Header
└── Page Content


---

# 8. Sidebar

Navigation hierarchy:

OVERVIEW
- Overview
- Schedule

OPERATIONS
- Quotes
- Jobs

RELATIONSHIPS
- Customers

RESOURCES
- Drivers
- Trucks

Rules:
- Minimal icons
- Neutral colours
- No colourful icons
- Active state uses subtle background/accent
- Section labels use small muted typography
- Avoid dark oversized sidebar


---

# 9. Buttons

## Primary

Used for the main action of a page.

Examples:
+ New Quote
  Schedule Job
  Add Customer

## Secondary

Used for supporting actions.

Examples:
Cancel
Back
View Details

## Destructive

Only for:
Cancel Quote
Cancel Job
Delete actions

Rules:
- One obvious primary action per section
- Avoid multiple competing primary buttons
- Height approximately 40px
- No gradients
- No excessive shadows


---

# 10. Inputs

Height:
40px

Font:
14px / 400

Border:
1px

Radius:
8px

States:
Default
Hover
Focus
Error
Disabled

Labels always appear above inputs.


---

# 11. Tables

Tables are the primary information component.

Used for:
- Quotes
- Jobs
- Customers
- Drivers
- Trucks

Table header:
12px / 600

Table content:
14px / 400

Recommended row height:
52–56px

Rules:
- Subtle horizontal dividers
- Avoid heavy grid lines
- Avoid strong zebra striping
- Primary identifier slightly stronger
- Secondary information muted
- Keep actions minimal
- Rows may use subtle hover state


---

# 12. Status Badges

Shape:
Pill

Typography:
12px / 500

Use subtle background tint rather than saturated solid colours.

Examples:

PENDING
ACCEPTED
PAID
SCHEDULED
IN PROGRESS
COMPLETED
CANCELLED

Colour communicates semantic status only.


---

# 13. Cards

Cards should NOT be the default layout primitive.

Use cards only when information genuinely forms an independent group.

Prefer:

Typography
Whitespace
Dividers
Tables

over:

Card inside card inside card

Dashboard KPI cards should be visually lightweight.


---

# 14. Schedule Timeline

Schedule is a core FleetFlow component.

Structure:

Resource Name | Timeline
| 08 09 10 11 12 13 14 15 16 17

Each row represents:
- Driver
  or
- Truck

Each job is represented as a time block.

Empty timeline space represents AVAILABLE.

Do NOT fill available time with green.

Job blocks should display:
- Job ID
- Route
- Start/end time when space permits

Interaction:
- Hover → quick details
- Click → job detail drawer

Views:
- Drivers
- Trucks
- Day
- Week


---

# 15. Availability

Availability should communicate conflicts visually.

Requested period:
09:00 ───────────────── 16:00

Resources:

Ahmad
████████ Job J-104
CONFLICT

John
Available

Amir
Available

Rules:
- Existing jobs create occupied blocks
- Empty space means availability
- Conflicts use semantic warning/error styling
- Avoid filling entire available areas with colour


---

# 16. Feedback States

Every data-driven page must support:

Loading
Empty
Error
Success

Forms additionally support:
Validation Error

Scheduling additionally supports:
Resource Conflict

Use concise operational language.

Example:

Driver unavailable

The selected driver already has an overlapping
job during this period.

[Select another driver]


---

# 17. Motion

Allowed:
- Subtle hover transitions
- Drawer open/close
- Modal transition
- Dropdown transition
- Loading skeleton
- Toast feedback

Avoid:
- Parallax
- 3D effects
- Animated gradients
- Continuous animations
- Decorative motion

Motion should explain state changes, not decorate the interface.


---

# 18. Core Components

Layout:
- AppShell
- Sidebar
- TopBar
- PageHeader

Data:
- DataTable
- StatusBadge
- Metric
- EmptyState
- ErrorState
- Skeleton

Forms:
- Button
- Input
- Select
- DatePicker
- TimePicker

Overlay:
- Modal
- Drawer
- Dropdown
- Toast

Scheduling:
- ScheduleTimeline
- TimelineHeader
- ResourceRow
- JobBlock
- AvailabilitySelector


---

# 19. Design Rule

FleetFlow should feel:

Clean
Calm
Operational
Professional
Information-dense
Consistent

When choosing between a decorative design and a clearer
operational design, always choose clarity.