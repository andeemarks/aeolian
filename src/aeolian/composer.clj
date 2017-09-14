(ns aeolian.composer
  (:require [aeolian.midi.core :as midi]
            [aeolian.parser :as parser]
            [aeolian.abc.tempo :as t]
            [aeolian.abc.notes :as n]
            [aeolian.abc.core :as abc]
            [taoensso.timbre :as log]
            [schema.core :as s]
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

(defn build-lyrics [current-source-file]
  (abc/lyrics-for current-source-file))

(defn build-instrument [current-author]
  (midi/instrument-command-for current-author))

(s/defschema ^:const Measure
  {:notes [s/Str]
   :method-length (s/maybe s/Num)})

(s/defn build-measure :- Measure
 [measure-lines-metrics composition-key method-length]
 (loop [measure {}
        remaining-line-metrics measure-lines-metrics
        current-method-length method-length]
     (if (not remaining-line-metrics)
       measure
       (let [
            ;  _ (log/info measure)
             metric-components (first remaining-line-metrics)
             final-note          (abc/note
                                  (build-note (:line-length metric-components) composition-key)
                                  (build-instrument (:author metric-components))
                                  (build-lyrics (:source-file metric-components))
                                  (build-tempo (:complexity metric-components)))]

        (recur
         {:notes (conj (:notes measure) final-note) :method-length current-method-length}
         (next remaining-line-metrics)
         (:method-length metric-components))))))

(defn metrics-to-measure [metrics-in-measure composition-key method-length]
 (let [measure               (build-measure metrics-in-measure composition-key method-length)
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
        mapped-notes         (map #(metrics-to-measure %1 composition-key 1) metrics-in-measures)]
    (apply str mapped-notes)))

(defn compose [metrics composition-key]
  (if (> (count metrics) 0)
    (map-metrics metrics composition-key)))
