(ns aeolian.core-test
  (:use midje.sweet)
  (:require [aeolian.core :as core]))

(fact "generating notation file name is based on original-file-name"
	(core/notation-file-name "foo") => "foo.abc")
