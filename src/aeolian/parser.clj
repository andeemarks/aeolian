(ns aeolian.parser)

; Sample metric line - last field (complexity) is optional.
; /home/amarks/Code/aeolian/resources/Notification.java#203 82 3

(defn- metric-line-to-bits [metric]
	(clojure.string/split metric #"\s+"))

(defn- check-valid-line-number [metric]
	(let [file-line-id (first (metric-line-to-bits metric))]
		(Integer/parseInt (last (clojure.string/split file-line-id #"#")))))

(defn complexity-from-metric [metric]
	(check-valid-line-number metric)
	(if (> (count (metric-line-to-bits metric)) 2)
		(Integer/parseInt (nth (metric-line-to-bits metric) 2))
		0))

(defn line-length-from-metric [metric]
	(check-valid-line-number metric)
	(Integer/parseInt (nth (metric-line-to-bits metric) 1)))
