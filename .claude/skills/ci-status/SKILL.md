---
name: ci-status
description: Wait for the latest smoke tests run (or a given run id) of javapackager/JavaPackager and summarize it per job, with the PASS/FAIL checks and errors of failed jobs. Use after pushing to devel, or when the user asks how CI is going.
---

# CI status

The smoke tests (`.github/workflows/smoke-tests.yml`) package `samples/hello-world` on GNU/Linux, Windows and macOS (every `macStartup`, plus an `administratorRequired` job) with Maven, and once per platform with Gradle (output in `build/` instead of `target/`), and run `.github/scripts/check-*-app.sh`.

## Steps

1. Run the script in the background (it waits until the run completes) and tell the user you'll report when it finishes:

   ```bash
   bash .claude/skills/ci-status/ci-status.sh [run-id]
   ```

   Without a run id it takes the latest `smoke-tests.yml` run on `devel`. Right after a push, wait ~20 s first so the new run exists.
2. If the user asks before it finishes, check job status with `gh run view <id> --repo javapackager/JavaPackager --json jobs --jq '.jobs[] | "\(.name)\t\(.status)\t\(.conclusion)"'`; don't guess.
3. Report as a table or list: run link, conclusion, and each failed job with its failed step and the relevant `FAIL:`/error lines.

## Reading failures

- Failure in "Build and install plugin" or "Package sample app": a build or plugin error. Look for `[ERROR] Failed`, `Caused by`, `Lexical error` (Velocity templates), `NoSuchMethodError` (dependency version mismatch between Maven and Gradle).
- Failure in "Check generated app": read the `FAIL:` lines; the app output (`args=`, `smoke.prop=`, `os.arch=`) is printed just before.
- `hdiutil ... Resource busy` on macOS is a known intermittent runner issue; `GenerateDmg` retries it. Report it, but don't treat a single occurrence as a regression.
- `error connecting to api.github.com` comes from the local network (flaky DNS), not from CI: retry.
