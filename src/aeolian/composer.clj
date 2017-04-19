(ns aeolian.composer
	(:require [aeolian.parser :as parser]
						[aeolian.tempo :as t]
						[aeolian.abc.header :as h]
						[aeolian.midi.drums :as d]
						[clojure.java.io :as io]))

(def c-major-notes ["C," "E," "G," "B," "z" "C" "E" "G" "B" "z" "c" "e" "g" "b" "z" "c'" "e'" "g'" "b'" "z"])
(def c-major-chords ["\"C\"" "\"F\"" "\"G\"" "\"Am\""])
(def c-major-root "\"Am\"")
(def output-header (str h/header d/beat "\n" t/abc-template t/default-tempo "\n"))
(def notes-per-measure 4)

(defn- metric-to-note-index [metric]
	(mod (parser/line-length-from-metric metric) (count c-major-notes)))

(defn- metric-to-note [metric]
	; (println metric)
	(let [complexity (parser/complexity-from-metric metric)
				note (nth c-major-notes (metric-to-note-index metric))
				; _ (println complexity)
				; _ (println note)
				]
		(cond (> complexity 1)
			(str "\n" (t/as-abc complexity) "\n" note)
			; note
			:else note)))

(defn- map-metrics [metrics]
	(let [
		mapped-notes (map #(metric-to-note %1) metrics)
		; _ (println mapped-notes)
		notes-in-measures (apply str (flatten (interpose (str "|\n" c-major-root) (partition notes-per-measure mapped-notes)))) 
		; _ (println notes-in-measures)
		]
		(str output-header "|" c-major-root notes-in-measures "|")))

(defn compose [line-metrics]
	(let [
		header (first line-metrics)
		metrics (rest line-metrics)]
		(if (<= (count metrics) 0)
			nil
 			(map-metrics metrics))))
