---
title: Templates
description: Customize launchers, desktop entries, Info.plist and installer scripts with your own Velocity templates
---

Many files JavaPackager generates (the GNU/Linux startup script, the macOS `Info.plist`, the Inno Setup and WiX scripts...) come from [Apache Velocity](https://velocity.apache.org/engine/2.4/user-guide.html) templates. You can replace any of them with your own.

## Overriding a template

Put a template with the same path in your `assetsDir`. For example, `assets/windows/iss.vtl` replaces the built-in Inno Setup script. JavaPackager looks in `assetsDir` first and falls back to its own templates.

Start from a copy of the [built-in template](https://github.com/javapackager/JavaPackager/tree/main/src/main/resources) of the version you use, and change only what you need.

:::caution
Your templates replace whole files, so they don't get the fixes of newer JavaPackager versions. Check them when upgrading.
:::

## Built-in templates

| Template | Generates | When |
| --- | --- | --- |
| `linux/startup.sh.vtl` | The app's startup script | Always |
| `linux/desktop.vtl` | The desktop entry | Always |
| `linux/desktop-appimage.vtl` | The AppImage desktop entry | AppImage |
| `linux/mime.xml.vtl` | The MIME types file | With file associations |
| `linux/control.vtl` | The DEB `control` file | DEB |
| `linux/assembly.xml.vtl` | The zipball and tarball layout | Maven bundles |
| `mac/Info.plist.vtl` | The app's `Info.plist` | Unless `customInfoPlist` |
| `mac/RuntimeInfo.plist.vtl` | The bundled JRE's `Info.plist` | With `bundleJre`, unless `customRuntimeInfoPlist` |
| `mac/startup.vtl` | The script that asks for an administrator password | With `administratorRequired` |
| `mac/entitlements.plist.vtl` | The default entitlements | Signing without `entitlements` |
| `mac/customize-dmg.applescript.vtl` | The DMG window layout | DMG |
| `mac/assembly.xml.vtl` | The zipball and tarball layout | Maven bundles |
| `windows/exe.manifest.vtl` | The EXE manifest (administrator privileges) | Always |
| `windows/ini.vtl` | The WinRun4J settings | `exeCreationTool` is `winrun4j` |
| `windows/why-ini.vtl` | The Why launcher settings | `exeCreationTool` is `why` |
| `windows/startup.vbs.vtl` | The script that runs the bootstrap script | With a bootstrap script |
| `windows/iss.vtl` | The Inno Setup script | Setup |
| `windows/wxs.vtl` | The WiX source of the MSI | MSI |
| `windows/msm.wxs.vtl` | The WiX source of the MSM | MSM |
| `windows/assembly.xml.vtl` | The zipball and tarball layout | Maven bundles |

## Variables

Templates can use these variables:

| Variable | Value |
| --- | --- |
| `$info` | The packager, with every configuration property (`$info.name`, `$info.version`, `$info.winConfig.productVersion`...) and runtime values such as `$info.appFolder`, `$info.jarFile`, `$info.executable` or `$info.iconFile`. |
| `$StringUtils` | Apache Commons Lang's [`StringUtils`](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/StringUtils.html), e.g. `$StringUtils.capitalize($info.name)`. |
| `$GUID` | Java's `UUID` class, e.g. `$GUID.randomUUID()`. |

## Your own values

To pass your own values to your templates, use the `extra` map:

```xml title="Maven"
<extra>
    <supportEmail>support@example.com</supportEmail>
</extra>
```

```groovy title="Gradle"
javapackager {
    extra = [ supportEmail: 'support@example.com' ]
}
```

And use them in a template as `$info.extra.supportEmail` or `${info.extra["supportEmail"]}`.

## Byte order mark

Generated files are UTF-8, without a byte order mark (BOM), except the Inno Setup script, which needs one. The `templates` property changes it per template:

```xml title="Maven"
<templates>
    <template>
        <name>windows/iss.vtl</name>
        <bom>false</bom>
    </template>
</templates>
```

```groovy title="Gradle"
javapackager {
    templates = [ new io.github.javapackager.model.Template('windows/iss.vtl', false) ]
}
```

## Velocity and shell scripts

Velocity reads `${name[@]}` (Bash array expansion) as its own syntax and fails. In shell templates, wrap such lines in an unparsed block, which Velocity copies as is:

```bash
#[[
"${JVMDefaultOptions[@]}"
]]#
```
