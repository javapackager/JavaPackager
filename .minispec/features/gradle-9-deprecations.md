# Gradle 9 deprecations in the Gradle plugin

## Goal

Package with the Gradle plugin without deprecation warnings, so it keeps working on Gradle 9 and 10.

## Context

- The Gradle smoke test (`ci-gradle-smoke-test`) showed these warnings with `--warning-mode all` (Gradle 8.14.3):
  - `PackageTask` had `Boolean isX()` getters (`bundleJre`, `generateInstaller`, `administratorRequired`…). Gradle 9 ignores them as properties.
  - `PackageTask.createPackager()` called `Task.getProject()` at execution time (fails in Gradle 10, not configuration-cache friendly).
  - `JavaPluginConvention` (removed in Gradle 9), read by the launch4j Gradle plugin to default `jreMinVersion` from `targetCompatibility`.
  - `CopyProcessingSpec.setMode()` (removed in Gradle 9), used by launch4j 3.0.7 when extracting its binaries.
- `javac -Xlint:deprecation` flags `TaskContainer.create()` (5 places).
- `javac` on JDK 21+ warns that the Java 8 target is obsolete; the target is deliberate.

## Changes

- `PackageTask`: `isX()` → `getX()` for the 9 `Boolean` properties, no deprecated aliases (2.0.0 is a clean break, ADR-003). Scripts that set them (`bundleJre = true`) are unaffected.
- `PackageTask`: read the extension and a `Provider` of the project version at configuration time.
- `gradle/CreateWindowsExeLaunch4j`: always set `jreMinVersion`, defaulting like launch4j does (target compatibility, plus `.0`) but through `JavaPluginExtension`.
- `edu.sc.seis.launch4j:launch4j` 3.0.7 → 4.0.0 (Java 8 bytecode, Gradle 7.0+).
- `TaskContainer.create()` → `register()`: lazy for the `package` task, `.get()` where the task is used right away (copy libs, tarball, zipball, launch4j library task).
- `build.gradle`: `-Xlint:-options`.

## Acceptance

- `./gradlew -p samples/hello-world package --warning-mode all` prints no deprecation warnings.
- `javac -Xlint:deprecation` reports nothing.
- The Gradle smoke tests pass on GNU/Linux, Windows and macOS.
