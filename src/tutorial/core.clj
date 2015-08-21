(ns tutorial.core)

(def c-major-notes ["C" "E" "G" "B" "z" "c" "e" "g" "b" "z" "c'" "e'" "g'" "b'" "z"])
(def c-major-chords ["\"C\"" "\"F\"" "\"G\"" "\"Am\""])
(def c-major-root "\"Am\"")
(def tempo-root "Q:1/4=")
(def default-tempo 120)
(def header "X:1\nT:Hello World\nM:4/4\nL:1/4\nK:C\n%%MIDI program 0\n%%MIDI drum zd 60\n%%MIDI drumon\n%%MIDI gchord c\nQ:1/4=360\n")

(defn- metric-line-to-bits [metric]
	(clojure.string/split metric #"\s+"))

(defn- complexity-from-metric [metric]
	(if (> (count (metric-line-to-bits metric)) 2)
		(let [complexity (nth (metric-line-to-bits metric) 2)
			; _ (prn complexity)
			]
			(read-string complexity))
		0))

(defn- line-width-from-metric [metric]
	; (prn metric)
	(let [line-width (read-string (nth (metric-line-to-bits metric) 1))
		; _ (prn line-width)
		]
		line-width))

(defn- metric-to-note-index [metric]
	(let [note-index (mod (line-width-from-metric metric) (count c-major-notes))
		; _ (prn note-index)
		]
		note-index))

(defn- metric-to-note [metric]
	(let [complexity (complexity-from-metric metric)
				note (nth c-major-notes (metric-to-note-index metric))]
		(cond (> complexity 1)
			(str "\n" tempo-root (+ (* 20 complexity) default-tempo) "\n" note)
			; note
			:else note)))

(defn- generate-notation [notes-per-measure line-metrics]
	; (prn line-metrics)
	(let [mapped-notes (map #(metric-to-note %1) line-metrics)
		notes-in-measures (apply str (flatten (interpose (str "|\n" c-major-root) (partition notes-per-measure mapped-notes))))
		completed-score (str header "|" c-major-root notes-in-measures "|")
		_ (prn completed-score)
		]
		(spit "songaliser.abc", completed-score)
		)
	)

(defn -main [& args]
	(with-open [rdr (clojure.java.io/reader (first args))]
     (generate-notation 4 (line-seq rdr)))
	)