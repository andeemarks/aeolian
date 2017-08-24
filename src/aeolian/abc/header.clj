(ns aeolian.abc.header
  (:require [aeolian.tempo :as t]))

(def reference "\nX:1")
(def title-prefix "\nT:")
(def composer "\nC:AEOLIAN")
(def meter "\nM:4/4")
(def xxx-major-key "\nK:C")
(def xxx-minor-key "\nK:Amin")
(def note-length "\nL:1/8")
(def misc "\n%%MIDI chordvol 50")
(def drums "\n%%MIDI channel 1\n%%MIDI drum dd2d2ddd2d2d 59 51 51 59 59 51 51 59 100 50 50 100 100 50 50 100\n%%MIDI drumon")

(defn build-common-header [title key]
  (str reference title-prefix title composer meter note-length "\n" t/starting-tempo key misc drums))

(defn build-major-header [title]
  (build-common-header title xxx-major-key))

(defn build-minor-header [title]
  (build-common-header title xxx-minor-key))
