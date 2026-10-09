---
name: triage-issues
description: Triage the open GitHub issues of javapackager/JavaPackager (all, or a given list) against the devel branch, using parallel issue-triager subagents, and present them grouped by what to do next. Read-only: it never comments on, labels or closes issues.
---

# Triage issues

## Steps

1. Get the issues: the numbers the user gave, or all open ones:

   ```bash
   gh issue list --repo javapackager/JavaPackager --state open --limit 300 --json number,title,labels,updatedAt --jq '.[] | "\(.number)\t\(.updatedAt[:10])\t\([.labels[].name]|join(","))\t\(.title)"'
   ```

2. Split them into batches of about 13 and launch one `issue-triager` subagent per batch, **all in the same message** so they run in parallel, in the background. Put the issue numbers literally in each prompt (e.g. "Triage these issues: 484, 483, 482"); the agent already knows the categories and output format.
3. Before presenting results, double-check any claim of a regression or a "fixed in devel" that matters, by looking at the code yourself.
4. Present the result grouped by action, not by category:
   - Fixed in `devel`: close when released (candidates for `issue-update`).
   - Small fixes: table with issue, problem, file, and which OS is needed to verify.
   - Answer, close or ask for info.
   - Features: candidates for `/minispec-feature`.
   - Side findings (regressions), first and highlighted.
5. Propose next steps. Don't comment, label or close anything: that's `issue-update`, and only with the user's go-ahead.
