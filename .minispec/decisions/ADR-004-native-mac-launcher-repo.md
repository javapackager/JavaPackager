# ADR-004: Native macOS launcher source in its own repo

## Decision

The source of the native macOS launcher (`src/nativeStub.m`, `Makefile`) stays in [javapackager/nativeJavaApplicationStub](https://github.com/javapackager/nativeJavaApplicationStub), the former `universalJavaApplicationStub` fork, renamed and reduced to the native launcher. JavaPackager bundles the binaries of one of its releases in `src/main/resources/mac` (`nativeJavaApplicationStub`, `.arm64`, `.x86_64`) and refreshes them with `./gradlew updateNativeJavaApplicationStub` (version in `build.gradle`).

## Motivation

- ADR-001 archived the fork assuming only the Bash script was left there, but the native launcher, the default since 2.0.0 (ADR-002), is only written there. Archiving it left the default launcher without a maintained source (pointed out by Jacob Burroughs, its author).
- A separate repo keeps the Objective-C build (clang, `lipo`, macOS runner) out of the plugin's Java build and gives the launcher its own issues and releases, like `JavaLauncher.exe` from AstroImageJ/Why.
- Rejected: moving the Objective-C source into JavaPackager and compiling it in the smoke tests. One repo, but it mixes two builds and the binaries would change on every plugin build.

## Consequences

- A launcher fix takes a release in `nativeJavaApplicationStub` (its "Compile and release" workflow), a version bump in `updateNativeJavaApplicationStub`, the task run and a JavaPackager commit; the macOS smoke tests (`UNIVERSAL`, `ARM64`) check it.
- The bundled binaries must match a release; they are never built or edited locally. As of 2.0.0 they are release `20251202.010109`.
- The repo is still in tofi86's fork network until someone uses "Leave fork network" in its settings.
