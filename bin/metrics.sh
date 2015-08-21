#!/bin/bash

CHECKSTYLE_JAR=/path/to/checkstyle.jar
CHECKSTYLE_PROPS=../resources/checkstyle.xml

# Generate some complexity metrics from Checkstyle
java -jar $CHECKSTYLE_JAR -c $CHECKSTYLE_PROPS Foo.java | grep "Cyclomatic Complexity" | awk '{print $1 " " $5}' | awk -F: '{print $2 " " $4}' > complexity.txt

# Generate some line length metrics from awk
cat Foo.java | awk '{print NR " " length($0)}' > line-lengths.txt

# Merge the two datasets
join -a1 <(sort line-lengths.txt) <(sort complexity.txt) > combined-metrics.txt