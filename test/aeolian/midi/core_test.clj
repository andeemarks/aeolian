(ns aeolian.midi.core-test
	 (:use midje.sweet)
  	(:require [aeolian.midi.core :as midi]))

(facts "When picking instruments"
  (fact "their ids vary based on filename"
  	(some #(= (midi/instrument-for "Foo.java") %) midi/instruments) => truthy
  	(some #(= (midi/instrument-for "Bar.java") %) midi/instruments) => truthy
  )

  (fact "their ids are deterministic based on filename"
  	(= (midi/instrument-for "Foo.java") (midi/instrument-for "Foo.java")) => truthy
  )

  (fact "their ids are different based on filename"
  	(= (midi/instrument-for "Foo.java") (midi/instrument-for "Bar.java")) => falsey
  )
)
