(ns aeolian.abc.notes-test
  (:use midje.sweet)
  (:require [aeolian.abc.notes :as notes]
  			[clojure.string :as str]))

(tabular "accompanying chords"
	(fact "increase in pitch along with method length"
		(notes/pick-chord-for-method-length ?length) => (contains ?expected-chord)
		?length ?expected-chord
		1 			"C"
		5 			"C"
		6 			"D"
		10 			"D"
		11 			"E"
		15 			"E"
		16 			"F"
		20 			"F"
		21 			"G"
		29 			"G"
		30 			"A"
		39 			"A"
		40 			"B"
		400 		"B"
		4000 		"B"))
