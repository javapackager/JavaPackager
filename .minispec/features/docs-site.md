# Documentation site

## Goal

Move the user documentation to a Docusaurus site in `docs/`, published on GitHub Pages, and keep `README.md` minimal.

## Context

- `README.md` (426 lines) holds the whole user guide: usage, artifacts, every property, assets, templates, runtime JVM options, snapshots, building and publishing.
- `docs/` has per-platform property pages, Maven/Gradle samples, `manifest.md`, the Windows tools guide and a DMG image. Some content is outdated or wrong (old `fvarrui` links, bad Windows paths in samples, old bundle names, a broken XML closing tag) and some properties only link to the code (`fileAssociations`, `scripts`).
- No GitHub Pages site yet. Node 26 and npm are available locally.
- Decisions (user, 2026-10-11): publish from `main` (the site documents the latest release), no versioned docs.

## Changes

- Docusaurus (classic preset) project in `docs/`; pages in `docs/content/` (existing files moved with `git mv`; not Docusaurus' default `docs/docs`, user choice), images in `docs/static/img/`.
- Site URL `https://javapackager.github.io/JavaPackager/`; workflow `.github/workflows/docs.yml` builds on push and pull requests to `devel`/`main` touching `docs/` and deploys on push to `main` (plus manual dispatch) with `actions/deploy-pages`.
- Content, checked against the code (defaults, types, Maven and Gradle syntax):
  - Introduction, getting started (Maven, Gradle), generated artifacts.
  - Configuration reference: common properties, GNU/Linux, macOS, Windows, manifest, file associations, scripts.
  - Guides: bundling a JRE (customized, full, existing, `additionalJlinkArgs`, other platforms, JDK 24+), icons and assets, templates, runtime JVM options, multiple executions and platforms, code signing, Windows tools, snapshots, upgrading from 1.x.
  - Contributing: building the plugin, smoke tests, publishing.
- `README.md`: what it is, quick start (Maven and Gradle), link to the site, maintainers wanted, apps packaged with it, license.
- `CLAUDE.md` and conventions: documentation lives in `docs/content/` (new properties are documented there).

## Acceptance

- `npm run build` in `docs/` succeeds with no broken links.
- The site is live at the URL above after a push to `main`.
- Every property in `PackagerSettings` and the platform configs is documented with its real default.
- `README.md` fits in one screen or two and links to the site.
