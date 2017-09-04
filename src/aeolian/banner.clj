(ns aeolian.banner
  (:require [clansi :as ansi]))

(defn- line [text & [args]] (println (ansi/style text args)))

(defn banner []
  (line "                      lll iii                 " :bg-blue)
  (line "  aa aa   eee   oooo  lll       aa aa nn nnn  " :bg-green)
  (line " aa aaa ee   e oo  oo lll iii  aa aaa nnn  nn " :bg-cyan)
  (line "aa  aaa eeeee  oo  oo lll iii aa  aaa nn   nn " :bg-green)
  (line " aaa aa  eeeee  oooo  lll iii  aaa aa nn   nn " :bg-blue)
  (println ""))
