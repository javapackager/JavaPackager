---
title: File associations
description: Register the file types your app opens
---

`fileAssociations` registers file types for your app, so users can open those files with it (double click, "Open with...").

```xml title="Maven"
<fileAssociations>
    <fileAssociation>
        <mimeType>application/x-foo</mimeType>
        <extension>foo</extension>
        <description>Foo document</description>
    </fileAssociation>
</fileAssociations>
```

```groovy title="Gradle"
import io.github.javapackager.model.FileAssociation

javapackager {
    fileAssociations = [
        new FileAssociation(mimeType: 'application/x-foo', extension: 'foo', description: 'Foo document')
    ]
}
```

| Field | Description |
| --- | --- |
| `extension` | File extension, without the dot. |
| `mimeType` | MIME type of the files. |
| `description` | Description of the file type shown to users. |

The app receives the file to open as a command-line argument.

## Per platform

| Platform | What's registered |
| --- | --- |
| GNU/Linux | The MIME types in the app's desktop entry (DEB, RPM, AppImage). The DEB also installs a MIME type definition (`/usr/share/mime/packages/${name}.xml`) with the extension. |
| macOS | Document types and exported types in `Info.plist`, with the type identifier `${appId}.${extension}`. |
| Windows | The Setup registers the extension and an "Open" command for the app, and removes them on uninstall. |

:::note
The MSI doesn't register file associations yet; use the Setup if you need them.
:::

On macOS, files are opened through Apple events: Java apps get them with `java.awt.Desktop.setOpenFileHandler`, not as arguments.
