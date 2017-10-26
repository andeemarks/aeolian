(ns aeolian.midi.instrument-test
  (:use midje.sweet)
  (:require [aeolian.midi.instrument :refer :all]))

(facts "When picking instruments"
       (fact "their ids vary based on filename"
             instruments => (contains (instrument-for "Foo.java"))
             instruments => (contains (instrument-for "Bar.java")))

       (fact "their ids are deterministic based on filename"
             (instrument-for "Foo.java") => (instrument-for "Foo.java"))

       (fact "their ids are different based on filename"
             (instrument-for "Foo.java") =not=> (instrument-for "Bar.java")))
