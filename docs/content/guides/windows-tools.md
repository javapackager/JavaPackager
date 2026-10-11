---
title: Windows tools
description: Install Inno Setup and WiX Toolset to build Windows installers
---

The app EXE doesn't need anything installed, but the Windows installers do:

| Artifact | Tool | Command in `PATH` |
| --- | --- | --- |
| Setup (`.exe`) | [Inno Setup](https://jrsoftware.org/isinfo.php) | `iscc` |
| MSI and MSM | [WiX Toolset](https://wixtoolset.org/) 4 or newer | `wix` |
| MSI and MSM | [WiX Toolset](https://wixtoolset.org/) 3 | `candle` and `light` |

If `wix` is in the `PATH`, JavaPackager uses it; otherwise it falls back to `candle` and `light`.

If you don't need an installer, disable it (`generateSetup`, `generateMsi`) or all of them (`generateInstaller`).

## With Chocolatey

1. [Install Chocolatey](https://chocolatey.org/install).
2. Run as administrator in CMD or PowerShell:

   ```powershell
   choco install -y innosetup wixtoolset
   ```

This installs Inno Setup and WiX 3, and adds them to the `PATH`.

## With Scoop

[Scoop](https://scoop.sh/) installs tools in your user folder and doesn't need administrator privileges.

1. [Install Scoop](https://scoop.sh/).
2. Run in CMD or PowerShell:

   ```powershell
   scoop bucket add extras
   scoop install inno-setup
   scoop install wixtoolset
   ```

This installs Inno Setup and WiX 3, and adds them to the `PATH`.

## WiX 4 or newer

Chocolatey and Scoop install WiX 3. WiX 4 and newer are installed as a .NET tool, which needs the [.NET SDK](https://dotnet.microsoft.com/download):

```powershell
dotnet tool install --global wix
```

The `wix` command is then in the `PATH`.

:::note
Inno Setup 5 must be the Unicode version, because JavaPackager writes the Setup script in UTF-8. Inno Setup 6 is always Unicode.
:::
