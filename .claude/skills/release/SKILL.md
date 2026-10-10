---
name: release
description: Prepare and publish a JavaPackager release to Maven Central and GitHub - release notes from devel commits and fix notes, version bump, signing check, releaseToCentral, verification, and closing the fixed issues. Use when the user wants to release a new version or draft its release notes.
---

# Release

Publishing a release is irreversible (Maven Central never deletes versions), so confirm with the user before each outward step: pushing to `main`, `releaseToCentral`, publishing the GitHub release, and closing issues.

## 1. Release notes

- Source: `git log --oneline --no-merges origin/main..origin/devel`, the `fix-*` notes in `.minispec/features/`, the ADRs, and `release-*.md`.
- Audience: plugin users. In English, following the user's Markdown rules, in the style of earlier releases (`gh release view <tag> --repo javapackager/JavaPackager`).
- Sections, in this order: Breaking changes (with a "How to upgrade" section and ready-to-copy snippets), New features, Fixed issues (`#n` references), Maintenance.
- Leave out regressions that only existed in `devel` and never reached a release.
- Only say "fixed" for what CI or users verified; otherwise ask for confirmation in the notes ("should fix #n, please confirm").
- Create or update the draft: `gh release create vX.Y.Z --repo javapackager/JavaPackager --draft --target <branch> --title "vX.Y.Z" --notes-file <file>` (or `gh release edit`). A draft has no tag until it's published.

## 2. Preconditions

- Smoke tests green on `devel` (`ci-status` skill).
- `.minispec/features/release-*.md` acceptance met (e.g. feedback window on issues closed).
- Signing set up in `~/.gradle/gradle.properties` (`signing.keyId`, `signing.password`, `signing.secretKeyRingFile`, or `signing.gnupg.*`), with the public key on a keyserver. Ask the user; never read the file's values.
- Central Portal user token in `ossrhUsername`/`ossrhPassword`.
- The user runs Gradle with a JDK (see memory: the `java` on their PATH is a JRE).

## 3. Publish

1. Set `version` in `build.gradle` to `X.Y.Z` (no `-SNAPSHOT`), update the sample's `javapackager.version`, commit.
2. Merge `devel` into `main` and push (ask first). Point the draft release's target to `main`.
3. The user runs `./gradlew releaseToCentral`. It uploads through the Portal OSSRH Staging API in `user_managed` mode; then they review the deployment at https://central.sonatype.com/publishing/deployments and click **Publish**.
4. Verify it's on Maven Central (it can take a while to appear):

   ```bash
   curl -s https://repo1.maven.org/maven2/io/github/javapackager/javapackager/maven-metadata.xml | grep -E "<release>|<latest>"
   ```

5. Publish the GitHub release (`gh release edit vX.Y.Z --repo javapackager/JavaPackager --draft=false`), which creates the tag.
6. Bump `devel` to the next `-SNAPSHOT`.

## 4. After the release

- Run the `issue-update` skill with "Released" comments: close the issues labelled `fixed`, and update the `feedback` ones.
- Close `release-*.md` following MiniSpec: promote anything permanent to `core/` or an ADR, then delete the note (ask first).
