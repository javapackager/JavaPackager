# JavaPackager

[![Maven Central](https://img.shields.io/maven-central/v/io.github.javapackager/javapackager)](https://central.sonatype.com/artifact/io.github.javapackager/javapackager)
[![GPL-3.0](https://img.shields.io/badge/license-GPL--3.0-%250778B9.svg)](https://www.gnu.org/licenses/gpl-3.0.html)
[![Docs](https://img.shields.io/badge/docs-javapackager.github.io-8a5a2b)](https://javapackager.github.io/JavaPackager/)

JavaPackager is a hybrid plugin for **Maven** and **Gradle** that packages Java applications as native Windows, macOS and GNU/Linux executables, and generates installers for them: Inno Setup, MSI, DMG, PKG, DEB, RPM and AppImage, with an optional customized JRE.

**:books: Documentation: https://javapackager.github.io/JavaPackager/**

> [!WARNING]
> Since 2.0.0 the coordinates are `io.github.javapackager` (`groupId`, Gradle plugin id and Java packages). See [Upgrading to 2.0](https://javapackager.github.io/JavaPackager/getting-started/upgrading).

## Quick start

### Maven

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

```bash
mvn package
```

### Gradle

```groovy
plugins {
    id 'io.github.javapackager' version '2.0.0'
}

javapackager {
    mainClass = 'com.example.Main'
}
```

```bash
gradle package
```

The app and its installers are generated in `target/` or `build/`. See the [documentation](https://javapackager.github.io/JavaPackager/) for every property, platform and guide.

## Project maintainers needed

JavaPackager has been maintained for many years, but its maintainers have little time. To keep it alive, we're looking for contributors to help with issues, reviews and documentation.

:point_right: **[Join the discussion](https://github.com/javapackager/JavaPackager/discussions/460)**

## Apps packaged with JavaPackager

- [AstroImageJ](http://astroimagej.com/)
- [Astro Pixel Processor](https://www.astropixelprocessor.com/)
- [GistFX](https://github.com/RedmondSims/GistFX)
- [Spektar Design Lab](https://spektar.io/)

## Contributing

Pull requests go to the `devel` branch. See [Contributing](https://javapackager.github.io/JavaPackager/contributing) to build the plugin and run the smoke tests, and the [releases](https://github.com/javapackager/JavaPackager/releases) for what changed in each version.

JavaPackager is released under the [GPL-3.0](LICENSE) license.
