(ns aeolian.core
	(:require [aeolian.parser :as parser]
						[aeolian.composer :as composer]
						[aeolian.tempo :as t]
						[aeolian.midi.core :as midi]
						[aeolian.abc.header :as h]
						[clojure.java.io :as io]))

(defn build-header [metrics-file-name duplicate-metrics]
	(let [duplication-percentage (float 
									(* 100 
									(/ 
										(:duplicate-lines duplicate-metrics) 
										(:total-lines duplicate-metrics))))]
		(if (< duplication-percentage 10)
			(str (h/build-major-header metrics-file-name) "\n" midi/header)
			(str (h/build-minor-header metrics-file-name) "\n" midi/header))))

(defn notation-file-name [original-file-name]
	(str original-file-name ".abc"))

(defn- generate-notation-from [metrics-file-name]
	(with-open [rdr (clojure.java.io/reader metrics-file-name)]
     	(let [notation-file-name (notation-file-name metrics-file-name)
     				composition (composer/compose (line-seq rdr))]
 				(spit notation-file-name, (str (build-header metrics-file-name) composition))
     		(println (str "Generated " notation-file-name))))
	)

(defn -main [& args]
	(if-let [metrics-file-name (first args)]
		(if (.exists (io/as-file metrics-file-name))
			(let [duplicate-metrics (second args)]
				(println duplicate-metrics)
				(generate-notation-from metrics-file-name))
			(println (str "Error: cannot find metrics file - " metrics-file-name)))
		(println "Error: no metrics file supplied")))
