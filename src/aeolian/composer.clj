(ns aeolian.composer
	(:require [aeolian.parser :as parser]
						[aeolian.tempo :as t]
						[aeolian.midi.drums :as d]
						[aeolian.abc.notes :as n]
						[taoensso.timbre :as log]
						[aeolian.abc.header :as h]))

(log/set-level! :info)

(def notes-per-measure 8)

(defn- build-note [line-length]
	(n/pick-note-for-line-length line-length))

(defn adjust-for-complexity [metric]
	(let [complexity (parser/complexity-from-metric metric)]
		(if (> complexity 1) 
			(str "\n" (t/tempo-for complexity)))))
	
(defn adjust-for-indentation [metric]
	nil)

(defn metric-to-note [metric]
		(let [
					raw-note (str (build-note (parser/line-length-from-metric metric)) " ")
					final-note-bits (cons (adjust-for-indentation metric)
														(cons (adjust-for-complexity metric) (list raw-note)))
					final-note (apply str (interpose " " (filter #(not (nil? %)) final-note-bits)))
					]
			final-note))

(defn- metrics-to-measure [metric-idx metrics-in-measure total-metrics]
	(log/debug (str "Processing measure " (+ 1 metric-idx) " of " total-metrics))
	(let [measure (map #(metric-to-note %1) metrics-in-measure)
				method-length (parser/find-longest-method-length-in metrics-in-measure)
				accompanying-chord (n/pick-chord-for-method-length method-length)]
		(str "| \"" accompanying-chord "\"" (apply str measure) " |\n")))

(defn- map-metrics [metrics]
	(let [
			metrics-in-measures (partition notes-per-measure metrics)
			mapped-notes (map-indexed #(metrics-to-measure %1 %2 (count metrics-in-measures)) metrics-in-measures)
			notes-in-measures (apply str mapped-notes)
		]
		notes-in-measures))

(defn compose [metrics]
	(if (<= (count metrics) 0)
		nil
			(map-metrics metrics)))
