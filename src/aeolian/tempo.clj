(ns aeolian.tempo)

(def prefix "\nQ:1/4=")
(def default-tempo 140)
(def starting-tempo (str prefix default-tempo))

(defn generate [complexity]
	(+ (* 20 complexity) default-tempo))

(defn tempo-for [complexity]
	(str prefix (generate complexity)))

