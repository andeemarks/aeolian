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
	echo -e "\e[33mGenerating Checkstyle cyclomatic complexity metrics...\e[0m"
	java -jar $DIR/resources/checkstyle-7.4-all.jar -c $DIR/resources/checkstyle-complexity.xml $SOURCEFILE | grep "CyclomaticComplexity" | awk '{print $2 " " $3}' | awk -F: '{print $1 " " $2 " " $4}' > /tmp/complexity.txt
	echo -e "\e[33mGenerating Checkstyle line length metrics...\e[0m"
	java -jar $DIR/resources/checkstyle-7.4-all.jar -c $DIR/resources/checkstyle-linelength.xml $SOURCEFILE | grep "LineLength" | awk '{print $2 " " $3}' | awk -F: '{print $1 " " $2 " " $3}' > /tmp/line-lengths.txt
	echo -e "\e[33mMerging the two datasets...\e[0m"
	join -a1 <(sort -g --key=2 /tmp/line-lengths.txt) <(sort -g --key=2 /tmp/complexity.txt) > combined-metrics.txt
	echo -e "\e[33mGenerating ABC notation...\e[0m"
	lein run combined-metrics.txt
	echo -e "\e[33mGenerating MIDI...\e[0m"
	abc2midi combined-metrics.txt.abc
	echo -e "\e[33mPlaying MIDI...\e[0m"
	timidity combined-metrics.txt1.mid
else
    >&2 echo "Error: $SOURCEFILE does not exist or cannot be read"
    exit 1
fi