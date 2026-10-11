**How to test `{{VERSION}}`**

> [!IMPORTANT]
> Since 2.0.0 the `groupId`, the Gradle plugin id and the Java packages are `io.github.javapackager` (see [Upgrading to 2.0](https://javapackager.github.io/JavaPackager/getting-started/upgrading)).

Maven, add the snapshots repository and use the snapshot version:

```xml
<pluginRepositories>
    <pluginRepository>
        <id>central-portal-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <releases><enabled>false</enabled></releases>
        <snapshots><enabled>true</enabled></snapshots>
    </pluginRepository>
</pluginRepositories>
[...]
<plugin>
    <groupId>io.github.javapackager</groupId>
    <artifactId>javapackager</artifactId>
    <version>{{VERSION}}</version>
    [...]
</plugin>
```

Gradle, add the snapshots repository in `settings.gradle`:

```groovy
pluginManagement {
    repositories {
        maven { url = 'https://central.sonatype.com/repository/maven-snapshots/' }
        gradlePluginPortal()
    }
}
```

and use the snapshot version in `build.gradle`:

```groovy
plugins {
    id 'io.github.javapackager' version '{{VERSION}}'
}
```

See [Snapshot versions](https://javapackager.github.io/JavaPackager/guides/snapshots) for more.
