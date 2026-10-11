---
title: Snapshot versions
---

Snapshot versions are built from the `devel` branch and contain the fixes and features that are not released yet. They are published to the [Central Portal snapshots repository](https://central.sonatype.com/repository/maven-snapshots/io/github/javapackager/javapackager/), so you need to add that repository to your build.

:::caution
Snapshots change without notice. Use them to test a fix, not in production builds.
:::

## Maven

Add the snapshots repository as a plugin repository in your `pom.xml`:

```xml
<pluginRepositories>
    <pluginRepository>
        <id>central-portal-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </pluginRepository>
</pluginRepositories>
```

Then use the snapshot version:

```xml
<plugin>
    <groupId>io.github.javapackager</groupId>
    <artifactId>javapackager</artifactId>
    <version>2.0.1-SNAPSHOT</version>
    [...]
</plugin>
```

## Gradle

Add the snapshots repository to the plugin repositories in `settings.gradle`, keeping the Gradle Plugin Portal:

```groovy
pluginManagement {
    repositories {
        maven {
            url = 'https://central.sonatype.com/repository/maven-snapshots/'
        }
        gradlePluginPortal()
    }
}
```

Then use the snapshot version in `build.gradle`:

```groovy
plugins {
    id 'io.github.javapackager' version '2.0.1-SNAPSHOT'
}
```

## A specific snapshot build

Each snapshot build has its own version, made of the base version, a timestamp and a build number, e.g. `2.0.1-20261011.101500-3`. Use it instead of `-SNAPSHOT` to stick to that build. The list of builds is in the version's [`maven-metadata.xml`](https://central.sonatype.com/repository/maven-snapshots/io/github/javapackager/javapackager/).
