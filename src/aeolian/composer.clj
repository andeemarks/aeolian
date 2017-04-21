(ns aeolian.composer
	(:require [aeolian.parser :as parser]
						[aeolian.tempo :as t]
						[aeolian.midi.drums :as d]
						[aeolian.abc.notes :as n]
						[aeolian.abc.header :as h]))

(def notes-per-measure 4)

(defn- metric-to-octave-note [line-length]
	(let [octave (n/pick-octave-for-line-length line-length)]
		(nth octave (mod line-length (count octave)))))

(defn build-note [line-length]
	(if (< line-length 10)
		n/rest-note
		(metric-to-octave-note line-length)))

(defn adjust-for-complexity [metric]
	(let [complexity (parser/complexity-from-metric metric)]
		(if (> complexity 1) 
			(t/as-abc complexity))))
	
(defn adjust-for-method-length [metric]
	(let [method-length (parser/method-length-from-metric metric)]
		(if (> method-length 1) 
			(d/volume-for method-length))))

(defn metric-to-note [metric]
	; (println metric)
	(let [
				raw-note (build-note (parser/line-length-from-metric metric))
				final-note-bits (cons (adjust-for-method-length metric) 
													(cons (adjust-for-complexity metric) (list raw-note)))
				final-note (apply str (interpose "\n" (filter #(not (nil? %)) final-note-bits)))
				; _ (println raw-note)
				]

		final-note))

(defn- map-metrics [metrics]
	(let [
			mapped-notes (map #(metric-to-note %1) metrics)
			notes-in-measures 
				(apply str 
					(flatten 
						(interpose 
							(str "|\n") 
							(partition notes-per-measure mapped-notes)))) 
		]
		(str n/major-root notes-in-measures)))

(defn compose [line-metrics]
	(let [
		header (first line-metrics)
		metrics (rest line-metrics)]
		(if (<= (count metrics) 0)
			nil
 			(map-metrics metrics))))
