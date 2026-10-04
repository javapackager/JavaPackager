# Dependency updates

## Goal

Make Dependabot see the real Gradle dependencies and update the outdated ones within their current major line.

## Context

- Dependabot alerts are enabled, but GitHub's dependency graph only sees `src/it/simple-it/pom.xml` (2 packages), none of `build.gradle`. Zero alerts means it isn't looking, not that everything is safe.
- There is no `.github/dependabot.yml`, so there are no version update PRs.
- Many dependencies are several releases behind (checked against Maven Central and the Gradle Plugin Portal on 2026-10-04). `commons-compress` 1.21 is likely affected by DoS CVEs fixed in 1.26.0 (CVE-2024-25710, CVE-2024-26308), to be confirmed by Dependabot.
- Constraints: Java 8 compatibility (conventions); `maven-plugin-api` sets the minimum Maven version users need; Maven artifacts are `provided` in the POM (see `fix-maven-provided-scope.md`).

## Changes

Step 1: Dependabot

- Workflow `.github/workflows/dependency-submission.yml` with `gradle/actions/dependency-submission` on push to `devel` and `master`.
- `.github/dependabot.yml`: weekly updates for `gradle` (root), `github-actions` and `maven` (`samples/hello-world`).

Step 2: updates within the same major line (one commit, validated by the smoke tests)

| Dependency | From | To |
| --- | --- | --- |
| `commons-compress` | 1.21 | 1.28.0 |
| `commons-io` | 2.16.1 | 2.22.0 |
| `commons-lang3` | 3.11 | 3.21.0 |
| `commons-collections4` | 4.3 | 4.6.0 |
| `velocity-engine-core` | 2.3 | 2.4.1 |
| `jdeb` | 1.9 | 1.14 |
| `mojo-executor` | 2.3.0 | 2.4.1 |
| `plexus-utils` | 3.5.1 | 3.6.2 |
| `launch4j` (Gradle library) | 3.0.5 | 3.0.7 |
| `launch4j-maven-plugin` (pinned in `maven/CreateWindowsExeLaunch4j`) | 2.4.1 | 2.7.0 (#465) |
| `maven-plugin-plugin` / `maven-plugin-annotations` | 3.9.0 / 3.6.0 | 3.16.0 |
| `maven-embedder`, `maven-compat` (descriptor generation only) | 3.6.0 | latest 3.9.x |
| `wagon-http`, `wagon-provider-api` | 3.4.1 | 3.5.3 |
| `slf4j-simple` (descriptor generation only) | 1.7.30 | 1.7.36 |
| `de.undercouch.download` | 5.0.4 | 5.7.0 |

- Remove `junit` (there are no tests).
- Keep `maven-plugin-api` at 3.6.0 unless there's a reason to raise the minimum Maven version.

## Out of scope (follow-ups, one note each)

- `jsign-core` 6.0 → 7.x: major; check API changes and Java 8 support.
- `launch4j` (Gradle library) 4.0.0: major.
- `com.gradle.plugin-publish` 1.1.0 → 2.x: major.
- Gradle wrapper 8.14.3 → 9.x: needs Java 17 to run Gradle.
- `io.codearte.nexus-staging`: remove when releases move to the Central Portal (`release-2-0-0.md`).
- `redline` 1.2.10: already the latest but looks unmaintained; consider a replacement.

## Acceptance

- GitHub's dependency graph lists the `build.gradle` dependencies, and Dependabot alerts reflect them.
- Dependabot opens version update PRs for Gradle, GitHub Actions and the sample.
- Every dependency in the step 2 table is updated, the build compiles for Java 8, and the smoke tests pass on all platforms with no "wrong scope" warning.
