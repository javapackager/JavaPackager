# Maven API dependencies published in the wrong scope

## Problem

When building the plugin, `maven-plugin-plugin` warns that `maven-plugin-api`, `maven-core`, `maven-model` and other Maven artifacts are in `runtime` scope but should be `provided`. Maven users download Maven 3.0.5 and 3.6.0 jars that Maven itself already provides, which risks class conflicts with newer Maven versions.

## Cause

- `maven-plugin-api` is an `implementation` dependency in `build.gradle`, and Gradle publishes it with `runtime` scope in the POM.
- `mojo-executor` (Maven 3.0.5) and `jdeb` (Maven 3.6.0) are Maven plugins themselves and bring `org.apache.maven:*` artifacts transitively.

## Solution

`pom.withXml` in `build.gradle` now edits only the published POM:

- `maven-plugin-api` gets `provided` scope.
- `mojo-executor` and `jdeb` exclude `org.apache.maven:*`.

Gradle dependencies don't change. The plugin also publishes Gradle module metadata (`.module`), which Gradle consumers use instead of the POM, so they are not affected.

## Verification

- Checked that the published `2.0.0-SNAPSHOT` includes a `.module` file and that its POM has the `published-with-gradle-metadata` marker.
- Generated POM locally: `maven-plugin-api` is `provided`; `mojo-executor` and `jdeb` have the exclusion.
- `generatePluginDescriptor` locally: no "wrong scope" warning, 1 mojo descriptor found.
- Pending: the Maven smoke tests (all platforms) must still package and run the sample.
