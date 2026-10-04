# Architecture

## Flow

```
PackageMojo (Maven) | PackagePlugin + PackageTask (Gradle)
  → Context.setContext(MavenContext | GradleContext)
  → PackagerFactory.createPackager(platform) + fluent settings
  → packager.createApp() → packager.generateInstallers() → packager.createBundles()
```

## Key pieces

- `packagers/Context` — static singleton hiding the build tool. Declares tool-specific operations (runnable JAR, copy dependencies, tarball, zipball, license, Launch4j EXE) and registers installer generators per platform.
- `maven/MavenContext` — implements them by running other Maven plugins through `mojo-executor` (`maven/*` classes).
- `gradle/GradleContext` — implements them with Gradle tasks (`gradle/*` classes with the same names).
- `PackagerSettings` — every configuration property, with fluent setters. Also the base of the Gradle extension `PackagePluginExtension`.
- `Packager` — shared pipeline: init, resolve resources/icons/license, app folder structure, bundle JRE, run installer generators. Subclasses `WindowsPackager`, `MacPackager`, `LinuxPackager` implement `doInit`, `doCreateAppStructure`, `doCreateApp`.
- `ArtifactGenerator<T>` — unit of work: `skip(packager)` decides, `doApply(packager)` produces a `File`. Installers (`GenerateDeb`, `GenerateDmg`, `GenerateMsi`, ...), `BundleJre` and the Windows EXE creators are generators.
- `utils/VelocityUtils` — renders `.vtl` templates, first from the user's `assetsDir`, then from the classpath. The packager is exposed as `$info` (plus `$StringUtils`, `$GUID`, `$features`).
- `utils/CommandUtils` — runs external tools (`jlink`, `jdeps`, `iscc`, `candle`/`light`, `hdiutil`, `pkgbuild`, `codesign`, `rcedit`).
- `utils/Logger` — wraps the Maven or Gradle logger, with indentation helpers.
- `net/jsign/WindowsSigner` — Windows code signing on top of `jsign-core`.

## Windows launcher

`WindowsConfig.exeCreationTool` picks how the EXE is built: `launch4j` (via the context), `winrun4j` (`CreateWindowsExeWinRun4j`) or `why` (`CreateWindowsExeWhy`).

## WiX version

`GenerateMsm` detects the WiX major version (`wix -version`, falling back to `candle`/`light` as v3) and stores it in the packager. `GenerateMsi` relies on that, so MSM must stay registered before MSI in `Context`. v3 uses `candle` + `light`; v4+ uses `wix build`.

## Folder map

- `src/main/java/.../javapackager/maven/` — Maven mojo and Maven-based operations.
- `src/main/java/.../javapackager/gradle/` — Gradle plugin, task, extension and Gradle-based operations.
- `src/main/java/.../javapackager/packagers/` — build-tool-independent pipeline and generators.
- `src/main/java/.../javapackager/model/` — configuration model (`WindowsConfig`, `MacConfig`, `LinuxConfig`, `FileAssociation`, ...).
- `src/main/java/.../javapackager/utils/` — helpers (files, commands, Velocity, JDK, icons, XML).
- `src/main/resources/{linux,mac,windows}/` — Velocity templates, default icons and bundled native binaries (WinRun4J, `JavaLauncher.exe`, `rcedit-x64.exe`, `nativeJavaApplicationStub*`, `universalJavaApplicationStub.sh`).
- `docs/` — user documentation for platform-specific properties and samples.
- `samples/hello-world/` — sample app packaged by the smoke tests, with the same settings in `pom.xml` and `build.gradle` (Maven writes to `target/`, Gradle to `build/`).
- `.github/workflows/` and `.github/scripts/` — smoke tests, dependency submission and the per-platform checks.
