(ns aeolian.parser)

(defn- metric-line-to-bits [metric]
	(clojure.string/split metric #"\s+"))

(defn- check-valid-line-number [metric]
	(Integer/parseInt (nth (metric-line-to-bits metric) 0)))

(defn complexity-from-metric [metric]
	(check-valid-line-number metric)
	(if (> (count (metric-line-to-bits metric)) 2)
		(Integer/parseInt (nth (metric-line-to-bits metric) 2))
		0))

(defn line-width-from-metric [metric]
	(check-valid-line-number metric)
	(Integer/parseInt (nth (metric-line-to-bits metric) 1)))
