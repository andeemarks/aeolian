#!/bin/bash

if [[ $# -eq 0 ]]; then
    >&2 echo "Usage: go.sh <source-file.java>"
    exit 1
fi

if ! which abc2midi &> /dev/null; then
	>&2 echo “Error: Please install abc2midi and make sure it is in your path”
	exit 1
fi

if ! which timidity &> /dev/null; then
	>&2 echo “Error: Please install timidity and make sure it is in your path”
	exit 1
fi

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
SOURCEFILE=$1
if [[ -r $SOURCEFILE ]]; then
	echo "Generating some complexity metrics from Checkstyle..."
	java -jar $DIR/resources/checkstyle-7.4-all.jar -c $DIR/resources/checkstyle.xml $SOURCEFILE | grep "Cyclomatic Complexity" | awk '{print $2 " " $6}' | awk -F: '{print $2 " " $4}' > /tmp/complexity.txt
	echo "Generating some line length metrics from awk..."
	cat $SOURCEFILE | awk '{print NR " " length($0)}' > /tmp/line-lengths.txt
	echo "Merging the two datasets..."
	join -a1 <(sort /tmp/line-lengths.txt) <(sort /tmp/complexity.txt) > combined-metrics.txt
	echo "Generating ABC notation..."
	lein run combined-metrics.txt
	echo "Generating MIDI..."
	abc2midi combined-metrics.txt.abc
	echo "Playing MIDI..."
	timidity combined-metrics.txt1.mid
else
    >&2 echo "Error: $SOURCEFILE does not exist or cannot be read"
    exit 1
fi