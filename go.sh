#!/bin/bash

if [[ $# -eq 0 ]]; then
    >&2 echo "Usage: go.sh <source-file.java>"
    exit 1
fi

if ! which abc2midi &> /dev/null; then
	>&2 echo “Error: Please install abc2midi and make sure it is in your path”
	exit 1
fi

if ! which lein &> /dev/null; then
	>&2 echo “Error: Please install Leinginen and make sure it is in your path”
	exit 1
fi

if ! which java &> /dev/null; then
	>&2 echo “Error: Please install Java and make sure it is in your path”
	exit 1
fi

if ! which timidity &> /dev/null; then
	>&2 echo “Error: Please install timidity and make sure it is in your path”
	exit 1
fi

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CHECKSTYLEDIR=$DIR/resources
SOURCEFILE=$1
RAWSOURCECLASSNAME=$(basename ${SOURCEFILE} .java)
COMBINEDMETRICSFILE=${RAWSOURCECLASSNAME}.metrics
COMPLEXITYMETRICS=/tmp/${RAWSOURCECLASSNAME}.complexity.metrics
LINELENGTHMETRICS=/tmp/${RAWSOURCECLASSNAME}.line-length.metrics
METHODLENGTHMETRICS=/tmp/${RAWSOURCECLASSNAME}.method-length.metrics
INDENTATIONMETRICS=/tmp/${RAWSOURCECLASSNAME}.indentation.metrics

if [[ -r $SOURCEFILE ]]; then
	echo -e "\033[33mProcessing ${SOURCEFILE}...\033[0m"
	echo -e "\033[33mGenerating Checkstyle cyclomatic complexity metrics...\033[0m"
	java -jar $CHECKSTYLEDIR/checkstyle-7.4-all.jar -c $CHECKSTYLEDIR/checkstyle-complexity.xml $SOURCEFILE | grep "[ERROR]" | awk '{print $2 " " $3}' | awk -F: '{{printf "%s#%d CC=%d\n", $1, $2, $4 }}' > ${COMPLEXITYMETRICS}
	echo -e "\033[33mGenerating Checkstyle line length metrics...\033[0m"
	java -jar $CHECKSTYLEDIR/checkstyle-7.4-all.jar -c $CHECKSTYLEDIR/checkstyle-linelength.xml $SOURCEFILE | grep "[ERROR]" | awk '{print $2 " " $3}' | awk -F: '{printf "%s#%d LL=%d\n", $1, $2, $3 }' > ${LINELENGTHMETRICS}
	echo -e "\033[33mGenerating Checkstyle method length metrics...\033[0m"
	java -jar $CHECKSTYLEDIR/checkstyle-7.4-all.jar -c $CHECKSTYLEDIR/checkstyle-methodlength.xml $SOURCEFILE | grep "[ERROR]" | awk '{print $2 " " $3}' | awk -F: '{printf "%s#%d ML=%d\n", $1, $2, $4 }' > ${METHODLENGTHMETRICS}
	echo -e "\033[33mGenerating Checkstyle indentation metrics...\033[0m"
	java -jar $CHECKSTYLEDIR/checkstyle-7.4-all.jar -c $CHECKSTYLEDIR/checkstyle-indentation.xml $SOURCEFILE | grep "[ERROR]" | awk '{print $2 " " $3}' | awk -F: '{printf "%s#%d IND\n", $1, $2 }' > ${INDENTATIONMETRICS}
	echo -e "\033[33mMerging all datasets...\033[0m"
	join -a 1 <(sort -k 1b,1 ${LINELENGTHMETRICS}) <(sort -k 1b,1 ${COMPLEXITYMETRICS}) > ${COMBINEDMETRICSFILE}.tmp
	join -a 1 <(sort -k 1b,1 ${COMBINEDMETRICSFILE}.tmp) <(sort -k 1b,1 ${INDENTATIONMETRICS}) | sort -V > ${COMBINEDMETRICSFILE}.tmp2
	join -a 1 <(sort -k 1b,1 ${COMBINEDMETRICSFILE}.tmp2) <(sort -k 1b,1 ${METHODLENGTHMETRICS}) | sort -V > ${COMBINEDMETRICSFILE}
	rm -f ${COMBINEDMETRICSFILE}.tmp
	rm -f ${COMBINEDMETRICSFILE}.tmp2
	echo -e "\033[33mGenerating ABC notation...\033[0m"
	lein run ${COMBINEDMETRICSFILE}
	echo -e "\033[33mGenerating MIDI...\033[0m"
	abc2midi ${COMBINEDMETRICSFILE}.abc -o ${COMBINEDMETRICSFILE}.mid
	echo -e "\033[33mPlaying MIDI...\033[0m"
	# timidity ${COMBINEDMETRICSFILE}.mid
	exit 0
else
    >&2 echo "Error: $SOURCEFILE does not exist or cannot be read"
    exit 1
fi