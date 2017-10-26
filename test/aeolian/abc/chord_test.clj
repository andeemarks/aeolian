(ns aeolian.abc.chord-test
  (:use midje.sweet)
  (:require [aeolian.abc.chord :refer :all]
            [aeolian.abc.key :as k]
            [clojure.string :as str]))

(tabular "accompanying chords"
         (fact "increase in pitch along with method length within the corresponding key"
               (chord-for-method-length ?method-length ?key ?current-method-length) => (contains ?expected-chord))
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
