---
title: Common properties
description: Every JavaPackager property shared by all platforms, with its default value
---

These properties apply to every platform. Platform-specific ones are in [GNU/Linux](linux.md), [macOS](macos.md) and [Windows](windows.md).

With Maven, properties go in the plugin's `<configuration>`; most can also be set from the command line, e.g. `mvn package -DbundleJre=true`. With Gradle, they go in the `javapackager` extension or in your own `PackageTask` tasks, where the app's `name` and `description` are called `appName` and `appDescription`.

When a default differs between Maven and Gradle, both are given.

## App information

| Property | Default | Description |
| --- | --- | --- |
| `mainClass` | Maven: the `exec.mainClass` property | **Mandatory.** Fully qualified name of the app's main class. |
| `name` | Maven: the project's `name`, or `artifactId`. Gradle: the project name | App name: names the app folder, the executable, the installers and the icon looked up in `assetsDir`. It must be a valid file name. |
| `displayName` | Maven: the project's `name`. Gradle: `name` | Name shown to users: menus, shortcuts, the macOS menu bar. |
| `description` | the project's description, or `displayName` | App description, used by the packages and installers. |
| `version` | the project's version | App version, used in the installer names and their metadata. |
| `url` | Maven: the project's `url` | App website: DEB homepage and Windows signatures. |
| `organizationName` | Maven: the project's organization, or `ACME` | Organization: DEB maintainer, Windows publisher and version info, macOS copyright. |
| `organizationUrl` | Maven: the project's organization URL | Organization website (Setup). |
| `organizationEmail` | | Organization email (DEB maintainer). |
| `licenseFile` | Maven: the project's first license URL (downloaded), or `LICENSE`. Gradle: `LICENSE` in the root project | License copied into the app and shown by the Windows installers. |

## Packaging

| Property | Default | Description |
| --- | --- | --- |
| `platform` | `auto` | Target platform: `auto` (the one running the build), `linux`, `mac` or `windows`. See [several platforms](../guides/multiple-executions.md#several-platforms). |
| `arch` | the architecture running the build | Target architecture: `x64`, `x86` or `aarch64`. Used by the DEB, RPM, MSI and WinRun4J. On Windows only `x64` and `x86` are valid. |
| `outputDirectory` | Maven: `target`. Gradle: `build` | Where everything is generated. |
| `assetsDir` | `assets` in the project folder | Your icons and template overrides. See [icons and assets](../guides/icons-and-assets.md). |
| `generateInstaller` | `true` | Generates the installers. Each one can also be disabled in its platform config. |
| `forceInstaller` | `false` | Builds the AppImage, DMG, PKG, Setup, MSI and MSM even when the target platform isn't the one running the build. They need their native tools, so it rarely works. |
| `createZipball` | `false` | Creates `${name}-${version}-${platform}.zip` with the app folder. |
| `zipballName` | `${name}-${version}-${platform}` | Zipball name, without extension. Maven only. |
| `createTarball` | `false` | Creates `${name}-${version}-${platform}.tar.gz` with the app folder. |
| `tarballName` | `${name}-${version}-${platform}` | Tarball name, without extension. Maven only. |

## App contents

| Property | Default | Description |
| --- | --- | --- |
| `runnableJar` | | Your own JAR to bundle (e.g. a fat JAR). If not set, JavaPackager builds a runnable JAR from your project. |
| `copyDependencies` | `true` | Copies the runtime dependencies into the `libs` folder. Set it to `false` with a fat JAR. |
| `additionalResources` | | Files and folders copied into the app folder (`Contents/Resources` on macOS). |
| `manifest` | | Additional entries and sections in the runnable JAR's manifest. See [manifest](manifest.md). |
| `fileAssociations` | | File types opened by the app. See [file associations](file-associations.md). |
| `scripts` | | A bootstrap script run before the app. See [Scripts](#scripts). |
| `extra` | | Your own values for [custom templates](../guides/templates.md). |
| `templates` | | Template options (byte order mark). See [templates](../guides/templates.md#byte-order-mark). |

## Bundled JRE

See [bundling a JRE](../guides/bundling-a-jre.md) for how these work together.

| Property | Default | Description |
| --- | --- | --- |
| `bundleJre` | Maven: `false`. Gradle: `true` | Bundles a JRE with the app. |
| `customizedJre` | `true` | Builds the JRE with only the modules the app needs. Otherwise, all modules are included. |
| `modules` | | Modules of the customized JRE. If set, `jdeps` isn't used to find them. |
| `additionalModules` | | Modules added to the ones found by `jdeps` or listed in `modules`. |
| `additionalModulePaths` | | Folders with more modules (e.g. JavaFX `jmods`), for `jdeps` and `jlink`. |
| `additionalJlinkArgs` | | Arguments appended to the `jlink` call, e.g. `--include-locales=en,es`. |
| `jrePath` | | An existing JRE to bundle, instead of building one. |
| `jdkPath` | the JDK running Maven or Gradle | JDK providing the JRE's modules; a JDK for the target platform when it isn't the one running the build. |
| `packagingJdk` | Maven: the JDK running Maven. Gradle: the Java toolchain, or the JDK running Gradle | JDK whose `jdeps` and `jlink` are run. |
| `jreDirectoryName` | `jre` | Name of the JRE folder in the app. |
| `jreMinVersion` | Gradle: the Java `targetCompatibility`, for Launch4j | Minimum Java version the app needs, checked when no JRE is bundled. |

## Runtime

| Property | Default | Description |
| --- | --- | --- |
| `vmArgs` | | JVM options, one per element, e.g. `-Xmx1g`. Not supported by the Why launcher on Windows. |
| `appArgs` | | Default arguments for the app. GNU/Linux and macOS only. |
| `classpath` | | Extra class path entries, separated by `;` or `:`. Relative entries are resolved against the app folder. |
| `envPath` | | Value of the `PATH` environment variable for the app (GNU/Linux and macOS). It replaces the user's `PATH`: include `$PATH` in it to extend it. |
| `useResourcesAsWorkingDir` | `true` | Uses the app folder as working directory (always `true` on macOS, where it's `Contents/Resources`). |
| `administratorRequired` | `false` | Runs the app with administrator privileges: `pkexec` on GNU/Linux, an administrator password prompt on macOS, a UAC prompt on Windows. |

JVM options can also be changed after packaging, without rebuilding the app: see [runtime JVM options](../guides/runtime-jvm-options.md).

## Gradle only

| Property | Default | Description |
| --- | --- | --- |
| `duplicatesStrategy` | `WARN` | What to do when two dependencies copied into `libs` have the same file name (Gradle's `DuplicatesStrategy`). |

## Syntax

Lists and maps:

```xml title="Maven"
<vmArgs>
    <vmArg>-Xmx1g</vmArg>
    <vmArg>-Dapp.title=My App</vmArg>
</vmArgs>
<additionalResources>
    <additionalResource>src/main/config</additionalResource>
    <additionalResource>README.txt</additionalResource>
</additionalResources>
<extra>
    <myKey>my value</myKey>
</extra>
```

```groovy title="Gradle"
javapackager {
    vmArgs = [ '-Xmx1g', '-Dapp.title=My App' ]
    additionalResources = [ file('src/main/config'), file('README.txt') ]
    extra = [ myKey: 'my value' ]
}
```

In Gradle, enum values like `platform` can be strings: `platform = 'windows'`.

## Scripts

`scripts` sets a bootstrap script, run every time the app starts, just before it:

```xml title="Maven"
<scripts>
    <bootstrap>src/main/scripts/bootstrap.sh</bootstrap>
</scripts>
```

```groovy title="Gradle"
javapackager {
    scripts {
        bootstrap = file('src/main/scripts/bootstrap.sh')
    }
}
```

The script is copied into the app's `scripts` folder.

| Platform | How it runs |
| --- | --- |
| GNU/Linux | The startup script runs it, if it's executable, before starting Java. |
| macOS | The launcher runs it, if it's executable, before starting Java. |
| Windows | A `${name}.vbs` script runs it, and then the EXE; installer shortcuts point to the `.vbs`. |

On Windows, use a script Windows can run (`.bat`, `.cmd`, `.vbs`...). `scripts` also accepts `preInstall` and `postInstall`, but they aren't implemented yet and are ignored.

## Not working as expected

- `iconFile` is ignored: set the icon with `linuxConfig.pngFile`, `macConfig.icnsFile` and `winConfig.icoFile`, or put it in `assetsDir` (see [icons and assets](../guides/icons-and-assets.md)).
- `zipballName` and `tarballName` are ignored by the Gradle plugin.
