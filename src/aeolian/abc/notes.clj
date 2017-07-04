(ns aeolian.abc.notes
  (:require [aeolian.abc.key :as k]))

(def rest-note "z")
(def major-raw-notes ["C" "E" "G" "B"])
(def major-octave-1 (map #(str %1 ",") major-raw-notes))
(def major-octave-2 major-raw-notes)
(def major-octave-3 (map clojure.string/lower-case major-raw-notes))
(def major-octave-4 (map #(str %1 "'") major-octave-3))
(def major-octave-5 (map #(str %1 "'") major-octave-4))
(def major-chords ["C" "Dmin" "Emin" "F" "G" "A+" "B+"])

(def minor-raw-notes ["A" "C" "E" "G"])
(def minor-octave-1 (map #(str %1 ",") minor-raw-notes))
(def minor-octave-2 minor-raw-notes)
(def minor-octave-3 (map clojure.string/lower-case minor-raw-notes))
(def minor-octave-4 (map #(str %1 "'") minor-octave-3))
(def minor-octave-5 (map #(str %1 "'") minor-octave-4))
(def minor-chords ["Cmin" "Ddim" "Eb" "Fmin" "Gmin" "Ab+" "Bb+"])

(defn- chord [chord-index composition-key]
  (if (= k/major composition-key)
    (str "\"" (nth major-chords chord-index) "\"")
    (str "\"" (nth minor-chords chord-index) "\"")))

(defn pick-chord-for-method-length [method-length composition-key]
  (cond
    (nil? method-length) (chord 0 composition-key)
    (<= 1 method-length 5) (chord 0 composition-key)
    (<= 6 method-length 10) (chord 1 composition-key)
    (<= 11 method-length 15) (chord 2 composition-key)
    (<= 16 method-length 20) (chord 3 composition-key)
    (<= 21 method-length 29) (chord 4 composition-key)
    (<= 30 method-length 39) (chord 5 composition-key)
    (<= 40 method-length) (chord 6 composition-key)
    :else (chord 0 composition-key)))

(defn- pick-note-from-octave [octave line-length]
  (nth octave (mod line-length (count octave))))

(defn pick-note-for-line-length [line-length composition-key]
  (if (= k/major composition-key)
    (cond
      (< line-length 10) (pick-note-from-octave major-octave-1 line-length)
      (<= 10 line-length 39) (pick-note-from-octave major-octave-2 line-length)
      (<= 40 line-length 79) (pick-note-from-octave major-octave-3 line-length)
      (<= 80 line-length 99) (pick-note-from-octave major-octave-4 line-length)
      (<= 100 line-length) (pick-note-from-octave major-octave-5 line-length))
    (cond
      (< line-length 10) (pick-note-from-octave minor-octave-1 line-length)
      (<= 10 line-length 39) (pick-note-from-octave minor-octave-2 line-length)
      (<= 40 line-length 79) (pick-note-from-octave minor-octave-3 line-length)
      (<= 80 line-length 99) (pick-note-from-octave minor-octave-4 line-length)
      (<= 100 line-length) (pick-note-from-octave minor-octave-5 line-length))))