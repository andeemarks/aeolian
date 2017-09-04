(ns aeolian.abc.tempo)

(def prefix "Q:1/4=")
(def default-tempo 120)
(def starting-tempo (str prefix default-tempo))

(defn generate [complexity]
  (+ (* 20 complexity) default-tempo))

(defn tempo-for [complexity]
  (str prefix (generate complexity)))
