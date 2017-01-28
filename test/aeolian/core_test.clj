(ns aeolian.core-test
  (:use midje.sweet)
  (:require [aeolian.core :as core]))

(fact "generating notation file name is based on original-file-name"
	(core/notation-file-name "foo") => "foo.abc")

(facts "tempo"
	(fact "is in abc notation"
		(core/complexity-to-tempo 5) => #"Q:1/4=\d")
	(fact "defaults to 120 bpm"
		(core/complexity-to-tempo 0) => "Q:1/4=120"))
