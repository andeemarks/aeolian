(ns aeolian.core
	(:require [aeolian.parser :as parser]
						[aeolian.tempo :as tempo]
						[clojure.java.io :as io]))

(def c-major-notes ["C" "E" "G" "B" "z" "c" "e" "g" "b" "z" "c'" "e'" "g'" "b'" "z"])
(def c-major-chords ["\"C\"" "\"F\"" "\"G\"" "\"Am\""])
(def c-major-root "\"Am\"")
(def drum-beat "%%MIDI program 0\n%%MIDI drum zd 60\n%%MIDI drumon\n%%MIDI gchord c")
(def header (str "X:1\nT:Hello World\nM:4/4\nL:1/4\nK:C\n" drum-beat "\n" tempo-root default-tempo "\n"))


(defn- metric-to-note-index [metric]
	(mod (parser/line-width-from-metric metric) (count c-major-notes)))

(defn- metric-to-note [metric]
	(let [complexity (parser/complexity-from-metric metric)
				note (nth c-major-notes (metric-to-note-index metric))]
		(cond (> complexity 1)
			(str "\n" (tempo/as-abc complexity) "\n" note)
			; note
			:else note)))

(defn- generate-notation [notes-per-measure line-metrics notation-file-name]
	(let [mapped-notes (map #(metric-to-note %1) line-metrics)
		notes-in-measures (apply str (flatten (interpose (str "|\n" c-major-root) (partition notes-per-measure mapped-notes))))
		completed-score (str header "|" c-major-root notes-in-measures "|") ]
		(spit notation-file-name, completed-score) ) )

(defn notation-file-name [original-file-name]
	(str original-file-name ".abc"))

(defn- generate-notation-from [metrics-file-name]
	(println (str "Generating ABC Notation from " metrics-file-name "..."))
	(with-open [rdr (clojure.java.io/reader metrics-file-name)]
     (let [notation-file-name (notation-file-name metrics-file-name)
     		_ (generate-notation 4 (line-seq rdr) notation-file-name)]
     		(println (str "Generated " notation-file-name))))
	)

(defn -main [& args]
	(if-let [metrics-file-name (first args)]
		(if (.exists (io/as-file metrics-file-name))
			(generate-notation-from metrics-file-name)
			(println (str "Error: cannot find metrics file - " metrics-file-name)))
		(println "Error: no metrics file supplied")))
