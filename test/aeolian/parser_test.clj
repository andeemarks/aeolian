(ns aeolian.parser-test
  (:use midje.sweet)
  (:require [aeolian.parser :as parser]))

(facts "When parsing a metric line"
  (facts "the line length"
    (fact "can be found"
      (parser/line-length-from-metric "/Notification.java#190 LL=13") => 13)

    (fact "must be numeric"
      (parser/line-length-from-metric "/Notification.java#190 LL=abc") => (throws Exception)))

  (facts "the complexity"
    (fact "can be found"
      (parser/complexity-from-metric "/Notification.java#190 CC=9") => 9)

    (fact "must be numeric"
      (parser/complexity-from-metric "/Notification.java#190 CC=abc") => (throws Exception))

    (fact "defaults to 0"
      (parser/complexity-from-metric "/Notification.java#190 LL=13") => 0))

  (facts "the method length"
    (fact "can be found"
      (parser/method-length-from-metric "/Notification.java#190 ML=16") => 16)

    (fact "must be numeric"
      (parser/method-length-from-metric "/Notification.java#190 ML=abc") => (throws Exception))

    (fact "defaults to 0"
      (parser/method-length-from-metric "/Notification.java#190 LL=13") => 0)
      (parser/method-length-from-metric "/Notification.java#190 LL=13 CC=9") => 0))
