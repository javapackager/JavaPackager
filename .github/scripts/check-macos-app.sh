#!/usr/bin/env bash
# Checks the macOS app generated from samples/hello-world.
# Usage: check-macos-app.sh <target dir> <macStartup> <administratorRequired>
set -u

TARGET="$1"
MAC_STARTUP="$2"
ADMIN="$3"

NAME="HelloWorld"
VERSION="1.0.0"
APP="$TARGET/$NAME/$NAME.app"
MACOS="$APP/Contents/MacOS"
STUB="$MACOS/universalJavaApplicationStub"

failures=0
pass() { echo "PASS: $*"; }
fail() { echo "FAIL: $*"; failures=$((failures + 1)); }

# app bundle
if [ -d "$APP" ]; then pass "app bundle exists: $APP"; else fail "app bundle missing: $APP"; exit 1; fi

# executable declared in Info.plist
EXE_NAME=$(/usr/libexec/PlistBuddy -c "Print :CFBundleExecutable" "$APP/Contents/Info.plist")
EXE="$MACOS/$EXE_NAME"
echo "CFBundleExecutable: $EXE_NAME"
if [ -x "$EXE" ]; then pass "executable present: $EXE"; else fail "executable missing or not executable: $EXE"; fi

# launcher stub (also called by the 'startup' script when administratorRequired=true, issues #473/#398)
if [ -x "$STUB" ]; then pass "launcher present: $STUB"; else fail "launcher missing: $STUB"; fi

# with administratorRequired=true, the 'startup' script must invoke the launcher
if [ "$ADMIN" = "true" ]; then
	if grep -q "SCRIPTPATH/$(basename "$STUB")" "$MACOS/startup"; then pass "startup script invokes $(basename "$STUB")"; else fail "startup script doesn't invoke $(basename "$STUB")"; cat "$MACOS/startup"; fi
fi

# compiled launchers must contain native arm64 code (issues #448/#449)
if [ "$MAC_STARTUP" != "SCRIPT" ] && [ -f "$STUB" ]; then
	file "$STUB"
	ARCHS=$(lipo -archs "$STUB" 2>/dev/null || true)
	echo "launcher archs: $ARCHS"
	case "$ARCHS" in
		*arm64*) pass "launcher has arm64 code" ;;
		*) fail "launcher has no arm64 code" ;;
	esac
fi

# run the app (not possible when administratorRequired=true: it asks for a password)
if [ "$ADMIN" != "true" ] && [ -x "$EXE" ]; then
	OUTPUT=$("$EXE" 2>&1)
	echo "--- app output ---"
	echo "$OUTPUT"
	echo "------------------"
	if echo "$OUTPUT" | grep -q "JavaPackager smoke test OK"; then pass "app runs"; else fail "app did not print the expected line"; fi
	if echo "$OUTPUT" | grep -q "os.arch=aarch64"; then pass "JVM runs natively on arm64"; else fail "JVM is not running as arm64 (Rosetta?)"; fi
	if echo "$OUTPUT" | grep -qF "args=[--foo, hello world]"; then pass "appArgs received"; else fail "appArgs not received as configured"; fi
fi

# installers (DMG customization runs osascript, issues #474/#377)
DMG="$TARGET/${NAME}_${VERSION}.dmg"
PKG="$TARGET/${NAME}_${VERSION}.pkg"
if [ -f "$DMG" ]; then pass "DMG generated: $DMG"; else fail "DMG not generated (see 'DMG image generation failed' in the build log)"; fi
if [ -f "$PKG" ]; then pass "PKG generated: $PKG"; else fail "PKG not generated"; fi

echo "$failures check(s) failed"
[ "$failures" -eq 0 ]
