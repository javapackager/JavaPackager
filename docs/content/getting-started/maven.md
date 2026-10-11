---
title: Maven
description: Package your app with the JavaPackager Maven plugin
---

JavaPackager needs Maven 3.6 or newer and runs on Java 8 or newer. To bundle a customized JRE, build with a JDK 9 or newer.

## Add the plugin

Add the plugin to the `build/plugins` section of your `pom.xml`, with an execution of the `package` goal:

```xml
<plugin>
    <groupId>io.github.javapackager</groupId>
    <artifactId>javapackager</artifactId>
    <version>2.0.0</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>package</goal>
            </goals>
            <configuration>
                <mainClass>com.example.Main</mainClass>
                <bundleJre>true</bundleJre>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Check the latest version on [Maven Central](https://central.sonatype.com/artifact/io.github.javapackager/javapackager) or in the [releases](https://github.com/javapackager/JavaPackager/releases).

`mainClass` is the only mandatory property, and it can be omitted if the `exec.mainClass` property is set in your `pom.xml`:

```xml
<properties>
    <exec.mainClass>com.example.Main</exec.mainClass>
</properties>
```

## Package your app

```bash
mvn package
```

The app and its installers are generated in `target/`. See [generated artifacts](generated-artifacts.md) for what you get, and which tools each installer needs.

By default the app doesn't bundle a JRE (`bundleJre` is `false`), so users need Java installed. With `bundleJre` set to `true`, as above, a [customized JRE](../guides/bundling-a-jre.md) with only the modules your app needs is bundled with it.

## A fuller example

```xml
<configuration>
    <!-- mandatory -->
    <mainClass>com.example.Main</mainClass>
    <!-- optional -->
    <name>MyApp</name>
    <displayName>My App</displayName>
    <bundleJre>true</bundleJre>
    <generateInstaller>true</generateInstaller>
    <administratorRequired>false</administratorRequired>
    <platform>auto</platform>
    <vmArgs>
        <vmArg>-Xmx512m</vmArg>
    </vmArgs>
    <additionalResources>
        <additionalResource>README.md</additionalResource>
        <additionalResource>config</additionalResource>
    </additionalResources>
    <linuxConfig>...</linuxConfig>
    <macConfig>...</macConfig>
    <winConfig>...</winConfig>
</configuration>
```

Every property is described in the [configuration reference](../configuration/common.md), and the platform-specific ones in [GNU/Linux](../configuration/linux.md), [macOS](../configuration/macos.md) and [Windows](../configuration/windows.md).

## Properties taken from your project

JavaPackager takes some values from the `pom.xml`, so you don't need to repeat them:

| Property | Taken from |
| --- | --- |
| `name` | `name`, or `artifactId` if it has none |
| `displayName` | `name` (the project's, even if the plugin's `name` is set) |
| `version` | `version` |
| `description` | `description` |
| `url` | `url` |
| `organizationName`, `organizationUrl` | `organization` |
| `licenseFile` | the first license's `url` (downloaded), or a `LICENSE` file in the project folder |
| `mainClass` | the `exec.mainClass` property |

## More examples

See the [Maven samples](../samples/maven.md): bundling a full or an existing JRE, your own fat JAR, several executions and several platforms.
