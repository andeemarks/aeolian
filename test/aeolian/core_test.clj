(ns aeolian.core-test
  (:use midje.sweet)
  (:require [aeolian.core :as core]
  			[clojure.string :as str]))

(facts "duplication metrics dictate the key"
	(fact "no code to check will produce a major key"
    (core/build-header "foo.metrics" {:duplicate-lines 0 :total-lines 0}) => (contains "K:C"))

	(fact ">= 10% duplicate code will produce a minor key"
		(core/build-header "foo.metrics" {:duplicate-lines 10 :total-lines 100}) => (contains "K:Amin")
		(core/build-header "foo.metrics" {:duplicate-lines 50 :total-lines 500}) => (contains "K:Amin")
		(core/build-header "foo.metrics" {:duplicate-lines 21 :total-lines 200}) => (contains "K:Amin"))

	(fact "< 10% duplicate code will produce a major key"
		(core/build-header "foo.metrics" {:duplicate-lines 9 :total-lines 100}) => (contains "K:C")
		(core/build-header "foo.metrics" {:duplicate-lines 49 :total-lines 500}) => (contains "K:C")
		(core/build-header "foo.metrics" {:duplicate-lines 19 :total-lines 200}) => (contains "K:C")))

(fact "generating notation file name is based on original-file-name"
	(core/notation-file-name "foo") => "foo.abc")
