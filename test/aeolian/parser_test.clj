(ns aeolian.parser-test
  (:use midje.sweet)
  (:require [aeolian.parser :as parser]))

(facts "When parsing a metric line"
  (fact "the line length is the second value"
    (parser/line-width-from-metric "1 2 3") => 2)

  (fact "the complexity is the third value"
    (parser/complexity-from-metric "1 2 3") => 3)

  ; (fact "the line length must be numeric"
  ;   (parser/complexity-from-metric "1 a 3") => (throws Exception))

  (fact "complexity values default if not supplied"
    (parser/complexity-from-metric "1 2") => 0)
  	)
