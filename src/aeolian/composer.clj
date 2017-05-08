(ns aeolian.composer
	(:require [aeolian.parser :as parser]
						[aeolian.tempo :as t]
						[aeolian.midi.core :as midi]
						[aeolian.abc.notes :as n]
						[aeolian.abc.core :as abc]
						[taoensso.timbre :as log]
						[aeolian.abc.header :as h]))

(log/set-level! :info)

(def source-file (atom ""))
(def author (atom ""))

(defn get-source-file []
  @source-file)

(defn get-author []
  @author)

(defn update-source-file [new-source-file]
  (swap! source-file (fn [f] new-source-file)))

(defn update-author [new-author]
  (swap! author (fn [f] new-author)))

(def notes-per-measure 8)

(defn build-note [line-length]
	(n/pick-note-for-line-length line-length))

(defn adjust-for-complexity [metric]
	(let [complexity (:complexity metric)]
		(if (> complexity 1) 
			(abc/inline (t/tempo-for complexity)))))

(defn adjust-for-indentation [metric]
	(let [indentation-error (:indentation? metric)]
		(if (not (nil? indentation-error))
			(midi/volume-boost))))
	
(defn adjust-for-file-change [current-source-file]
	(if (not (= current-source-file (get-source-file)))
		(abc/lyrics-for current-source-file)
		nil))
	
(defn adjust-for-author-change [current-author]
	(if (not (= current-author (get-author)))
		(midi/instrument-command-for current-author)
		nil))

(defn metric-to-note [metric]
	(log/debug (str "Processing metric " metric))
	(let [
		metric-components (parser/parse metric)
		current-source-file (:source-file metric-components)
		current-author 		(:author metric-components)
		raw-note 			(str (build-note (:line-length metric-components)) " ")
		final-note-bits (cons (adjust-for-author-change current-author)
							(cons (adjust-for-file-change current-source-file)
								(cons (adjust-for-complexity metric-components) (list raw-note))))
		final-note (apply str (interpose " " (filter #(not (nil? %)) final-note-bits)))
		]
		(update-source-file current-source-file)
		(update-author current-author)
		final-note))

(defn- metrics-to-measure [metric-idx metrics-in-measure total-metrics]
	(log/debug (str "Processing measure " (+ 1 metric-idx) " of " total-metrics))
	(let [measure (map #(metric-to-note %1) metrics-in-measure)
			method-length (parser/find-longest-method-length-in metrics-in-measure)
			accompanying-chord (n/pick-chord-for-method-length method-length)]
		(abc/measure (str accompanying-chord (apply str measure)))))

(defn- split-metrics-into-equal-measures [metrics]
	(partition 
		notes-per-measure ; size of each partition
		notes-per-measure ; index to start next partition
		[(last metrics)]  ; value to pad out small partitions
		metrics))

(defn- map-metrics [metrics]
	(let [
			metrics-in-measures (split-metrics-into-equal-measures metrics)
			mapped-notes (map-indexed #(metrics-to-measure %1 %2 (count metrics-in-measures)) metrics-in-measures)
			notes-in-measures (apply str mapped-notes)
		]
		notes-in-measures))

(defn compose [metrics]
	(if (<= (count metrics) 0)
		nil
			(map-metrics metrics)))
