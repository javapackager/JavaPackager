# ADR-003: Publish under the groupId io.github.javapackager

## Decision

Since 1.7.7, the plugin is published as `io.github.javapackager:javapackager` instead of `io.github.fvarrui:javapackager`. The Java packages (`io.github.fvarrui.javapackager.*`) and the Gradle plugin id (`io.github.fvarrui.javapackager.plugin`) do not change.

## Motivation

- The project moved to the `javapackager` GitHub organization, which owns the verified Central Portal namespace `io.github.javapackager`.
- OSSRH was shut down. The Central Portal account that publishes the project only has `io.github.javapackager`; `io.github.fvarrui` is not available there.
- Rejected: renaming the Java packages and the Gradle plugin id as well. It would break every user's Gradle build (`type: io.github.fvarrui.javapackager.gradle.PackageTask`, `apply plugin: ...`) for no functional gain.

## Consequences

- Breaking change for every user: they must update the `groupId` (Maven) or the `classpath` coordinates (Gradle) when upgrading. It must be the first item in the 1.7.7 release notes, and the README warns about it.
- Releases up to 1.7.6 stay available under `io.github.fvarrui`.
- No Maven relocation POM is published under `io.github.fvarrui` (that namespace isn't available in the current account). If it is recovered, publishing a relocation POM would let Maven point old coordinates to the new ones.
- Releases still need to be migrated from OSSRH (`nexus-staging`) to the Central Portal before 1.7.7.
