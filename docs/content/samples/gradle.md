---
title: Gradle samples
description: Ready-to-copy Gradle configurations
---

All samples assume the plugin is applied in `build.gradle` (replace `2.0.0` with the [latest version](https://central.sonatype.com/artifact/io.github.javapackager/javapackager)):

```groovy
plugins {
    id 'io.github.javapackager' version '2.0.0'
}
```

The plugin registers a `package` task, configured with the `javapackager` extension. Your own `PackageTask` tasks take their properties from the extension too, and any property set in the task overrides the extension's one. In a task, the app's name property is `appName`, as `name` is the task's own name.

## Minimal configuration

### With the default task

```groovy
javapackager {
    mainClass = 'com.example.Main'
}
```

Run `gradle package`. With Gradle, `bundleJre` is `true` by default, so this bundles a customized JRE. Add `bundleJre = false` to leave it out (users then need Java installed).

### With your own task

```groovy
tasks.register('packageMyApp', io.github.javapackager.gradle.PackageTask) {
    dependsOn build
    mainClass = 'com.example.Main'
}
```

Run `gradle packageMyApp`.

## Bundle a full JRE

```groovy
javapackager {
    mainClass = 'com.example.Main'
    customizedJre = false
}
```

## Bundle an existing JRE

```groovy
javapackager {
    mainClass = 'com.example.Main'
    jrePath = file('C:/Program Files/Java/jre1.8.0_231')
}
```

## Bundle your own fat JAR

```groovy
javapackager {
    mainClass = 'com.example.Main'
    runnableJar = file('path/to/your/own/fat.jar')
    copyDependencies = false
}
```

## Several executions

```groovy
import io.github.javapackager.gradle.PackageTask

javapackager {
    // common configuration
    mainClass = 'com.example.Main'
}
tasks.register('packageMyAppWithJRE', PackageTask) {
    dependsOn build
    appName = 'Sample'
    bundleJre = true
}
tasks.register('packageMyAppWithoutJRE', PackageTask) {
    dependsOn build
    appName = 'Sample-nojre'
    bundleJre = false
}
tasks.register('packageMyApp') {
    dependsOn 'packageMyAppWithJRE', 'packageMyAppWithoutJRE'
}
```

On Windows, `gradle packageMyApp` generates (among others) `Sample_x.y.z.exe` with a bundled JRE and `Sample-nojre_x.y.z.exe` without one, plus their MSI installers if WiX is installed.

## Several platforms

```groovy
import io.github.javapackager.gradle.PackageTask

javapackager {
    // common configuration
    mainClass = 'com.example.Main'
    generateInstaller = false
}
tasks.register('packageMyAppForLinux', PackageTask) {
    dependsOn build
    platform = 'linux'
    createTarball = true
    jdkPath = file('X:/path/to/linux/jdk')
}
tasks.register('packageMyAppForMac', PackageTask) {
    dependsOn build
    platform = 'mac'
    createTarball = true
    jdkPath = file('X:/path/to/mac/jdk')
}
tasks.register('packageMyAppForWindows', PackageTask) {
    dependsOn build
    platform = 'windows'
    createZipball = true
}
tasks.register('packageMyApp') {
    dependsOn 'packageMyAppForLinux', 'packageMyAppForMac', 'packageMyAppForWindows'
}
```

Run on Windows, `gradle packageMyApp` generates:

- `${name}-${version}-linux.tar.gz` with the GNU/Linux app.
- `${name}-${version}-mac.tar.gz` with the macOS app.
- `${name}-${version}-windows.zip` with the Windows app.

Each one includes a customized JRE for its platform. The Windows app uses the JDK running the build; the others need a JDK for their platform, with its `jmods` folder, in `jdkPath` (see [bundling a JRE for other platforms](../guides/bundling-a-jre.md#other-platforms)). Built on GNU/Linux or macOS, the Windows task needs a Windows JDK in `jdkPath` instead.
