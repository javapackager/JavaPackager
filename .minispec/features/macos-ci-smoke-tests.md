# macOS smoke tests in GitHub Actions

## Goal

Package a sample app on GitHub's Apple Silicon macOS runners and check the result, so macOS issues can be verified without owning a Mac.

## Context

- There are no tests and no CI workflows (`.github/` only has issue templates).
- The maintainer has no Mac. Several open issues can only be checked on macOS:
  - #448 and #449: launcher fails or runs under Rosetta on ARM / macOS 15. Possibly fixed by the native launcher in devel (f02c0b0), but unconfirmed.
  - #473 and #398: with `administratorRequired=true`, `MacPackager.processStartupScript()` never copies the launcher into `Contents/MacOS`.
  - #474 and #377: `GenerateDmg` always runs `osascript` to customize the DMG, which fails on headless CI.
- GitHub `macos-14` and `macos-15` runners are arm64 and free for public repos. Docker can't run macOS.
- Output layout: app at `target/<name>/<name>.app`, DMG at `target/<name>_<version>.dmg`, PKG at `target/<name>_<version>.pkg`.

## Changes

- Add a minimal Maven sample app under `samples/hello-world/` that prints a known line and exits. It takes the plugin version as a property, so CI uses the snapshot built from the repo.
- Add the workflow `.github/workflows/smoke-tests.yml` (job `macos`):
  - Triggers: push and pull request on `devel` and `master`, plus manual dispatch.
  - Builds the plugin with `./gradlew publishToMavenLocal`.
  - Matrix over `macos-14` and `macos-15` and `macStartup` (`UNIVERSAL`, `ARM64`, `SCRIPT`), with one extra job using `administratorRequired=true`.
  - Packages the sample with `mvn package`, with `bundleJre=true` and installers enabled.
- Checks per job:
  - The `.app` exists and `Contents/MacOS/universalJavaApplicationStub` is present and executable.
  - For compiled launchers, `file`/`lipo` reports arm64.
  - Running the launcher directly prints the sample's known line.
  - The DMG and PKG exist. A DMG failure is reported, not hidden.
- Upload `target/*.dmg`, `target/*.pkg` and the build log as artifacts.
- Known failures are allowed: `continue-on-error` on the `administratorRequired` job until #473/#398 is fixed.

## Acceptance

- The workflow runs on `devel` and its results are visible in the Actions tab.
- `UNIVERSAL`, `ARM64` and `SCRIPT` jobs show whether the app launches natively on ARM, which gives an answer for #448/#449.
- The `administratorRequired` job reproduces #473/#398 (missing launcher) before the fix and passes after it.
- The DMG step shows whether `osascript` fails headless on the runner (#474/#377).
- No changes to plugin code in this feature; fixes go in their own bugfix notes.
