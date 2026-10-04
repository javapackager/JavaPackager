# Fix #369: app launched after install runs as the wrong user

## Problem

The Inno Setup "Launch app" checkbox at the end of the install starts the app as the administrator who approved the UAC prompt, not as the logged-in user (settings, files and registry entries end up in the admin's profile).

## Cause

The `[Run]` entry in `windows/iss.vtl` had the `runascurrentuser` flag, so the app inherited Setup's elevated credentials.

## Solution

`windows/iss.vtl`: `runascurrentuser` only when `administratorRequired` is true (the app needs admin anyway, and this avoids a second UAC prompt). Otherwise no flag: Inno's default for `postinstall` entries is to run as the original user.

## Verification

The Windows smoke tests check the template still renders and the setup is built. Running as the original user needs a manual test on Windows (install as a standard user with other admin credentials, tick "Launch", check the process owner): ask the reporter to test the snapshot (`feedback` label).
