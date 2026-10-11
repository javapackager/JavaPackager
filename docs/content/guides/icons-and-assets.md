---
title: Icons and assets
description: Set your app's icon on every platform, and where to put your assets
---

The assets folder (`assetsDir`, `assets` in your project by default) holds your app icons and your [custom templates](templates.md), organized by platform:

```text
assets/
├── linux/
├── mac/
└── windows/
```

## Icons

Each platform needs its own icon format:

| Platform | Format | Property | File in `assetsDir` |
| --- | --- | --- | --- |
| GNU/Linux | PNG | `linuxConfig.pngFile` | `linux/${name}.png` |
| macOS | ICNS | `macConfig.icnsFile` | `mac/${name}.icns` |
| Windows | ICO | `winConfig.icoFile` | `windows/${name}.ico` |

JavaPackager looks for the icon in this order:

1. The platform's icon property, if set.
2. The file named after the app in `assetsDir`, e.g. `assets/windows/MyApp.ico` for an app named `MyApp`.
3. A default icon (a coffee cup) if there's none.

So the simplest way is to name your icons after your app and put them in `assetsDir`:

```text
assets/
├── linux/
│   └── MyApp.png
├── mac/
│   └── MyApp.icns
└── windows/
    └── MyApp.ico
```

:::tip
On GNU/Linux, name the PNG `${name}.png` even if you set `pngFile`: the desktop entry installed by the DEB and RPM expects that name.
:::

Some tools to create icons:

- ICNS: [MacIcns](https://github.com/EasyG0ing1/MacIcns), or `iconutil` on macOS.
- ICO: most image editors, or online converters.

The common `iconFile` property is not used: set the icon per platform.

## Other assets

Besides icons, `assetsDir` can override:

- Any of the [Velocity templates](templates.md) (`.vtl` files).
- The Why launcher on Windows: `assets/windows/JavaLauncher.exe`.

Other built-in files, such as the macOS launchers or the DMG background, can't be replaced from `assetsDir`; some have their own property (e.g. `macConfig.backgroundImage`, `macConfig.customLauncher`).
