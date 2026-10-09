# MiniSpec v1.0

A lightweight specification standard for AI-assisted software development.

MiniSpec separates **permanent knowledge** (what the project *is*) from
**temporal knowledge** (what is *being worked on*). The goal is to give an AI
agent the minimum context it needs — and nothing more.

## Reading contract (for the agent)

**Always read first:**

- `core/project.md`
- `core/conventions.md`
- `core/principles.md`

**Read on demand:**

- Touching architecture → `core/architecture.md`
- Unsure about the stack → `core/stack.md`
- Domain terms → `core/glossary.md`
- Working on a feature → the matching `features/*.md`
- Structural decision → the relevant `decisions/ADR-*.md`

**Negative rule:** do not read anything you don't need for the task.

## Structure

```
.minispec/
├── README.md          this file — the reading contract
├── core/              PERMANENT knowledge (rarely changes)
├── decisions/         ADRs — important decisions (permanent)
├── features/          TEMPORAL knowledge — work in progress only
└── templates/         templates for new documents
```

## Writing rules

1. **Hard limit: 100 lines per document.** `project.md` ≤ 50, `principles.md` ≤ 20.
2. Short sentences and lists. No filler prose.
3. One document = one topic. Never mix permanent and temporal knowledge.
4. Don't explain general technologies or concepts — only what is specific to
   this project.
5. Don't duplicate what the code already says. Document the *why*, not the *how*.

## Document formats

- **Feature** — `Goal / Context / Changes / Acceptance`
- **ADR** — `Decision / Motivation / Consequences`
- **Bugfix** — `Problem / Cause / Solution / Verification`

## Naming

- Core: fixed names (`project.md`, `architecture.md`, `stack.md`,
  `conventions.md`, `glossary.md`, `principles.md`).
- ADR: `ADR-NNN-slug.md`, sequential, never renumbered.
- Feature / Bugfix: `slug.md` (kebab-case).

## Feature lifecycle

A feature file describes **work in progress only**. When a feature is done:

1. Promote anything **permanent** it revealed (a decision → ADR, a rule →
   `conventions.md`, a term → `glossary.md`).
2. **Delete** the feature file.

If `features/` is empty, nothing is in flight.

## Maintenance

- Changing any rule in this standard bumps the version declared at the top.
- Keep documents small. When in doubt, cut.
