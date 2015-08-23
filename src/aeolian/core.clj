(ns aeolian.core
	(:require [aeolian.parser :as parser]))

(def c-major-notes ["C" "E" "G" "B" "z" "c" "e" "g" "b" "z" "c'" "e'" "g'" "b'" "z"])
(def c-major-chords ["\"C\"" "\"F\"" "\"G\"" "\"Am\""])
(def c-major-root "\"Am\"")
(def tempo-root "Q:1/4=")
(def default-tempo 120)
(def drum-beat "%%MIDI program 0\n%%MIDI drum zd 60\n%%MIDI drumon\n%%MIDI gchord c")
(def header (str "X:1\nT:Hello World\nM:4/4\nL:1/4\nK:C\n" drum-beat "\n" tempo-root default-tempo "\n"))


(defn- metric-to-note-index [metric]
	(let [note-index (mod (parser/line-width-from-metric metric) (count c-major-notes))
		; _ (prn note-index)
		]
		note-index))

(defn- complexity-to-tempo [complexity]
	(str tempo-root (+ (* 20 complexity) default-tempo)))

(defn- metric-to-note [metric]
	(let [complexity (parser/complexity-from-metric metric)
				note (nth c-major-notes (metric-to-note-index metric))]
		(cond (> complexity 1)
			(str "\n" (complexity-to-tempo complexity) "\n" note)
			; note
			:else note)))

(defn- generate-notation [notes-per-measure line-metrics notation-file-name]
	; (prn line-metrics)
	(let [mapped-notes (map #(metric-to-note %1) line-metrics)
		notes-in-measures (apply str (flatten (interpose (str "|\n" c-major-root) (partition notes-per-measure mapped-notes))))
		completed-score (str header "|" c-major-root notes-in-measures "|")
		; _ (prn completed-score)
		]
		(spit notation-file-name, completed-score)
		notation-file-name
		)
	)

(defn -main [& args]
	(println (str "Generating ABC Notation from " (first args) "..."))
	(with-open [rdr (clojure.java.io/reader (first args))]
     (let [notation-file-name (generate-notation 4 (line-seq rdr) (str (first args) ".abc"))]
     	(println (str "Generated " notation-file-name))))
	)