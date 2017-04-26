(ns aeolian.abc.notes-test
  (:use midje.sweet)
  (:require [aeolian.abc.notes :as notes]))

(facts "accompanying chords"
	(fact "increase in pitch along with method length"
		(notes/pick-chord-for-method-length 1) => "C"
		(notes/pick-chord-for-method-length 5) => "C"
		(notes/pick-chord-for-method-length 6) => "D"
		(notes/pick-chord-for-method-length 10) => "D"
		(notes/pick-chord-for-method-length 11) => "E"
		(notes/pick-chord-for-method-length 15) => "E"
		(notes/pick-chord-for-method-length 16) => "F"
		(notes/pick-chord-for-method-length 20) => "F"
		(notes/pick-chord-for-method-length 21) => "G"
		(notes/pick-chord-for-method-length 29) => "G"
		(notes/pick-chord-for-method-length 30) => "A"
		(notes/pick-chord-for-method-length 39) => "A"
		(notes/pick-chord-for-method-length 40) => "B"
		(notes/pick-chord-for-method-length 400) => "B"
		(notes/pick-chord-for-method-length 4000) => "B"
		))
