(ns aeolian.abc.tempo-test
  (:use midje.sweet)
  (:require [aeolian.abc.tempo :as tempo]))

(facts "tempo"
       (fact "is in abc notation"
             (tempo/tempo-for 5) => #"Q:1/4=\d")
       (fact "increases with complexity"
             (> (tempo/generate 2) (tempo/generate 1)) => truthy
             (> (tempo/generate 3) (tempo/generate 2)) => truthy)

       (fact "defaults to 140 bpm"
             (tempo/generate 0) => 120))
