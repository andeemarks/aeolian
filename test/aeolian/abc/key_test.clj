(ns aeolian.abc.key-test
  (:use midje.sweet)
  (:require [aeolian.abc.key :as k]))

(facts "duplication metrics dictate the key"
       (fact "no code to check will produce a major key"
             (k/determine-key {:duplicate-lines 0 :total-lines 0}) => k/major)

       (fact ">= 10% duplicate code will produce a minor key"
             (k/determine-key {:duplicate-lines 10 :total-lines 100}) => k/minor
             (k/determine-key {:duplicate-lines 50 :total-lines 500}) => k/minor
             (k/determine-key {:duplicate-lines 21 :total-lines 200}) => k/minor)

       (fact "< 10% duplicate code will produce a major key"
             (k/determine-key {:duplicate-lines 9 :total-lines 100}) => k/major
             (k/determine-key {:duplicate-lines 49 :total-lines 500}) => k/major
             (k/determine-key {:duplicate-lines 19 :total-lines 200}) => k/major))
