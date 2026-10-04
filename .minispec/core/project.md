# Project

## What

JavaPackager is a hybrid Maven/Gradle plugin, published as a single artifact (`io.github.fvarrui:javapackager`), that packages Java applications as native Windows, macOS or GNU/Linux apps and generates installers for them.

## What it does

- Builds a native app folder: runnable JAR, dependencies, launcher executable or startup script, icons and optional bundled JRE.
- Generates a customized JRE with `jdeps` + `jlink`, or bundles an existing one.
- Generates installers: DEB, RPM and AppImage (Linux); DMG and PKG (macOS); Inno Setup EXE, MSI and MSM (Windows).
- Creates zipball/tarball bundles of the app folder.
- Lets users override any icon or Velocity template from their own `assetsDir`.

## For whom

- Java developers who want to distribute desktop apps without chaining several plugins.
- Users configure it through `pom.xml` (goal `package`) or a Gradle `PackageTask`; see `README.md` and `docs/`.

## Goal

One plugin to rule them all: package and distribute a Java app natively from a single build configuration.
