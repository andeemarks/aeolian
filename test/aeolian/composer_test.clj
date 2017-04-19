(ns aeolian.composer-test
  (:use midje.sweet)
  (:require [aeolian.composer :as c]
            [clojure.string :as str]
            [aeolian.tempo :as t]
            [aeolian.abc.notes :as n]))

(facts "when processing metrics"
  (facts "line length is mapped to note"
    (fact "empty lines are mapped to rests"
      (c/metric-to-note "Foo.java#1 0") => n/rest-note)

    (some #(= (c/metric-to-note "Foo.java#1 3") %) n/major-notes ) => truthy
    (some #(= (c/metric-to-note "Foo.java#1 30") %) n/major-notes ) => truthy
    (some #(= (c/metric-to-note "Foo.java#1 300") %) n/major-notes ) => truthy
    (some #(= (c/metric-to-note "Foo.java#1 33") %) n/major-notes ) => truthy)

  (fact "complexity > 1 is mapped to tempo"
    (str/index-of (c/metric-to-note "Foo.java#1 30 1") t/abc-template ) => falsey
    (str/index-of (c/metric-to-note "Foo.java#1 30 10") t/abc-template ) => truthy
    (str/index-of (c/metric-to-note "Foo.java#1 30 5") t/abc-template ) => truthy
    (str/index-of (c/metric-to-note "Foo.java#1 30 3") t/abc-template ) => truthy)
  )

(facts "when opening metrics files"
  (fact "the header line is ignored"
    (c/compose ["[source-file#line-number] [line-length] [cyclomatic-complexity]"]) => nil )

  (fact "subsequent lines are used in composition"
    (c/compose ["[source-file#line-number] [line-length] [cyclomatic-complexity]"
                        "/home/amarks/Code/aeolian/resources/Notification.java#1 3"
                        "/home/amarks/Code/aeolian/resources/Notification.java#10 70"
                        "/home/amarks/Code/aeolian/resources/Notification.java#100 99"
                        ]) => truthy ))