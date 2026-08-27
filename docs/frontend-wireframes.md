# FleetFlow Frontend Wireframes

## 1. Document Purpose

This document defines the page structure, information hierarchy, user actions, and navigation flow for the FleetFlow V1 frontend.

It is a low-fidelity wireframe specification.

This document defines:

- What information appears on each page
- Where major information is positioned
- What actions the dispatcher can perform
- How pages connect to each other
- What application states need to be represented

This document does **not** define:

- Exact colours
- Exact typography
- Shadows
- Border radius
- Animation styling
- Final visual appearance

Those rules are defined separately in:

`docs/frontend-design.md`

---

# 2. Product Scope

FleetFlow V1 is a dispatcher-focused freight operations management system.

The primary user is:

**Dispatcher**

The dispatcher manages the operational workflow from customer enquiry through quotation, payment confirmation, resource scheduling, and freight job execution.

## Core Workflow

```text
Customer
    ↓
Create Quote
    ↓
PENDING
    ↓
Accept Quote
    ↓
ACCEPTED + UNPAID
    ↓
Record Payment
    ↓
ACCEPTED + PAID
    ↓
Schedule Job
    ↓
Select Pickup / Drop-off Time
    ↓
Check Driver Availability
    ↓
Check Truck Availability
    ↓
Assign Resources
    ↓
Create Job
    ↓
SCHEDULED
    ↓
IN_PROGRESS
    ↓
COMPLETED
```

Alternative quote outcomes:

```text
PENDING
   ├── ACCEPTED
   ├── REJECTED
   └── CANCELLED
```

After successful job creation:

```text
Quote
ACCEPTED + PAID
        ↓
     CONVERTED
        ↓
       Job
```

---

# 3. V1 Role Boundaries

## Dispatcher Can

- View operational overview
- View daily resource schedule
- Create customers
- View customers
- View customer quotation history
- Create quotations
- Accept quotations
- Reject quotations
- Cancel quotations where allowed
- Record quote payment
- Schedule freight jobs
- Assign drivers
- Assign trucks
- View driver availability
- View truck availability
- View jobs
- Update operational job status
- View driver information
- View truck information

## Dispatcher Does Not Manage

- Employee account creation
- Employee roles
- Driver registration
- Truck registration
- User authentication administration
- System configuration

Driver and truck records are treated as existing company resources in V1.

These administration capabilities may be introduced through a future Admin module.

---

# 4. Information Architecture

```text
FleetFlow
│
├── Overview
│
├── Schedule
│
├── Operations
│   ├── Quotes
│   └── Jobs
│
├── Relationships
│   └── Customers
│
└── Resources
    ├── Drivers
    └── Trucks
```

Primary navigation:

```text
OVERVIEW
Overview
Schedule

OPERATIONS
Quotes
Jobs

RELATIONSHIPS
Customers

RESOURCES
Drivers
Trucks
```

V1 should not contain placeholder navigation such as:

- Analytics
- Reports
- Messages
- Settings
- Notifications
- Billing

unless those features actually exist.

---

# 5. Global Application Shell

## Purpose

Provide consistent navigation and page structure throughout the Dispatcher application.

## Wireframe

```text
┌──────────────────────────────────────────────────────────────────────────┐
│                  │                                                       │
│  FLEETFLOW       │                                      Dispatcher  YC   │
│  Freight Ops     │                                                       │
│                  ├───────────────────────────────────────────────────────┤
│  OVERVIEW        │                                                       │
│  Overview        │                                                       │
│  Schedule        │                                                       │
│                  │                                                       │
│  OPERATIONS      │                                                       │
│  Quotes          │                                                       │
│  Jobs            │                PAGE CONTENT                           │
│                  │                                                       │
│  RELATIONSHIPS   │                                                       │
│  Customers       │                                                       │
│                  │                                                       │
│  RESOURCES       │                                                       │
│  Drivers         │                                                       │
│  Trucks          │                                                       │
│                  │                                                       │
│                  │                                                       │
└──────────────────┴───────────────────────────────────────────────────────┘
```

## Structure

```text
AppShell
├── Sidebar
├── TopBar
└── MainContent
    ├── PageHeader
    └── PageContent
```

## Sidebar Behaviour

The sidebar remains visible while navigating between management pages.

The active page should be clearly indicated without excessive visual decoration.

---

# 6. Overview

## Purpose

Answer the operational question:

> What requires the dispatcher's attention today?

The Overview is an operational dashboard rather than an analytics dashboard.

## Wireframe

```text
OVERVIEW

Good morning.
Here's what needs your attention today.


PENDING QUOTES             AWAITING PAYMENT             TODAY'S JOBS

      08                         03                           06


─────────────────────────────────────────────────────────────────────────


TODAY'S SCHEDULE                                         View schedule →

                  08    10    12    14    16    18

Ahmad                   ━━━━━━━ J-104 ━━━━━━━

John              ━━━ J-103 ━━━

Amir                                      ━━━ J-106 ━━━


─────────────────────────────────────────────────────────────────────────


REQUIRES ATTENTION                         RESOURCE STATUS

Q-108   Pending quote                       Drivers
        ABC Trading                         8 Active

Q-105   Awaiting payment                    Trucks
        NexMart Sdn Bhd                     6 Active
                                            2 Maintenance

J-094   Pickup approaching
        Kuala Lumpur → Penang
```

## Primary Information

### Operational Metrics

- Pending quotes
- Accepted quotes awaiting payment
- Today's jobs

### Today's Schedule

Compact preview of driver assignments.

### Requires Attention

Examples:

- Pending quotation
- Accepted quotation awaiting payment
- Upcoming pickup
- Scheduling issue

### Resource Status

Examples:

- Active drivers
- Active trucks
- Trucks under maintenance

## Actions

```text
View Schedule
    → Schedule

Quote Item
    → Quote Detail

Job Item
    → Job Detail
```

## Avoid

Do not add decorative charts without operational value.

Examples to avoid:

- Random revenue chart
- Pie chart
- Donut chart
- Fake percentage growth
- Decorative analytics

---

# 7. Quotes

## Purpose

Manage quotations from creation through acceptance, payment, and job conversion.

## Wireframe

```text
QUOTES

Manage customer quotations from creation to job conversion.

                                                        + New Quote


All 24      Pending 8      Accepted 5      Converted 9      Cancelled 2


─────────────────────────────────────────────────────────────────────────


[ Search quotations... ]                     [ Status ▾ ]     [ Date ▾ ]


QUOTE    CUSTOMER         ROUTE                 PICKUP       PRICE     STATUS

Q-108    ABC Trading      Kuala Lumpur          30 Aug       RM2,200   PENDING
                          → Penang

Q-107    NexMart          Johor Bahru           30 Aug       RM1,850   ACCEPTED
                          → Kuala Lumpur

Q-106    Lim Logistics    Klang                 28 Aug       RM1,400   CONVERTED
                          → Ipoh
```

## Filters

```text
All
Pending
Accepted
Rejected
Cancelled
Converted
```

## Search

Search should support useful identifiers such as:

- Quote ID
- Customer name
- Company name

## Actions

```text
+ New Quote
    → New Quote

Click Quote Row
    → Quote Detail
```

---

# 8. New Quote

## Purpose

Create a freight quotation for an existing customer.

## Wireframe

```text
NEW QUOTE

Create a freight quotation for a customer.


CUSTOMER

Customer
[ Search or select customer...                         ▾ ]


────────────────────────────────────────────────────────────


ROUTE

Pickup Location
[ Kuala Lumpur                                           ]

Drop-off Location
[ Penang                                                 ]


Preferred Pickup Date
[ 30 / 08 / 2026 ]


────────────────────────────────────────────────────────────


PRICING

Quoted Price
RM [ 2,200.00                                             ]


────────────────────────────────────────────────────────────


                                      Cancel     Create Quote
```

## Customer Selection

The quotation must be associated with an existing customer.

If the required customer does not exist:

```text
Can't find the customer?

+ Add Customer
```

The dispatcher can create the customer first and then continue creating the quotation.

## Actions

```text
Cancel
    → Quotes

Create Quote
    → Create quotation
    → Quote Detail
```

---

# 9. Quote Detail

## Purpose

Display quotation information and expose only actions that are valid for the current quotation state.

The frontend must reflect backend business rules.

---

## 9.1 Pending Quote

```text
QUOTE Q-108                                             PENDING

ABC Trading
Created 27 August 2026


ROUTE

Kuala Lumpur
     ↓
Penang


Preferred Pickup
30 August 2026

Quoted Price
RM 2,200.00


──────────────────────────────────────────────────────


PAYMENT

Not available until quote is accepted.


──────────────────────────────────────────────────────


                         Reject Quote       Accept Quote
```

Possible additional action:

```text
Cancel Quote
```

only where permitted by backend rules.

---

## 9.2 Accepted + Unpaid

```text
QUOTE Q-108                                            ACCEPTED

ABC Trading


ROUTE

Kuala Lumpur
     ↓
Penang


Preferred Pickup
30 August 2026

Quoted Price
RM 2,200.00


──────────────────────────────────────────────────────


PAYMENT

Status
UNPAID

Amount
RM 2,200.00


──────────────────────────────────────────────────────


                                      Record Payment
```

`Schedule Job` must not be shown or enabled while the quotation is unpaid.

---

## 9.3 Accepted + Paid

```text
QUOTE Q-108                                            ACCEPTED

...


PAYMENT

Status
PAID

Amount
RM 2,200.00


──────────────────────────────────────────────────────


                                      Schedule Job →
```

---

## 9.4 Converted Quote

```text
QUOTE Q-108                                           CONVERTED

ABC Trading


ROUTE

Kuala Lumpur
     ↓
Penang


──────────────────────────────────────────────────────


PAYMENT

PAID


──────────────────────────────────────────────────────


JOB

J-104

30 August 2026
09:00 → 16:00

Driver
Ahmad Rahman

Truck
VBC 2314


──────────────────────────────────────────────────────


                                         View Job →
```

## State-Based Actions

```text
PENDING
├── Accept
├── Reject
└── Cancel (where permitted)

ACCEPTED + UNPAID
└── Record Payment

ACCEPTED + PAID
└── Schedule Job

CONVERTED
└── View Job

REJECTED
└── No operational conversion action

CANCELLED
└── No operational conversion action
```

---

# 10. Record Payment

## Purpose

Allow the Dispatcher to confirm that payment has been received for an accepted quotation.

V1 records payment status only. It does not implement an external payment gateway.

## Wireframe

```text
RECORD PAYMENT

Quote Q-108
ABC Trading


Quoted Amount
RM 2,200.00


Current Status
UNPAID


────────────────────────────────────────────────────


Confirm that payment has been received for this quote.


                              Cancel     Mark as Paid
```

## Success

After successful payment confirmation:

```text
Payment recorded successfully.
```

The quotation returns to:

```text
ACCEPTED + PAID
```

and `Schedule Job` becomes available.

---

# 11. Schedule Job

## Purpose

Convert an eligible paid quotation into a scheduled freight job.

This is one of the core FleetFlow workflows.

---

## 11.1 Quote Context

```text
SCHEDULE JOB

Quote Q-108 · ABC Trading

Kuala Lumpur → Penang

Quoted Price
RM 2,200.00
```

---

## 11.2 Schedule Selection

```text
────────────────────────────────────────────────────────────


SCHEDULE


Pickup

[ 30 Aug 2026 ]       [ 09:00 ]


Expected Drop-off

[ 30 Aug 2026 ]       [ 16:00 ]


Requested Duration

09:00 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ 16:00
```

Changing either date/time should refresh resource availability.

---

# 12. Driver Availability

## Purpose

Show which drivers are available for the requested job period and why unavailable drivers cannot be selected.

## Wireframe

```text
DRIVER AVAILABILITY


Requested
30 Aug 2026 · 09:00 → 16:00


                 08   09   10   11   12   13   14   15   16   17


Ahmad Rahman          ███████████████████████
                      J-104
                      09:00 → 14:00

                      CONFLICT


John Tan      ███████
             J-103

                               ─────────────────────────────

                               Available


Amir Hassan

             ───────────────────────────────────────────────

                               Available



○ Ahmad Rahman     Unavailable · conflicts with J-104

● John Tan         Available

○ Amir Hassan      Available
```

## Behaviour

Unavailable resources cannot be selected.

Available resources can be selected.

The UI should explain the conflict rather than only displaying:

```text
Unavailable
```

Example:

```text
Conflicts with J-104 · 09:00–14:00
```

## Important Principle

Empty timeline space represents availability.

Do not fill all available time with green.

---

# 13. Truck Availability

## Purpose

Show truck availability for the requested job period.

## Wireframe

```text
TRUCK AVAILABILITY


Requested
30 Aug 2026 · 09:00 → 16:00


                    09   10   11   12   13   14   15   16


VBC 2314 · Volvo FH

────────────────────────────────────────────────────────
Available


WXY 8821 · Scania R

               ███████████████
               J-102
               11:00 → 14:00

Unavailable


ABC 1921 · Volvo FM

────────────────────────────────────────────────────────
Available



● VBC 2314      Available

○ WXY 8821      Conflict · J-102

○ ABC 1921      Available
```

## Additional Resource Rules

A truck may also be unavailable because of its operational status.

Example:

```text
ABC 1921
Unavailable · Maintenance
```

Truck availability therefore depends on:

```text
Truck Status
+
Schedule Conflict
```

---

# 14. Schedule Job Confirmation

After selecting schedule, driver, and truck:

```text
JOB SUMMARY


Quote
Q-108

Route
Kuala Lumpur → Penang

Pickup
30 Aug 2026 · 09:00

Expected Drop-off
30 Aug 2026 · 16:00

Driver
John Tan

Truck
VBC 2314 · Volvo FH


──────────────────────────────────────────────────────


FINAL PRICE

RM [ 2,200.00 ]


──────────────────────────────────────────────────────


                               Cancel     Schedule Job
```

## Submission Behaviour

The backend must perform final conflict validation when the job is submitted.

Frontend availability is informational and should not replace server-side validation.

Possible failure:

```text
Driver unavailable

The selected driver now has another job overlapping
with the requested schedule.

Please select another driver or change the schedule.

[ Review Availability ]
```

This protects against availability changing between selection and submission.

---

# 15. Schedule

## Purpose

Provide a visual overview of driver and truck allocation.

Schedule is one of FleetFlow's primary operational interfaces.

## Primary Controls

```text
[ Drivers ] [ Trucks ]

‹ Previous Day     30 August 2026     Next Day ›

[ Day ] [ Week ]
```

Day view is the V1 priority.

Week view may be implemented after the core Day view is stable.

---

## 15.1 Driver Schedule

```text
SCHEDULE

Plan and review freight resource assignments.


[ Drivers ] [ Trucks ]                       [ Day ] [ Week ]

                            ‹  30 August 2026  ›


──────────────────────────────────────────────────────────────────────────


                  08   09   10   11   12   13   14   15   16   17   18


Ahmad Rahman
Driver #02             ┌────────────────────────────────────┐
                       │ J-104                              │
                       │ Kuala Lumpur → Penang              │
                       │ 09:00 → 16:00                      │
                       └────────────────────────────────────┘


John Tan
Driver #03    ┌───────────────┐
              │ J-103         │
              │ 08:00 → 11:00 │
              └───────────────┘


Amir Hassan
Driver #04                                          ┌───────────────┐
                                                    │ J-106         │
                                                    │ 14:00 → 17:00 │
                                                    └───────────────┘
```

## Behaviour

Each row represents one resource.

Each block represents one job.

The block's horizontal position represents start time.

The block's width represents job duration.

Empty timeline space represents availability.

---

## 15.2 Truck Schedule

Switching to Trucks reuses the same timeline structure.

```text
                  08   09   10   11   12   13   14   15   16   17


VBC 2314
Volvo FH               ┌────────────────────────────────────┐
                       │ J-104                              │
                       │ 09:00 → 16:00                      │
                       └────────────────────────────────────┘


WXY 8821
Scania R      ┌───────────────┐
              │ J-103         │
              └───────────────┘


ABC 1921
Volvo FM
Maintenance
```

A maintenance/inactive truck should be visibly distinguished from a truck that is simply available.

---

# 16. Schedule Job Drawer

## Purpose

Allow the Dispatcher to inspect a scheduled job without leaving the timetable.

Clicking a job block opens a side drawer.

```text
                                      ┌──────────────────────────┐
                                      │ JOB J-104                │
                                      │                          │
                                      │ SCHEDULED                │
                                      │                          │
                                      │ ROUTE                    │
                                      │ Kuala Lumpur             │
                                      │       ↓                  │
                                      │ Penang                   │
                                      │                          │
                                      │ SCHEDULE                 │
                                      │ 30 Aug                   │
                                      │ 09:00 → 16:00            │
                                      │                          │
                                      │ DRIVER                   │
                                      │ Ahmad Rahman             │
                                      │                          │
                                      │ TRUCK                    │
                                      │ VBC 2314 · Volvo FH      │
                                      │                          │
                                      │ PRICE                    │
                                      │ RM 2,200                 │
                                      │                          │
                                      │          View Job →      │
                                      └──────────────────────────┘
```

## Actions

```text
View Job
    → Job Detail
```

---

# 17. Jobs

## Purpose

Monitor scheduled and active freight operations.

## Wireframe

```text
JOBS

Monitor scheduled and active freight operations.


All 18       Scheduled 6       In Progress 3       Completed 8


─────────────────────────────────────────────────────────────────────────


[ Search jobs... ]                          [ Status ▾ ]       [ Date ▾ ]


JOB      ROUTE             DRIVER        TRUCK        PICKUP        STATUS

J-104    KL → Penang       Ahmad         VBC 2314     30 Aug 09:00  SCHEDULED

J-103    Klang → Ipoh      John          ABC 1921     30 Aug 08:00  IN PROGRESS

J-102    Johor → KL        Amir          WXY 8821     29 Aug 10:00  COMPLETED
```

## Filters

```text
All
Scheduled
In Progress
Completed
Cancelled
```

## Search

Potential search fields:

- Job ID
- Customer
- Driver
- Truck registration

## Actions

```text
Click Job
    → Job Detail
```

---

# 18. Job Detail

## Purpose

Display the complete operational state of a freight job.

## Wireframe

```text
JOB J-104                                               SCHEDULED


Kuala Lumpur
      ↓
Penang


────────────────────────────────────────────────────


SCHEDULE

Pickup
30 August 2026 · 09:00

Expected Drop-off
30 August 2026 · 16:00


────────────────────────────────────────────────────


RESOURCES

Driver
Ahmad Rahman
Driver #02

Truck
VBC 2314
Volvo FH


────────────────────────────────────────────────────


COMMERCIAL

Quote
Q-108

Final Price
RM 2,200.00


────────────────────────────────────────────────────


STATUS

SCHEDULED


                                      Start Job
```

## Job Lifecycle Actions

```text
SCHEDULED
    ↓
Start Job

IN_PROGRESS
    ↓
Complete Job

COMPLETED
    ↓
No further operational progression
```

Cancellation should only be exposed where supported by backend business rules.

---

# 19. Customers

## Purpose

Provide lightweight customer management for Dispatchers.

## Wireframe

```text
CUSTOMERS

Manage customer records and quotation history.

                                                     + Add Customer


[ Search customers... ]


────────────────────────────────────────────────────────────────────


CUSTOMER           COMPANY               PHONE               QUOTES

John Lim           JL Trading            +60 ...                4

Sarah Tan          ST Logistics          +60 ...                2

ABC Trading        ABC Trading Sdn Bhd   +60 ...                7
```

## Search

Search by:

- Customer name
- Company
- Phone
- Email where useful

## Actions

```text
+ Add Customer
    → Add Customer

Click Customer
    → Customer Detail
```

---

# 20. Add Customer

## Purpose

Allow a Dispatcher to create a customer record before creating quotations.

## Wireframe

```text
ADD CUSTOMER

Create a customer record.


CUSTOMER INFORMATION


Customer Name
[                                                     ]

Company Name
[                                                     ]


Phone
[                                                     ]

Email
[                                                     ]


Address
[                                                     ]
[                                                     ]


────────────────────────────────────────────────────


                              Cancel     Add Customer
```

Required fields should reflect backend validation.

---

# 21. Customer Detail

## Purpose

Show customer contact information and quotation history.

## Wireframe

```text
ABC TRADING

ABC Trading Sdn Bhd


CONTACT INFORMATION


Phone
+60 ...

Email
operations@abctrading.com

Address
Kuala Lumpur, Malaysia


──────────────────────────────────────────────────────────


QUOTATION HISTORY


QUOTE     ROUTE               PRICE       STATUS

Q-108     KL → Penang         RM2,200     PENDING

Q-091     KL → Johor          RM1,800     CONVERTED

Q-074     Klang → Ipoh        RM1,200     CONVERTED


──────────────────────────────────────────────────────────


                                           + New Quote
```

## Actions

```text
Quote Row
    → Quote Detail

+ New Quote
    → New Quote with customer preselected
```

This page visually represents the relationship:

```text
Customer
    ↓
Quotes
    ↓
Jobs
```

---

# 22. Drivers

## Purpose

Allow the Dispatcher to inspect driver status, licence information, and availability.

Driver management is read-only in V1.

## Wireframe

```text
DRIVERS

View driver availability and licence information.


[ Search drivers... ]


──────────────────────────────────────────────────────────────


DRIVER          LICENCE       EXPIRY          TODAY        STATUS

Ahmad Rahman    D123456       20 May 2027     J-104        ACTIVE

John Tan        D872192       15 Oct 2026     J-103        ACTIVE

Amir Hassan     D192821       08 Feb 2028     Available    ACTIVE
```

## Actions

```text
Click Driver
    → Driver Detail
```

No `Add Driver` button in Dispatcher V1.

---

# 23. Driver Detail

## Purpose

Display driver information and upcoming assignments.

## Wireframe

```text
AHMAD RAHMAN                                           ACTIVE


CONTACT

Phone
+60 ...


LICENCE

Licence Number
D123456

Licence Expiry
20 May 2027


────────────────────────────────────────────────────


TODAY

J-104
Kuala Lumpur → Penang
09:00 → 16:00


────────────────────────────────────────────────────


UPCOMING SCHEDULE

30 AUG      J-104      KL → Penang       09:00 → 16:00

02 SEP      J-119      Klang → Ipoh      08:00 → 12:00


────────────────────────────────────────────────────


                                      View Schedule →
```

## Actions

```text
Job
    → Job Detail

View Schedule
    → Schedule with driver context where supported
```

---

# 24. Trucks

## Purpose

Allow the Dispatcher to inspect fleet availability and operational status.

Truck management is read-only in V1.

## Wireframe

```text
TRUCKS

View fleet availability and operational status.


[ Search trucks... ]


──────────────────────────────────────────────────────────────


REGISTRATION      MODEL          CAPACITY       TODAY       STATUS

VBC 2314          Volvo FH       12,000 kg      J-104       ACTIVE

WXY 8821          Scania R       15,000 kg      J-102       ACTIVE

ABC 1921          Volvo FM       10,000 kg      —           MAINTENANCE
```

## Truck States

Examples:

```text
ACTIVE
MAINTENANCE
INACTIVE
```

## Actions

```text
Click Truck
    → Truck Detail
```

No `Add Truck` button in Dispatcher V1.

---

# 25. Truck Detail

## Purpose

Display vehicle information, operational status, and upcoming assignments.

## Wireframe

```text
VBC 2314                                                ACTIVE

Volvo FH


VEHICLE INFORMATION


Registration
VBC 2314

VIN
XXXXXXXXXXXXXXXXX

Capacity
12,000 kg


────────────────────────────────────────────────────


TODAY

J-104
Kuala Lumpur → Penang
09:00 → 16:00


────────────────────────────────────────────────────


UPCOMING SCHEDULE

30 AUG      J-104       KL → Penang       09:00 → 16:00

03 SEP      J-121       KL → Johor        10:00 → 18:00


────────────────────────────────────────────────────


                                      View Schedule →
```

## Actions

```text
Job
    → Job Detail

View Schedule
    → Schedule with truck context where supported
```

---

# 26. Loading State

Every data-driven page must have a loading state.

Example:

```text
QUOTES

Manage customer quotations.


────────────────────────────────────────────────────

[ Loading row ]
[ Loading row ]
[ Loading row ]
[ Loading row ]
```

Prefer structural loading/skeleton states over disruptive full-page spinners where practical.

---

# 27. Empty State

Every list should define an empty state.

## Example — Quotes

```text
QUOTES


No quotations yet.

Create the first quotation to start managing
a freight request.


                        + New Quote
```

## Example — Schedule

```text
SCHEDULE


No jobs scheduled for 30 August 2026.

All active resources are currently available.
```

---

# 28. Error State

Every API-driven view should handle errors.

Example:

```text
Unable to load quotations.

The quotation data could not be retrieved.

Please try again.


                            Retry
```

The page shell should remain usable when possible.

---

# 29. Resource Conflict State

Scheduling requires a dedicated conflict state.

Example:

```text
Driver unavailable

Ahmad Rahman already has an overlapping job
during the selected period.

J-104
09:00 → 14:00


                       Select Another Driver
```

Truck example:

```text
Truck unavailable

WXY 8821 is already assigned to J-102
during the requested period.


                       Select Another Truck
```

---

# 30. Form Validation State

Forms must show validation close to the affected field.

Example:

```text
Expected Drop-off

[ 30 Aug 2026 ] [ 08:00 ]

Expected drop-off must be later than pickup time.
```

Avoid generic:

```text
Something went wrong.
```

when the backend provides a meaningful validation reason.

---

# 31. Navigation Flow

## Primary Operational Flow

```text
Customers
    │
    ├── Add Customer
    │
    └── Customer Detail
             │
             └── New Quote
                    ↓
                  Quotes
                    ↓
               Quote Detail
                    ↓
                 PENDING
                    ↓
                 ACCEPT
                    ↓
             ACCEPTED + UNPAID
                    ↓
              Record Payment
                    ↓
              ACCEPTED + PAID
                    ↓
               Schedule Job
                    ↓
          Select Date / Time
                    ↓
          Driver Availability
                    ↓
           Truck Availability
                    ↓
              Confirm Job
                    ↓
                  Jobs
                    ↓
               Job Detail
                    ↓
                Schedule
```

---

# 32. Page Relationship Map

```text
                         OVERVIEW
                            │
              ┌─────────────┼──────────────┐
              ↓             ↓              ↓
           QUOTES        SCHEDULE         JOBS
              │                            │
              ↓                            ↓
        Quote Detail                  Job Detail
              │
       ACCEPTED + PAID
              │
              ↓
        Schedule Job
              │
       ┌──────┴──────┐
       ↓             ↓
 Driver Availability  Truck Availability
       │             │
       └──────┬──────┘
              ↓
          Create Job
              │
              ↓
           Schedule


CUSTOMERS ──→ Customer Detail ──→ Quote Detail

DRIVERS ───→ Driver Detail ─────→ Schedule

TRUCKS ────→ Truck Detail ──────→ Schedule
```

---

# 33. Quote State and UI Mapping

```text
PENDING
│
├── Accept
│      ↓
│   ACCEPTED
│      ↓
│   UNPAID
│      ↓
│   Record Payment
│      ↓
│     PAID
│      ↓
│   Schedule Job
│      ↓
│   CONVERTED
│
├── Reject
│      ↓
│   REJECTED
│
└── Cancel
       ↓
    CANCELLED
```

The frontend should not expose actions that violate this lifecycle.

---

# 34. Job State and UI Mapping

```text
SCHEDULED
    │
    │ Start Job
    ↓
IN_PROGRESS
    │
    │ Complete Job
    ↓
COMPLETED
```

Alternative:

```text
SCHEDULED / supported state
        ↓
    CANCELLED
```

Cancellation availability must follow backend business rules.

---

# 35. Schedule Interaction Model

The Schedule should follow this conceptual model:

```text
Resource
   │
   │                 TIME
   │
   │    08 09 10 11 12 13 14 15 16 17
   │
   ├── Driver A   [=======JOB=======]
   │
   ├── Driver B         [===JOB===]
   │
   └── Driver C
```

Core visual channels:

```text
Horizontal position
→ Start time

Block width
→ Duration

Vertical position
→ Resource

Empty space
→ Availability

Job block
→ Occupied period
```

The timetable should prioritize readability over decoration.

---

# 36. Schedule V1 Priority

## Required

- Driver day schedule
- Truck day schedule
- Previous/next date navigation
- Job blocks
- Job detail drawer
- Availability during job scheduling
- Conflict explanation

## Optional After Core V1

- Week view
- Drag-and-drop scheduling
- Current-time indicator
- Advanced filtering
- Resource grouping

Drag-and-drop is specifically not required for the first implementation because backend validation remains the source of truth.

---

# 37. Responsive Behaviour

FleetFlow is primarily a desktop operations system.

Priority:

```text
Desktop
    ↓
Tablet
    ↓
Mobile
```

The scheduling timeline should be optimized for desktop use.

On smaller screens:

- Sidebar may collapse
- Tables may horizontally scroll or simplify
- Detail drawers may become full-screen panels
- Timeline may horizontally scroll

Do not compromise the desktop operational experience solely to force all timetable information onto a narrow mobile screen.

---

# 38. Interaction Principles

Interactions should communicate system state rather than decorate the interface.

Use:

- Row hover
- Button hover
- Focus states
- Drawers
- Modals where appropriate
- Toast feedback
- Loading skeletons
- Status transitions
- Timeline job hover/click

Avoid:

- Parallax
- 3D effects
- Cursor effects
- Animated backgrounds
- Continuous decorative motion
- Excessive page transitions

---

# 39. Core Frontend Components

The wireframes imply the following reusable component structure.

## Application

```text
AppShell
Sidebar
TopBar
PageHeader
```

## Data

```text
DataTable
StatusBadge
Metric
SearchInput
FilterControl
EmptyState
ErrorState
LoadingSkeleton
```

## Forms

```text
Button
Input
Select
DateInput / DatePicker
TimeInput / TimePicker
FormField
ValidationMessage
```

## Overlay

```text
Drawer
Modal
Dropdown
Toast
```

## Scheduling

```text
ScheduleTimeline
TimelineHeader
TimeScale
ResourceRow
JobBlock
AvailabilityTimeline
ResourceSelector
ConflictMessage
```

---

# 40. Backend Data Requirements Identified by Wireframes

The frontend wireframes require more than basic CRUD operations.

Before frontend implementation, the existing Spring Boot API should be reviewed for the following capabilities.

## Customers

Frontend requires:

```text
List customers
Get customer
Create customer
Get customer's quotes
```

## Quotes

Frontend requires:

```text
List quotes
Get quote
Create quote
Update quote status
Record payment
Find converted job
```

## Jobs

Frontend requires:

```text
List jobs
Get job
Create job from quote
Update job status
Filter jobs by date/status
```

## Drivers

Frontend requires:

```text
List active drivers
Get driver
Get driver schedule
```

## Trucks

Frontend requires:

```text
List trucks
Get truck
Get truck schedule
Filter operational status
```

## Schedule

The timetable requires data similar to:

```text
GET jobs for a specific date
+
Driver assignment
+
Truck assignment
+
Start datetime
+
End datetime
+
Job status
```

## Availability

Job scheduling requires the frontend to understand:

```text
Which drivers are available?
Which drivers conflict?
Which job causes each conflict?

Which trucks are available?
Which trucks conflict?
Which job causes each conflict?
Which trucks are unavailable because of fleet status?
```

Existing backend conflict-count methods may be sufficient for final validation but may not provide enough information for the visual timetable and availability interface.

---

# 41. Backend Remains Source of Truth

The frontend may display availability before job creation.

However:

```text
Frontend availability
        ↓
Improves UX

Backend validation
        ↓
Guarantees correctness
```

Final scheduling must always be validated by the backend.

The frontend must not assume that a resource remains available simply because it was available when the page first loaded.

---

# 42. V1 Seed Data Assumption

FleetFlow V1 assumes that initial operational resources already exist.

Development/demo data may include:

```text
Dispatcher
Drivers
Trucks
Customers
```

These may initially be populated through database seed/sample data.

Dispatcher V1 can create customers through the UI.

Driver and truck creation are outside the Dispatcher scope and may be introduced in a future Admin module.

---

# 43. Future Role Expansion

The current architecture should allow future role-specific interfaces without requiring them in V1.

## Future Admin Module

Potential capabilities:

```text
Employee management
Driver registration
Driver activation/deactivation
Truck registration
Fleet status management
Role management
System administration
```

## Future Customer Portal

Potential capabilities:

```text
Customer authentication
Request quotation
View quotation
Accept/reject quotation
Make payment
View job status
Track freight progress
Request cancellation
```

These features should not be represented as fake or inactive pages in Dispatcher V1.

---

# 44. Implementation Priority

Frontend implementation should follow vertical slices rather than generating the entire frontend at once.

## Phase 1 — Foundation

```text
Application Shell
Sidebar
Routing
Design tokens
Shared components
```

## Phase 2 — Customer & Quote Workflow

```text
Customers
Add Customer
Customer Detail
Quotes
New Quote
Quote Detail
Payment
```

## Phase 3 — Scheduling

```text
Schedule Job
Driver Availability
Truck Availability
Schedule Timeline
Job Drawer
```

## Phase 4 — Job Operations

```text
Jobs
Job Detail
Job status transitions
```

## Phase 5 — Resource Views

```text
Drivers
Driver Detail
Trucks
Truck Detail
```

## Phase 6 — Overview

Build Overview after the core operational pages exist so that its metrics and schedule preview are based on real system data rather than placeholder dashboard content.

---

# 45. Design Principle

When implementing these wireframes:

> FleetFlow should behave like an operational management system, not a marketing website.

Prioritize:

```text
Clarity
Information hierarchy
Operational efficiency
State visibility
Consistency
Low visual noise
```

The final visual treatment is defined in `frontend-design.md`.

The wireframes in this document remain the primary reference for page structure, information placement, navigation, and operational interaction.