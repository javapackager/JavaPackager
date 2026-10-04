# ADR-003: Move coordinates, packages and plugin id to io.github.javapackager

## Decision

Since 1.7.7, everything that named `io.github.fvarrui` moves to `io.github.javapackager`, in one clean break with no compatibility aliases:

- Maven coordinates: `io.github.javapackager:javapackager` (was `io.github.fvarrui:javapackager`).
- Java packages: `io.github.javapackager.*` (was `io.github.fvarrui.javapackager.*`), e.g. the Gradle task type `io.github.javapackager.gradle.PackageTask`.
- Gradle plugin id: `io.github.javapackager` (was `io.github.fvarrui.javapackager.plugin`).

## Motivation

- The project moved to the `javapackager` GitHub organization, which owns the verified Central Portal namespace `io.github.javapackager`.
- OSSRH was shut down. The Central Portal account that publishes the project only has `io.github.javapackager`; `io.github.fvarrui` is not available there.
- Users must already edit their build because the coordinates change, so renaming the packages and the plugin id at the same time costs them one edit instead of two.
- Rejected: keeping the old package and plugin id (a project-wide `fvarrui` name left forever), and deprecated aliases for one version (more code to maintain for a change users make once).

## Consequences

- Breaking change for every user. Maven: update the `groupId`. Gradle: update the `classpath` coordinates, `apply plugin:` and `type:` of their tasks. It must be the first item in the 1.7.7 release notes, and the README warns about it.
- Releases up to 1.7.6 stay available under `io.github.fvarrui`.
- No Maven relocation POM is published under `io.github.fvarrui`, since that namespace isn't available in the current account.
- Publishing to the Gradle Plugin Portal under the new id would need ownership of `io.github.javapackager` there. The README already uses the Maven Central (`buildscript`) mode, so this doesn't block 1.7.7.
- Releases still need to be migrated from OSSRH (`nexus-staging`) to the Central Portal before 1.7.7.
