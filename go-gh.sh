#!/bin/bash

if [[ $# -lt 2 ]]; then
    >&2 echo "Usage: go-gh.sh <github-user> <github-repo>"
    exit 1
fi

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
GITHUB_USER=$1
GITHUB_REPO=$2

echo -e "\e[33mChecking GitHub for repo '$GITHUB_REPO' for user '$GITHUB_USER'...\e[0m"
curl "https://api.github.com/repos/$GITHUB_USER/$GITHUB_REPO/languages"

WORK_DIR=`mktemp -d -p "$DIR"`
BIN_DIR=$DIR

echo -e "\e[33mCloning repo to $WORK_DIR...\e[0m"
cd $WORK_DIR

git clone -q https://github.com/$GITHUB_USER/$GITHUB_REPO.git

cd $BIN_DIR

echo -e "\e[33mRunning metrics on all Java files...\e[0m"

# find . -name "*.java" | xargs -t -n1 git blame -f -t -e | awk '{print $1 " " $2 " " $3 " " $6}' > blames.txt
find $WORK_DIR -regextype sed -regex ".*[^Test]\.java" -exec ./go.sh '{}' $WORK_DIR \;

UBERMETRICSFILE=${WORK_DIR}/${GITHUB_REPO}.metrics
echo -e "\033[33mBuilding Uber metrics file...\033[0m"
cat $WORK_DIR/*.metrics.all > ${UBERMETRICSFILE}

echo -e "\033[33mGenerating ABC notation...\033[0m"
lein run ${UBERMETRICSFILE}

echo -e "\033[33mGenerating MIDI...\033[0m"
abc2midi ${UBERMETRICSFILE}.abc -o ${UBERMETRICSFILE}.mid

echo -e "\033[33mPlaying All MIDI...\033[0m"
timidity ${UBERMETRICSFILE}.mid

function cleanup {
  rm -rf "$WORK_DIR"
  echo "Deleted temp working directory $WORK_DIR"
}

# register the cleanup function to be called on the EXIT signal
trap cleanup EXIT
