# Fix #439: customized JRE fails with split packages

## Problem

Since 1.7.6, building a customized JRE fails for apps whose dependencies split a package across JARs (e.g. netty 4.1 `netty-codec` and `netty-codec-xml`): `jdeps` exits with `Error: Modules io.netty.codec.xml and io.netty.codec export package io.netty.handler.codec.xml to module ...`. 1.7.5 worked.

## Cause

`BundleJre.getRequiredModules()` runs `jdeps` with `--add-modules=ALL-MODULE-PATH --module-path=<jar>;<libs>`, so every JAR becomes an automatic module, and the module system rejects two modules exporting the same package. 1.7.5 analysed the JARs on the class path, where split packages are allowed.

## Solution

`BundleJre` runs `jdeps` through a `jdeps(...)` helper: the module path first (kept, as it handles modular apps), and if `jdeps` fails there, it logs a warning and repeats the analysis on the class path (`--class-path <libs>/*` plus the additional module paths).

## Verification

Local (Windows, JDK 22): `jdeps` on the module path reproduces the reported error with netty 4.1.115 `netty-codec` and `netty-codec-xml`; on the class path it prints the same modules as the module path does when there's no split package. A Gradle sample app with those dependencies and `customizedJre=true` packages after the fallback warning and runs with the customized JRE. The smoke tests check the module path path still works.
