# macOS app with administratorRequired has no launcher (#473, #398)

## Problem

With `administratorRequired=true`, the macOS app does nothing after the user enters the admin password (#398). `Contents/MacOS/universalJavaApplicationStub` is missing from the bundle (#473). Reproduced by the macOS smoke tests (run 37165552012, job `macos-15 / SCRIPT / admin`).

## Cause

`MacPackager.processStartupScript()` only copied the launcher (precompiled stub or `customLauncher`) in the non-admin branch. In admin mode it only rendered the `startup` helper script, and `mac/startup.vtl` invoked a hardcoded `universalJavaApplicationStub` that was never copied.

## Solution

- `processStartupScript()` always copies the launcher to `Contents/MacOS` and keeps it in a new `launcher` field (`getLauncher()`). In admin mode `startup` is the executable; otherwise the launcher is.
- `mac/startup.vtl` invokes `$SCRIPTPATH/${info.launcher.name}` instead of the hardcoded name, so `customLauncher` also works in admin mode.
- Custom `startup.vtl` templates in users' `assetsDir` keep working: the default launcher is still named `universalJavaApplicationStub`.
- The smoke test checks that the `startup` script invokes an existing launcher, and the admin job no longer has `continue-on-error`.

## Verification

- macOS smoke tests: the admin job must pass (launcher present, invoked by `startup`).
- Not verifiable in CI: the password prompt and the app actually starting with admin rights. #398 reporters said a similar fix (branch `issue-398`, before the native launcher) still didn't start the app. Ask them to test a snapshot before closing #398. #473 can be closed with this fix.
