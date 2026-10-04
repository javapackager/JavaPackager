# vmArgs with spaces break the Windows EXE (Launch4j)

## Problem

On Windows, a `vmArg` containing spaces (e.g. `-Dsmoke.prop=hello world`) breaks the Launch4j EXE: `Error: Could not find or load main class world`. Found by the `windows` smoke test job (run 37168023674) after adding that `vmArg` to the sample.

## Cause

Launch4j joins the `<opt>` values into a single command line without quoting them, so the JVM receives `-Dsmoke.prop=hello` and treats `world` as the main class. Both `maven/CreateWindowsExeLaunch4j` and `gradle/CreateWindowsExeLaunch4j` passed `vmArgs` as they were.

## Solution

`AbstractCreateWindowsExe.quoteVmArgs()` wraps in double quotes every VM arg that contains whitespace and isn't already quoted. Both Launch4j generators (Maven and Gradle) use it.

## Verification

- Logic compiled with `--release 8` and tested locally: `-Xmx256m` unchanged, `-Dsmoke.prop=hello world` quoted, already-quoted args unchanged.
- Pending: the `windows` smoke test job must print `smoke.prop=hello world`.
- Not covered: the `winrun4j` and `why` EXE tools (CI only tests the default, `launch4j`).
