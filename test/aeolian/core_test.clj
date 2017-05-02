(ns aeolian.core-test
  (:use midje.sweet)
  (:require [aeolian.core :as core]
  			[clojure.string :as str]))

(facts "duplication metrics dictate the key"
	(fact ">= 10% duplicate code will produce a minor key"
		(str/includes? (core/build-header "foo.metrics" {:duplicate-lines 10 :total-lines 100}) "K:Cmin") => truthy
		(str/includes? (core/build-header "foo.metrics" {:duplicate-lines 50 :total-lines 500}) "K:Cmin") => truthy
		(str/includes? (core/build-header "foo.metrics" {:duplicate-lines 21 :total-lines 200}) "K:Cmin") => truthy )
	(fact "< 10% duplicate code will produce a major key"
		(str/includes? (core/build-header "foo.metrics" {:duplicate-lines 9 :total-lines 100}) "K:Cmin") => falsey
		(str/includes? (core/build-header "foo.metrics" {:duplicate-lines 49 :total-lines 500}) "K:Cmin") => falsey
		(str/includes? (core/build-header "foo.metrics" {:duplicate-lines 19 :total-lines 200}) "K:Cmin") => falsey ))

(fact "generating notation file name is based on original-file-name"
	(core/notation-file-name "foo") => "foo.abc")
