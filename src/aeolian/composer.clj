(ns aeolian.composer
	(:require [aeolian.parser :as parser]
						[aeolian.tempo :as tempo]
						[clojure.java.io :as io]))

(def c-major-notes ["C" "E" "G" "B" "z" "c" "e" "g" "b" "z" "c'" "e'" "g'" "b'" "z"])
(def c-major-chords ["\"C\"" "\"F\"" "\"G\"" "\"Am\""])
(def c-major-root "\"Am\"")
(def drum-beat "%%MIDI program 0\n%%MIDI drum zd 60\n%%MIDI drumon\n%%MIDI gchord c")
(def header (str "X:1\nT:Hello World\nM:4/4\nL:1/4\nK:C\n" drum-beat "\n" tempo/abc-template tempo/default-tempo "\n"))

(defn- metric-to-note-index [metric]
	(mod (parser/line-length-from-metric metric) (count c-major-notes)))

(defn- metric-to-note [metric]
	(let [complexity (parser/complexity-from-metric metric)
				note (nth c-major-notes (metric-to-note-index metric))]
		(cond (> complexity 1)
			(str "\n" (tempo/as-abc complexity) "\n" note)
			; note
			:else note)))

(defn compose [notes-per-measure line-metrics notation-file-name]
	(let [mapped-notes (map #(metric-to-note %1) (rest line-metrics))
		notes-in-measures (apply str (flatten (interpose (str "|\n" c-major-root) (partition notes-per-measure mapped-notes))))
		completed-score (str header "|" c-major-root notes-in-measures "|") ]
		(spit notation-file-name, completed-score) ) )
