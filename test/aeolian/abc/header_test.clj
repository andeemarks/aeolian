(ns aeolian.abc.header-test
  (:use midje.sweet)
  (:require [aeolian.abc.header :refer :all]
            [aeolian.abc.key :as k]))

(facts "header"
       (fact "can be specified in minor key"
             (build-minor-header "hi") => (build-common-header "hi" k/minor))
       (fact "can be specified in major key"
             (build-major-header "hi") => (build-common-header "hi" k/major)))
