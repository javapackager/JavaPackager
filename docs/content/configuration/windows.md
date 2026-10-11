---
title: Windows
description: winConfig properties, EXE creation tools, Inno Setup, MSI and MSM, registry entries
---

Windows-specific properties go in `winConfig`. Code signing is explained in the [code signing guide](../guides/code-signing.md#windows), and the tools to install in the [Windows tools guide](../guides/windows-tools.md).

```xml title="Maven"
<winConfig>
    <icoFile>assets/windows/MyApp.ico</icoFile>
    <exeCreationTool>launch4j</exeCreationTool>
    <headerType>gui</headerType>
    <generateSetup>true</generateSetup>
    <generateMsi>true</generateMsi>
    <msiUpgradeCode>3f1c0e5a-8a7e-4f53-9d1b-0a6a2d1f6c11</msiUpgradeCode>
    <productVersion>1.2.0.0</productVersion>
    <setupMode>askTheUser</setupMode>
    <setupLanguages>
        <english>compiler:Default.isl</english>
        <spanish>compiler:Languages\Spanish.isl</spanish>
    </setupLanguages>
    <disableRunAfterInstall>false</disableRunAfterInstall>
</winConfig>
```

```groovy title="Gradle"
javapackager {
    winConfig {
        icoFile = file('assets/windows/MyApp.ico')
        exeCreationTool = 'launch4j'
        headerType = 'gui'
        generateSetup = true
        generateMsi = true
        msiUpgradeCode = '3f1c0e5a-8a7e-4f53-9d1b-0a6a2d1f6c11'
        productVersion = '1.2.0.0'
        setupMode = 'askTheUser'
        setupLanguages = [
            english: 'compiler:Default.isl',
            spanish: 'compiler:Languages\\Spanish.isl'
        ]
        disableRunAfterInstall = false
    }
}
```

## General properties

| Property | Default | Description |
| --- | --- | --- |
| `icoFile` | `${assetsDir}/windows/${name}.ico`, or the default icon | Icon of the EXE, Setup and MSI, in ICO format. |
| `exeCreationTool` | `launch4j` | Tool that builds the app EXE: `launch4j`, `winrun4j` or `why`. See [EXE creation tools](#exe-creation-tools). |
| `generateSetup` | `true` | Generates the Inno Setup installer `${name}_${version}.exe`. Needs `iscc`. |
| `generateMsi` | `true` | Generates the MSI installer `${name}_${version}.msi`. Needs WiX. |
| `generateMsm` | `false` | Generates the MSI merge module `${name}_${version}.msm`. It's also generated whenever `generateMsi` is `true`, because the MSI is built from it. |
| `msiUpgradeCode` | derived from `organizationName` and `name` | `UpgradeCode` of the MSI: a new MSI with the same code upgrades the installed version. The build logs it; set it to keep it if the app name or organization change. |
| `signing` | | Code signing configuration. See [code signing](../guides/code-signing.md#windows). |
| `registry` | | Registry values written by the installers. See [Registry entries](#registry-entries). |

Installers can only be built on Windows; on other platforms they are skipped, unless `forceInstaller` is `true`.

## EXE properties

| Property | Default | Description |
| --- | --- | --- |
| `headerType` | `gui` | `gui` or `console` (a console window shows the app's output). Launch4j only. |
| `wrapJar` | `true` | Embeds the JAR in the EXE; if `false`, the JAR is copied next to it. Launch4j only. |
| `vmLocation` | `bin/client/jvm.dll` or `bin/server/jvm.dll` | `jvm.dll` path relative to the bundled JRE. WinRun4J only, with `bundleJre`. |
| `companyName` | `organizationName` | Company name in the EXE version info. |
| `fileDescription` | `description` | File description in the EXE version info. |
| `fileVersion` | `version` as `x.x.x.x` | File version (`x.x.x.x`). |
| `txtFileVersion` | `version` | File version as text. Launch4j with Maven only. |
| `productVersion` | `version` as `x.x.x.x` | Product version (`x.x.x.x`). Also the MSI version. |
| `txtProductVersion` | `version` | Product version as text. Launch4j only. |
| `productName` | `name` | Product name. |
| `internalName` | `name` | Internal name. |
| `originalFilename` | `${name}.exe` | Original file name. Not applied by Launch4j with Gradle. |
| `copyright` | `organizationName` | Copyright. Launch4j only. |
| `trademarks` | `organizationName` | Trademarks. Launch4j only. |
| `language` | | Version info language, in Launch4j's format (e.g. `ENGLISH_US`). Launch4j only. |

## Setup properties

| Property | Default | Description |
| --- | --- | --- |
| `setupMode` | `installForAllUsers` | `installForAllUsers`, `installForCurrentUser` or `askTheUser`. See [Setup modes](#setup-modes). |
| `setupLanguages` | English and Spanish | Map of Inno Setup languages to `.isl` message files (several files separated by commas). Setting it replaces the default languages. |
| `shortcutName` | `displayName` | Name of the desktop shortcut (Setup) and the Start menu shortcut (MSI). |
| `createDesktopIconTask` | `true` | Setup offers an optional "Create a desktop icon" task (unchecked by default). |
| `disableDirPage` | `true` | Hides the **Select Destination Location** page. |
| `disableProgramGroupPage` | `true` | Hides the **Select Start Menu Folder** page. |
| `disableFinishedPage` | `true` | Hides the **Setup Completed** page. |
| `disableWelcomePage` | `true` | Hides the **Welcome** page. |
| `disableRunAfterInstall` | `true` | If `false`, Setup launches the app after installing it (not in silent installs). |
| `removeOldLibs` | `false` | Deletes the `libs` folder of a previous installation before installing, so old dependency JARs don't stay. |

## EXE creation tools

| Feature | `launch4j` (default) | `winrun4j` | `why` |
| --- | --- | --- | --- |
| Builds on GNU/Linux and macOS | Yes | No | No |
| `headerType` (console or GUI) | Yes | No | No |
| `wrapJar` | Yes | No (JAR next to the EXE) | No (JAR next to the EXE) |
| `vmArgs` | Yes | Yes | No: use a [runtime options file](../guides/runtime-jvm-options.md) |
| `jreMinVersion` (no bundled JRE) | Yes | Yes | Yes |
| Runtime options file (`${name}.l4j.ini`) | Yes | No | Yes |
| Administrator manifest, icon, version info | Yes | Yes | Yes |

All three embed a manifest that requests administrator privileges when `administratorRequired` is `true`.

- **[Launch4j](https://launch4j.sourceforge.net/)** builds a 32-bit EXE, which runs a 64-bit JRE too. With Maven it runs `launch4j-maven-plugin`; with Gradle, the Launch4j Gradle library. VM arguments containing spaces are quoted.
- **[WinRun4J](https://winrun4j.sourceforge.net/)** uses a 32 or 64-bit launcher depending on `arch`, and reads its settings from `${name}.ini`.
- **[Why](https://github.com/AstroImageJ/Why)** is a small native launcher; it reads `launcher.ini` and the runtime options file. You can replace the launcher with your own `${assetsDir}/windows/JavaLauncher.exe`.

:::note
`appArgs` are not supported on Windows yet: they are ignored by the three tools.
:::

With a [bootstrap script](common.md#scripts), a `${name}.vbs` script runs it and then the EXE, and it becomes the app's entry point in the installers.

## Setup modes

The Setup is built with Inno Setup. It installs the app in `Program Files` (for all users) or in `%LOCALAPPDATA%\Programs` (for the current user), adds a Start menu entry, registers the file associations, and can be uninstalled from the Windows settings. Its `AppId` is the app `name`, so a new Setup of the same app upgrades the installed one.

| `setupMode` | Behaviour | Override from the command line |
| --- | --- | --- |
| `installForAllUsers` | Needs administrator privileges; installs for all users. | `/CURRENTUSER` installs for the current user |
| `installForCurrentUser` | No administrator privileges; installs for the current user. | `/ALLUSERS` installs for all users |
| `askTheUser` | Asks the user. | `/ALLUSERS` or `/CURRENTUSER` |

When the Setup launches the app after installing it, the app runs as the user who started the installer, not as the administrator who approved it, unless `administratorRequired` is `true`.

## MSI and MSM

The MSI and the MSM are built with [WiX Toolset](https://wixtoolset.org/). If `wix` (WiX 4 or newer) is in the `PATH` it's used; otherwise JavaPackager uses `candle` and `light` (WiX 3).

The MSM (merge module) contains the whole app, a Start menu shortcut and the registry entries; you can merge it into your own MSI. The MSI installs that module for all users in `Program Files` (x64).

:::caution MSI upgrades
A new MSI upgrades the installed version when both have the same `msiUpgradeCode`. By default it's derived from `organizationName` and `name`, so it only changes if they do: if you rename the app or the organization, set `msiUpgradeCode` to the previous code (the build logs it). The MSI version is `productVersion`, by default `version` as `x.x.x.x` (e.g. `2.1.0-SNAPSHOT` becomes `2.1.0.0`); if `version` doesn't fit an MSI version (major and minor up to 255), it's `1.0.0.0` and you should set it. Installing an older version over a newer one is refused.
:::

The MSI doesn't create a desktop shortcut nor register file associations.

## Registry entries

`registry` adds values to the Windows Registry during installation.

| Field | Default | Description |
| --- | --- | --- |
| `key` | | `ROOT:subkey`, e.g. `HKCU:Software\MyApp`. Use `\` as separator. Roots: `HKCU`, `HKLM`, `HKCR`, `HKU` (and `HKCC`, Setup only). |
| `valueName` | | Value name; empty for the key's default value. |
| `valueType` | `REG_SZ` | `REG_SZ`, `REG_EXPAND_SZ`, `REG_MULTI_SZ`, `REG_DWORD`, `REG_QWORD` or `REG_BINARY`. |
| `valueData` | `""` | Value data. |

On uninstall, the Setup removes the values it wrote; the MSI removes the whole key, including any other values in it.

This example adds a `greeting` string value with `hello` to `HKEY_CURRENT_USER\Software\MyApp`:

```xml title="Maven"
<registry>
    <entries>
        <entry>
            <key>HKCU:Software\MyApp</key>
            <valueName>greeting</valueName>
            <valueType>REG_SZ</valueType>
            <valueData>hello</valueData>
        </entry>
    </entries>
</registry>
```

```groovy title="Gradle"
import io.github.javapackager.model.*

javapackager {
    winConfig {
        registry = new Registry([
            new RegistryEntry('HKCU:Software\\MyApp', 'greeting', ValueType.REG_SZ, 'hello')
        ])
    }
}
```
