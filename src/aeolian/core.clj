(ns aeolian.core
  (:require [aeolian.parser :as parser]
            [aeolian.composer :as composer]
            [aeolian.midi.core :as midi]
            [taoensso.timbre :as log]
            [aeolian.abc.header :as h]
            [aeolian.abc.key :as k]
            [clojure.java.io :as io]))

(defn build-header-for-key [metrics-file-name key]
  (str (h/build-major-header metrics-file-name) "\n" midi/header))

(defn notation-file-name [original-file-name]
  (str original-file-name ".abc"))

(defn generate-notation-from [metrics-file-name duplicate-metrics]
  (with-open [rdr (clojure.java.io/reader metrics-file-name)]
    (let [composition-key 		(k/determine-key duplicate-metrics)
          notation-file-name 	(notation-file-name metrics-file-name)
          composition 				(composer/compose (line-seq rdr) composition-key)]
      (spit notation-file-name, (str (build-header-for-key metrics-file-name composition-key) composition))
      (log/info (str "Generated " notation-file-name)))))

(defn -main [& args]
	(println "                      lll iii     ")            
	(println "  aa aa   eee   oooo  lll       aa aa nn nnn  ")            
	(println " aa aaa ee   e oo  oo lll iii  aa aaa nnn  nn ")            
	(println "aa  aaa eeeee  oo  oo lll iii aa  aaa nn   nn ")            
	(println " aaa aa  eeeee  oooo  lll iii  aaa aa nn   nn ")            
	(println "")
	                                                               
  (if-let [metrics-file-name (first args)]
    (if (.exists (io/as-file metrics-file-name))
      (let [duplicate-metrics (read-string (second args))]
        (generate-notation-from metrics-file-name duplicate-metrics))
      (log/error (str "Error: cannot find metrics file - " metrics-file-name)))
    (log/error "Error: no metrics file supplied")))
