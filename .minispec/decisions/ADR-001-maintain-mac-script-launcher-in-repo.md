# ADR-001: Maintain the macOS script launcher in this repo

## Decision

`src/main/resources/mac/universalJavaApplicationStub.sh`, the launcher used with `macStartup=SCRIPT`, is maintained directly in JavaPackager. It is no longer downloaded from the `fvarrui/universalJavaApplicationStub` fork: the `updateUniversalJavaApplicationStub` Gradle task is removed and the fork is archived.

## Motivation

- The fork existed to merge upstream changes from `tofi86/universalJavaApplicationStub`, which is deprecated. Nothing will come from upstream any more.
- Since the migration to the native launcher (`nativeJavaApplicationStub*`), only the `.sh` script was still used. The download task also fetched three compiled binaries that are no longer needed.
- Fixes to the script (e.g. `appArgs` with spaces, #305) took two repos, a fork release and a download. Now they take one commit, verified by the macOS `SCRIPT` smoke test jobs on the same push.
- Rejected: keeping the fork (extra process with no upstream to merge) and patching the downloaded file locally (the next download would overwrite it).

## Consequences

- The script is edited here like any other resource, and its MIT license header (Copyright Tobias Fischer) must be kept. A header line records that it has been maintained here since 1.7.7.
- Any fix to the script must be checked by the `SCRIPT` jobs of `.github/workflows/smoke-tests.yml`.
- Known limitation still open: `VMOptions` are read as a single string, so a `vmArg` containing spaces is split in `SCRIPT` mode.
- Follow-up, not decided here: whether `macStartup` should default to `UNIVERSAL` instead of `SCRIPT`.
