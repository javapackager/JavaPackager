---
name: minispec-adr
description: Record an architectural decision as a new ADR under .minispec/decisions/ from the ADR template. Use when an important, hard-to-reverse decision is made.
---

# MiniSpec: new ADR

Record a permanent architectural decision under `.minispec/decisions/`.

## Steps

1. Read `.minispec/README.md` for the standard.
2. Read the template `.minispec/templates/adr.md`.
3. Determine the next sequential number: list `.minispec/decisions/`, find the
   highest `ADR-NNN-*` and add 1. Numbers are never reused or renumbered.
4. Create `.minispec/decisions/ADR-NNN-<slug>.md` with `<slug>` in kebab-case.
5. Fill the three sections:
   - **Decision** — what was decided, one or two sentences.
   - **Motivation** — why; what problem it solves, what was rejected.
   - **Consequences** — what it commits us to; trade-offs, follow-ups.

## Rules

- One page. No more. Document the *why*, not the implementation.
- Only record decisions that are important and worth not re-litigating. Don't
  write an ADR for plain stack facts.
- If the decision changes a rule, also update `.minispec/core/conventions.md`.
