# appArgs ignored on macOS (#305)

## Problem

On macOS, `appArgs` never reach the app (`args=[]`) with any launcher (`UNIVERSAL`, `ARM64`, `SCRIPT`). Found by the macOS smoke test jobs (run 37166814791).

## Cause

Commit 3818d4a (#305) put the `Arguments` key in `mac/Info.plist.vtl` at the top level of the plist, after the `JavaX` dictionary is closed. Both launchers read it as `:JavaX:Arguments`: `universalJavaApplicationStub.sh` through `plist_get_java`, and `nativeJavaApplicationStub` from the same dictionary as `MainClass` and `ClassPath`.

## Solution

The `Arguments` array is moved inside the `JavaX` dictionary, after `RelocateJar`.

## Verification

- Rendered the template locally with Velocity 2.3 and parsed the result with Python `plistlib`: `JavaX.Arguments = ['--foo', 'hello world']`, with no top-level `Arguments`.
- Pending: the macOS smoke test jobs must print `args=[--foo, hello world]`.
- When this passes, #305 is done for macOS and Linux. Windows still has no `appArgs` support.
