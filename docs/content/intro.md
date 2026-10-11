---
slug: /
sidebar_label: Introduction
title: JavaPackager
---

JavaPackager is a hybrid plugin for **Maven** and **Gradle** that packages Java applications as native Windows, macOS and GNU/Linux executables, and generates installers for them.

With a single plugin and a few lines of configuration you get:

- **A native app folder** with an executable launcher, your JAR and its dependencies, and optionally a bundled JRE.
- **A customized JRE** built with `jdeps` and `jlink`, containing only the modules your app needs.
- **Installers and packages**: Inno Setup EXE, MSI and MSM on Windows; DMG and PKG on macOS; DEB, RPM and AppImage on GNU/Linux.
- **Zipballs and tarballs** of the app folder.
- **Code signing** of Windows executables and installers, and code signing and notarization of macOS apps.

## Why JavaPackager

It was born while teaching students how to build and distribute their Java apps: a chain of several plugins was needed to do it, so JavaPackager became a plugin to rule them all. The same configuration works with Maven and Gradle, and the same project can be packaged for every platform.

## Where to start

- [Package your app with Maven](getting-started/maven.md) or [with Gradle](getting-started/gradle.md).
- See what you get in [Generated artifacts](getting-started/generated-artifacts.md).
- Upgrading from 1.x? Read [Upgrading to 2.0](getting-started/upgrading.md) first: the coordinates changed.
- Look up every property in the [Configuration reference](configuration/common.md).

## Apps packaged with JavaPackager

- [AstroImageJ](http://astroimagej.com/)
- [Astro Pixel Processor](https://www.astropixelprocessor.com/)
- [GistFX](https://github.com/RedmondSims/GistFX)
- [Spektar Design Lab](https://spektar.io/)

Is your app packaged with JavaPackager? [Tell us](https://github.com/javapackager/JavaPackager/discussions) and we'll add it to the list.

## Help wanted

JavaPackager is maintained by volunteers in their spare time. If you'd like to help with issues, reviews or documentation, [join the discussion](https://github.com/javapackager/JavaPackager/discussions/460).
