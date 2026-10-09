#!/usr/bin/env bash
# Waits for a smoke tests run and summarizes it: one line per job and, for failed jobs, the
# PASS/FAIL checks and errors from the log.
# Usage: ci-status.sh [run-id]   (default: latest smoke-tests.yml run on devel)
set -u

REPO="${REPO:-javapackager/JavaPackager}"

# gh calls fail now and then with "error connecting to api.github.com": retry them
retry() { for i in 1 2 3 4 5; do "$@" 2>/dev/null && return 0; sleep 5; done; return 1; }

RID="${1:-$(retry gh run list --repo "$REPO" --workflow smoke-tests.yml --branch devel --limit 1 --json databaseId --jq '.[0].databaseId')}"
[ -z "$RID" ] && { echo "No run found"; exit 1; }
echo "run $RID: https://github.com/$REPO/actions/runs/$RID"

while [ "$(retry gh run view "$RID" --repo "$REPO" --json status --jq .status)" != "completed" ]; do sleep 30; done

retry gh run view "$RID" --repo "$REPO" --json conclusion,headSha,jobs \
	--jq '"conclusion: \(.conclusion) (commit \(.headSha[0:7]))", (.jobs[] | "\(.conclusion)\t\(.name)\t" + ([.steps[] | select(.conclusion=="failure") | .name] | join(", ")))'

for JID in $(retry gh run view "$RID" --repo "$REPO" --json jobs --jq '.jobs[] | select(.conclusion=="failure") | .databaseId'); do
	NAME=$(retry gh run view "$RID" --repo "$REPO" --json jobs --jq ".jobs[] | select(.databaseId==$JID) | .name")
	echo
	echo "===== $NAME"
	# job logs come with ANSI escapes and a timestamp prefix
	retry gh api "repos/$REPO/actions/jobs/$JID/logs" --allow-escape-sequences \
		| sed 's/\x1b\[[0-9;]*m//g' | cut -c30- \
		| grep -E '^(PASS|FAIL)|^args=|^smoke\.prop=|BUILD FAILURE|\[ERROR\] Failed|Caused by|Exception:|generation failed|retrying|Lexical error|error:' \
		| grep -v 'urls\[' | head -40
done
