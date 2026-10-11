---
title: Bundling a JRE
description: Bundle a customized, full or existing JRE with your app, for this or another platform
---

A bundled JRE lets your users run the app without installing Java, and fixes the Java version the app runs on. Enable it with `bundleJre`.

| Build tool | `bundleJre` default |
| --- | --- |
| Maven | `false` |
| Gradle | `true` |

## A customized JRE

By default (`customizedJre` is `true`), JavaPackager builds a JRE with only the modules your app needs:

1. `jdeps` analyses your app's JAR and its dependencies to find the JDK modules they use.
2. `jlink` builds a JRE with those modules into the `jre` folder of the app.

```xml title="Maven"
<bundleJre>true</bundleJre>
```

```groovy title="Gradle"
javapackager {
    bundleJre = true
}
```

A customized JRE is much smaller than a full one, because it leaves out the modules your app doesn't use.

The build must run on a JDK 9 or newer (`jlink` and `jdeps` come with the JDK). The JRE is the same version as that JDK, so build with the Java version your app should run on.

### When modules are missing

`jdeps` can't see modules that are only loaded by reflection or through services, such as `jdk.crypto.ec` (TLS connections), `jdk.localedata` (locales other than English) or `java.scripting`. If the app fails with `ClassNotFoundException` or similar at runtime, add those modules with `additionalModules`:

```xml title="Maven"
<additionalModules>
    <additionalModule>jdk.crypto.ec</additionalModule>
    <additionalModule>jdk.localedata</additionalModule>
</additionalModules>
```

```groovy title="Gradle"
javapackager {
    additionalModules = ['jdk.crypto.ec', 'jdk.localedata']
}
```

To skip `jdeps` and choose the modules yourself, list them all in `modules`.

If `jdeps` fails on the module path (for example, when two dependencies contain the same package), JavaPackager retries the analysis on the class path. If it still can't find any module, it bundles all of them.

### Modules outside the JDK

For modules that aren't in the JDK, such as the JavaFX `jmods`, add their folders with `additionalModulePaths`. They are used by both `jdeps` and `jlink`:

```xml title="Maven"
<additionalModulePaths>
    <additionalModulePath>/path/to/javafx-jmods-21</additionalModulePath>
</additionalModulePaths>
```

### Tuning jlink

JavaPackager runs `jlink` with `--strip-debug`, `--no-header-files` and `--no-man-pages`, plus `--compress=2` below JDK 21. With `additionalJlinkArgs` you can append more options to the `jlink` call. For example, to keep only the English and Spanish locale data (it needs the `jdk.localedata` module):

```xml title="Maven"
<additionalModules>
    <additionalModule>jdk.localedata</additionalModule>
</additionalModules>
<additionalJlinkArgs>
    <additionalJlinkArg>--include-locales=en,es</additionalJlinkArg>
</additionalJlinkArgs>
```

```groovy title="Gradle"
javapackager {
    additionalModules = ['jdk.localedata']
    additionalJlinkArgs = ['--include-locales=en,es']
}
```

Your arguments come last, so a `--compress` there overrides the default one. Valid options depend on the JDK version:

| JDK | Compression option |
| --- | --- |
| 9 to 20 | `--compress=0`, `1` or `2` |
| 21 and newer | `--compress=zip-0` to `zip-9` (`0`, `1` and `2` still work, but are deprecated) |

:::tip
An image compressed by `jlink` can't be compressed again by the installers. If you distribute the app in an installer or a zipball, `--compress=0` (or `zip-0`) may give a smaller download.
:::

## A full JRE

Set `customizedJre` to `false` to bundle every module of the JDK. It's bigger, but nothing will be missing:

```xml title="Maven"
<bundleJre>true</bundleJre>
<customizedJre>false</customizedJre>
```

## An existing JRE

To bundle a JRE you already have, instead of building one, set `jrePath` to its folder:

```xml title="Maven"
<bundleJre>true</bundleJre>
<jrePath>C:\Program Files\Java\jre1.8.0_311</jrePath>
```

```groovy title="Gradle"
javapackager {
    bundleJre = true
    jrePath = file('C:/Program Files/Java/jre1.8.0_311')
}
```

This is the only way to bundle a Java 8 JRE, or to bundle a JRE when the build runs on Java 8. On macOS you can give the JRE bundle folder: if needed, JavaPackager uses its `Contents/Home`.

## Other platforms

To package your app for another platform (e.g. a Windows app built on GNU/Linux), set `platform` and give a JDK for that platform with `jdkPath`. JavaPackager builds the JRE with the `jlink` of the JDK running the build, from the modules (`jmods`) of `jdkPath`:

```xml title="Maven"
<platform>windows</platform>
<bundleJre>true</bundleJre>
<jdkPath>/path/to/windows/jdk-21</jdkPath>
```

```groovy title="Gradle"
javapackager {
    platform = 'windows'
    bundleJre = true
    jdkPath = file('/path/to/windows/jdk-21')
}
```

- The target JDK must be the same Java version as the JDK running the build (a `jlink` rule).
- It must include the `jmods` folder: since JDK 24, some builds (e.g. Temurin) come without it, so download a JDK that includes it for the other platform.
- Without `jdkPath`, JavaPackager warns that it can't build a JRE for another platform and packages the app without one.

See [several executions and platforms](multiple-executions.md) to package for every platform in one build.

## Which JDK is used

| Property | Default | Use |
| --- | --- | --- |
| `packagingJdk` | Maven: the JDK running Maven. Gradle: the project's Java toolchain, if any, or the JDK running Gradle. | Runs `jdeps` and `jlink`. |
| `jdkPath` | The JDK running Maven or Gradle | Provides the modules (`jmods`) of the JRE, and decides the target platform's JRE. |
| `jreDirectoryName` | `jre` | Folder of the JRE in the app (`Contents/PlugIns/jre.jre` on macOS). |

If you use a Gradle toolchain different from the JDK running Gradle, set `jdkPath` to the toolchain's JDK too, so `jlink` and the modules are the same version.

JDK 24 and newer can come without `jmods` ([JEP 493](https://openjdk.org/jeps/493)). For your own platform it doesn't matter: `jlink` then takes the modules from the JDK itself. For another platform, `jmods` are needed.

## What's left out

The bundled JRE has no `legal` folder (the JDK's third-party license notices) and no `man` pages: they break macOS code signing. If you distribute the JRE, consider shipping its license notices with your app.
