# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## MiniSpec (read first)

Before writing any code, read `.minispec/README.md` and follow its reading contract. As a minimum, always read `.minispec/core/project.md`, `.minispec/core/conventions.md` and `.minispec/core/principles.md`; read the rest of `.minispec/` only on demand (architecture, stack, glossary, the relevant feature or ADR). Keep features small and don't write redundant documentation.

Project knowledge (architecture, conventions, glossary) lives in `.minispec/core/`, not here. Work in progress (features and bugfixes for GitHub issues) lives in `.minispec/features/`.

## Commands

Use the wrapper (`./gradlew` or `gradlew.bat` on Windows).

| Command | Purpose |
| --- | --- |
| `./gradlew build` | Compile, generate the Maven plugin descriptor and build the jars |
| `./gradlew clean build` | Rebuild from scratch when the Maven plugin descriptor looks stale |
| `./gradlew publishToMavenLocal` | Install the plugin into `~/.m2` to test it from a sample Maven/Gradle project |
| `./gradlew updateUniversalJavaApplicationStub` | Re-download the `universalJavaApplicationStub` files into `src/main/resources/mac`. Only the `.sh` script is still used; the binary launchers are now `nativeJavaApplicationStub*` and this task does not update them |
| `./gradlew updateWhyJavaLauncher` | Re-download `JavaLauncher.exe` into `src/main/resources/windows` |

There are no unit tests, so there is no way to run a single test. `src/it/simple-it` is a leftover Maven invoker skeleton not wired into the Gradle build. Changes are verified by publishing to Maven local and packaging a real project on the affected OS.

`compileJava` triggers `generatePluginDescriptor`, which runs an embedded Maven against the generated POM to produce `META-INF/maven/plugin.xml` from the `@Mojo`/`@Parameter` annotations.

Publishing (OSSRH + Gradle Plugin Portal, signed) requires the `ossrhUsername`/`ossrhPassword` properties; the version is set in `build.gradle`.
