# Release 2.0.1

## Goal

Fix the bugs found while documenting 2.0.0 (#501, #502, #504 to #510), clear the Dependabot alerts (#500) and validate the Velocity templates in CI, then release 2.0.1.

## Context

- The docs review (2026-10-11) checked every property against the code and found the bugs below; each one has its own issue.
- `dependabot-alerts.md` holds the Dependabot part; the template check is done (`./gradlew checkTemplates`, CI job `templates`).

## Changes

| Issue | Fix | Verified by |
| --- | --- | --- |
| #501 | `iconFile` is used when no platform icon is set and it has the target platform's format (`.png`, `.icns`, `.ico`); otherwise a warning. Forwarded by the Gradle `PackageTask`. | Code review |
| #502 | Gradle `PackageTask` forwards `zipballName` and `tarballName`; Gradle `CreateZipball` appends `.zip` like Maven. | Code review |
| #504 | `msiUpgradeCode` defaults to a name-based UUID (organization and name), logged; `fileVersion`/`productVersion` default to `version` as `x.x.x.x` (or `1.0.0.0` with a warning if it doesn't fit an MSI version); `AllowSameVersionUpgrades="yes"`. | jshell test of the version conversion; Windows smoke tests build the MSI |
| #505 | Linux `compareVersions()` compares the leading digits of each part numerically, `1.x` as `x`; wrapped in `#[[ ]]#`. | `checkTemplates` (CI job `templates`) runs 8 cases (17 vs 8, 9 vs 11, 1.8 vs 8...) on the rendered script |
| #506 | `GDK_BACKEND=x11` in the `pkexec` launch. | Code review |
| #507 | The Linux script `exec`s Java instead of `exit 0`. | Linux smoke test: `--exit=3` returns 3 (the sample exits with the code given) |
| #508 | macOS bundle permission rules use `${jreDirectoryName}.jre` (Gradle tasks and `mac/assembly.xml.vtl`); Gradle also sets 755 on `lib/jspawnhelper`. | macOS smoke test: `bin/java` and `jspawnhelper` are 755 in the tarball |
| #509 | Notarization zip built with `ditto -c -k --keepParent`; a blank `keyChainProfile` fails early. | Code review (notarization needs an Apple account) |
| #510 | `WindowsSigner` passes the keystore to jsign only when set. | Code review |

## Acceptance

- Smoke tests green on every platform with the new checks.
- Each issue gets a "fixed in devel" comment and the `fixed` label; closed when 2.0.1 is released.
- The release notes list the behaviour changes: `iconFile` now applies, MSI upgrade code and Windows versions derived from the app.
