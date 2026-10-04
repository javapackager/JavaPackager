# Conventions

- Keep Java 8 compatibility: no APIs or syntax newer than Java 8.
- Indent Java with tabs.
- Build with the wrapper: `./gradlew build`. Test changes with `./gradlew publishToMavenLocal` and a real sample project; there are no unit tests.
- Any change must keep working in both Maven and Gradle. Build-tool-specific code goes only in `maven/` or `gradle/`, behind `Context`.
- A new configuration property is wired in: `PackagerSettings` (field + fluent setter + getter), `PackageMojo` (`@Parameter` + fluent call in `execute()`), `PackageTask` (annotated field + fluent call in `createPackager()` with `defaultIfNull(taskValue, extension.getX())`), and documented in `README.md` or `docs/*-specific-properties.md`.
- Platform-specific options go in `WindowsConfig`, `MacConfig` or `LinuxConfig`, not in `PackagerSettings`.
- New artifacts are `ArtifactGenerator` subclasses. Platform and external-tool checks go in `skip()`, not in `doApply()`.
- Template names and the variables they use are public API: users override them from `assetsDir`. Renaming a template or changing its variables is a breaking change.
- Log through `utils/Logger`, never `System.out`.
- Run external tools through `utils/CommandUtils`.
- Bundled native binaries in `src/main/resources` are not edited by hand. `updateWhyJavaLauncher` refreshes `JavaLauncher.exe`.
- `mac/universalJavaApplicationStub.sh` is maintained in this repo (ADR-001): edit it here and keep its MIT license header.
- A fix is verified by packaging on the affected OS; record how in the bugfix note.
