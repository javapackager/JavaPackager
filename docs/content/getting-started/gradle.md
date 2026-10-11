---
title: Gradle
description: Package your app with the JavaPackager Gradle plugin
---

JavaPackager supports Gradle 7 and newer, including Gradle 9. To bundle a customized JRE, build with a JDK 9 or newer.

## Apply the plugin

Apply the plugin in `build.gradle`:

```groovy
plugins {
    id 'io.github.javapackager' version '2.0.0'
}
```

No extra repositories are needed: the plugin is published on Maven Central, and Gradle finds it through the Gradle Plugin Portal, its default plugin repository. Check the latest version on [Maven Central](https://central.sonatype.com/artifact/io.github.javapackager/javapackager) or in the [releases](https://github.com/javapackager/JavaPackager/releases).

The plugin applies the `java` plugin, and adds a `package` task and a `javapackager` extension to configure it.

## Configure and package your app

Configure the app in the `javapackager` extension:

```groovy
javapackager {
    mainClass = 'com.example.Main'
}
```

`mainClass` is the only mandatory property. Then run:

```bash
gradle package
```

The `package` task runs after `build`. The app and its installers are generated in `build/`. See [generated artifacts](generated-artifacts.md) for what you get, and which tools each installer needs.

With Gradle, `bundleJre` is `true` by default: a [customized JRE](../guides/bundling-a-jre.md) with only the modules your app needs is bundled with it, so users don't need Java installed. Set `bundleJre = false` to leave it out.

:::note
The Maven plugin doesn't bundle a JRE by default (`bundleJre` is `false` there).
:::

## A fuller example

```groovy
javapackager {
    // mandatory
    mainClass = 'com.example.Main'
    // optional
    name = 'MyApp'
    displayName = 'My App'
    bundleJre = true
    generateInstaller = true
    administratorRequired = false
    platform = 'auto'
    vmArgs = [ '-Xmx512m' ]
    additionalResources = [ file('README.md'), file('config') ]
    linuxConfig {
        ...
    }
    macConfig {
        ...
    }
    winConfig {
        ...
    }
}
```

Every property is described in the [configuration reference](../configuration/common.md), and the platform-specific ones in [GNU/Linux](../configuration/linux.md), [macOS](../configuration/macos.md) and [Windows](../configuration/windows.md).

## Your own tasks

To package the app more than once (several platforms, with and without JRE...), register your own tasks of type `PackageTask`. They take every property from the `javapackager` extension, and the ones you set in the task override them:

```groovy
tasks.register('packageForWindows', io.github.javapackager.gradle.PackageTask) {
    dependsOn build
    platform = 'windows'
    createZipball = true
}
```

:::note
In a task, the app's name property is `appName`, because `name` is the task's own name.
:::

See [several executions and platforms](../guides/multiple-executions.md) and the [Gradle samples](../samples/gradle.md).

## Properties taken from your project

| Property | Taken from |
| --- | --- |
| `name` | the project name |
| `version` | the project version |
| `description` | the project description, as it is when the plugin is applied (set `description` in `javapackager` if you set the project's later) |
| `licenseFile` | a `LICENSE` file in the root project folder |
| `jreMinVersion` | the Java `targetCompatibility` (for Launch4j on Windows) |
| `packagingJdk` | the Java toolchain, if the project defines one |

`mainClass` is not taken from the `application` plugin: set it in `javapackager`.
