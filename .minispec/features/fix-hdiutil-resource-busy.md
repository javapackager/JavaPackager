# DMG generation fails intermittently with "Resource busy"

## Problem

DMG generation sometimes fails with `hdiutil: create failed - Resource busy`. The rest of the build succeeds, but no DMG is produced. Seen in 1 of 7 macOS smoke test jobs (run 37165552012, `macos-14 / ARM64`). Common on CI runners, where the system scans new disk images.

## Cause

`hdiutil` can fail transiently while macOS (Spotlight, XProtect) holds the image or volume. `GenerateDmg` ran each `hdiutil` command once and gave up on the first error.

## Solution

`GenerateDmg` runs every `hdiutil` command (`create`, `attach`, `detach`, `convert`) through a private `hdiutil(...)` helper. It retries up to 5 times, waiting 5, 10, 15 and 20 s (raised from 3 attempts after `hdiutil convert` failed 3 times in a row with "Resource temporarily unavailable" on macos-14, run 37219554202), and logs a warning for each retry. After the last attempt the original error is thrown as before.

## Verification

- The failure is intermittent, so it can't be reproduced on demand.
- The macOS smoke tests must stay green. If the error shows up again, the build log must show the `hdiutil ... failed (attempt n of 5), retrying` warning, followed by a DMG.
- Run 37166331226: 7/7 green, no retries needed.
- Run 37170371112: the final `detach` failed with `Resource busy`, but the volume was unmounted a moment later. The retries then failed with `No such file or directory`, and no DMG was produced. `detach` now has its own helper: it is done as soon as the mount folder is gone.

## Not done

- `hdiutil detach -force` was considered and rejected: the only `detach` failure so far resolved itself (see above), and the first `detach` could unmount a user's own volume that happens to have the same name. If logs show `detach` failing all 5 attempts, add `-force` only to the final `detach`.
- `hdiutil create -srcfolder` unmounts internally and can't be forced. Avoiding that needs a blank image + `ditto` + our own `detach`, which means computing the image size. Only worth doing if `create` keeps failing despite the retries.
