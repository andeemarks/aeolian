(ns aeolian.composer-test
  (:use midje.sweet)
  (:require [aeolian.composer :as composer]))

(facts "when opening metrics files"
  (fact "the header line is ignored"
    (composer/compose 4 [] "foo.txt") => nil
    ))