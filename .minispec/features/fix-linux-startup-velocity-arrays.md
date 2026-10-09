# Linux packaging fails with a Velocity lexical error

## Problem

On `devel`, every Linux build fails: `Lexical error, Encountered: "@" (64) at linux/startup.sh.vtl[line 70, column 89]`. Found by the `linux` smoke test job (run 37166814791).

## Cause

Commit e870712 ("Fix shellcheck issues on startup script", #471) introduced bash array expansions such as `"${option[@]}"`, `"${JVMDefaultOptions[@]}"` and `"${AppArguments[@]}"`. Velocity reads `${name[` as one of its own references with an index and fails on `@`. (`${#v1[@]}` is fine because `${#` is not a Velocity reference.)

## Solution

The three lines in `linux/startup.sh.vtl` with `${name[@]}` (JVM options file, `pkexec` launch, normal launch) are wrapped in Velocity unparsed blocks `#[[ ... ]]#`. None of them contain Velocity references.

## Verification

- Rendered the old template locally with Velocity 2.3: same `Lexical error ... line 70, column 89`.
- Rendered the fixed template: no errors, `bash -n` OK, and the output lines are identical to the bash source.
- Pending: the `linux` smoke test job must package the app and run it with `appArgs` (`args=[--foo, hello world]`).
