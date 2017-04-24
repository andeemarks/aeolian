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
	(let [complexity (second (re-find #"CC=(\w+)" metric))]
		(if (not (nil? complexity))
			(Integer/parseInt complexity)
			0)))

(defn method-length-from-metric [metric]
	(check-valid-line-number metric)
	(let [method-length (second (re-find #"ML=(\w+)" metric))]
		(if (not (nil? method-length))
			(Integer/parseInt method-length)
			0)))

(defn indentation-from-metric [metric]
	(check-valid-line-number metric)
	(re-find #"IND(\s*)" metric))

(defn line-length-from-metric [metric]
	(check-valid-line-number metric)
	(let [line-length (second (re-find #"LL=(\w+)" metric))]
		(Integer/parseInt line-length)))
