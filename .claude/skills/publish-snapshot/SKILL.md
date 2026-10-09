---
name: publish-snapshot
description: Publish the current devel build of JavaPackager as a -SNAPSHOT to the Central Portal snapshots repository and verify it. Use when the user asks to publish (or refresh) the snapshot, e.g. so issue reporters can test the latest fixes.
---

# Publish snapshot

Publishes the plugin (Maven plugin, Gradle plugin marker, sources and javadoc) to https://central.sonatype.com/repository/maven-snapshots/ with `./gradlew publish`. Snapshots aren't signed and need no Portal step.

## Rules

- Credentials are the Central Portal user token in `ossrhUsername`/`ossrhPassword` in `~/.gradle/gradle.properties`. Gradle reads them; never open, print, grep or pass that file or its values yourself.
- Only publish a commit that is pushed and whose smoke tests passed: a snapshot is what issue reporters are told to test.
- Never publish a release version with this skill (use the `release` skill).

## Steps

1. Preconditions, stop and report if any fails:
   - On `devel`, working tree clean, `HEAD` pushed (`git status -sb` shows no ahead/behind).
   - `version` in `build.gradle` ends with `-SNAPSHOT`.
   - The latest `smoke-tests.yml` run for `HEAD` succeeded (`gh run list --repo javapackager/JavaPackager --workflow smoke-tests.yml --branch devel --limit 1 --json headSha,conclusion`). If it's still running, use the `ci-status` skill and wait.
2. Note the current snapshot timestamp, to compare later:

   ```bash
   curl -s https://central.sonatype.com/repository/maven-snapshots/io/github/javapackager/javapackager/<version>/maven-metadata.xml | grep -o '<timestamp>[^<]*'
   ```

3. Publish with the JDK (the `java` on PATH is a JRE on the user's machine), in the background since it takes a few minutes:

   ```bash
   JAVA_HOME="/c/Program Files/GraalVM/graalvm-community-openjdk-22.0.2+9.1" ./gradlew publish --no-daemon
   ```

   On "Host desconocido" / unknown host errors, retry once (flaky DNS). A 401/403 means the token is wrong or expired: tell the user to regenerate it at https://central.sonatype.com/usertoken and update the file themselves.
4. Verify: the `maven-metadata.xml` timestamp is newer than in step 2, and the new POM still has `provided` scope for `maven-plugin-api` and `maven-core`. Also check the Gradle plugin marker exists: `.../io/github/javapackager/io.github.javapackager.gradle.plugin/<version>/maven-metadata.xml`.
5. Report the version, the new timestamp and build number, and the commit it was built from (`git log --oneline -1`). Offer the `issue-update` skill if there are issues waiting to test it.
