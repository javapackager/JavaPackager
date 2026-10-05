# Customized JRE fails with JDK 24+ without jmods

## Problem

Packaging with a JDK 24 or newer from Temurin (and other vendors) fails whenever a customized JRE is bundled (`bundleJre` without `jrePath`): `jmods folder doesn't exist: /usr/lib/jvm/temurin-25-jdk-amd64/jmods`. Found while testing PR #491 with JDK 11, 17, 21 and 25; the smoke tests only used JDK 17.

## Cause

- Since JDK 24 (JEP 493) a JDK may come without `jmods`: its `jlink` links the modules from its own run-time image when `java.base` isn't on `--module-path`. `BundleJre` always required `<jdkPath>/jmods`.
- The default `--compress=2` (dropped on JDK 21+, where it's deprecated) was decided by the Java running Maven or Gradle, not by the `jlink` that runs (the `packagingJdk`'s).

## Solution

- `BundleJre`: when `jmods` is missing, `jdkPath` is the packaging JDK and its version is 24+, `jmods` is left out of `--module-path` (the option is dropped if nothing else is on it), so `jlink` uses the run-time image. A JDK for another platform (`jdkPath`) still needs its `jmods`; the error says so.
- `--compress=2` is decided by the packaging JDK's version (`JDKUtils.getJavaMajorVersion(File)`, from its `release` file).
- Smoke tests: new GNU/Linux Maven job packaging with Temurin 25.

## Verification

- New `ubuntu-latest / maven / JDK 25` smoke test job green; the other jobs unchanged.
