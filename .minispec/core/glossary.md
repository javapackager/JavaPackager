# Glossary

## App folder

The generated directory `${outputDirectory}/${name}` with the native app: launcher, JAR, `libs`, JRE and resources. Installers and bundles are built from it.

## Assets dir

`assetsDir` (default `assets/` in the project). Holds user icons and templates organized by platform (`linux/`, `mac/`, `windows/`) that replace the plugin defaults with the same name.

## Bootstrap script

Optional user script (`scripts.bootstrap`) run by the startup script before launching the app.

## Bundle

Zipball or tarball of the app folder (`createZipball`, `createTarball`).

## Context

The build-tool abstraction (`MavenContext` or `GradleContext`), held statically in `Context`.

## Customized JRE

JRE generated with `jlink`, containing only the modules found by `jdeps` or listed in `modules`.

## EXE creation tool

How the Windows launcher is built: `launch4j`, `winrun4j` or `why`.

## Generator

An `ArtifactGenerator`: a step that may be skipped and produces one file (installer, JRE, EXE).

## Installer

A platform package built from the app folder: DEB, RPM, AppImage, DMG, PKG, Inno Setup EXE (setup), MSI, MSM.

## Packager

The platform-specific orchestrator (`WindowsPackager`, `MacPackager`, `LinuxPackager`).

## Platform

Target OS: `auto`, `linux`, `mac` or `windows`. May differ from the OS running the build.

## Runnable JAR

The app JAR with a manifest pointing to `mainClass`, created by the plugin unless `runnableJar` is given.

## Mac startup

macOS launcher type (`macStartup`): `UNIVERSAL`, `X86_64`, `ARM64` (bundled `nativeJavaApplicationStub` binaries) or `SCRIPT` (`universalJavaApplicationStub.sh`). The launcher is always copied into the app as `Contents/MacOS/universalJavaApplicationStub`.
