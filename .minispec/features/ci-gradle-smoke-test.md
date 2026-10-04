# Gradle smoke test

## Goal

Package the sample app through the Gradle plugin in CI, so the Gradle side is tested like the Maven side.

## Context

- The smoke tests only use the Maven plugin (`samples/hello-world/pom.xml`). The Gradle plugin, its new id `io.github.javapackager` and `io.github.javapackager.gradle.PackageTask` (ADR-003) are not tested anywhere.
- Gradle consumers resolve dependencies from the published `.module` file, not the POM, so POM fixes (e.g. `provided` scopes) don't cover them.
- `GradleContext` uses different code (`gradle/*` tasks) for runnable JAR, dependencies, tarball, zipball and Launch4j.

## Changes

- Add `samples/hello-world/build.gradle` and `settings.gradle` that apply the plugin from Maven local (`buildscript { repositories { mavenLocal(); mavenCentral(); gradlePluginPortal() } }`) with the same settings as the Maven sample.
- Add a job (at least `ubuntu-latest`, ideally all three OSes) that runs `publishToMavenLocal`, then `gradle packageMyApp` in the sample, then the existing `check-*-app.sh` script.

## Acceptance

- The Gradle job packages and runs the sample, with `appArgs` and `vmArgs` received, on the platforms it covers.
- Breaking the Gradle plugin id or task type makes the job fail.
