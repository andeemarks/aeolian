#!/bin/bash

if [[ $# -lt 2 ]]; then
    >&2 echo "Usage: go-gh.sh <github-user> <github-repo>"
    exit 1
fi

# check deps
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
GITHUB_USER=$1
GITHUB_REPO=$2

echo -e "\033[33mChecking GitHub for repo '$GITHUB_REPO' for user '$GITHUB_USER'...\033[0m"

WORK_DIR=`mktemp -d -p "$DIR"`
BIN_DIR=$DIR

# Get code
echo -e "\033[33mCloning repo to $WORK_DIR...\033[0m"
cd $WORK_DIR
git clone -q https://github.com/$GITHUB_USER/$GITHUB_REPO.git
# git clone -q https://github.com/ReactiveX/RxJava.git

cd $BIN_DIR


# Git blame each file to get author and commit SHA
echo -e "\033[33mFind commit history for each Java file...\033[0m"
pushd $WORK_DIR/$GITHUB_REPO >/dev/null
find . -regex ".*[^Test]\.java" | xargs -n1 git blame -f -t -e | awk -v PREFIX=${WORK_DIR}/${GITHUB_REPO}/ -F "[ ()]+" '{print PREFIX $2 "#"$6 " AU=" $3 " TS=" $4}' > ${WORK_DIR}/blames.txt
popd >/dev/null

echo -e "\033[34mGenerating Checkstyle duplication metrics...\033[0m"
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CHECKSTYLEDIR=$DIR/resources
java -jar $CHECKSTYLEDIR/simian-2.4.0.jar -threshold=10 -excludes="**/test/**/*.java" $WORK_DIR/$GITHUB_REPO/**/*.java | tail -3 | head -2 > ${WORK_DIR}/duplication.txt
DUPLICATESTATS=`cat ${WORK_DIR}/duplication.txt`
TOTALDUPLINESRE='Found ([0-9]+).*'
TOTALLINESRE='Processed a total of ([0-9]+).*'
[[ "${DUPLICATESTATS}" =~ ${TOTALDUPLINESRE} ]]
TOTALDUPLINES=${BASH_REMATCH[1]}
[[ "${DUPLICATESTATS}" =~ ${TOTALLINESRE} ]] 
TOTALLINES=${BASH_REMATCH[1]}
echo ${TOTALDUPLINES}
echo ${TOTALLINES}

# Run each non Test source file through go.sh
echo -e "\033[33mRunning metrics on all Java files...\033[0m"
find $WORK_DIR -regex ".*[^Test]\.java" -exec ./go.sh '{}' $WORK_DIR \;

UBERMETRICSFILE=${WORK_DIR}/${GITHUB_REPO}.metrics
echo -e "\033[33mBuilding uber metrics file...\033[0m"
cat $WORK_DIR/*.metrics.all > ${UBERMETRICSFILE}

echo -e "\033[33mJoining metrics file with Git commit history...\033[0m"
join -a 1 <(sort -k 1b,1 ${WORK_DIR}/blames.txt) <(sort -k 1b,1 ${UBERMETRICSFILE}) | sort -V > ${UBERMETRICSFILE}.history
sed -i.bak '/LL=/!d' ${UBERMETRICSFILE}.history

echo -e "\033[33mGenerating ABC notation...\033[0m"
lein run ${UBERMETRICSFILE}.history "{:duplicate-lines ${TOTALDUPLINES} :total-lines ${TOTALLINES}}"

ABCFILE=${UBERMETRICSFILE}.history.abc
MIDIFILE=${UBERMETRICSFILE}.mid
echo -e "\033[33mGenerating MIDI...\033[0m"
abc2midi ${ABCFILE} -s -o ${MIDIFILE}

echo -e "\033[33mPlaying MIDI...\033[0m"
timidity ${MIDIFILE}

MIDIARCHIVEDIR=${DIR}/archive/midi
ABCARCHIVEDIR=${DIR}/archive/abc
echo -e "\033[33mArchiving generated files...\033[0m"
cp ${MIDIFILE} ${MIDIARCHIVEDIR}/${GITHUB_REPO}.$( date +"%Y-%m-%d_%H-%M-%S" ).mid
cp ${ABCFILE} ${ABCARCHIVEDIR}/${GITHUB_REPO}.$( date +"%Y-%m-%d_%H-%M-%S" ).abc

function cleanup {
  rm -rf "$WORK_DIR"
  echo -e "\033[33mDeleted temp working directory $WORK_DIR\033[0m"
}

# register the cleanup function to be called on the EXIT signal
# trap cleanup EXIT
