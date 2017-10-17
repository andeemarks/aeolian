(ns aeolian.parser
            (:require
             [taoensso.timbre :as log]))

(log/set-level! :info)

(defn- metric-line-to-bits [metric]
  (clojure.string/split metric #"\s+"))

(defn- check-valid-line-number [metric]
  (let [file-line-id (first (metric-line-to-bits metric))]
    (Integer/parseInt (last (clojure.string/split file-line-id #"#")))))

(defn- source-file-from-metric [metric]
  (if-let [source-file (second (re-find #"([\w.]+)\#\d+" metric))]
    source-file))

(defn- complexity-from-metric [metric]
  (if-let [complexity (second (re-find #"CC=(\w+)" metric))]
    (Integer/parseInt complexity)
    0))

(defn- method-length-from-metric [metric]
  (if-let [method-length (second (re-find #"ML=(\w+)" metric))]
    (Integer/parseInt method-length)
    nil))

(defn- indentation-from-metric [metric]
  (second (re-find #"IND(\s*)" metric)))

(defn- author-from-metric [metric]
  (second (re-find #"AU=(\S*)\s" metric)))

(defn- timestamp-from-metric [metric]
  (if-let [timestamp (second (re-find #"TS=(\d+)" metric))]
    (Integer/parseInt timestamp)
    nil))

(defn- line-length-from-metric [metric]
  (let [line-length (second (re-find #"LL=(\w+)" metric))]
    (Integer/parseInt line-length)))

(defn- type-from-metric [metric]
  (cond 
    (second (re-find #"FL=(\w+)" metric)) :file
    (second (re-find #"MC=(\w+)" metric)) :class
    (second (re-find #"ML=(\w+)" metric)) :method
    :else :regular))

(defn parse
  [metric]
  (check-valid-line-number metric)
  (let [parsed-metric
       {:author (author-from-metric metric)
        :line-length (line-length-from-metric metric)
        :source-file (source-file-from-metric metric)
        :method-length (method-length-from-metric metric)
        :indentation? (indentation-from-metric metric)
        :complexity (complexity-from-metric metric)
        :type (type-from-metric metric)
        :timestamp (timestamp-from-metric metric)}]
        parsed-metric))
