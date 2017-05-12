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
		4000 	"B" k/major)
