(ns aeolian.composer-test
  (:use midje.sweet)
  (:require [aeolian.composer :as c]
            [clojure.string :as str]
            [aeolian.tempo :as t]
            [aeolian.abc.notes :as notes]))

(facts "when processing metrics"
  (fact "line length is mapped to note"
    (some #(= (c/metric-to-note "Foo.java#1 3") %) notes/major-notes ) => truthy
    (some #(= (c/metric-to-note "Foo.java#1 30") %) notes/major-notes ) => truthy
    (some #(= (c/metric-to-note "Foo.java#1 300") %) notes/major-notes ) => truthy
    (some #(= (c/metric-to-note "Foo.java#1 33") %) notes/major-notes ) => truthy)

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