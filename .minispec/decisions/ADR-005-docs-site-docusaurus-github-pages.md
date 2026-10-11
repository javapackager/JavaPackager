# ADR-005: User documentation as a Docusaurus site on GitHub Pages

## Decision

The user documentation is a Docusaurus site in `docs/` (pages in `docs/content/`), published to GitHub Pages (https://javapackager.github.io/JavaPackager/) from `main`, with no versioned docs. `README.md` is only a quick start that links to it.

## Motivation

- The README had grown into a 426-line manual, hard to navigate and partly outdated; the pages in `docs/` were scattered and had wrong defaults.
- A site gives navigation, search-friendly pages and a place for guides, without leaving the repo: docs are reviewed and changed with the code.
- Publishing from `main` keeps the site in step with the latest release on Maven Central; publishing from `devel` would document unreleased features.
- Versioned docs (one copy per release) were rejected: too much upkeep for a project with one supported major. Differences between versions go in the release notes and the upgrading page.
- `docs/content/` instead of Docusaurus' default `docs/docs/`, to avoid the confusing repeated name.

## Consequences

- Docs are changed in `devel` with the code, and reach the site when `devel` is merged into `main` (a fix to the docs alone can be merged earlier).
- `.github/workflows/docs.yml` builds the site on `devel` and pull requests (broken links fail the build) and deploys it from `main`.
- A new property or behaviour is documented in `docs/content/` (conventions), checked against the code, with Maven and Gradle syntax.
- The site documents only the latest release; users of older versions rely on the release notes.
