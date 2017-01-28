(ns aeolian.tempo)

(def tempo-root "Q:1/4=")
(def default-tempo 120)

(defn generate [complexity]
	(str tempo-root (+ (* 20 complexity) default-tempo)))
