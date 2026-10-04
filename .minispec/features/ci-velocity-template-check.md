# Validate Velocity templates in CI

## Goal

Catch Velocity parse errors in `src/main/resources/**/*.vtl` in a fast CI check, before any platform job packages an app.

## Context

- A Velocity lexical error in `linux/startup.sh.vtl` (bash `"${name[@]}"` read as a Velocity reference) broke every GNU/Linux build in `devel`. It was only found when the Linux smoke test packaged the sample (see `fix-linux-startup-velocity-arrays.md`).
- Templates are rendered at packaging time by `utils/VelocityUtils`; nothing parses them during the build.
- Users can override templates from `assetsDir`, so templates are public API and must stay valid.

## Changes

- A small check that parses every `.vtl` with the Velocity version in `build.gradle`: either a Gradle task (`validateTemplates`, wired into `check`) or a step in `.github/workflows/smoke-tests.yml` before the platform jobs.
- For shell templates (`*.sh.vtl`, `startup.vtl`), render them with a stub `$info` and run `bash -n` on the result.

## Acceptance

- Reverting the `#[[ ]]#` fix in `startup.sh.vtl` makes the check fail with the template name and line.
- The check runs in under a minute and doesn't need any OS-specific tool.
