**How to test `{{VERSION}}`**

> [!IMPORTANT]
> Since 2.0.0 the `groupId`, the Gradle plugin id and the Java packages are `io.github.javapackager` (see the [README in `devel`](https://github.com/javapackager/JavaPackager/blob/devel/README.md)). On macOS, `macStartup` defaults to `UNIVERSAL`.

Maven, add the snapshots repository and use the new coordinates:

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

Gradle:

```groovy
buildscript {
    repositories {
        maven { url = 'https://central.sonatype.com/repository/maven-snapshots/' }
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath 'io.github.javapackager:javapackager:{{VERSION}}'
    }
}

apply plugin: 'io.github.javapackager'

task packageMyApp(type: io.github.javapackager.gradle.PackageTask, dependsOn: build) {
    [...]
}
```
