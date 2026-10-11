---
title: Several executions and platforms
description: Package the same app several times, or for several platforms, in one build
---

One build can package the app several times with different settings: with and without a JRE, with different names, or for several platforms.

## Maven: several executions

Put the common settings in the plugin's `configuration` and add one `execution` per package, each with an `id` and its own settings. Every execution runs the `package` goal:

```xml
<plugin>
    <groupId>io.github.javapackager</groupId>
    <artifactId>javapackager</artifactId>
    <version>2.0.0</version>
    <configuration>
        <mainClass>com.example.Main</mainClass>
        <bundleJre>true</bundleJre>
    </configuration>
    <executions>
        <execution>
            <id>package-full</id>
            <phase>package</phase>
            <goals>
                <goal>package</goal>
            </goals>
        </execution>
        <execution>
            <id>package-lite</id>
            <phase>package</phase>
            <goals>
                <goal>package</goal>
            </goals>
            <configuration>
                <name>MyApp-lite</name>
                <bundleJre>false</bundleJre>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## Gradle: several tasks

Put the common settings in the `javapackager` extension and register one `PackageTask` per package. A task's properties override the extension's ones. In a task, the app's name is `appName`:

```groovy
import io.github.javapackager.gradle.PackageTask

javapackager {
    mainClass = 'com.example.Main'
}
tasks.register('packageLite', PackageTask) {
    dependsOn build
    appName = 'MyApp-lite'
    bundleJre = false
}
```

`gradle package packageLite` builds both. To run several tasks with one name, register a task that depends on them:

```groovy
tasks.register('packageAll') {
    dependsOn 'package', 'packageLite'
}
```

:::note
A `linuxConfig`, `macConfig` or `winConfig` block in a task replaces the extension's whole block; it isn't merged property by property.
:::

## Several platforms

Each package targets the `platform` you set (`auto`, the platform running the build, by default). Use a different name or bundle format per platform, so packages don't overwrite each other: zipballs and tarballs include the platform in their name (`${name}-${version}-${platform}.zip`).

When packaging for another platform:

- The app folder, zipball and tarball can be built on any platform. DEB and RPM packages too.
- The other installers need their own platform: Windows installers need Windows, DMG and PKG need macOS, AppImage needs GNU/Linux. They are skipped elsewhere, so set `generateInstaller` to `false` for those packages.
- A bundled JRE needs a JDK for the target platform in `jdkPath` (see [bundling a JRE for other platforms](bundling-a-jre.md#other-platforms)).
- Dependencies with native code are copied for the platform running the build. Use classifiers or profiles to pick the right ones for each target.

Complete examples are in the [Maven samples](../samples/maven.md#several-platforms) and the [Gradle samples](../samples/gradle.md#several-platforms).

To build every installer, the usual approach is a CI workflow that packages the app on each platform, e.g. GitHub Actions with Windows, macOS and Ubuntu runners.
