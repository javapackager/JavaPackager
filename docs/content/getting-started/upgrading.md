---
title: Upgrading to 2.0
description: What changes when upgrading from JavaPackager 1.7.x to 2.0
---

JavaPackager 2.0 moved to the [`javapackager`](https://github.com/javapackager) organization and changed its coordinates. This page lists everything you may need to change when upgrading from 1.7.x. All the changes are in the [release notes](https://github.com/javapackager/JavaPackager/releases/tag/v2.0.0).

## New coordinates

Everything named `io.github.fvarrui` is now `io.github.javapackager`. Versions up to 1.7.6 remain available under the old coordinates.

| What | 1.7.x | 2.0 |
| --- | --- | --- |
| Maven `groupId` | `io.github.fvarrui` | `io.github.javapackager` |
| Gradle plugin id | `io.github.fvarrui.javapackager.plugin` | `io.github.javapackager` |
| Java packages | `io.github.fvarrui.javapackager.*` | `io.github.javapackager.*` |
| Gradle task type | `io.github.fvarrui.javapackager.gradle.PackageTask` | `io.github.javapackager.gradle.PackageTask` |

```xml title="Maven"
<plugin>
    <groupId>io.github.javapackager</groupId>
    <artifactId>javapackager</artifactId>
    <version>2.0.0</version>
    [...]
</plugin>
```

```groovy title="Gradle"
plugins {
    id 'io.github.javapackager' version '2.0.0'
}
```

With Gradle, the `plugins` block no longer needs extra repositories, and the old `buildscript` and `apply plugin` setup isn't needed. Update every import of the plugin's classes too, e.g. `io.github.javapackager.model.MacStartup`.

## Checklist

- **macOS launcher.** `macStartup` now defaults to `UNIVERSAL`, a native launcher that runs natively on Apple Silicon and Intel (it was `SCRIPT`). To keep the previous launcher, set `<macStartup>SCRIPT</macStartup>` (Gradle: `macStartup = MacStartup.SCRIPT`). The launcher file is still `Contents/MacOS/universalJavaApplicationStub`, so custom templates keep working.
- **Zipball and tarball names (Maven).** They follow `name` and `version` now, like with Gradle: `${name}-${version}-${platform}.zip` and `.tar.gz` (they used to follow `build.finalName`). Update the scripts that pick them up, or set `zipballName` and `tarballName`.
- **AppImage and RPM names.** Now `${name}_${version}.AppImage` (was `${name}.AppImage`) and `${name}_${version}.rpm` (was `${name}-${version}-1.<arch>.rpm`), like the other installers.
- **`vmArgs` with spaces.** Each element is passed as a single argument, spaces included, so `-Dtitle=My App` now works. In 1.7.x the GNU/Linux script, the macOS `SCRIPT` launcher and Launch4j split elements on spaces, so if an element holds several options, like `-Xms256m -Xmx1g`, split it into one element per option.
- **Gradle getters.** The boolean properties of `PackageTask` use `getX()` instead of `isX()` (`getBundleJre()`, `getGenerateInstaller()`, ...), as Gradle 9 requires. Setting them (`bundleJre = true`) doesn't change; only code that calls the getters does.
- **Snapshots** are published to the [Central Portal snapshots repository](../guides/snapshots.md).

## What you get for free

- Gradle 9 support.
- `appArgs`: default arguments for your app (GNU/Linux and macOS).
- `additionalJlinkArgs`, and customized JREs with JDK 24 and newer.
- WiX Toolset 4 and newer for MSI installers.
- More modules detected in customized JREs: `jdeps` now analyses your dependencies too.
- Many fixes on every platform; see the [release notes](https://github.com/javapackager/JavaPackager/releases/tag/v2.0.0).
