# Dependabot security alerts (#500)

## Goal

Clear the Dependabot alerts on `master`: fix the ones that reach users, and fix or dismiss with a reason the ones only in the plugin's own build.

## Context

- 23 open alerts after 2.0.0 (4 critical, 3 high, 15 moderate, 1 low), all from the Gradle dependency graph (`dependency-submission` workflow submits every configuration, `settings.gradle` as manifest).
- Packages: Bouncy Castle (`bcprov`/`bcpkix` `jdk18on`, `bcprov`/`bcpg` `jdk15on`, 18 alerts), `maven-core` < 3.8.1, `maven-shared-utils` < 3.3.3, `aircompressor` < 2.0.3, `guava` < 32, `log4j-api` < 2.25.5.
- Users get only what the published POM (Maven) or `.module` (Gradle) declares; the `mavenEmbedder` configuration (plugin descriptor generation) and the signing plugin never reach them.
- Constraints: Java 8 bytecode, minimum Maven version set by `maven-plugin-api` 3.6.0, published POM shaping in `pom.withXml` (conventions).
- Follow-ups left from the 2.0.0 dependency updates: `jsign-core` 6 → 7 (check Java 8 and API), `com.gradle.plugin-publish` 2.x, Gradle wrapper 9 (needs Java 17 to run Gradle), `redline` 1.2.10 (unmaintained, look for a replacement).

## Changes

- For each alerted package, find the dependency path (`./gradlew dependencyInsight --configuration <conf> --dependency <name>`) and whether it's published.
- Published ones: update the direct dependency, or add a constraint, and check Java 8 bytecode (`check-deps` skill).
- Build-only ones: update when cheap (e.g. the embedded Maven), otherwise dismiss with the reason.
- Prefer `*-jdk18on` over the discontinued `*-jdk15on` Bouncy Castle artifacts.

## Acceptance

- No open alert for a dependency users download, or each remaining one dismissed with a reason in #500.
- The smoke tests pass on every platform, and the published POM keeps no wrong-scope or Gradle-only dependency.
