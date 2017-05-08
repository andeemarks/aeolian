(ns aeolian.parser)

; Sample metric line - last field (complexity) is optional.
; /home/amarks/Code/aeolian/resources/Notification.java#203 82 3

(defn- metric-line-to-bits [metric]
	(clojure.string/split metric #"\s+"))

(defn- check-valid-line-number [metric]
	(let [file-line-id (first (metric-line-to-bits metric))]
		(Integer/parseInt (last (clojure.string/split file-line-id #"#")))))

(defn source-file-from-metric [metric]
	(check-valid-line-number metric)
	(let [source-file (second (re-find #"(\w+)\.java\#\d+" metric))]
		(if (not (nil? source-file))
			(str source-file ".java"))))

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
			nil)))

(defn indentation-from-metric [metric]
	(check-valid-line-number metric)
	(re-find #"IND(\s*)" metric))

(defn author-from-metric [metric]
	(check-valid-line-number metric)
	(second (re-find #"AU=(\S*)\s" metric)))

(defn timestamp-from-metric [metric]
	(check-valid-line-number metric)
	(second (re-find #"TS=(\d+)" metric)))

(defn line-length-from-metric [metric]
	(check-valid-line-number metric)
	(let [line-length (second (re-find #"LL=(\w+)" metric))]
		(Integer/parseInt line-length)))

(defn find-longest-method-length-in [metrics]
	(let [all-method-lengths (remove nil? 
								(map #(method-length-from-metric %1) metrics))]
		(last (sort all-method-lengths))))

(defn parse [metric]
	(let [author (author-from-metric metric)]
		{:author author}))
