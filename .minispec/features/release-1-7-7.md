# Release 1.7.7

## Goal

Publish 1.7.7 from `devel`, with release notes that warn about the breaking changes.

## Context

- `devel` has many fixes since 1.7.6, found or verified by the smoke tests (see the `fix-*` notes).
- Releases still use OSSRH (`nexus-staging` plugin), which was shut down. Snapshots already go to the Central Portal.

## Changes

- Migrate release publishing to the Central Portal.
- Write the release notes. Breaking changes first:
  - `groupId`, Java packages and Gradle plugin id are now `io.github.javapackager` (ADR-003).
  - `macStartup` defaults to `UNIVERSAL`; `<macStartup>SCRIPT</macStartup>` restores the old launcher (ADR-002).
- Close the issues labelled `fixed` once the release is out.

## Acceptance

- `io.github.javapackager:javapackager:1.7.7` is on Maven Central.
- The release notes list the breaking changes and the fixed issues.
