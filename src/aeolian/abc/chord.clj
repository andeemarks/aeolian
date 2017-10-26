(ns aeolian.abc.chord
  (:require [aeolian.abc.key :as k]))

(def ^:const major-chords ["C" "Dm" "Em" "F" "G" "Am" "Bdim"])
(def ^:const minor-chords ["Cm" "Ddim" "_E" "Fm" "Gm" "_A+" "_B+"])

(defn- chord [chord-index composition-key]
  (if (= k/major composition-key)
    (str "\"" (nth major-chords chord-index) "\"")
    (str "\"" (nth minor-chords chord-index) "\"")))

(defn chord-for-method-length [method-length composition-key current-method-length]
  (cond
    (nil? method-length) (chord-for-method-length (or current-method-length 0) composition-key nil)
    (<= 1 method-length 5) (chord 0 composition-key)
    (<= 6 method-length 10) (chord 1 composition-key)
    (<= 11 method-length 15) (chord 2 composition-key)
    (<= 16 method-length 20) (chord 3 composition-key)
    (<= 21 method-length 29) (chord 4 composition-key)
    (<= 30 method-length 39) (chord 5 composition-key)
    (<= 40 method-length) (chord 6 composition-key)
    :else (chord 0 composition-key)))
