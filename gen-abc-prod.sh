#!/usr/bin/env bash
java -jar aeolian-standalone.jar ${UBERMETRICSFILE}.history "{:duplicate-lines ${TOTALDUPLINES} :total-lines ${TOTALLINES}}"
