(ns aeolian.abc.notepitch-test
  (:use midje.sweet)
  (:require [aeolian.abc.notepitch :refer :all]
            [aeolian.abc.key :as k]
            [clojure.string :as str]))

(facts "line length is mapped to note"
       (fact "empty lines are mapped to rests"
             (note-for-line-length 0 k/major) => rest-note)

       (tabular
        (fact "longer lines are mapped to actual notes with longer lines at higher octaves"
              ?expected-note => (contains (note-for-line-length ?line-length ?composition-key)))
        ?expected-note    ?line-length  ?composition-key
        major-octave-1    1             k/major
        major-octave-1    9             k/major
        major-octave-2    10            k/major
        major-octave-2    39            k/major
        major-octave-3    40            k/major
        major-octave-3    79            k/major
        major-octave-4    80            k/major
        major-octave-4    99            k/major
        major-octave-5    100           k/major
        major-octave-5    200           k/major
        major-octave-5    2000          k/major
        minor-octave-1    1             k/minor
        minor-octave-1    9             k/minor
        minor-octave-2    10            k/minor
        minor-octave-2    39            k/minor
        minor-octave-3    40            k/minor
        minor-octave-3    79            k/minor
        minor-octave-4    80            k/minor
        minor-octave-4    99            k/minor
        minor-octave-5    100           k/minor
        minor-octave-5    200           k/minor
        minor-octave-5    2000          k/minor))
