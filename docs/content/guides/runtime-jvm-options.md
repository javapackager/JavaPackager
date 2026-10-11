---
title: Runtime JVM options
description: Change the JVM options of a packaged app without rebuilding it
---

`vmArgs` are fixed when the app is packaged. To let users (or yourself) change JVM options afterwards, for example the maximum memory, the app can read them from a file at startup: `${name}.l4j.ini`, named after [Launch4j](https://launch4j.sourceforge.net/docs.html), which introduced it.

The options in the file are added after `vmArgs`. Lines starting with `#` are comments.

```ini title="MyApp.l4j.ini"
# Additional JVM options
-Xms256m
-Xmx2g
-Dapp.mode=production
```

:::tip
Write one option per line: it works with every launcher.
:::

## Where the file goes

| Platform | Location | Supported by |
| --- | --- | --- |
| GNU/Linux | Next to the executable | The startup script |
| macOS | `Contents/Resources` | The `SCRIPT` launcher; the native launchers read the same `Info.plist` setting |
| Windows | Next to the EXE | Launch4j (default) and Why; WinRun4J doesn't read it |

To ship a default file with the app, add it to `additionalResources`: it's copied to the right place on each platform.

```xml title="Maven"
<additionalResources>
    <additionalResource>MyApp.l4j.ini</additionalResource>
</additionalResources>
```

```groovy title="Gradle"
javapackager {
    additionalResources = [ file('MyApp.l4j.ini') ]
}
```

## Format details

- **GNU/Linux:** each line is split on spaces, so several options can share a line, but an option can't contain spaces.
- **macOS (`SCRIPT` launcher):** each line is one option, spaces included.
- **Windows (Launch4j):** options are separated by spaces or new lines, and environment variables like `%APPDATA%` are expanded.
