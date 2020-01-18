(ns aeolian.composer
  (:gen-class)
  (:require [aeolian.midi.instrument :as midi]
            [aeolian.abc.tempo :as t]
            [aeolian.abc.notepitch :as pitch]
            [aeolian.abc.chord :as chord]
            [aeolian.abc.notelength :as length]
            [aeolian.abc.core :as abc]
            [taoensso.timbre :as log]
            [schema.core :as s]
            [aeolian.abc.header :as h]))

(log/set-level! :info)

(def ^:const notes-per-measure 8)

(s/defschema ^:const Measure
  {:notes [s/Str]
   :method-length (s/maybe s/Num)})

(s/defschema ^:const ParsedMetricLine
  "A schema for a single line of parsed metrics"
  {:author (s/maybe s/Str)
   :line-length s/Num
   :line s/Num
   :source-file s/Str
   :method-length (s/maybe s/Num)
   :method-count (s/maybe s/Num)
   :file-length (s/maybe s/Num)
   :indentation-violation? s/Bool
   :complexity s/Num
   :type s/Keyword
   :timestamp (s/maybe s/Num)})

(defn find-longest-method-length-in [metrics]
  (:method-length (apply max-key #(or (:method-length %) 0) metrics)))

(defn build-note [line-length composition-key] (pitch/note-for-line-length line-length composition-key))

(defn build-tempo [complexity]
  (when (> complexity 1)
    (abc/inline (t/tempo-for complexity))))

(defn map-indentation [indentation?]
  (when (not (nil? indentation?))
    (midi/volume-boost)))

(defn build-note-length [metric-type]
  ; (println metric-type)
  (case metric-type
    :regular length/whole
    :method length/half
    :class length/quarter
    :file length/eighth))

(defn build-lyrics [current-source-file] (abc/lyrics-for current-source-file))

(defn build-instrument [current-author] (midi/instrument-command-for current-author))

(s/defn build-measure :- Measure
  [measure-lines-metrics :- [ParsedMetricLine]
   composition-key
   method-length]
  (loop [measure {}
         remaining-line-metrics measure-lines-metrics
         current-method-length method-length]
    (if (not remaining-line-metrics)
      measure
      (let [metric-components (first remaining-line-metrics)
            final-note          (abc/note
                                 (build-note (:line-length metric-components) composition-key)
                                 (build-note-length (:type metric-components))
                                 (build-instrument (:author metric-components))
                                 (build-lyrics (:source-file metric-components))
                                 (build-tempo (:complexity metric-components)))]
        (recur
         {:notes (conj (:notes measure) final-note) :method-length current-method-length}
         (next remaining-line-metrics)
         (:method-length metric-components))))))

(s/defn metrics-to-measure
  [metrics-in-measure :- [ParsedMetricLine]
   composition-key
   method-length]
  (let [measure               (build-measure metrics-in-measure composition-key method-length)
        current-method-length (find-longest-method-length-in metrics-in-measure)
        accompanying-chord    (chord/chord-for-method-length current-method-length composition-key (:method-length measure))]
    (abc/measure accompanying-chord (:notes measure))))

(s/defn split-metrics-into-equal-measures :- [[ParsedMetricLine]]
  [metrics :- [ParsedMetricLine]]
  (partition notes-per-measure notes-per-measure [(last metrics)] metrics))

(s/defn map-metrics
  [metrics :- [ParsedMetricLine]
   composition-key]
  (let [metrics-in-measures (split-metrics-into-equal-measures metrics)
        mapped-notes         (map #(metrics-to-measure %1 composition-key 1) metrics-in-measures)]
    (apply str mapped-notes)))

(s/defn compose [metrics :- [ParsedMetricLine] composition-key]
  (when (> (count metrics) 0)
    (map-metrics metrics composition-key)))
