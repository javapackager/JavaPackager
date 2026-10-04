# DMG generation fails intermittently with "Resource busy"

## Problem

DMG generation sometimes fails with `hdiutil: create failed - Resource busy`. The rest of the build succeeds, but no DMG is produced. Seen in 1 of 7 macOS smoke test jobs (run 37165552012, `macos-14 / ARM64`). Common on CI runners, where the system scans new disk images.

## Cause

`hdiutil` can fail transiently while macOS (Spotlight, XProtect) holds the image or volume. `GenerateDmg` ran each `hdiutil` command once and gave up on the first error.

## Solution

`GenerateDmg` runs every `hdiutil` command (`create`, `attach`, `detach`, `convert`) through a private `hdiutil(...)` helper. It retries up to 3 times, waiting 5 s and then 10 s, and logs a warning for each retry. After the last attempt the original error is thrown as before.

## Verification

- The failure is intermittent, so it can't be reproduced on demand.
- The macOS smoke tests must stay green. If the error shows up again, the build log must show the `hdiutil ... failed (attempt n of 3), retrying` warning, followed by a DMG.
