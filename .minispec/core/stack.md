# Stack

## Language and build

- Java 8 (source and target compatibility).
- Gradle 8.14.3 (wrapper), building both the Gradle plugin and the Maven plugin. The plugin supports Gradle 9 consumers.
- Embedded Maven 3.9 + `maven-plugin-plugin` 3.16 to generate the Maven plugin descriptor.

## Libraries

- Maven Plugin API, `mojo-executor`.
- Gradle API, Launch4j Gradle plugin (`edu.sc.seis.launch4j`).
- Apache Velocity 2.4 (templates).
- Commons IO, Lang3, Collections4, Compress.
- `jdeb` (DEB), `redline` (RPM), `jsign-core` (Windows signing).

## External tools at runtime

- JDK `jdeps` and `jlink`.
- Windows: Inno Setup (`iscc`), WiX Toolset 3 (`candle`, `light`) or 4+ (`wix build`), WinRun4J, Why `JavaLauncher.exe`, `rcedit`.
- macOS: `hdiutil`, `pkgbuild`, `codesign`, bundled `nativeJavaApplicationStub` launchers (or `universalJavaApplicationStub.sh` script).
- Linux: `appimagetool` (downloaded automatically) for AppImage.

## Publishing

- Maven Central through the Central Portal: snapshots repo, and releases (signed) through the Portal OSSRH Staging API (`releaseToCentral`), then published by hand in the Portal. Release candidates (`X.Y.Z-rcN`) are published the same way.
- Not published on the Gradle Plugin Portal: its repository (`plugins.gradle.org/m2`) redirects to Maven Central, so `plugins { id 'io.github.javapackager' version '...' }` works with no extra repositories (#490).
- Dependabot sees the Gradle build through `.github/workflows/dependency-submission.yml`; version update PRs come from `.github/dependabot.yml`.
