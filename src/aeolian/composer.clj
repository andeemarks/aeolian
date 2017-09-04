(ns aeolian.composer
  (:require [aeolian.parser :as parser]
            [aeolian.midi.core :as midi]
            [aeolian.abc.tempo :as t]
            [aeolian.abc.notes :as n]
            [aeolian.abc.core :as abc]
            [taoensso.timbre :as log]
            [aeolian.abc.header :as h]))

(log/set-level! :info)

(def notes-per-measure 8)

(defn build-note [line-length composition-key]
  (n/pick-note-for-line-length line-length composition-key))

(defn build-tempo [complexity]
  (if (> complexity 1)
    (abc/inline (t/tempo-for complexity))))

(defn map-indentation [indentation?]
  (if (not (nil? indentation?))
    (midi/volume-boost)))

(defn build-lyrics [current-source-file source-file]
  (if (not (= current-source-file source-file))
    (abc/lyrics-for current-source-file)))

(defn build-instrument [current-author author]
  (if (not (= current-author author))
    (midi/instrument-command-for current-author)))

(defn build-measure [measure-lines-metrics composition-key source-file author method-length]
 (loop [measure {}
        remaining-line-metrics measure-lines-metrics
        current-source-file source-file
        current-method-length method-length
        current-author author]
     (if (empty? remaining-line-metrics)
       measure
       (let [
            ;  _ (log/info measure)
             metric-components (parser/parse (first remaining-line-metrics))
             final-note          (abc/note
                                  (build-note (:line-length metric-components) composition-key)
                                  (build-instrument current-author author)
                                  (build-lyrics current-source-file source-file)
                                  (build-tempo (:complexity metric-components)))]

        (recur
         {:notes (conj (:notes measure) final-note) :author current-author :source-file current-source-file :method-length current-method-length}
         (rest remaining-line-metrics)
         (:source-file metric-components)
         (:method-length metric-components)
         (:author metric-components))))))

(defn metrics-to-measure [metrics-in-measure composition-key source-file author method-length]
 (let [measure               (build-measure metrics-in-measure composition-key source-file author method-length)
       current-method-length (parser/find-longest-method-length-in metrics-in-measure)
       accompanying-chord    (n/pick-chord-for-method-length current-method-length composition-key (:method-length measure))]
   (abc/measure accompanying-chord (:notes measure))))

(defn- split-metrics-into-equal-measures [metrics]
  (partition
   notes-per-measure ; size of each partition
   notes-per-measure ; index to start next partition
   [(last metrics)]  ; value to pad out small partitions
   metrics))

(defn- map-metrics [metrics composition-key]
  (let [metrics-in-measures (split-metrics-into-equal-measures metrics)
        mapped-notes         (map #(metrics-to-measure %1 composition-key "" "" 1) metrics-in-measures)]
    (apply str mapped-notes)))

(defn compose [metrics composition-key]
  (if (> (count metrics) 0)
    (map-metrics metrics composition-key)))
