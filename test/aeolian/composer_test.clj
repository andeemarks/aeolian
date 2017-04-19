(ns aeolian.composer-test
  (:use midje.sweet)
  (:require [aeolian.composer :as composer]))

(facts "when opening metrics files"
  (fact "the header line is ignored"
    (composer/compose ["[source-file#line-number] [line-length] [cyclomatic-complexity]"]) => nil )

  (fact "subsequent lines are used in composition"
    (composer/compose ["[source-file#line-number] [line-length] [cyclomatic-complexity]"
                        "/home/amarks/Code/aeolian/resources/Notification.java#1 3"
                        "/home/amarks/Code/aeolian/resources/Notification.java#10 70"
                        "/home/amarks/Code/aeolian/resources/Notification.java#100 99"
                        ]) => truthy ))