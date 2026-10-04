# appArgs passed as the literal "appArg" on Linux

## Problem

On Linux, every value in `appArgs` reaches the app as the literal string `appArg` instead of its configured value. Only in `devel`; not in any release.

## Cause

Commit e870712 ("Fix shellcheck issues on startup script", #471) rewrote the `appArgs` block of `src/main/resources/linux/startup.sh.vtl` as a bash array, but wrote `AppArguments+=("appArg")` without the `$`. Velocity then emits the fixed text instead of the loop variable.

## Solution

`startup.sh.vtl:79` is now `AppArguments+=("$appArg")`, the same pattern as `vmArgs` on line 66.

## Verification

- Rendered lines 77-80 of the template with Velocity 2.3 (the plugin's version), with `appArgs = ["--foo", "hello world"]`, and ran the result in bash: the array holds `--foo` and `hello world` (the space is kept).
- With `appArgs = null`, the array is empty (0 elements).
- Not yet verified with a full Linux package. A Linux job in CI, like the macOS smoke tests, would cover it.
