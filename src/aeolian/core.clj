(ns aeolian.core
	(:require [aeolian.parser :as parser]
						[aeolian.composer :as composer]
						[aeolian.tempo :as t]
						[aeolian.midi.core :as midi]
						[aeolian.abc.header :as h]
						[clojure.java.io :as io]))

(def composition-key (atom ""))

(defn build-header [metrics-file-name duplicate-metrics]
	(let [duplicate-lines (:duplicate-lines duplicate-metrics)
			total-lines (:total-lines duplicate-metrics)
			duplication-percentage (float 
									(* 	100 
									(/ 	duplicate-lines total-lines)))]
		(if (< duplication-percentage 10)
			(do
				(swap! composition-key (fn [f] :major))
				(str (h/build-major-header metrics-file-name) "\n" midi/header))
			(do
				(swap! composition-key (fn [f] :minor))
				(str (h/build-minor-header metrics-file-name) "\n" midi/header)))))

(defn notation-file-name [original-file-name]
	(str original-file-name ".abc"))

(defn- generate-notation-from [metrics-file-name duplicate-metrics]
	(with-open [rdr (clojure.java.io/reader metrics-file-name)]
     	(let [notation-file-name (notation-file-name metrics-file-name)
     				composition (composer/compose (line-seq rdr))]
 				(spit notation-file-name, (str (build-header metrics-file-name duplicate-metrics) composition))
     		(println (str "Generated " notation-file-name))))
	)

(defn -main [& args]
	(if-let [metrics-file-name (first args)]
		(if (.exists (io/as-file metrics-file-name))
			(let [duplicate-metrics (read-string (second args))]
				(generate-notation-from metrics-file-name duplicate-metrics))
			(println (str "Error: cannot find metrics file - " metrics-file-name)))
		(println "Error: no metrics file supplied")))
