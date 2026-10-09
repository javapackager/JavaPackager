# Customized JRE fails for apps without dependencies

## Problem

On `devel`, with `bundleJre=true`, packaging an app that has no dependencies fails in `jlink`: `FindException: Module Warning: Path does not exist: .../libs java.base not found`. Found by the macOS smoke tests (run 37165281699, all 7 jobs). Affects every platform, not only macOS.

## Cause

Commit 7311b4d (the fix for #421) passes `libsFolder` and the JAR to `jdeps` as arguments. With no dependencies, `libs/` is never created. `jdeps` then prints `Warning: Path does not exist: <libs>` on stdout. `BundleJre.getRequiredModules()` reads that line as part of the module list, and the warning text reaches `jlink --add-modules`.

## Solution

In `BundleJre.getRequiredModules()`, a missing `libsFolder` is set to `null`. It is then left out of the `jdeps` call (`Commandline` skips `null` arguments) and of the module path (`getModulePaths()`).

## Verification

- Reproduced with a local `jdeps` (JDK 22): a non-existent path argument prints `Warning: Path does not exist: ...` on stdout; without it the output is clean.
- Could not compile locally (Gradle can't reach Maven Central from this environment).
- Pending: the macOS smoke tests must get past "Package sample app".
- #421 (missing modules) must stay fixed: apps with dependencies still pass `libs/` to `jdeps`.
