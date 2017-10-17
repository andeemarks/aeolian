#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CHECKSTYLEDIR=${DIR}/resources
SOURCEFILE=$1
TEMPDIR="/tmp"
OUTPUTDIR=${2:-$TEMPDIR}
RAWSOURCECLASSNAME=$(basename ${SOURCEFILE} .java)
SEMIUID=`od -x /dev/urandom | head -1 | awk '{OFS="-"; print $2}'`
COMBINEDMETRICSFILE=${OUTPUTDIR}/${RAWSOURCECLASSNAME}.${SEMIUID}.metrics.all
COMPLEXITYMETRICS=${OUTPUTDIR}/${RAWSOURCECLASSNAME}.complexity.metrics
LINELENGTHMETRICS=${OUTPUTDIR}/${RAWSOURCECLASSNAME}.line-length.metrics
METHODLENGTHMETRICS=${OUTPUTDIR}/${RAWSOURCECLASSNAME}.method-length.metrics
FILELENGTHMETRICS=${OUTPUTDIR}/${RAWSOURCECLASSNAME}.file-length.metrics
INDENTATIONMETRICS=${OUTPUTDIR}/${RAWSOURCECLASSNAME}.indentation.metrics

function cleanup {
	rm -f ${COMBINEDMETRICSFILE}.tmp
	rm -f ${COMBINEDMETRICSFILE}.tmp2
	rm -f ${COMPLEXITYMETRICS}
	rm -f ${LINELENGTHMETRICS}
	rm -f ${METHODLENGTHMETRICS}
	rm -f ${FILELENGTHMETRICS}
	rm -f ${INDENTATIONMETRICS}
	rm -f *.bak

  	echo -e "\033[34mDeleted temp metrics files\033[0m"
}

function check-usage() {
	if [[ $# -eq 0 ]]; then
	    >&2 echo "Usage: go.sh <source-file.java> <output-dir>"
	    exit 1
	fi
}

function collect-checkstyle-metrics() {
	echo -e "\033[1;34mProcessing ${RAWSOURCECLASSNAME}...\033[0m"
	echo -e "\033[34m\Generating Checkstyle cyclomatic complexity metrics...\033[0m"
	java -jar ${CHECKSTYLEDIR}/checkstyle-7.4-all.jar -c ${CHECKSTYLEDIR}/checkstyle-complexity.xml ${SOURCEFILE} | grep "[ERROR]" | awk '{print $2 " " $3}' | awk -F: '{{printf "%s#%d CC=%d\n", $1, $2, $4 }}' > ${COMPLEXITYMETRICS}
	echo -e "\033[34m |Generating Checkstyle line length metrics...\033[0m"
	java -jar ${CHECKSTYLEDIR}/checkstyle-7.4-all.jar -c ${CHECKSTYLEDIR}/checkstyle-linelength.xml ${SOURCEFILE} | grep "[ERROR]" | awk '{print $2 " " $3}' | awk -F: '{printf "%s#%d LL=%d\n", $1, $2, $3 }' > ${LINELENGTHMETRICS}
	echo -e "\033[34m |Generating Checkstyle method length metrics...\033[0m"
	java -jar ${CHECKSTYLEDIR}/checkstyle-7.4-all.jar -c ${CHECKSTYLEDIR}/checkstyle-methodlength.xml ${SOURCEFILE} | grep "[ERROR]" | awk '{print $2 " " $3}' | awk -F: '{printf "%s#%d ML=%d\n", $1, $2, $4 }' > ${METHODLENGTHMETRICS}
	echo -e "\033[34m |Generating Checkstyle file length metrics...\033[0m"
	java -jar ${CHECKSTYLEDIR}/checkstyle-7.4-all.jar -c ${CHECKSTYLEDIR}/checkstyle-filelength.xml ${SOURCEFILE} | grep "[ERROR]" | awk '{print $2 " " $3}' | awk -F: '{printf "%s#%d FL=%d\n", $1, $2, $4 }' > ${FILELENGTHMETRICS}
	echo -e "\033[34m/Generating Checkstyle indentation metrics...\033[0m"
	java -jar ${CHECKSTYLEDIR}/checkstyle-7.4-all.jar -c ${CHECKSTYLEDIR}/checkstyle-indentation.xml ${SOURCEFILE} | grep "[ERROR]" | awk '{print $2 " " $3}' | awk -F: '{printf "%s#%d IND\n", $1, $2 }' > ${INDENTATIONMETRICS}
	# echo -e "\033[34mMerging all metrics...\033[0m"
	join -a 1 <(sort -k 1b,1 ${LINELENGTHMETRICS}) <(sort -k 1b,1 ${COMPLEXITYMETRICS}) > ${COMBINEDMETRICSFILE}.tmp
	join -a 1 <(sort -k 1b,1 ${COMBINEDMETRICSFILE}.tmp) <(sort -k 1b,1 ${INDENTATIONMETRICS}) | sort -V > ${COMBINEDMETRICSFILE}.tmp2
	join -a 1 <(sort -k 1b,1 ${COMBINEDMETRICSFILE}.tmp2) <(sort -k 1b,1 ${FILELENGTHMETRICS}) | sort -V > ${COMBINEDMETRICSFILE}.tmp3
	join -a 1 <(sort -k 1b,1 ${COMBINEDMETRICSFILE}.tmp3) <(sort -k 1b,1 ${METHODLENGTHMETRICS}) | sort -V > ${COMBINEDMETRICSFILE}
}

# set -e
set -o pipefail
set -o nounset

check-usage $@

if [[ -r ${SOURCEFILE} ]]; then
	collect-checkstyle-metrics
	exit 0
else
  >&2 echo "Error: ${SOURCEFILE} does not exist or cannot be read"
  exit 1
fi

trap cleanup EXIT
