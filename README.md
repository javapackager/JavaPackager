<p align="center">
  <img src="docs/static/img/logo.png" alt="JavaPackager logo" width="160">
</p>

<h1 align="center">JavaPackager</h1>

<p align="center">
  <a href="https://central.sonatype.com/artifact/io.github.javapackager/javapackager"><img src="https://img.shields.io/maven-central/v/io.github.javapackager/javapackager" alt="Maven Central"></a>
  <a href="https://www.gnu.org/licenses/gpl-3.0.html"><img src="https://img.shields.io/badge/license-GPL--3.0-%250778B9.svg" alt="GPL-3.0"></a>
  <a href="https://javapackager.github.io/JavaPackager/"><img src="https://img.shields.io/badge/docs-javapackager.github.io-b55d00" alt="Docs"></a>
</p>

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

| App | Description | Stars |
| --- | --- | --- |
| [WePush](https://github.com/rememberber/WePush) | Bulk message sender: WeChat templates, SMS, email and more | ![GitHub stars](https://img.shields.io/github/stars/rememberber/WePush?style=flat&label=%E2%98%85) |
| [MooTool](https://github.com/rememberber/MooTool) | Handy tool set for developers | ![GitHub stars](https://img.shields.io/github/stars/rememberber/MooTool?style=flat&label=%E2%98%85) |
| [bilibili-backup](https://github.com/hzhilong/bilibili-backup) | Backup and restore of Bilibili accounts | ![GitHub stars](https://img.shields.io/github/stars/hzhilong/bilibili-backup?style=flat&label=%E2%98%85) |
| [RedisFX](https://github.com/tanhuang2016/RedisFX) | Lightweight Redis GUI client (JavaFX) | ![GitHub stars](https://img.shields.io/github/stars/tanhuang2016/RedisFX?style=flat&label=%E2%98%85) |
| [RedisFront](https://github.com/dromara/RedisFront) | Cross-platform Redis client (Swing) | ![GitHub stars](https://img.shields.io/github/stars/dromara/RedisFront?style=flat&label=%E2%98%85) |
| [MooInfo](https://github.com/rememberber/MooInfo) | System and hardware information viewer | ![GitHub stars](https://img.shields.io/github/stars/rememberber/MooInfo?style=flat&label=%E2%98%85) |

See [all the apps](https://javapackager.github.io/JavaPackager/#apps-packaged-with-javapackager) packaged with JavaPackager.

## Contributing

Pull requests go to the `devel` branch. See [Contributing](https://javapackager.github.io/JavaPackager/contributing) to build the plugin and run the smoke tests, and the [releases](https://github.com/javapackager/JavaPackager/releases) for what changed in each version.

JavaPackager is released under the [GPL-3.0](LICENSE) license.
