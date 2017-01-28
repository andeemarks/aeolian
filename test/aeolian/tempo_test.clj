(ns aeolian.tempo-test
  (:use midje.sweet)
  (:require [aeolian.tempo :as tempo]))

(facts "tempo"
	(fact "is in abc notation"
		(tempo/generate 5) => #"Q:1/4=\d")
	(fact "defaults to 120 bpm"
		(tempo/generate 0) => "Q:1/4=120"))
