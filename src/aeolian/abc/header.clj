(ns aeolian.abc.header
	(:require [aeolian.tempo :as t]))

(def reference "\nX:1")
(def title-prefix "\nT:")
(def composer "\nC:AEOLIAN")
(def meter "\nM:4/4")
(def xxx-major-key "\nK:C")
(def xxx-minor-key "\nK:Cmin")
(def note-length "\nL:1/8")

(defn build-common-header [title key]
	(str reference title-prefix title composer meter note-length "\n" t/starting-tempo key))

(defn build-major-header [title]
	(build-common-header title xxx-major-key))

(defn build-minor-header [title]
	(build-common-header title xxx-minor-key))
