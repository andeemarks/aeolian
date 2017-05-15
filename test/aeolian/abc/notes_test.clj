(ns aeolian.abc.notes-test
  (:use midje.sweet)
  (:require [aeolian.abc.notes :as notes]
            [aeolian.abc.key :as k]
            [clojure.string :as str]))

(tabular "accompanying chords"
         (fact "increase in pitch along with method length within the corresponding key"
               (notes/pick-chord-for-method-length ?length ?key) => (contains ?expected-chord))
         ?length ?expected-chord ?key
         1 		"C"	k/major
         5 		"C" k/major
         6 		"D" k/major
         10 		"D" k/major
         11 		"E" k/major
         15 		"E" k/major
         16 		"F" k/major
         20 		"F" k/major
         21 		"G" k/major
         29 		"G" k/major
         30 		"A" k/major
         39 		"A" k/major
         40 		"B" k/major
         400 	"B" k/major
         4000 	"B" k/major
         1 		"A"	k/minor
         5 		"A" k/minor
         6 		"B" k/minor
         10 		"B" k/minor
         11 		"C" k/minor
         15 		"C" k/minor
         16 		"D" k/minor
         20 		"D" k/minor
         21 		"E" k/minor
         29 		"E" k/minor
         30 		"F" k/minor
         39 		"F" k/minor
         40 		"G" k/minor
         400 	"G" k/minor
         4000 	"G" k/minor)
