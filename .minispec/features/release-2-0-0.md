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
- 2026-10-04: PR #489 (Till Seifert) cherry-picked as 656bd94 and closed; PR #487 (Jacob, draft) commented, its install-and-open macOS test ported in 168e68c, asked Jacob to close it.
- 2026-10-04: issue cleanup. Closed as answered or stale: #436, #435, #368, #381, #403, #382, #338. Duplicates: #377 → #474, #371 → #338, #404 → #384, #336 → #334. `wontfix`: #240, #247, #199. Removed stale `working on` from #229, #216, #345, #344, #431. #444 fixed (77047b7), labelled `fixed`. #439 (jdeps split packages since 1.7.6) left open: possible real bug.
- 2026-10-04: #439 fixed (b554f4a), asked to test `2.0.0-SNAPSHOT` build 7 (label `feedback`).
- 2026-10-04: no Gradle Plugin Portal publishing (1.x never was there either): Gradle users get the plugin and its marker from Maven Central, as the README explains. `releaseToCentral` rehearsed with a local, uncommitted `2.0.0-rc1`: signed, uploaded, HTTP 200; the user published it: `2.0.0-rc1` is on Maven Central (plugin and Gradle marker), tag `v2.0.0-rc1` on 860a415 and a GitHub pre-release. Asked #448, #449, #389, #398, #369, #482, #393, #439 to test it.
- 2026-10-04: #483 (shared JRE for several EXEs) answered with a tested workaround (Why launcher + `why-ini.vtl` override with `jvm_install=../jre`; backslashes fail), label `feedback`; kept open as an enhancement for 2.1 (external JRE option for the three Windows launchers).
- 2026-10-05: labels tidied (`enhancement` on #465, #345, #344, #384; `working on` removed from #398); #431 kept open as an `enhancement` (deep signing of native libraries inside dependency JARs, promised in the thread) instead of being closed.
- 2026-10-05: PR #491 (Till Seifert, `additionalJlinkArgs`) tested on branch `pr/491` with JDK 11, 17, 21, 25: arguments applied, the user's `--compress` wins, `--include-locales` needs `jdk.localedata` in `additionalModules`, `--compress=zip-N` only on JDK 21+. To merge, plus a README note. It revealed a customized JRE failing on JDK 24+ without jmods (`fix-jdk24-no-jmods`).

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
