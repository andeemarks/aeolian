(ns aeolian.parser-test
  (:use midje.sweet)
  (:require [aeolian.parser :as parser]))

(facts "When parsing a metric line"
  (facts "the line length"
    (fact "is the second value"
      (parser/line-length-from-metric "/Notification.java#190 LL=13 CC=9 ML=16") => 13)

    (fact "must be numeric"
      (parser/line-length-from-metric "/Notification.java#190 LL=abc CC=9 ML=16") => (throws Exception)))

  (facts "the complexity"
    (fact "is the third value"
      (parser/complexity-from-metric "/Notification.java#190 LL=13 CC=9 ML=16") => 9)

    (fact "must be numeric"
      (parser/complexity-from-metric "/Notification.java#190 LL=13 CC=abc ML=16") => (throws Exception))

    (fact "defaults to 0"
      (parser/complexity-from-metric "/Notification.java#190 LL=13") => 0))

  (facts "the method length"
    (fact "is the fourth value"
      (parser/method-length-from-metric "/Notification.java#190 LL=13 CC=9 ML=16") => 16)

    (fact "must be numeric"
      (parser/method-length-from-metric "/Notification.java#190 LL=13 CC=abc ML=abc") => (throws Exception))

    (fact "defaults to 0"
      (parser/method-length-from-metric "/Notification.java#190 LL=13") => 0)
      (parser/method-length-from-metric "/Notification.java#190 LL=13 CC=9") => 0))
