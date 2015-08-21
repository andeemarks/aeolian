#!/bin/bash

CHECKSTYLE_JAR=../resources/checkstyle-6.9-all.jar
CHECKSTYLE_PROPS=../resources/checkstyle.xml

# Generate some complexity metrics from Checkstyle
java -jar $CHECKSTYLE_JAR -c $CHECKSTYLE_PROPS $1 | grep "Cyclomatic Complexity" | awk '{print $1 " " $5}' | awk -F: '{print $2 " " $4}' > complexity.txt

# Generate some line length metrics from awk
cat $1 | awk '{print NR " " length($0)}' > line-lengths.txt

# Merge the two datasets
join -a1 <(sort line-lengths.txt) <(sort complexity.txt) | sort -n > combined-metrics.txt