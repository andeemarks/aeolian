(ns aeolian.parser
  (:require
   [taoensso.timbre :as log]))

(log/set-level! :info)

(defn- type-from-metric [metric]
  (cond
    (:file-length metric) :file
    (:method-count metric) :class
    (:method-length metric) :method
    :else :regular))

(defn- convert [raw-metric]
  {
    :line-length (. Integer parseInt (:line-length raw-metric))
    :source-file (:source-file raw-metric)
    :author (:author raw-metric)
    :method-length (. Integer parseInt (or (:method-length raw-metric) "0"))
    :method-count (. Integer parseInt (or (:method-count raw-metric) "0"))
    :file-length (. Integer parseInt (or (:file-length raw-metric) "0"))
    :timestamp (:timestamp raw-metric)
    :line (:line raw-metric)
    :indentation-violation? (or (:indentation-violation? raw-metric) false)
    :complexity (. Integer parseInt (or (:complexity raw-metric) "0"))
    })

(defn parse [metric]
  (let [raw-metric (read-string metric)
        metric-type {:type (type-from-metric raw-metric)}
        default-values {:indentation-violation? false :timestamp nil :method-length nil :complexity 0 :author ""}]
    (merge default-values metric-type (convert raw-metric))))
