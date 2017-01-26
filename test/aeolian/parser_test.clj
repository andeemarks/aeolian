(ns aeolian.parser-test
  (:use midje.sweet)
  (:require [aeolian.parser :as parser]))

(facts "When parsing a metric line"
  (facts "the line length"
    (fact "is the second value"
      (parser/line-width-from-metric "1 2 3") => 2)

    (fact "must be numeric"
      (parser/line-width-from-metric "1 a 3") => (throws Exception)))

  (facts "the complexity"
    (fact "is the third value"
      (parser/complexity-from-metric "1 2 3") => 3)

    (fact "must be numeric"
      (parser/complexity-from-metric "1 2 a") => (throws Exception))

    (fact "defaults to 0"
      (parser/complexity-from-metric "1 2") => 0))

  (fact "the line number is the first value"
    (parser/line-width-from-metric "a 2 3") => (throws Exception)))
