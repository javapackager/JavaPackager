---
name: minispec-feature
description: Create a new MiniSpec feature document under .minispec/features/ from the feature template. Use when starting work on a feature, refactor, or migration that needs a short spec.
---

# MiniSpec: new feature

Create a temporal feature document under `.minispec/features/`.

## Steps

1. Read `.minispec/README.md` to honor the standard (writing rules, formats).
2. Read the template `.minispec/templates/feature.md`.
3. Derive a kebab-case slug for the feature (e.g. `github-sync`). The file is
   `.minispec/features/<slug>.md`. If it already exists, update it instead of
   overwriting blindly.
4. Fill the four sections (Goal / Context / Changes / Acceptance):
   - **Goal** — one sentence.
   - **Context** — current state: what exists, what's missing, why now.
   - **Changes** — short bullet list of the work.
   - **Acceptance** — observable done conditions.
5. Base content on the real code and the user's description. Don't invent.

## Rules

- Max 100 lines. Short sentences and lists. No filler.
- This is TEMPORAL knowledge: describe work in progress only.
- Reminder for later: when the feature is done, promote anything permanent it
  revealed (a decision → ADR, a rule → conventions, a term → glossary) and then
  DELETE the feature file. Mention this to the user; do not delete now.
