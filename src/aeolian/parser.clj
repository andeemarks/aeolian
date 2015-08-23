(ns aeolian.parser)

(defn- metric-line-to-bits [metric]
	(clojure.string/split metric #"\s+"))

(defn complexity-from-metric [metric]
	(if (> (count (metric-line-to-bits metric)) 2)
		(let [complexity (nth (metric-line-to-bits metric) 2)
			; _ (prn complexity)
			]
			(read-string complexity))
		0))

(defn line-width-from-metric [metric]
	; (prn metric)
	(let [line-width (read-string (nth (metric-line-to-bits metric) 1))
		; _ (prn line-width)
		]
		line-width))