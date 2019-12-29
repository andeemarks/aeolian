#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CHECKSTYLEDIR=${DIR}/resources
SOURCEFILE=$1
TEMPDIR="/tmp"
OUTPUTDIR=${2:-$TEMPDIR}
RAWSOURCECLASSNAME=$(basename ${SOURCEFILE} .java)
SEMIUID=`od -x /dev/urandom | head -1 | awk '{OFS="-"; print $2}'`
COMBINEDMETRICSFILE=${OUTPUTDIR}/${RAWSOURCECLASSNAME}.${SEMIUID}.metrics.all

function cleanup {
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
	echo -e "\033[34m\Generating Checkstyle metrics...\033[0m"
	java  -Doutput.file=${COMBINEDMETRICSFILE} -classpath "${CHECKSTYLEDIR}/edn-listener.jar:${CHECKSTYLEDIR}/checkstyle-7.4-all.jar" com.puppycrawl.tools.checkstyle.Main -c "${CHECKSTYLEDIR}/checkstyle-all.xml" "${SOURCEFILE}" > /dev/null
	# Turn the checkstyle checker class names into keywords aeolian is expecting
	sed -i 's/com.puppycrawl.tools.checkstyle.checks.sizes.LineLengthCheck/line-length/g' "${COMBINEDMETRICSFILE}"
	sed -i 's/com.puppycrawl.tools.checkstyle.checks.sizes.MethodLengthCheck/method-length/g' "${COMBINEDMETRICSFILE}"
	sed -i 's/com.puppycrawl.tools.checkstyle.checks.metrics.CyclomaticComplexityCheck/complexity/g' "${COMBINEDMETRICSFILE}"
	sed -i 's/com.puppycrawl.tools.checkstyle.checks.sizes.MethodCountCheck/method-count/g' "${COMBINEDMETRICSFILE}"
	sed -i 's/com.puppycrawl.tools.checkstyle.checks.sizes.FileLengthCheck/file-length/g' "${COMBINEDMETRICSFILE}"
}

# set -e
set -o pipefail
set -o nounset

check-usage "${@}"

if [[ -r ${SOURCEFILE} ]]; then
	collect-checkstyle-metrics
	exit 0
else
  >&2 echo "Error: ${SOURCEFILE} does not exist or cannot be read"
  exit 1
fi

trap cleanup EXIT
