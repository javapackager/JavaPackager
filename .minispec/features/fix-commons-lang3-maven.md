# NoSuchMethodError on ObjectUtils.getIfNull with Maven

## Problem

On `devel`, packaging with Maven fails on macOS: `NoSuchMethodError: ObjectUtils.getIfNull(Object, Supplier)` in `MacConfig.setDefaults()`. Gradle builds work. Found by the macOS smoke tests (run 37165027375, all 7 jobs).

## Cause

Commit 917fa6a ("Gradle 9 Support", #485) replaced `defaultIfNull` with `getIfNull(Object, Supplier)`, which only exists in commons-lang3 3.10 and later. `build.gradle` declared 3.9:

- Gradle picks the highest version in the graph (3.11, pulled in transitively), so it compiles and runs.
- Maven uses the declared 3.9 (nearest wins), so the method is missing at runtime.

## Solution

`build.gradle` now declares `commons-lang3:3.11`, the version Gradle already resolved, so Maven and Gradle use the same jar.

## Verification

- `./gradlew dependencies --configuration runtimeClasspath` shows `commons-lang3:3.11` with no conflict.
- Pending: the macOS smoke tests (Maven) must get past "Package sample app".
