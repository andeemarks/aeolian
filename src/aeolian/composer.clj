(ns aeolian.composer
	(:require [aeolian.parser :as parser]
						[aeolian.tempo :as t]
						[aeolian.midi.drums :as d]
						[aeolian.abc.notes :as n]
						[aeolian.abc.header :as h]))

(def output-header (str h/header d/drum-track t/abc-template t/default-tempo "\n"))
(def notes-per-measure 4)

(defn- metric-to-note-index [line-length]
	(mod line-length (count n/major-notes)))

(defn build-note [metric]
	(let [line-length (parser/line-length-from-metric metric)]
		(if (< line-length 10)
			n/rest-note
			(nth n/major-notes (metric-to-note-index line-length))
			))
		)

(defn metric-to-note [metric]
	; (println metric)
	(let [complexity (parser/complexity-from-metric metric)
				note (build-note metric)
				; _ (println note)
				]
		(cond (> complexity 1)
			(str "\n" (t/as-abc complexity) "\n" note)
			; note
			:else note)))

(defn- map-metrics [metrics]
	(let [
			mapped-notes (map #(metric-to-note %1) metrics)
			notes-in-measures (apply str (flatten (interpose (str "|\n" n/major-root) (partition notes-per-measure mapped-notes)))) 
		]
		(str output-header "|" n/major-root notes-in-measures "|")))

(defn compose [line-metrics]
	(let [
		header (first line-metrics)
		metrics (rest line-metrics)]
		(if (<= (count metrics) 0)
			nil
 			(map-metrics metrics))))
