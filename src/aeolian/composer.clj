(ns aeolian.composer
	(:require [aeolian.parser :as parser]
						[aeolian.tempo :as t]
						[aeolian.midi.drums :as d]
						[aeolian.abc.notes :as n]
						[taoensso.timbre :as log]
						[aeolian.abc.header :as h]))

(def notes-per-measure 8)

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
			(str "\n" (t/tempo-for complexity)))))
	
(defn adjust-for-method-length [metric]
	nil)

(defn adjust-for-indentation [metric]
	nil)
	; (let [method-length (parser/method-length-from-metric metric)]
	; 	(if (> method-length 1) 
	; 		(str "\n%%MIDI control 7 " (+ (* 2 method-length) 50)))))

(defn metric-to-note 
	([metric]
		(let [
					raw-note (str (build-note (parser/line-length-from-metric metric)) " ")
					final-note-bits (cons (adjust-for-indentation metric)
														(cons (adjust-for-method-length metric) 
															(cons (adjust-for-complexity metric) (list raw-note))))
					final-note (apply str (interpose " " (filter #(not (nil? %)) final-note-bits)))
					]

					; (println (str metric " becomes " final-note))
			final-note))
	([metric-idx metric total-metrics]
		(log/info (str "Processing metric " (+ 1 metric-idx) " of " total-metrics))
		(metric-to-note metric)))

(defn- map-metrics [metrics]
	(let [
			mapped-notes (map-indexed #(metric-to-note %1 %2 (count metrics)) metrics)
			notes-in-measures 
				(apply str 
					(flatten 
						(interpose 
							(str "|\n|" n/major-root) 
							(partition notes-per-measure mapped-notes)))) 
		]
		(str "|" n/major-root notes-in-measures " |")))

(defn compose [metrics]
	(if (<= (count metrics) 0)
		nil
			(map-metrics metrics)))
