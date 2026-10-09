# Fix #484: PackageTask without the plugin applied

## Problem

Registering a `PackageTask` without applying the JavaPackager plugin fails at execution with `Cannot invoke "...PackagePluginExtension.getDuplicatesStrategy()" because "extension" is null`, which doesn't say what's wrong.

## Cause

`PackageTask.createPackager()` uses the `javapackager` extension and `Context`, both created only by `PackagePlugin.apply()`. Since cf50caa the extension was also read when the task was created, so applying the plugin after registering the task failed too.

## Solution

`gradle/PackageTask.java` keeps the project's `ExtensionContainer` (read at configuration time) and looks the extension up in `createPackager()`. If the extension or the Gradle context is missing, it throws a `GradleException` that says to apply `io.github.javapackager`.

## Verification

Local Gradle project (Windows): a `PackageTask` without the plugin fails with the new message; registering the task and applying the plugin afterwards packages the app.
