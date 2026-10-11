---
title: GNU/Linux
description: linuxConfig properties, the startup script, DEB, RPM and AppImage
---

GNU/Linux-specific properties go in `linuxConfig`.

```xml title="Maven"
<linuxConfig>
    <pngFile>assets/linux/MyApp.png</pngFile>
    <generateDeb>true</generateDeb>
    <generateRpm>true</generateRpm>
    <generateAppImage>true</generateAppImage>
    <wrapJar>true</wrapJar>
    <installationPath>/opt</installationPath>
    <categories>
        <category>Utility</category>
        <category>Development</category>
    </categories>
</linuxConfig>
```

```groovy title="Gradle"
javapackager {
    linuxConfig {
        pngFile = file('assets/linux/MyApp.png')
        generateDeb = true
        generateRpm = true
        generateAppImage = true
        wrapJar = true
        installationPath = '/opt'
        categories = ['Utility', 'Development']
    }
}
```

## Properties

| Property | Default | Description |
| --- | --- | --- |
| `pngFile` | `${assetsDir}/linux/${name}.png`, or the default icon | App icon in PNG format. Name it `${name}.png`, as the desktop entry expects. |
| `generateDeb` | `true` | Generates the DEB package `${name}_${version}.deb`. |
| `generateRpm` | `true` | Generates the RPM package `${name}_${version}.rpm`. |
| `generateAppImage` | `true` | Generates the [AppImage](https://appimage.org/) `${name}_${version}.AppImage`. |
| `wrapJar` | `true` | `true`: the executable is the startup script with the JAR appended, in a single file. `false`: the JAR is next to the script. |
| `installationPath` | `/opt` | Folder where the DEB and RPM packages install the app (`${installationPath}/${name}`). |
| `categories` | `Utility` | [Main categories](https://specifications.freedesktop.org/menu-spec/latest/apa.html) of the app's desktop entry. |

## The startup script

The app's executable, `${name}`, is a Bash script (with the JAR appended when `wrapJar` is `true`, which Java can still run, as it reads a JAR from its end). When the app starts, the script:

1. Finds Java: the bundled JRE, or else `java` in the `PATH`, or else `$JAVA_HOME/bin/java`. If there's none, it shows "Java not installed" and exits. Without a bundled JRE, it also checks `jreMinVersion`.
2. Sets `PATH` to `envPath`, if given (include `$PATH` in it to extend it rather than replace it).
3. Changes to the app folder, if `useResourcesAsWorkingDir` is `true` (default).
4. Runs the [bootstrap script](common.md#scripts), if any.
5. Starts the app with `vmArgs`, the options in the [runtime options file](../guides/runtime-jvm-options.md), `appArgs` and the arguments it received. With `classpath`, it runs `java -cp <jar>:<classpath> <mainClass>`; otherwise `java -jar <jar>`.

Each element of `vmArgs` and `appArgs` is passed as a single argument, spaces included: write `-Xms256m` and `-Xmx1g` as two elements, not one.

With `administratorRequired`, the app runs through `pkexec`, which asks for an administrator password (it needs polkit and an authentication agent, standard on desktop distributions).

## DEB and RPM

Both packages are built in Java (with jdeb and Redline), so they can be built on any platform. They install:

- The app folder in `${installationPath}/${name}` (`/opt/${name}` by default).
- A desktop entry in `/usr/share/applications/${name}.desktop`, so the app shows in the applications menu.
- A link `/usr/local/bin/${name}`, so the app can be run from a terminal.
- With `fileAssociations`, a MIME types file in `/usr/share/mime/packages/${name}.xml`.

The DEB package maintainer is `organizationName` and `organizationEmail`, its homepage `url` and its description `description`. The architecture comes from `arch`. Neither package depends on a Java package, so bundle a JRE or tell your users to install Java.

In the RPM version, `-` is replaced by `_` (RPM doesn't allow it), e.g. `1.0.0-SNAPSHOT` becomes `1.0.0_SNAPSHOT`.

## AppImage

The AppImage is a single executable file that runs on most distributions without installation. JavaPackager downloads [`appimagetool`](https://github.com/AppImage/appimagetool) the first time (it needs network access), and the AppImage can only be built on GNU/Linux.

To run an AppImage, the user's system needs FUSE (`libfuse2` on many distributions), or the AppImage can be run with `--appimage-extract-and-run`.

## Building on other platforms

With `platform=linux` you can build the app folder, the DEB and RPM packages, the zipball and the tarball on Windows or macOS. To bundle a JRE, set `jdkPath` to a GNU/Linux JDK (see [bundling a JRE](../guides/bundling-a-jre.md#other-platforms)). The AppImage needs GNU/Linux.
