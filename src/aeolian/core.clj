(ns aeolian.core
  (:require [aeolian.composer :as composer]
            [aeolian.midi.core :as midi]
            [aeolian.parser :as parser]
            [taoensso.timbre :as log]
            [aeolian.abc.header :as h]
            [aeolian.abc.key :as k]
            [aeolian.banner :as banner]
            [clojure.java.io :as io]))

(defn build-header [metrics-file-name key]
  (str (h/build-common-header metrics-file-name key) "\n" midi/header))

(defn notation-file-name [original-file-name]
  (str original-file-name ".abc"))

(defn generate-notation-from
  "Using the contents of the `input-file-name` and `duplicate-metrics`, write
  ABC notation to `output-file-name`.
  "
  [input-file-name output-file-name duplicate-metrics]
  (with-open [rdr (clojure.java.io/reader input-file-name)]
    (let [composition-key     (k/determine-key duplicate-metrics)
          parsed-metrics      (map #(parser/parse %) (line-seq rdr))
          composition         (composer/compose parsed-metrics composition-key)]
      (spit output-file-name  (str (build-header input-file-name composition-key) composition))
      (log/info "Generated " output-file-name))))

(defn -main
  "Command line entry point for Aeolian.

  Usage:

  `lein run [metrics-file] [duplication-metrics]`

  where:

  * `metrics-file` is the name of the file containing all the generated metrics for the repository being processed.
  * `duplication-metrics` is a Clojure hash of the form `{:duplicate-lines n1 :total-lines n2}` with `n1` and `n2` being non-negative integers and `n1` >= `n2`

  Example:

  `lein run repo.metrics {:duplicate-lines 533 :total-lines 15}`
  "
  [& args]
  (banner/banner)

  (if-let [metrics-file-name (first args)]
    (if (.exists (io/as-file metrics-file-name))
      (let [duplicate-metrics (read-string (second args))]
        (generate-notation-from metrics-file-name (notation-file-name metrics-file-name) duplicate-metrics))
      (log/error (str "Error: cannot find metrics file - " metrics-file-name)))
    (log/error "Error: no metrics file supplied")))
