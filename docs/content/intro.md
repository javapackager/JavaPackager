---
slug: /
sidebar_label: Introduction
title: JavaPackager
---

<img src={require('/img/logo.png').default} alt="JavaPackager logo" className="intro-logo" />

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

| App | Description | Stars |
| --- | --- | --- |
| [WePush](https://github.com/rememberber/WePush) | Bulk message sender: WeChat templates, SMS, email and more | ![GitHub stars](https://img.shields.io/github/stars/rememberber/WePush?style=flat&label=%E2%98%85) |
| [MooTool](https://github.com/rememberber/MooTool) | Handy tool set for developers | ![GitHub stars](https://img.shields.io/github/stars/rememberber/MooTool?style=flat&label=%E2%98%85) |
| [bilibili-backup](https://github.com/hzhilong/bilibili-backup) | Backup and restore of Bilibili accounts | ![GitHub stars](https://img.shields.io/github/stars/hzhilong/bilibili-backup?style=flat&label=%E2%98%85) |
| [RedisFX](https://github.com/tanhuang2016/RedisFX) | Lightweight Redis GUI client (JavaFX) | ![GitHub stars](https://img.shields.io/github/stars/tanhuang2016/RedisFX?style=flat&label=%E2%98%85) |
| [RedisFront](https://github.com/dromara/RedisFront) | Cross-platform Redis client (Swing) | ![GitHub stars](https://img.shields.io/github/stars/dromara/RedisFront?style=flat&label=%E2%98%85) |
| [MooInfo](https://github.com/rememberber/MooInfo) | System and hardware information viewer | ![GitHub stars](https://img.shields.io/github/stars/rememberber/MooInfo?style=flat&label=%E2%98%85) |
| [Monitee agent](https://github.com/Krillsson/monitee-agent) | Server monitoring API and backend for Monitee | ![GitHub stars](https://img.shields.io/github/stars/Krillsson/monitee-agent?style=flat&label=%E2%98%85) |
| [MQTT Insight](https://github.com/ptma/mqtt-insight) | MQTT GUI client | ![GitHub stars](https://img.shields.io/github/stars/ptma/mqtt-insight?style=flat&label=%E2%98%85) |
| [VisionBench](https://github.com/deltacv/VisionBench) | Develop, test and tune EasyOpenCV pipelines on your computer | ![GitHub stars](https://img.shields.io/github/stars/deltacv/VisionBench?style=flat&label=%E2%98%85) |
| [windows-process](https://github.com/great-jin/windows-process) | Windows process manager | ![GitHub stars](https://img.shields.io/github/stars/great-jin/windows-process?style=flat&label=%E2%98%85) |
| [Java Geometry Expert](https://github.com/kovzol/Java-Geometry-Expert) | Automated geometry theorem proving | ![GitHub stars](https://img.shields.io/github/stars/kovzol/Java-Geometry-Expert?style=flat&label=%E2%98%85) |
| [Rawky](https://github.com/DeflatedPickle/Rawky) | Pixel art editor | ![GitHub stars](https://img.shields.io/github/stars/DeflatedPickle/Rawky?style=flat&label=%E2%98%85) |
| [RODA-in](https://github.com/keeps/roda-in) | Creates submission information packages for digital preservation | ![GitHub stars](https://img.shields.io/github/stars/keeps/roda-in?style=flat&label=%E2%98%85) |
| [Breakout](https://github.com/jedwards1211/breakout) | Interactive 3D cave survey viewer | ![GitHub stars](https://img.shields.io/github/stars/jedwards1211/breakout?style=flat&label=%E2%98%85) |
| [GistFX](https://github.com/RedmondSims/GistFX) | GitHub Gist manager | ![GitHub stars](https://img.shields.io/github/stars/RedmondSims/GistFX?style=flat&label=%E2%98%85) |
| [Astro Pixel Processor](https://www.astropixelprocessor.com/) | Astrophotography image processing | — |
| [Spektar Design Lab](https://spektar.io/) | Visual design of VST audio effects and instruments | — |

Is your app packaged with JavaPackager? [Tell us](https://github.com/javapackager/JavaPackager/discussions) and we'll add it to the list.

## Help wanted

JavaPackager is maintained by volunteers in their spare time. If you'd like to help with issues, reviews or documentation, [join the discussion](https://github.com/javapackager/JavaPackager/discussions/460).
