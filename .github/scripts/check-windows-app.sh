#!/usr/bin/env bash
# Checks the Windows app generated from samples/hello-world (run with Git Bash).
# Usage: check-windows-app.sh <output dir: target (Maven) or build (Gradle)>
set -u

TARGET="$1"

NAME="HelloWorld"
VERSION="1.0.0"
APP="$TARGET/$NAME"
EXE="$APP/$NAME.exe"

failures=0
pass() { echo "PASS: $*"; }
fail() { echo "FAIL: $*"; failures=$((failures + 1)); }

# app folder and executable
if [ -d "$APP" ]; then pass "app folder exists: $APP"; else fail "app folder missing: $APP"; exit 1; fi
if [ -f "$EXE" ]; then pass "executable present: $EXE"; else fail "executable missing: $EXE"; fi
if [ -f "$APP/jre/bin/java.exe" ]; then pass "bundled JRE present"; else fail "bundled JRE missing: $APP/jre/bin/java.exe"; fi

# run the app (winConfig.headerType=console in the sample, so output is visible)
# appArgs is not checked: it isn't implemented on Windows yet (#305)
if [ -f "$EXE" ]; then
	OUTPUT=$("$EXE" 2>&1)
	echo "--- app output ---"
	echo "$OUTPUT"
	echo "------------------"
	if echo "$OUTPUT" | grep -q "JavaPackager smoke test OK"; then pass "app runs"; else fail "app did not print the expected line"; fi
	if echo "$OUTPUT" | grep -qF "smoke.prop=hello world"; then pass "vmArgs received"; else fail "vmArgs not received as configured"; fi
fi

# installers (Inno Setup, WiX)
for ext in exe msi msm; do
	FILE="$TARGET/${NAME}_${VERSION}.$ext"
	if [ -f "$FILE" ]; then pass "$ext installer generated: $FILE"; else fail "$ext installer not generated (see the build log)"; fi
done

echo "$failures check(s) failed"
[ "$failures" -eq 0 ]
