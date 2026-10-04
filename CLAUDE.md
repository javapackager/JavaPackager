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
| `./gradlew updateWhyJavaLauncher` | Re-download `JavaLauncher.exe` into `src/main/resources/windows` |

There are no unit tests, so there is no way to run a single test. `src/it/simple-it` is a leftover Maven invoker skeleton not wired into the Gradle build. Changes are verified by the smoke tests in `.github/workflows/smoke-tests.yml`, which run on every push to `devel`/`master`: they package `samples/hello-world` with Maven on GNU/Linux, Windows and macOS (each `macStartup`) and run `.github/scripts/check-*-app.sh`. Build with a JDK 17+ (a JRE can't compile).

`compileJava` triggers `generatePluginDescriptor`, which runs an embedded Maven against the generated POM to produce `META-INF/maven/plugin.xml` from the `@Mojo`/`@Parameter` annotations.

Publishing uses a Central Portal user token in the `ossrhUsername`/`ossrhPassword` properties (`~/.gradle/gradle.properties`). Snapshots: `./gradlew publish` (Central Portal snapshots repo). Releases: `./gradlew releaseToCentral` (Portal OSSRH Staging API, then manual Publish in the Portal). The version is set in `build.gradle`.

## Project skills and agents

| Skill or agent | Use it to |
| --- | --- |
| `ci-status` | Wait for the latest smoke tests run and summarize failures per job |
| `triage-issues` (with the `issue-triager` agent) | Classify open GitHub issues against `devel`, in parallel, read-only |
| `issue-update` | Draft and post "fixed in devel", "please test" or "released" comments, with labels |
| `check-deps` | Find outdated dependencies, check Java 8 bytecode and declared vs resolved versions |
| `release` | Release notes, `releaseToCentral`, verification and closing issues |
| `minispec-feature`, `minispec-bugfix`, `minispec-adr`, `minispec-implement` | MiniSpec notes and implementation |
