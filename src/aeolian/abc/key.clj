(ns aeolian.abc.key)

(def major :major)
(def minor :minor)

(defn- duplication-percentage [duplicate-metrics]
  (let [duplicate-lines (:duplicate-lines duplicate-metrics)
        total-lines 		(if (zero? (:total-lines duplicate-metrics)) 1 (:total-lines duplicate-metrics))]
    (float
     (* 	100
         (/ 	duplicate-lines total-lines)))))

(defn determine-key [duplicate-metrics]
  (if (< (duplication-percentage duplicate-metrics) 10)
    major
    minor))
