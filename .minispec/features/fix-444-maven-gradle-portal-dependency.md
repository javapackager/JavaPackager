# Fix #444: Maven users needed the Gradle Plugin Portal

## Problem

Maven builds that can only reach Maven Central (a corporate mirror, a restricted network) fail to resolve the plugin: `Could not find artifact edu.sc.seis.launch4j:launch4j:jar:3.0.5`.

## Cause

The published POM declared the launch4j Gradle library (`edu.sc.seis.launch4j:launch4j`), which is only published on the Gradle Plugin Portal, and added the Portal as a `<repository>` so Maven could find it. Only the Gradle plugin uses that library (`gradle/*`). The smoke tests didn't notice: the embedded Maven that generates the plugin descriptor downloads it into `~/.m2`, where the sample then found it.

## Solution

- `build.gradle` (`pom.withXml`): the POM no longer has the launch4j Gradle library nor the Portal repository. Gradle consumers are unaffected: they use the `.module` file and need `gradlePluginPortal()` (documented in the README since #444).
- Smoke tests: the plugin is installed into, and the samples resolve from, an empty local Maven repo (`-Dmaven.repo.local=build/m2-smoke`), so they resolve dependencies like a user would.

## Verification

The generated POM has no `launch4j` nor `plugins.gradle.org`; `./gradlew build` still generates the plugin descriptor. Smoke tests pass with the isolated repo on every platform, for Maven and Gradle.
