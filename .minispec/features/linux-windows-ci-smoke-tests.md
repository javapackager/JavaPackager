# Linux and Windows smoke tests in GitHub Actions

## Goal

Extend the macOS smoke tests to Linux and Windows, so fixes on those platforms (appArgs, Inno Setup, WiX) are verified in CI.

## Context

- The `macos` job of `.github/workflows/smoke-tests.yml` packages `samples/hello-world` on macOS and checks the result (see `macos-ci-smoke-tests.md`).
- Pending verifications need Linux or Windows: the `appArgs` fix in `linux/startup.sh.vtl`, the WiX v4+ support (#477), and future `iss.vtl` fixes (#369, #338, #261).
- Linux output: executable `target/HelloWorld/HelloWorld`, plus `HelloWorld_1.0.0.deb`, `.rpm` and `.AppImage`. AppImage runs `appimagetool`, which needs FUSE 2.
- Windows output: `target/HelloWorld/HelloWorld.exe` (Launch4j by default), plus `HelloWorld_1.0.0.exe` (Inno Setup `iscc`), `.msi` and `.msm` (WiX).
- `appArgs` is not implemented on Windows yet (#305).

## Changes

- Sample app: print the received arguments, set `appArgs` (`--foo`, `hello world`), and set `winConfig.headerType=console` so the Windows EXE output can be read.
- Rename `macos-smoke.yml` to `.github/workflows/smoke-tests.yml`, with three jobs:
  - `macos`: the current matrix.
  - `linux`: `ubuntu-latest`, with `libfuse2t64` installed.
  - `windows`: `windows-latest`, with Inno Setup (Chocolatey, if missing) and WiX 5 (`dotnet tool`), to test the `wix build` path.
- Add check scripts `check-linux-app.sh` and `check-windows-app.sh`: executable present, app runs natively with the bundled JRE, `appArgs` received (not on Windows), installers generated.
- The macOS check also verifies `appArgs` (Info.plist `Arguments`).

## Acceptance

- The three jobs run on each push to `devel`.
- The Linux job confirms the `appArgs` fix (`args=[--foo, hello world]`).
- The Windows job shows whether Inno Setup, MSI and MSM are generated with WiX 5.
- Any failure is either fixed or recorded as a bugfix note.
