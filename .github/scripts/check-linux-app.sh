#!/usr/bin/env bash
# Checks the GNU/Linux app generated from samples/hello-world.
# Usage: check-linux-app.sh <output dir: target (Maven) or build (Gradle)>
set -u

TARGET="$1"

NAME="HelloWorld"
VERSION="1.0.0"
APP="$TARGET/$NAME"
EXE="$APP/$NAME"

failures=0
pass() { echo "PASS: $*"; }
fail() { echo "FAIL: $*"; failures=$((failures + 1)); }

# app folder and executable
if [ -d "$APP" ]; then pass "app folder exists: $APP"; else fail "app folder missing: $APP"; exit 1; fi
if [ -x "$EXE" ]; then pass "executable present: $EXE"; else fail "executable missing or not executable: $EXE"; fi
if [ -x "$APP/jre/bin/java" ]; then pass "bundled JRE present"; else fail "bundled JRE missing: $APP/jre/bin/java"; fi

# run the app
if [ -x "$EXE" ]; then
	OUTPUT=$("$EXE" 2>&1)
	echo "--- app output ---"
	echo "$OUTPUT"
	echo "------------------"
	if echo "$OUTPUT" | grep -q "JavaPackager smoke test OK"; then pass "app runs"; else fail "app did not print the expected line"; fi
	if echo "$OUTPUT" | grep -qF "args=[--foo, hello world]"; then pass "appArgs received"; else fail "appArgs not received as configured"; fi
	if echo "$OUTPUT" | grep -qF "smoke.prop=hello world"; then pass "vmArgs received"; else fail "vmArgs not received as configured"; fi
	if echo "$OUTPUT" | grep -qE "^java.class.path=.*:extra$"; then pass "classpath received"; else fail "classpath not received as configured"; fi
fi

# installers
for ext in deb rpm AppImage; do
	FILE="$TARGET/${NAME}_${VERSION}.$ext"
	if [ -f "$FILE" ]; then pass "$ext generated: $FILE"; else fail "$ext not generated (see the build log)"; fi
done

echo "$failures check(s) failed"
[ "$failures" -eq 0 ]
