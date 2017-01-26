#!/bin/bash

if [[ $# -eq 0 ]]; then
    >&2 echo "Usage: go.sh <source-file.java>"
    exit 1
fi

if [[ -r $1 ]]; then
	echo "Generating some complexity metrics from Checkstyle..."
	java -jar checkstyle-7.4-all.jar -c checkstyle.xml $1 | grep "Cyclomatic Complexity" | awk '{print $2 " " $6}' | awk -F: '{print $2 " " $4}' > complexity.txt
	echo "Generating some line length metrics from awk..."
	cat $1 | awk '{print NR " " length($0)}' > line-lengths.txt
	echo "Merging the two datasets..."
	join -a1 <(sort line-lengths.txt) <(sort complexity.txt) > combined-metrics.txt
else
    >&2 echo "$1 does not exist or cannot be read"
    exit 1
fi