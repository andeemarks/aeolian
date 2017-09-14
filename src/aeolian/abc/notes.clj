(ns aeolian.abc.notes
  (:require [aeolian.abc.key :as k]))

(def ^:const rest-note "z")
(def ^:const major-raw-notes ["C" "E" "G" "B"])
(def ^:const major-octave-1 (map #(str %1 ",") major-raw-notes))
(def ^:const major-octave-2 major-raw-notes)
(def ^:const major-octave-3 (map clojure.string/lower-case major-raw-notes))
(def ^:const major-octave-4 (map #(str %1 "'") major-octave-3))
(def ^:const major-octave-5 (map #(str %1 "'") major-octave-4))
(def ^:const major-chords ["C" "Dm" "Em" "F" "G" "A+" "B+"])

(def ^:const minor-raw-notes ["C" "^D" "G" "^A"])
(def ^:const minor-octave-1 (map #(str %1 ",") minor-raw-notes))
(def ^:const minor-octave-2 minor-raw-notes)
(def ^:const minor-octave-3 (map clojure.string/lower-case minor-raw-notes))
(def ^:const minor-octave-4 (map #(str %1 "'") minor-octave-3))
(def ^:const minor-octave-5 (map #(str %1 "'") minor-octave-4))
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

(defn- octave-idx-to-octave-name [octave-prefix octave-idx]
  (intern 'aeolian.abc.notes (symbol (str octave-prefix octave-idx))))

(defn- key-to-octave [octave-idx composition-key]
  (if (= k/major composition-key)
    (var-get (octave-idx-to-octave-name "major-octave-" octave-idx))
    (var-get (octave-idx-to-octave-name "minor-octave-" octave-idx))))

(defn- note-from-octave [octave-idx line-length composition-key]
  (let [octave (key-to-octave octave-idx composition-key)]
    (nth octave (mod line-length (count octave)))))

(defn note-for-line-length [line-length composition-key]
  (cond
    (= 0 line-length) rest-note
    (<= 1 line-length 9) (note-from-octave 1 line-length composition-key)
    (<= 10 line-length 39) (note-from-octave 2 line-length composition-key)
    (<= 40 line-length 79) (note-from-octave 3 line-length composition-key)
    (<= 80 line-length 99) (note-from-octave 4 line-length composition-key)
    (<= 100 line-length) (note-from-octave 5 line-length composition-key)))
