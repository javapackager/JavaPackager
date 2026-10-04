---
name: issue-update
description: Draft and post updates on javapackager/JavaPackager GitHub issues - "fixed in devel, try the snapshot" comments with the fixed label, "please test" comments with the feedback label, and "released" comments that close fixed issues after a release. Always shows the drafts first; posts only after the user's explicit go-ahead.
---

# Issue updates

Comments are public and posted in the user's name: **show the drafts and wait for an explicit go-ahead** before posting, unless the user has just asked you to post them ("hazlo tú", "publícalos").

## Kinds of update

- **Fixed in `devel`** (label `fixed` = "Issue fixed and release pending"). What was fixed and the commit or PR, how CI checks it (smoke tests), that it's available in the current `-SNAPSHOT`, and the how-to-test block. Keep the issue **open** until the release.
- **Please test** (label `feedback` = "Waiting for feedback"). For fixes CI can't fully verify (macOS from Finder/DMG, admin password prompts, signing, notarization): what changed, what CI does and doesn't check, a clear question, and the how-to-test block.
- **Released.** After a release is on Maven Central: "Released in X.Y.Z" with the coordinates, then close the issue (`gh issue close <n> --reason completed`) and remove the `fixed` label.

## Steps

1. Read the current snapshot version from `build.gradle` (`version = ...`) and check it's published:

   ```bash
   curl -s -o /dev/null -w "%{http_code}\n" https://central.sonatype.com/repository/maven-snapshots/io/github/javapackager/javapackager/<version>/maven-metadata.xml
   ```

   Don't point users to a snapshot that returns 404.
2. For each issue, read it (`gh issue view <n> --repo javapackager/JavaPackager --comments`) and the related `fix-*` note or commit, so the text matches what was actually done.
3. Write one Markdown file per issue in the scratchpad, in English, following the user's Markdown rules (one line per paragraph). Append `snapshot-howto.md` (this folder) with `{{VERSION}}` replaced, for the first two kinds.
4. Show the drafts (or a summary plus one full example) and wait for the go-ahead.
5. Post and label:

   ```bash
   gh issue comment <n> --repo javapackager/JavaPackager --body-file <file>
   gh issue edit <n> --repo javapackager/JavaPackager --add-label fixed   # or feedback
   ```

6. Report a table with each issue, the kind of update, the label and the comment URL. Record the date and the issues in `.minispec/features/release-*.md`.

## Rules

- Don't claim something is fixed if CI can't verify it: use "please test" instead.
- Don't mention regressions that only existed in `devel` (users never had them).
