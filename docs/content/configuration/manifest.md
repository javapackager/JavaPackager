---
title: Manifest
description: Add entries and sections to the runnable JAR's manifest
---

`manifest` adds entries and sections to the `MANIFEST.MF` of the runnable JAR JavaPackager builds. It has no effect when you bundle your own JAR with `runnableJar`.

JavaPackager already writes `Main-Class` and `Class-Path` (pointing to the `libs` folder).

```xml title="Maven"
<manifest>
    <additionalEntries>
        <Created-By>Peter</Created-By>
    </additionalEntries>
    <sections>
        <section>
            <name>foo/</name>
            <entries>
                <Implementation-Version>foo1</Implementation-Version>
            </entries>
        </section>
    </sections>
</manifest>
```

```groovy title="Gradle"
javapackager {
    manifest {
        additionalEntries = [
            'Created-By': 'Peter'
        ]
        sections = [
            new io.github.javapackager.model.ManifestSection([
                name: 'foo/',
                entries: [
                    'Implementation-Version': 'foo1'
                ]
            ])
        ]
    }
}
```

Both produce this `MANIFEST.MF`, plus the entries JavaPackager writes:

```properties
Manifest-Version: 1.0
Created-By: Peter

Name: foo/
Implementation-Version: foo1
```

| Property | Description |
| --- | --- |
| `additionalEntries` | Map of entries added to the main section. |
| `sections` | List of named sections, each with a `name` and a map of `entries`. |
