# Stack

## Language and build

- Java 8 (source and target compatibility).
- Gradle 8.14.3 (wrapper), building both the Gradle plugin and the Maven plugin. The plugin supports Gradle 9 consumers.
- Embedded Maven 3.6 + `maven-plugin-plugin` 3.9.0 to generate the Maven plugin descriptor.

## Libraries

- Maven Plugin API, `mojo-executor`.
- Gradle API, Launch4j Gradle plugin (`edu.sc.seis.launch4j`).
- Apache Velocity 2.3 (templates).
- Commons IO, Lang3, Collections4, Compress.
- `jdeb` (DEB), `redline` (RPM), `jsign-core` (Windows signing).

## External tools at runtime

- JDK `jdeps` and `jlink`.
- Windows: Inno Setup (`iscc`), WiX Toolset 3 (`candle`, `light`) or 4+ (`wix build`), WinRun4J, Why `JavaLauncher.exe`, `rcedit`.
- macOS: `hdiutil`, `pkgbuild`, `codesign`, bundled `nativeJavaApplicationStub` launchers (or `universalJavaApplicationStub.sh` script).
- Linux: `appimagetool` (downloaded automatically) for AppImage.

## Publishing

- Maven Central (OSSRH, signed) and Gradle Plugin Portal.
