(ns aeolian.abc.header
	(:require [aeolian.tempo :as t]))

(def reference "\nX:1")
(def title-prefix "\nT:")
(def composer "\nC:AEOLIAN")
(def meter "\nM:4/4")
(def xxx-key-signature "\nK:C")
(def note-length "\nL:1/8")

(defn build-header [title]
	(str reference title-prefix title composer meter note-length "\n" t/starting-tempo xxx-key-signature))
