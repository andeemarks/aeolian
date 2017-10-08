#!/usr/bin/env bash
lein run ${UBERMETRICSFILE}.history "{:duplicate-lines ${TOTALDUPLINES} :total-lines ${TOTALLINES}}"
