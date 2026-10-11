---
title: Contributing
---

Contributions are welcome: bug reports, fixes, documentation and reviews. Open an [issue](https://github.com/javapackager/JavaPackager/issues) or a pull request against the `devel` branch.

## Branches

- `devel`: development branch. Pull requests target it.
- `main`: latest release. `devel` is merged into it when a version is released.

## Building the plugin

You need Git and a JDK 17 or newer (a JRE is not enough). The plugin itself targets Java 8.

```bash
git clone https://github.com/javapackager/JavaPackager.git --branch devel
cd JavaPackager
./gradlew publishToMavenLocal
```

On Windows, omit `./` (`gradlew publishToMavenLocal`).

This installs the plugin in your local Maven repository (`~/.m2`), so you can test it from any project using the version in `build.gradle` (e.g. `2.0.1-SNAPSHOT`). Gradle projects need `mavenLocal()` in the `pluginManagement` repositories of `settings.gradle`.

The build compiles the plugin and generates the Maven plugin descriptor with an embedded Maven, from the `@Mojo` and `@Parameter` annotations.

## Testing your changes

There are no unit tests. Changes are verified by the smoke tests (`.github/workflows/smoke-tests.yml`), which run on every push and pull request to `devel` and `main`. They package [`samples/hello-world`](https://github.com/javapackager/JavaPackager/tree/devel/samples/hello-world), a sample app with both a `pom.xml` and a `build.gradle`, with the Maven and the Gradle plugin on GNU/Linux, Windows and macOS (every `macStartup` type), and check the generated apps and installers.

To try a change locally, install the plugin and package the sample:

```bash
./gradlew publishToMavenLocal
cd samples/hello-world
mvn package
../../gradlew package
```

## Documentation

This site is built with [Docusaurus](https://docusaurus.io/) from the `docs/` folder, and published to GitHub Pages from `main`. To preview it locally (Node.js 20 or newer):

```bash
cd docs
npm install
npm start
```

A new configuration property must be documented in the [configuration reference](configuration/common.md) (or the platform page it belongs to).

## Publishing (maintainers)

Publishing uses a [Central Portal user token](https://central.sonatype.com/usertoken), set as `ossrhUsername` and `ossrhPassword` in `~/.gradle/gradle.properties`, and a GPG key for signing releases.

- Snapshot (a `-SNAPSHOT` version): `./gradlew publish`.
- Release (a version without `-SNAPSHOT`): `./gradlew releaseToCentral`. It uploads the signed release to the Central Portal, where it waits in [Deployments](https://central.sonatype.com/publishing/deployments) until it's reviewed and published.
