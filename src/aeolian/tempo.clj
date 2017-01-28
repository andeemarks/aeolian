(ns aeolian.tempo)

(def abc-template "Q:1/4=")
(def default-tempo 120)

(defn generate [complexity]
	(+ (* 20 complexity) default-tempo))

(defn as-abc [complexity]
	(str tempo-root (generate complexity)))
