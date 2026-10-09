---
name: check-deps
description: Check JavaPackager's build.gradle dependencies and plugins for newer stable versions (within the same major line and overall), verify Java 8 bytecode of candidate jars, and detect declared vs resolved version mismatches between Maven and Gradle consumers. Use before updating dependencies or when the user asks what is outdated.
---

# Check dependencies

## Steps

1. List the outdated dependencies (queries Maven Central and the Gradle Plugin Portal, ignoring alpha/beta/RC versions):

   ```bash
   python .claude/skills/check-deps/check_deps.py versions
   ```

   The `launch4j-maven-plugin` version is pinned in Java code, so the script doesn't see it. Check it by hand: `grep -n 'version("' src/main/java/io/github/javapackager/maven/CreateWindowsExeLaunch4j.java`, against `com.akathist.maven.plugins.launch4j:launch4j-maven-plugin` on Maven Central.
2. Detect **declared vs resolved** mismatches. Gradle consumers get the highest version in the graph; Maven consumers get the declared one. A direct dependency shown as `x -> y` means they differ, which has already caused runtime errors (commons-lang3, plexus-utils):

   ```bash
   ./gradlew dependencies --configuration runtimeClasspath -q | grep -E '^[+\\]--- .* -> '
   ```

   Fix it by declaring the resolved version.
3. Before proposing an update, check that the new jars are Java 8 bytecode (major 52); download them from Maven Central into the scratchpad:

   ```bash
   python .claude/skills/check-deps/check_deps.py bytecode <jar>...
   ```

4. Check for known vulnerabilities in GitHub's Dependabot alerts (`gh api "repos/javapackager/JavaPackager/dependabot/alerts?state=open"`). They only cover `master`.
5. Report three groups: safe updates (same major line, Java 8), major updates (one `/minispec-feature` note each), and deliberate exceptions.

## Deliberate exceptions

- `maven-plugin-api` and `maven-core` stay at 3.6.0: they set the minimum Maven version users need, and they're `provided` in the POM.
- `plexus-xml` stays in 3.x: 4.x is for Maven 4.
- The Maven embedder and resolver (`mavenEmbedder` configuration) only generate the plugin descriptor; keep them on the Maven 3.9 line unless `maven-plugin-plugin` needs more.

## After updating

- Build with a JDK: `./gradlew build`. Check that the output has no "wrong scope" warning (Maven artifacts must stay `provided`).
- Push and run the `ci-status` skill: the smoke tests must pass on every platform.
