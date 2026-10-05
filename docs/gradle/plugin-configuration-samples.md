# Plugin configuration samples for Gradle

All samples assume the plugin is applied in `build.gradle`:

```groovy
plugins {
    id 'io.github.javapackager' version '{latest.version}'
}
```

The plugin registers a `package` task configured with the `javapackager` extension. Your own `PackageTask` tasks take their properties from the extension too, and any property set in the task overrides the extension's one. In a task, the app's `name` property is called `appName`, as `name` is the task's own name.

## Minimal config

> :warning: This minimal configuration will not bundle a JRE, so final user will need one in order to run the app.

### Using default task

Add next to your `build.gradle` file:

```groovy
javapackager {
    mainClass = 'fvarrui.sample.Main'
}
```

And run `gradle package`.

### Using your own task

Add next task to your `build.gradle` file:

```groovy
tasks.register('packageMyApp', io.github.javapackager.gradle.PackageTask) {
    dependsOn build
    mainClass = 'fvarrui.sample.Main'
}
```

And run `gradle packageMyApp`.

## Bundle with a customized JRE

```groovy
javapackager {
    mainClass = 'fvarrui.sample.Main'
    bundleJre = true
}
```

> `customizedJre` is `true` by default, so you don't have to specify it.

## Bundle with a full JRE

```groovy
javapackager {
    mainClass = 'fvarrui.sample.Main'
    bundleJre = true
    customizedJre = false
}
```

## Bundle with an existing JRE

```groovy
javapackager {
    mainClass = 'fvarrui.sample.Main'
    bundleJre = true
    jrePath = file('C:/Program Files/Java/jre1.8.0_231')
}
```

## Bundle your own fat JAR

```groovy
javapackager {
    mainClass = 'fvarrui.sample.Main'
    bundleJre = true
    runnableJar = file('path/to/your/own/fat.jar')
    copyDependencies = false
}
```

## Multiple executions

```groovy
import io.github.javapackager.gradle.PackageTask

javapackager {
    // common configuration
    mainClass = 'fvarrui.sample.Main'
}
tasks.register('packageMyAppWithJRE', PackageTask) {
    dependsOn build
    appName = 'Sample'
    bundleJre = true
}
tasks.register('packageMyAppWithoutJRE', PackageTask) {
    dependsOn build
    appName = 'Sample-nojre'
    bundleJre = false
}
tasks.register('packageMyApp') {
    dependsOn 'packageMyAppWithJRE', 'packageMyAppWithoutJRE'
}
```

E.g. on Windows, last configuration will generate next artifacts:

* `Sample_x.y.z.exe` with a bundled JRE.
* `Sample-nojre_x.y.z.exe` without JRE.

## Bundling for multiple platforms

```groovy
import io.github.javapackager.gradle.PackageTask

javapackager {
    // common configuration
    mainClass = 'fvarrui.sample.Main'
    bundleJre = true
    generateInstaller = false
}
tasks.register('packageMyAppForLinux', PackageTask) {
    dependsOn build
    platform = 'linux'
    createTarball = true
    jdkPath = file('X:/path/to/linux/jdk')
}
tasks.register('packageMyAppForMac', PackageTask) {
    dependsOn build
    platform = 'mac'
    createTarball = true
    jdkPath = file('X:/path/to/mac/jdk')
}
tasks.register('packageMyAppForWindows', PackageTask) {
    dependsOn build
    platform = 'windows'
    createZipball = true
}
tasks.register('packageMyApp') {
    dependsOn 'packageMyAppForLinux', 'packageMyAppForMac', 'packageMyAppForWindows'
}
```

E.g. on Windows, running `packageMyApp` task will generate next artifacts:

* `${name}-${version}-linux.tar.gz` with the GNU/Linux application including a customized JRE.
* `${name}-${version}-mac.tar.gz` with the MacOS application including a customized JRE.
* `${name}-${version}-windows.zip` with the Windows application including a customized JRE.

As last sample is running on Windows, it's not necessary to specify a JDK when bundling for Windows (it uses current JDK by default). Otherwise, if running on GNU/Linux or MacOS, you have to specify a JDK for Windows.

> The JDK for another platform must include its `jmods` folder: since JDK 24 some builds (e.g. Temurin) come without it.
