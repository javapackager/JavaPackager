---
title: Code signing
description: Sign Windows executables and installers, and sign and notarize macOS apps
---

Signed apps don't trigger "unknown publisher" warnings on Windows, and macOS needs signed (and, for downloaded apps, notarized) apps to open them without Gatekeeper blocking them.

## Windows

JavaPackager signs Windows artifacts with [jsign](https://ebourg.github.io/jsign/), so it works on any platform and doesn't need Windows SDK tools. Add a `signing` block to `winConfig`:

```xml title="Maven"
<winConfig>
    <signing>
        <keystore>${project.basedir}/certs/keystore.p12</keystore>
        <storetype>PKCS12</storetype>
        <storepass>${env.KEYSTORE_PASSWORD}</storepass>
        <alias>myalias</alias>
    </signing>
</winConfig>
```

```groovy title="Gradle"
javapackager {
    winConfig {
        signing = new io.github.javapackager.model.WindowsSigning(
            keystore: file('certs/keystore.p12'),
            storetype: 'PKCS12',
            storepass: System.getenv('KEYSTORE_PASSWORD'),
            alias: 'myalias'
        )
    }
}
```

| Field | Description |
| --- | --- |
| `keystore` | Keystore file (`.jks`, `.p12`, `.pfx`), or the SunPKCS11 configuration file. |
| `storetype` | Keystore type: `JKS`, `JCEKS`, `PKCS12` or `PKCS11`. If not set, jsign infers it from the file extension. |
| `storepass` | Keystore password. |
| `alias` | Alias of the certificate in the keystore. Needed if the keystore has more than one. |
| `keypass` | Private key password, if it differs from `storepass`. |
| `certfile` | PKCS#7 certificate chain (`.p7b`, `.spc`), used with `keyfile` instead of a keystore. |
| `keyfile` | Private key (PEM or PVK), used with `certfile`. |
| `alg` | Digest algorithm: `SHA-1`, `SHA-256` (jsign's default), `SHA-384` or `SHA-512`. |

Signed artifacts:

- The app EXE, right after it's created, so the installers contain the signed EXE.
- The Setup (`.exe`) and the MSI.

The MSM, the zipball and the tarball are not signed. Signatures are timestamped, so they stay valid after the certificate expires.

:::caution
A signing error is logged but doesn't fail the build: check the build log. Don't write passwords in your build file; read them from environment variables or your build tool's settings.
:::

## macOS

On macOS, JavaPackager signs the `.app` with `codesign` by default, and can notarize it with `notarytool`. Both only run when building on macOS.

```xml title="Maven"
<macConfig>
    <developerId>Developer ID Application: Jane Doe (TEAMID1234)</developerId>
    <entitlements>assets/mac/entitlements.plist</entitlements>
    <notarizeApp>true</notarizeApp>
    <keyChainProfile>my-notary-profile</keyChainProfile>
</macConfig>
```

```groovy title="Gradle"
javapackager {
    macConfig {
        developerId = 'Developer ID Application: Jane Doe (TEAMID1234)'
        entitlements = file('assets/mac/entitlements.plist')
        notarizeApp = true
        keyChainProfile = 'my-notary-profile'
    }
}
```

### Signing

| Property | Default | Description |
| --- | --- | --- |
| `codesignApp` | `true` | Signs the app. |
| `developerId` | `-` | Signing identity, as shown by `security find-identity -v -p codesigning`. |
| `entitlements` | generated | Entitlements file. |
| `hardenedCodesign` | `true` | Enables the hardened runtime, required for notarization. |
| `provisionProfile` | | Provisioning profile embedded in the app before signing. |

With the default `developerId` (`-`), the app is signed ad-hoc: no certificate is needed and the app runs on the Mac that built it (Apple Silicon requires at least an ad-hoc signature), but Gatekeeper blocks it on other Macs when downloaded, and it can't be notarized. To distribute your app, use a *Developer ID Application* certificate from your Apple Developer account.

JavaPackager signs everything inside the app, from the inside out: every executable and `.dylib`, the bundled JRE, the launcher and finally the `.app`. If you don't give an `entitlements` file, it generates one with the entitlements a JVM needs under the hardened runtime (JIT, unsigned executable memory, disabled library validation, among others).

### Notarization

Notarization sends the signed app to Apple, which checks it and issues a ticket that Gatekeeper accepts. It needs `codesignApp`, a Developer ID certificate and the hardened runtime.

1. Store your notarization credentials in the keychain once, with an [app-specific password](https://support.apple.com/en-us/102654):

   ```bash
   xcrun notarytool store-credentials my-notary-profile --apple-id you@example.com --team-id TEAMID1234 --password <app-specific-password>
   ```

2. Set `notarizeApp` to `true` and `keyChainProfile` to the profile name.

JavaPackager zips the app, submits it with `xcrun notarytool submit --wait`, and staples the ticket to the app with `xcrun stapler staple`.

:::note
Only the `.app` is signed and notarized. The DMG and PKG are neither signed nor notarized; the app inside them is.
:::
