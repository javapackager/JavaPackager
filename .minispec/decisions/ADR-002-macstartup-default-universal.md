# ADR-002: macStartup defaults to UNIVERSAL

## Decision

Since 1.7.7, `macConfig.macStartup` defaults to `UNIVERSAL` (the native launcher `nativeJavaApplicationStub`, a universal x86_64 + arm64 binary) instead of `SCRIPT` (`universalJavaApplicationStub.sh`).

## Motivation

- The native launcher runs the app natively on Apple Silicon. Several issues about Rosetta and launch failures on macOS 15 (#448, #449, #389) are tied to the old launchers.
- The macOS smoke tests verify the native launcher on `macos-14` and `macos-15` (arm64): the app runs as `aarch64` and receives `appArgs` and `vmArgs`.
- `SCRIPT` relies on a bash script parsing `Info.plist` with `PlistBuddy`, which has had quoting bugs (args and VM options with spaces) and no upstream any more (ADR-001).
- Rejected: keeping `SCRIPT` as default to avoid a behavior change. Most users never set `macStartup` and would miss the native launcher.

## Consequences

- Behavior change for users who don't set `macStartup`: it must be listed in the 1.7.7 release notes, explaining that `<macStartup>SCRIPT</macStartup>` restores the old launcher.
- `SCRIPT` stays supported and tested in CI.
- The launcher file keeps the name `Contents/MacOS/universalJavaApplicationStub`, so custom templates that reference it keep working.
