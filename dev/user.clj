(ns user
  (:require [clojure.tools.namespace.repl :as tnr]
            [clojure.repl]
            [proto-repl.saved-values]))

(defn start []
  (println "Start completed"))

(defn reset []
  (tnr/refresh :after 'user/start))

(println "proto-repl-demo dev/user.clj loaded.")
