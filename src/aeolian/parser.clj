(ns aeolian.parser
            (:require
             [taoensso.timbre :as log]
             [schema.core :as s]))

(log/set-level! :info)

(defn- metric-line-to-bits [metric]
  (clojure.string/split metric #"\s+"))

(defn- check-valid-line-number [metric]
  (let [file-line-id (first (metric-line-to-bits metric))]
    (Integer/parseInt (last (clojure.string/split file-line-id #"#")))))

(defn- source-file-from-metric [metric]
  (check-valid-line-number metric)
  (let [source-file (second (re-find #"(\w+)\.java\#\d+" metric))]
    (if (not (nil? source-file))
      (str source-file ".java"))))

(defn- complexity-from-metric [metric]
  (check-valid-line-number metric)
  (let [complexity (second (re-find #"CC=(\w+)" metric))]
    (if (not (nil? complexity))
      (Integer/parseInt complexity)
      0)))

(defn- method-length-from-metric [metric]
  (check-valid-line-number metric)
  (let [method-length (second (re-find #"ML=(\w+)" metric))]
    (if (not (nil? method-length))
      (Integer/parseInt method-length)
      nil)))

(defn- indentation-from-metric [metric]
  (check-valid-line-number metric)
  (second (re-find #"IND(\s*)" metric)))

(defn- author-from-metric [metric]
  (check-valid-line-number metric)
  (second (re-find #"AU=(\S*)\s" metric)))

(defn- timestamp-from-metric [metric]
  (check-valid-line-number metric)
  (let [timestamp (second (re-find #"TS=(\d+)" metric))]
    (if (not (nil? timestamp))
      (Integer/parseInt timestamp)
      nil)))

(defn- line-length-from-metric [metric]
  (check-valid-line-number metric)
  (let [line-length (second (re-find #"LL=(\w+)" metric))]
    (Integer/parseInt line-length)))

(defn find-longest-method-length-in [metrics]
  (let [all-method-lengths (remove nil?
                                   (map #(method-length-from-metric %1) metrics))]
    (last (sort all-method-lengths))))

(s/defschema ^:const ParsedMetricLine
  "A schema for a single line of parsed metrics"
  {:author (s/maybe s/Str)
   :line-length s/Num
   :source-file s/Str
   :method-length (s/maybe s/Num)
   :indentation?  (s/maybe s/Str)
   :complexity s/Num
   :timestamp (s/maybe s/Num)})

(defn- validate-metric [] (s/validator ParsedMetricLine))

(s/defn parse :- ParsedMetricLine
 [metric]
 (let [parsed-metric
       {:author (author-from-metric metric)
        :line-length (line-length-from-metric metric)
        :source-file (source-file-from-metric metric)
        :method-length (method-length-from-metric metric)
        :indentation? (indentation-from-metric metric)
        :complexity (complexity-from-metric metric)
        :timestamp (timestamp-from-metric metric)}]
   ((validate-metric) parsed-metric)))
