(ns aeolian.abc.notes-test
  (:use midje.sweet)
  (:require [aeolian.abc.notes :as notes]
            [aeolian.abc.key :as k]
            [clojure.string :as str]))

(tabular "accompanying chords"
  (fact "increase in pitch along with method length within the corresponding key"
    (notes/pick-chord-for-method-length ?length ?key ?current-chord) => (contains ?expected-chord))
      ?length ?expected-chord ?key ?current-chord
      1     "C"     k/major 1
      5     "C"     k/major 1
      6     "Dm"    k/major 1
      10    "Dm"    k/major 1
      11    "Em"    k/major 1
      15    "Em"    k/major 1
      16    "F"     k/major 1
      20    "F"     k/major 1
      21    "G"     k/major 1
      29    "G"     k/major 1
      30    "A+"    k/major 1
      39    "A+"    k/major 1
      40    "B+"    k/major 1
      400   "B+"    k/major 1
      4000  "B+"    k/major 1
      1     "Cm"    k/minor 1
      5     "Cm"    k/minor 1
      6     "Ddim"  k/minor 1
      10    "Ddim"  k/minor 1
      11    "_E"    k/minor 1
      15    "_E"    k/minor 1
      16    "Fm"    k/minor 1
      20    "Fm"    k/minor 1
      21    "Gm"    k/minor 1
      29    "Gm"    k/minor 1
      30    "_A+"   k/minor 1
      39    "_A+"   k/minor 1
      40    "_B+"   k/minor 1
      400   "_B+"   k/minor 1
      4000  "_B+"   k/minor 1
      nil   "C"	    k/major 1
      nil 	"G"      k/major 29)
