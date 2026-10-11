---
title: Generated artifacts
description: What JavaPackager generates, and the tools each installer needs
---

JavaPackager generates these artifacts in the output directory (`target/` with Maven, `build/` with Gradle):

| Artifact | Description | Platform | Needs |
| --- | --- | --- | --- |
| `${name}/` | App folder: the native app, ready to run. | All | |
| `${name}-${version}-runnable.jar` | The app's runnable JAR. With Maven it's named after `build.finalName`: `${artifactId}-${version}-runnable.jar` by default. | All | |
| `${name}_${version}.deb` | DEB package. | GNU/Linux | |
| `${name}_${version}.rpm` | RPM package. | GNU/Linux | |
| `${name}_${version}.AppImage` | [AppImage](https://appimage.org/). | GNU/Linux | [FUSE 2](https://github.com/AppImage/AppImageKit/wiki/FUSE) to build it and to run it |
| `${name}_${version}.exe` | Setup installer. | Windows | [Inno Setup](../guides/windows-tools.md) (`iscc`) |
| `${name}_${version}.msi` | MSI installer. | Windows | [WiX Toolset](../guides/windows-tools.md) 3 or 4+ |
| `${name}_${version}.msm` | MSI merge module. | Windows | [WiX Toolset](../guides/windows-tools.md) 3 or 4+ |
| `${name}_${version}.dmg` | Disk image. | macOS | `hdiutil` (built in) |
| `${name}_${version}.pkg` | Installer package. | macOS | `pkgbuild` (built in) |
| `${name}-${version}-${platform}.zip` | Zipball of the app folder (`createZipball`). | All | |
| `${name}-${version}-${platform}.tar.gz` | Tarball of the app folder (`createTarball`). | All | |
| `assets/` | Intermediate files: rendered templates, icons, scripts. | All | |

Installers are only generated with `generateInstaller` (`true` by default), and each one can be disabled in its platform config (`generateDeb`, `generateDmg`, `generateMsi`...). Installers can only be built on their own platform: when the target `platform` differs from the one running the build, they are skipped.

If a tool an installer needs is missing, that installer fails with a warning and the build goes on.

## The app folder

The app folder is the native app. Its layout depends on the platform:

```text title="Windows"
${name}/
├── ${name}.exe          # launcher (with the JAR inside, if wrapJar)
├── libs/                # dependencies
└── jre/                 # bundled JRE (with bundleJre)
```

```text title="GNU/Linux"
${name}/
├── ${name}              # launcher script (with the JAR inside, if wrapJar)
├── libs/                # dependencies
└── jre/                 # bundled JRE (with bundleJre)
```

```text title="macOS"
${name}/
└── ${name}.app/         # app bundle, see the macOS page
```

The license file and `additionalResources` are copied into the app folder too (`Contents/Resources` on macOS). The [macOS page](../configuration/macos.md#app-bundle-layout) shows the whole `.app` layout.
