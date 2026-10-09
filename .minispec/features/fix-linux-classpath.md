# Fix: `classpath` ignored on GNU/Linux

## Problem

With the `classpath` property set, the GNU/Linux app doesn't start: `java` reports it can't access the JAR file. No GitHub issue; found while triaging.

## Cause

`linux/startup.sh.vtl` ran `java -jar "$BINARY:extra1:extra2"`. `-jar` takes a single JAR file and ignores any other classpath, so the joined string was taken as a file name. Present in 1.7.x too.

## Solution

`linux/startup.sh.vtl`: with `classpath` set, run `java -cp "$BINARY:<classpath>" <mainClass>`; without it, keep `java -jar "$BINARY"`. The runnable JAR's manifest `Class-Path` (`libs/`) still applies with `-cp`.

## Verification

The sample sets `classpath` to `extra` and prints `java.class.path`; `check-linux-app.sh` checks it ends with `:extra`, for Maven and Gradle.
