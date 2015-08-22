#!/bin/bash

if [ "$#" -ne 1 ]
then
  echo "Usage: metrics.sh <Java source file>"
  exit 1
fi

CHECKSTYLE_JAR=../resources/checkstyle-6.9-all.jar
CHECKSTYLE_PROPS=../resources/checkstyle.xml

TEMP_DIR=/tmp
TEMP_COMPLEXITY_METRICS=`mktemp /tmp/complexity.txt.XXXXXX` || exit 1
TEMP_LENGTH_METRICS=`mktemp /tmp/line-lengths.txt.XXXXXX` || exit 1

COMBINED_METRICS=combined-metrics.txt

echo "Processing $1..."
echo "1. Generating complexity metrics with Checkstyle..."
java -jar $CHECKSTYLE_JAR -c $CHECKSTYLE_PROPS $1 | grep "Cyclomatic Complexity" | awk '{print $1 " " $5}' | awk -F: '{print $2 " " $4}' > $TEMP_COMPLEXITY_METRICS

echo "2. Generating line length metrics with awk..."
cat $1 | awk '{print NR " " length($0)}' > $TEMP_LENGTH_METRICS

echo "3. Merging metric datasets..."
join -a1 <(sort $TEMP_LENGTH_METRICS) <(sort $TEMP_COMPLEXITY_METRICS) | sort -n > $COMBINED_METRICS

echo "Generated " $COMBINED_METRICS