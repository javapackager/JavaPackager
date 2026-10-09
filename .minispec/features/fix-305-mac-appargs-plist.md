# appArgs ignored on macOS (#305)

## Problem

On macOS, `appArgs` never reach the app (`args=[]`) with any launcher (`UNIVERSAL`, `ARM64`, `SCRIPT`). Found by the macOS smoke test jobs (run 37166814791).

## Cause

Commit 3818d4a (#305) put the `Arguments` key in `mac/Info.plist.vtl` at the top level of the plist, after the `JavaX` dictionary is closed. Both launchers read it as `:JavaX:Arguments`: `universalJavaApplicationStub.sh` through `plist_get_java`, and `nativeJavaApplicationStub` from the same dictionary as `MainClass` and `ClassPath`.

## Solution

The `Arguments` array is moved inside the `JavaX` dictionary, after `RelocateJar`.

With the key in place, the `SCRIPT` launcher still split `hello world` into two arguments: `universalJavaApplicationStub.sh` flattened the array with `xargs`. It now reads the `PlistBuddy` output one element per line (see ADR-001).

## Verification

- Rendered the template locally with Velocity 2.3 and parsed the result with Python `plistlib`: `JavaX.Arguments = ['--foo', 'hello world']`, with no top-level `Arguments`.
- Run 37167046972: native launchers (`UNIVERSAL`, `ARM64`) print `args=[--foo, hello world]`. `SCRIPT` printed `[--foo, hello, world]`.
- Script fix tested locally with simulated `PlistBuddy` output: 3 elements kept whole, `$VAR` still expanded, empty array gives 0 elements.
- Pending: the `SCRIPT` jobs must print `args=[--foo, hello world]`.
- When this passes, #305 is done for macOS and Linux. Windows still has no `appArgs` support.
