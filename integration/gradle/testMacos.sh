#!/usr/bin/env bash

set -e

test_app () {
  open /Applications/app.app

  # Wait for the app to actually run and make the test file (but give up after a minute so the job can fail)
  attempts=0
  until [ -f /tmp/javapackager-testfile.txt ] || [ $attempts -ge 30 ]
  do
      sleep 2
      attempts=$((attempts+1))
  done

  file_contents=$(cat /tmp/javapackager-testfile.txt)
  now_in_millis=$(date '+%s%N' | cut -b1-13)

  if [ "$file_contents" -lt "$((now_in_millis - 10000))" ]; then
      echo "Test file was too old (should never happen given the rms before)"
      exit 1
  fi
}

# Clean possible leftovers of previous test runs (should never exist on gh actions)
hdiutil detach /Volumes/app || true
sudo rm -rf /Applications/app.app /tmp/javapackager-testfile.txt

../../gradlew packageForMac
# Not removing this leaves the installer package trying to installer over this version of
# the app instead of installing to the real destination in /Applications
rm -rf app/build/app


hdiutil attach app/build/app_1.0.0-SNAPSHOT.dmg
cp -r /Volumes/app/app.app /Applications
hdiutil detach /Volumes/app

test_app

rm -rf /Applications/app.app /tmp/javapackager-testfile.txt

# This sudo is a bit annoying for local testing, but find in the actions build
sudo installer -pkg app/build/app_1.0.0-SNAPSHOT.pkg -target /

test_app

sudo rm -rf /Applications/app.app /tmp/javapackager-testfile.txt

