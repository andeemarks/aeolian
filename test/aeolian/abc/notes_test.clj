(ns aeolian.abc.notes-test
  (:use midje.sweet)
  (:require [aeolian.abc.notes :refer :all]
            [aeolian.abc.key :as k]
            [clojure.string :as str]))

(facts "line length is mapped to note"
  (fact "empty lines are mapped to rests"
        (pick-note-for-line-length 0 k/major) => rest-note)

 (tabular
  (fact "longer lines are mapped to actual notes with longer lines at higher octaves"
        ?expected-note => (contains (pick-note-for-line-length ?line-length ?composition-key)))
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

(tabular "accompanying chords"
  (fact "increase in pitch along with method length within the corresponding key"
    (pick-chord-for-method-length ?method-length ?key ?current-method-length) => (contains ?expected-chord))
  ?method-length  ?key      ?current-method-length ?expected-chord
  1               k/major   1 "C"
  5               k/major   1 "C"
  6               k/major   1 "Dm"
  10              k/major   1 "Dm"
  11              k/major   1 "Em"
  15              k/major   1 "Em"
  16              k/major   1 "F"
  20              k/major   1 "F"
  21              k/major   1 "G"
  29              k/major   1 "G"
  30              k/major   1 "A+"
  39              k/major   1 "A+"
  40              k/major   1 "B+"
  400             k/major   1 "B+"
  4000            k/major   1 "B+"
  1               k/minor   1 "Cm"
  5               k/minor   1 "Cm"
  6               k/minor   1 "Ddim"
  10              k/minor   1 "Ddim"
  11              k/minor   1 "_E"
  15              k/minor   1 "_E"
  16              k/minor   1 "Fm"
  20              k/minor   1 "Fm"
  21              k/minor   1 "Gm"
  29              k/minor   1 "Gm"
  30              k/minor   1 "_A+"
  39              k/minor   1 "_A+"
  40              k/minor   1 "_B+"
  400             k/minor   1 "_B+"
  4000            k/minor   1 "_B+"
  nil             k/major   1 "C"
  nil             k/major   29 "G")
