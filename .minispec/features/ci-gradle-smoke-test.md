# Gradle smoke test

## Goal

Package the sample app through the Gradle plugin in CI, so the Gradle side is tested like the Maven side.

## Context

- The smoke tests only use the Maven plugin (`samples/hello-world/pom.xml`). The Gradle plugin, its new id `io.github.javapackager` and `io.github.javapackager.gradle.PackageTask` (ADR-003) are not tested anywhere.
- Gradle consumers resolve dependencies from the published `.module` file, not the POM, so POM fixes (e.g. `provided` scopes) don't cover them.
- `GradleContext` uses different code (`gradle/*` tasks) for runnable JAR, dependencies, tarball, zipball and Launch4j.

## Changes

- One sample project for both build tools: `samples/hello-world` gets `build.gradle`, `settings.gradle` and `gradle.properties` next to `pom.xml`, sharing the sources and the settings (name, version, `vmArgs`, `appArgs`, `macStartup`, `administratorRequired`, console header). Maven writes to `target/`, Gradle to `build/`. The old javapackager/HelloWorldMaven and HelloWorldGradle repos are superseded by it.
- The plugin is resolved from Maven local through `pluginManagement`, with its version in the `javapackagerVersion` property (CI passes the version it built). No wrapper in the sample: it runs with the root wrapper (`./gradlew -p samples/hello-world package`).
- The smoke tests get a `tool` dimension: Linux and Windows run Maven and Gradle; macOS runs Gradle once (macos-15, UNIVERSAL). The `check-*-app.sh` scripts get `build` instead of `target`.

## Acceptance

- The Gradle job packages and runs the sample, with `appArgs` and `vmArgs` received, on the platforms it covers.
- Breaking the Gradle plugin id or task type makes the job fail.
