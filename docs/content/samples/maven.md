---
title: Maven samples
description: Ready-to-copy Maven configurations
---

Replace `2.0.0` with the [latest version](https://central.sonatype.com/artifact/io.github.javapackager/javapackager).

## Minimal configuration

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
            </configuration>
        </execution>
    </executions>
</plugin>
```

:::caution
This configuration doesn't bundle a JRE, so users need Java installed to run the app.
:::

JavaPackager takes some properties from the `pom.xml`, so you don't need to repeat them. For example, `mainClass` from `exec.mainClass`:

```xml
<project>
    <properties>
        <exec.mainClass>com.example.Main</exec.mainClass>
    </properties>
    <build>
        <plugins>
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
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

## Bundle a customized JRE

```xml
<configuration>
    <mainClass>com.example.Main</mainClass>
    <bundleJre>true</bundleJre>
</configuration>
```

`customizedJre` is `true` by default. See [bundling a JRE](../guides/bundling-a-jre.md).

## Bundle a full JRE

```xml
<configuration>
    <mainClass>com.example.Main</mainClass>
    <bundleJre>true</bundleJre>
    <customizedJre>false</customizedJre>
</configuration>
```

## Bundle an existing JRE

```xml
<configuration>
    <mainClass>com.example.Main</mainClass>
    <bundleJre>true</bundleJre>
    <jrePath>C:\Program Files\Java\jre1.8.0_311</jrePath>
</configuration>
```

## Bundle your own fat JAR

```xml
<configuration>
    <mainClass>com.example.Main</mainClass>
    <bundleJre>true</bundleJre>
    <runnableJar>path/to/your/own/fat.jar</runnableJar>
    <copyDependencies>false</copyDependencies>
</configuration>
```

## Several executions

Settings common to every execution go in the plugin's `configuration`, and each execution adds its own:

```xml
<plugin>
    <groupId>io.github.javapackager</groupId>
    <artifactId>javapackager</artifactId>
    <version>2.0.0</version>
    <configuration>
        <mainClass>com.example.Main</mainClass>
    </configuration>
    <executions>
        <execution>
            <id>bundle-with-jre</id>
            <phase>package</phase>
            <goals>
                <goal>package</goal>
            </goals>
            <configuration>
                <name>Sample</name>
                <bundleJre>true</bundleJre>
            </configuration>
        </execution>
        <execution>
            <id>bundle-without-jre</id>
            <phase>package</phase>
            <goals>
                <goal>package</goal>
            </goals>
            <configuration>
                <name>Sample-nojre</name>
                <bundleJre>false</bundleJre>
            </configuration>
        </execution>
    </executions>
</plugin>
```

On Windows, this generates (among others) `Sample_x.y.z.exe` with a bundled JRE and `Sample-nojre_x.y.z.exe` without one, plus their MSI installers if WiX is installed.

## Several platforms

```xml
<plugin>
    <groupId>io.github.javapackager</groupId>
    <artifactId>javapackager</artifactId>
    <version>2.0.0</version>
    <configuration>
        <mainClass>com.example.Main</mainClass>
        <bundleJre>true</bundleJre>
        <generateInstaller>false</generateInstaller>
    </configuration>
    <executions>
        <execution>
            <id>bundling-for-windows</id>
            <phase>package</phase>
            <goals>
                <goal>package</goal>
            </goals>
            <configuration>
                <platform>windows</platform>
                <createZipball>true</createZipball>
            </configuration>
        </execution>
        <execution>
            <id>bundling-for-linux</id>
            <phase>package</phase>
            <goals>
                <goal>package</goal>
            </goals>
            <configuration>
                <platform>linux</platform>
                <createTarball>true</createTarball>
                <jdkPath>X:\path\to\linux\jdk</jdkPath>
            </configuration>
        </execution>
        <execution>
            <id>bundling-for-mac</id>
            <phase>package</phase>
            <goals>
                <goal>package</goal>
            </goals>
            <configuration>
                <platform>mac</platform>
                <createTarball>true</createTarball>
                <jdkPath>X:\path\to\mac\jdk</jdkPath>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Run on Windows, this generates:

- `${name}-${version}-windows.zip` with the Windows app.
- `${name}-${version}-linux.tar.gz` with the GNU/Linux app.
- `${name}-${version}-mac.tar.gz` with the macOS app.

Each one includes a customized JRE for its platform. The Windows app uses the JDK running the build; the others need a JDK for their platform, with its `jmods` folder, in `jdkPath` (see [bundling a JRE for other platforms](../guides/bundling-a-jre.md#other-platforms)). Built on GNU/Linux or macOS, the Windows execution needs a Windows JDK in `jdkPath` instead.
