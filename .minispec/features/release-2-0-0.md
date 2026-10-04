# Release 2.0.0

## Goal

Publish 2.0.0 from `devel`, with release notes that warn about the breaking changes.

## Context

- Major version (after 1.7.6) because of the breaking changes below: users must edit their build to upgrade.
- `devel` has many fixes since 1.7.6, found or verified by the smoke tests (see the `fix-*` notes).
- Release publishing migrated to the Central Portal: `./gradlew releaseToCentral` uploads through the OSSRH Staging API in `user_managed` mode, then the release is published manually in the Portal. Not tried yet with a real release.
- Draft GitHub release `v2.0.0` created, targeting `devel`.
- 2026-10-04: issues #305, #421, #463, #470, #473, #477 commented and labelled `fixed`; #448, #449, #389, #398 asked to test `2.0.0-SNAPSHOT` (label `feedback`).
- 2026-10-04: #484 commented and labelled `fixed`; #369 asked to test `2.0.0-SNAPSHOT` build 5 (label `feedback`).
- 2026-10-04: answered and closed #380, #452 (not planned), #375, #466 (answered), #362, #408 (duplicates of #266); #393 and #482 answered and labelled `feedback` (#393: close if no reply in a few weeks).

## Changes

- Set up release signing (GPG key in `~/.gradle/gradle.properties`, public key on a keyserver) and run `releaseToCentral`.
- Wait for feedback on the macOS issues and #369 before publishing.
- Write the release notes. Breaking changes first:
  - `groupId`, Java packages and Gradle plugin id are now `io.github.javapackager` (ADR-003).
  - `macStartup` defaults to `UNIVERSAL`; `<macStartup>SCRIPT</macStartup>` restores the old launcher (ADR-002).
- Close the issues labelled `fixed` once the release is out.

## Acceptance

- `io.github.javapackager:javapackager:2.0.0` is on Maven Central.
- The release notes list the breaking changes and the fixed issues.
