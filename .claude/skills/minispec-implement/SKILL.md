---
name: minispec-implement
description: List pending MiniSpec features (.minispec/features/) and implement the chosen one following the MiniSpec reading contract. Use when the user wants to turn a feature spec into code. Accepts an optional feature slug as argument.
---

# MiniSpec: implement a feature

Turn a MiniSpec feature spec into code. MiniSpec is not a codegen pipeline: this
skill just loads the right context and implements the feature like any task.

## Steps

### 1. Pick the feature

- The files in `.minispec/features/` are the **pending / in-progress** work
  (MiniSpec's temporal knowledge). Each `*.md` is a candidate; `fix-*.md` are
  bugfix notes.
- If the user named a feature (argument or message), use that one. Match by slug
  (filename without `.md`); if ambiguous or not found, list and ask.
- If no feature was named, **list the features** with a one-line summary of each
  (read each file's `# title` and `Goal`) and **ask the user which to
  implement**. Do not guess.
- If `.minispec/features/` is empty, tell the user there's nothing pending and
  offer to create one with `/minispec-feature`.

### 2. Load context (reading contract)

Read `.minispec/README.md`, then follow its reading contract:

- Always: `core/project.md`, `core/conventions.md`, `core/principles.md`.
- The chosen `features/<slug>.md`.
- On demand only: `core/architecture.md`, `core/stack.md`, `core/glossary.md`,
  and any `decisions/ADR-*.md` the feature touches.

Do not read the whole `.minispec/` — only what the task needs.

### 3. Implement

- Write code that satisfies the feature's **Changes**, respecting
  `conventions.md` and `principles.md`.
- Stay within scope: no refactors outside the feature (a principle).

### 4. Verify

- Check the result against the feature's **Acceptance** section, point by point.
- Report what passes and what doesn't.

### 5. Close the feature (remind, don't auto-delete)

When the feature is done and verified, MiniSpec says to:

1. Promote anything **permanent** it revealed — a decision → ADR
   (`/minispec-adr`), a rule → `core/conventions.md`, a term → `core/glossary.md`.
2. Then **delete** `features/<slug>.md`.

Tell the user this and offer to do it. Do not delete without confirmation.

## Rules

- Don't invent requirements not in the spec; if the spec is thin, ask before
  coding.
- This skill chooses and implements; it does not rewrite the spec.
