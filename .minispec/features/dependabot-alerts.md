# Dependabot security alerts (#500)

## Goal

Clear the Dependabot alerts on `main`: fix the ones that reach users, and fix or dismiss with a reason the ones only in the plugin's own build.

## Context

- 23 open alerts after 2.0.0 (4 critical, 3 high, 15 moderate, 1 low), all from the Gradle dependency graph (`dependency-submission` workflow submits every configuration, `settings.gradle` as manifest).
- Packages: Bouncy Castle (`bcprov`/`bcpkix` `jdk18on`, `bcprov`/`bcpg` `jdk15on`, 18 alerts), `maven-core` < 3.8.1, `maven-shared-utils` < 3.3.3, `aircompressor` < 2.0.3, `guava` < 32, `log4j-api` < 2.25.5.
- Users get only what the published POM (Maven) or `.module` (Gradle) declares; the `mavenEmbedder` configuration (plugin descriptor generation) and the signing plugin never reach them.
- Constraints: Java 8 bytecode, minimum Maven version set by `maven-plugin-api` 3.6.0, published POM shaping in `pom.withXml` (conventions).
- 7 more alerts from the docs site's npm dependencies (`docs/package-lock.json`, transitive from Docusaurus): `tinypool`, `serialize-javascript`, `braces`, `postcss-selector-parser`, `uuid`. Build-only: the site is static HTML.
- Follow-ups left from the 2.0.0 dependency updates: `jsign-core` 6 → 7 (check Java 8 and API), `com.gradle.plugin-publish` 2.x, Gradle wrapper 9 (needs Java 17 to run Gradle), `redline` 1.2.10 (unmaintained, look for a replacement).

## Changes

- For each alerted package, find the dependency path (`./gradlew dependencyInsight --configuration <conf> --dependency <name>`) and whether it's published.
- Published ones: update the direct dependency, or add a constraint, and check Java 8 bytecode (`check-deps` skill).
- Build-only ones: update when cheap (e.g. the embedded Maven), otherwise dismiss with the reason.
- Prefer `*-jdk18on` over the discontinued `*-jdk15on` Bouncy Castle artifacts.
- Docs site: update Docusaurus, or force fixed versions with `overrides` in `docs/package.json` (then `npm run build`); dismiss the ones with no fix.

## Done (2026-10-11)

- Bouncy Castle `jdk18on` 1.86 (`bcprov`, `bcpkix`, `bcutil`) and `log4j-api` 2.25.5 declared directly (Java 8 bytecode checked); redline's `bcpg`/`bcprov-jdk15on` excluded and replaced by `bcpg-jdk18on` 1.86; `aircompressor` excluded from jdeb.
- `maven-plugin-api` and `maven-core` are `compileOnly` (added as `provided` in `pom.withXml`), and `org.apache.maven` is excluded from mojo-executor and jdeb in Gradle: Gradle consumers no longer get Maven, Guava or `maven-shared-utils`.
- Embedded Maven for the descriptor: 3.10.0, resolver 2.0.24, slf4j-simple 2.0.20; plugin-publish 2.2.1; GitHub Actions `checkout` v7, `setup-java` v6, `upload-artifact` v6 (Dependabot PRs #492, #493, #495 to #498). The descriptor is the same as 2.0.0's (51 parameters, Maven 3.6.0, Java 8).
- Docs site: `overrides` for `tinypool`, `serialize-javascript`, `postcss-selector-parser` and `uuid`; the site builds.
- Left: dismiss the alerts with no fix or not shipped (`maven-core` < 3.8.1, compileOnly; `braces`, already the latest), and close #494/#499 (`maven-plugin-api`/`maven-core` 3.10 would require Maven 3.10, `requiredMavenVersion` comes from it).

## Acceptance

- No open alert for a dependency users download, or each remaining one dismissed with a reason in #500.
- The smoke tests pass on every platform, and the published POM keeps no wrong-scope or Gradle-only dependency.
