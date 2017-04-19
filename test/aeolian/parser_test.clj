(ns aeolian.parser-test
  (:use midje.sweet)
  (:require [aeolian.parser :as parser]))

(facts "When parsing a metric line"
  (facts "the line length"
    (fact "is the second value"
      (parser/line-length-from-metric "/home/amarks/Code/aeolian/resources/Notification.java#47 66 1") => 66)

    (fact "must be numeric"
      (parser/line-length-from-metric "/home/amarks/Code/aeolian/resources/Notification.java#47 6h 1") => (throws Exception)))

  (facts "the complexity"
    (fact "is the third value"
      (parser/complexity-from-metric "/home/amarks/Code/aeolian/resources/Notification.java#47 66 1") => 1)

    (fact "must be numeric"
      (parser/complexity-from-metric "/home/amarks/Code/aeolian/resources/Notification.java#47 66 u") => (throws Exception))

    (fact "defaults to 0"
      (parser/complexity-from-metric "/home/amarks/Code/aeolian/resources/Notification.java#47 66") => 0))

  (fact "the line number is the first value"
    (parser/line-length-from-metric "a 2 3") => (throws Exception)))
