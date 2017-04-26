#!/bin/bash

if [[ $# -eq 0 ]]; then
    >&2 echo "Usage: go.sh <source-file.java> <output-dir>"
    exit 1
fi

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CHECKSTYLEDIR=$DIR/resources
SOURCEFILE=$1
TEMPDIR="/tmp"
OUTPUTDIR=${2:-$TEMPDIR}
RAWSOURCECLASSNAME=$(basename ${SOURCEFILE} .java)
SEMIUID=`od -x /dev/urandom | head -1 | awk '{OFS="-"; print $2}'`
COMBINEDMETRICSFILE=${OUTPUTDIR}/${RAWSOURCECLASSNAME}.${SEMIUID}.metrics.all
COMPLEXITYMETRICS=${OUTPUTDIR}/${RAWSOURCECLASSNAME}.complexity.metrics
LINELENGTHMETRICS=${OUTPUTDIR}/${RAWSOURCECLASSNAME}.line-length.metrics
METHODLENGTHMETRICS=${OUTPUTDIR}/${RAWSOURCECLASSNAME}.method-length.metrics
INDENTATIONMETRICS=${OUTPUTDIR}/${RAWSOURCECLASSNAME}.indentation.metrics

if [[ -r $SOURCEFILE ]]; then
	echo -e "\033[1;34mProcessing ${SOURCEFILE} into ${OUTPUTDIR}...\033[0m"
	echo -e "\033[34mGenerating Checkstyle cyclomatic complexity metrics...\033[0m"
	java -jar $CHECKSTYLEDIR/checkstyle-7.4-all.jar -c $CHECKSTYLEDIR/checkstyle-complexity.xml $SOURCEFILE | grep "[ERROR]" | awk '{print $2 " " $3}' | awk -F: '{{printf "%s#%d CC=%d\n", $1, $2, $4 }}' > ${COMPLEXITYMETRICS}
	echo -e "\033[34mGenerating Checkstyle line length metrics...\033[0m"
	java -jar $CHECKSTYLEDIR/checkstyle-7.4-all.jar -c $CHECKSTYLEDIR/checkstyle-linelength.xml $SOURCEFILE | grep "[ERROR]" | awk '{print $2 " " $3}' | awk -F: '{printf "%s#%d LL=%d\n", $1, $2, $3 }' > ${LINELENGTHMETRICS}
	echo -e "\033[34mGenerating Checkstyle method length metrics...\033[0m"
	java -jar $CHECKSTYLEDIR/checkstyle-7.4-all.jar -c $CHECKSTYLEDIR/checkstyle-methodlength.xml $SOURCEFILE | grep "[ERROR]" | awk '{print $2 " " $3}' | awk -F: '{printf "%s#%d ML=%d\n", $1, $2, $4 }' > ${METHODLENGTHMETRICS}
	echo -e "\033[34mGenerating Checkstyle indentation metrics...\033[0m"
	java -jar $CHECKSTYLEDIR/checkstyle-7.4-all.jar -c $CHECKSTYLEDIR/checkstyle-indentation.xml $SOURCEFILE | grep "[ERROR]" | awk '{print $2 " " $3}' | awk -F: '{printf "%s#%d IND\n", $1, $2 }' > ${INDENTATIONMETRICS}
	echo -e "\033[34mMerging all metrics...\033[0m"
	join -a 1 <(sort -k 1b,1 ${LINELENGTHMETRICS}) <(sort -k 1b,1 ${COMPLEXITYMETRICS}) > ${COMBINEDMETRICSFILE}.tmp
	join -a 1 <(sort -k 1b,1 ${COMBINEDMETRICSFILE}.tmp) <(sort -k 1b,1 ${INDENTATIONMETRICS}) | sort -V > ${COMBINEDMETRICSFILE}.tmp2
	join -a 1 <(sort -k 1b,1 ${COMBINEDMETRICSFILE}.tmp2) <(sort -k 1b,1 ${METHODLENGTHMETRICS}) | sort -V > ${COMBINEDMETRICSFILE}
	exit 0
else
    >&2 echo "Error: $SOURCEFILE does not exist or cannot be read"
    exit 1
fi

function cleanup {
	rm -f ${COMBINEDMETRICSFILE}.tmp
	rm -f ${COMBINEDMETRICSFILE}.tmp2
  	echo "\033[34mDeleted temp metrics files\033[0m"
}

# register the cleanup function to be called on the EXIT signal
trap cleanup EXIT
