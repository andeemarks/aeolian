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
echo -e "\e[33mCloning repo to $WORK_DIR...\e[0m"
cd $WORK_DIR

git clone https://github.com/$GITHUB_USER/$GITHUB_REPO.git

cd $GITHUB_REPO

echo -e "\e[33mListing files...\e[0m"

rm blames.txt
git ls-tree -r -z --name-only HEAD| xargs -t -0 -n1 git blame -f -t | awk '{print $2 " " $5}' > blames.txt

function cleanup {
  rm -rf "$WORK_DIR"
  echo "Deleted temp working directory $WORK_DIR"
}

# register the cleanup function to be called on the EXIT signal
# trap cleanup EXIT
