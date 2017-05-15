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

(defn build-note [line-length composition-key]
  (n/pick-note-for-line-length line-length composition-key))

(defn build-tempo [complexity]
  (if (> complexity 1)
    (abc/inline (t/tempo-for complexity))))

(defn map-indentation [indentation?]
  (if (not (nil? indentation?))
    (midi/volume-boost)))

(defn build-lyrics [current-source-file]
  (if (not (= current-source-file (get-source-file)))
    (abc/lyrics-for current-source-file)))

(defn build-instrument [current-author]
  (if (not (= current-author (get-author)))
    (midi/instrument-command-for current-author)))

(defn metric-to-note [metric composition-key]
  (log/debug (str "Processing metric " metric))
  (let [metric-components 	(parser/parse metric)
        current-source-file (:source-file metric-components)
        current-author 			(:author metric-components)
        note-components 		(conj
                           '()
                           (build-note (:line-length metric-components) composition-key)
                           (build-instrument current-author)
                           (build-lyrics current-source-file)
                           (build-tempo (:complexity metric-components)))
        final-note 						(apply str (interpose " " (filter #(not (nil? %)) note-components)))]
    (update-source-file current-source-file)
    (update-author current-author)
    final-note))

(defn metrics-to-measure [metrics-in-measure composition-key]
  (let [measure 						(map #(metric-to-note %1 composition-key) metrics-in-measure)
        method-length 			(parser/find-longest-method-length-in metrics-in-measure)
        accompanying-chord 	(n/pick-chord-for-method-length method-length composition-key)]
    (abc/measure (str accompanying-chord (apply str measure)))))

(defn- split-metrics-into-equal-measures [metrics]
  (partition
   notes-per-measure ; size of each partition
   notes-per-measure ; index to start next partition
   [(last metrics)]  ; value to pad out small partitions
   metrics))

(defn- map-metrics [metrics composition-key]
  (let [metrics-in-measures (split-metrics-into-equal-measures metrics)
        mapped-notes 				(map #(metrics-to-measure %1 composition-key) metrics-in-measures)]
    (apply str mapped-notes)))

(defn compose [metrics composition-key]
  (if (> (count metrics) 0)
    (map-metrics metrics composition-key)))
