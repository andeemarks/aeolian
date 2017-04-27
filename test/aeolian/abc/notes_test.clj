(ns aeolian.abc.notes-test
  (:use midje.sweet)
  (:require [aeolian.abc.notes :as notes]
  			[clojure.string :as str]))

(facts "accompanying chords"
	(fact "increase in pitch along with method length"
		(str/index-of (notes/pick-chord-for-method-length 1) "C" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 5) "C" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 6) "D" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 10) "D" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 11) "E" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 15) "E" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 16) "F" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 20) "F" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 21) "G" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 29) "G" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 30) "A" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 39) "A" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 40) "B" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 400) "B" ) => truthy
		(str/index-of (notes/pick-chord-for-method-length 4000) "B" ) => truthy
		))
