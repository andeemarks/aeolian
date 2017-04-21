(ns aeolian.tempo)

(def abc-template "Q:1/4=")
(def default-tempo 140)

(defn generate [complexity]
	(+ (* 20 complexity) default-tempo))

(defn as-abc [complexity]
	(str abc-template (generate complexity)))

