(ns aeolian.abc.notes-test
  (:use midje.sweet)
  (:require [aeolian.abc.notes :as notes]
            [aeolian.abc.key :as k]
            [clojure.string :as str]))

(tabular "accompanying chords"
         (fact "increase in pitch along with method length within the corresponding key"
               (notes/pick-chord-for-method-length ?length ?key) => (contains ?expected-chord))
         ?length ?expected-chord ?key
         1 		"C"	   k/major
         5 		"C"    k/major
         6 		"Dm" k/major
         10 	"Dm" k/major
         11 	"Em" k/major
         15 	"Em" k/major
         16 	"F"    k/major
         20 	"F"    k/major
         21 	"G"    k/major
         29 	"G"    k/major
         30 	"A+"    k/major
         39 	"A+"    k/major
         40 	"B+"    k/major
         400 	"B+"    k/major
         4000 "B+"    k/major
         1 		"Cm" k/minor
         5 		"Cm" k/minor
         6 		"Ddim" k/minor
         10 	"Ddim" k/minor
         11 	"Eb"   k/minor
         15 	"Eb"   k/minor
         16 	"Fm" k/minor
         20 	"Fm" k/minor
         21 	"Gm" k/minor
         29 	"Gm" k/minor
         30 	"Ab+"   k/minor
         39 	"Ab+"   k/minor
         40 	"Bb+"   k/minor
         400 	"Bb+"   k/minor
         4000 "Bb+"   k/minor)
